package Chatserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Asus
 */
public class Server {

    private ArrayList<ClientThread> arrayCli;
    private MainFrame mainFrame;
    private boolean keepGoing;

    public Server(MainFrame mn) {
        this.mainFrame = mn;
        arrayCli = new ArrayList<>();
        keepGoing = true;
    }

    public void start() {
        new catchClients().start();
    }

    private void display(Message msg) {
        mainFrame.append(new SimpleDateFormat("HH:mm").format(new Date()) + "(" + msg.getSender() + "): " + msg.getMsg());
    }

    private synchronized void sendToAll(Message msg) {
        if (arrayCli.isEmpty()) {
            return;
        }
        for (int i = arrayCli.size() - 1; i >= 0; --i) {
            ClientThread ct = arrayCli.get(i);
            if (!ct.writeMessage(msg)) {
                mainFrame.appendError("Disconnected Client '" + ct.lg.getUsername() + "' removed from list");
                ct.flag = false;
            }
        }
    }

    private synchronized void sendToOne(Message msg) {
        if (arrayCli.isEmpty() || msg.getSender().equals(msg.getReceiver())) {
            return;
        }
        for (int i = arrayCli.size() - 1; i >= 0; --i) {
            ClientThread ct = arrayCli.get(i);
            if (ct.lg.getUsername().equals(msg.getReceiver())) {
                if (!ct.writeMessage(msg)) {
                    mainFrame.appendError("Disconnected Client '" + ct.lg.getUsername() + "' removed from list");
                    ct.flag = false;
                } else {
                    return;
                }
            }
        }
    }

    private synchronized void remove(String user) {
        if (arrayCli.isEmpty()) {
            return;
        }
        for (int i = arrayCli.size() - 1; i >= 0; --i) {
            ClientThread ct = arrayCli.get(i);
            if (ct.lg.getUsername().equals(user)) {
                arrayCli.remove(i);
                return;
            }
        }
    }

    private synchronized void kickClients(Message message) {
        if (arrayCli.isEmpty()) {
            return;
        }
        for (int i = arrayCli.size() - 1; i >= 0; --i) {
            ClientThread ct = arrayCli.get(i);
            if (ct.lg.getUsername().equals(message.getReceiver())) {
                if (!ct.writeMessage(message)) {
                    mainFrame.appendError("Disconnected Client '" + ct.lg.getUsername() + "' removed from list");
                }
                ct.flag = false;
            }
        }
    }

    private void banClients(Message message) {
        kickClients(message);
        DatabaseConnection dt = new DatabaseConnection();
        dt.deleteClient(message.getReceiver());
    }

    private synchronized String getOnlines() {
        if (arrayCli.isEmpty()) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        for (int i = arrayCli.size() - 1; i >= 0; --i) {
            str.append(arrayCli.get(i).lg.getUsername()).append("\n");
        }
        return str.toString();
    }

    private synchronized void updateOnlines() {
        String str = getOnlines();
        for (int i = arrayCli.size() - 1; i >= 0; --i) {
            ClientThread ct = arrayCli.get(i);
            if (!ct.writeMessage(new Message(str, "SERVER", ct.lg.getUsername(), Message.Type.ONLINE))) {
                arrayCli.remove(i);
                mainFrame.appendError("Disconnected Client '" + ct.lg.getUsername() + "' removed from list");
            }
        }
    }

    public void sendServerMsg(Message message) {
        switch (message.getType()) {
            case BAN:
                banClients(message);
                sendToAll(new Message("'" + message.getReceiver() + "' was BANNED. Reason: " + mainFrame.getKickorbanmessage(), "SERVER", "ALL", Message.Type.MESSAGE));
                mainFrame.append("'" + message.getReceiver() + "' was BANNED. Reason: " + mainFrame.getKickorbanmessage());
                break;
            case KICK:
                kickClients(message);
                sendToAll(new Message("'" + message.getReceiver() + "' was KICKED. Reason: " + mainFrame.getKickorbanmessage(), "SERVER", "ALL", Message.Type.MESSAGE));
                mainFrame.append("'" + message.getReceiver() + "' was KICKED. Reason: " + mainFrame.getKickorbanmessage());
                break;
            case ONLINE:
                updateOnlines();
                break;
            case MESSAGE:
                if (message.getReceiver().equals("")) {
                    sendToAll(message);
                } else {
                    sendToOne(message);
                }
                display(message);
                break;
        }
    }

    class ClientThread extends Thread {

        private Socket socket;
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private Login lg;
        private boolean flag;

        ClientThread(Socket socket) {
            this.socket = socket;
            flag = true;
            try {
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());

                signUpLogin();
            } catch (IOException | ClassNotFoundException e) {
                mainFrame.appendError("Error in ClientThread constructor: " + e);
                flag = false;
            }
        }

        private void signUpLogin() throws IOException, ClassNotFoundException {
            lg = (Login) input.readObject();
            DatabaseConnection dt = new DatabaseConnection();

            if (lg.getValid()) {
                if (dt.newClient(lg)) {
                    output.writeObject(lg);
                    mainFrame.append("'" + lg.getUsername() + "' just connected (for the first time!).");
                    sendToAll(new Message("'" + lg.getUsername() + "' just connected (for the first time!).",
                            "SERVER", lg.getUsername(), Message.Type.MESSAGE));
                    return;
                }
            } else {
                if (dt.checkLogin(lg)) {
                    int aux = 0;
                    for (int i = 0; i < arrayCli.size(); i++) {
                        if (arrayCli.get(i).lg.getUsername().equals(this.lg.getUsername())) {
                            aux++;
                        }
                    }
                    if (aux < 1) {
                        lg.setValid(true);
                        output.writeObject(lg);
                        mainFrame.append("'" + lg.getUsername() + "' just connected.");
                        sendToAll(new Message("'" + lg.getUsername() + "' just connected.", "SERVER", lg.getUsername(), Message.Type.MESSAGE));
                        return;
                    }
                }
            }
            flag = false;
            lg.setValid(false);
            output.writeObject(lg);
        }

        @Override
        public void run() {
            if (flag) {
                mainFrame.imOnline(lg.getUsername());
                updateOnlines();
            }
            Message message;
            while (flag) {
                message = receiveMessage();
                if (!flag) {
                    break;
                }
                if (message == null) {
                    mainFrame.appendError("Client ''" + lg.getUsername() + "' sent a null message");
                    break;
                }
                if (message.getType() == Message.Type.LOGOUT) {
                    sendToOne(new Message("", "SERVER", message.getSender(), Message.Type.LOGOUT));
                    sendToAll(new Message("'" + message.getSender() + "' logged out.", "SERVER", "ALL", Message.Type.MESSAGE));
                    mainFrame.append("'" + message.getSender() + "' logged out.");
                    break;
                }
                switch (message.getType()) {
                    case CHANGEUSER:
                        changeUser(message);
                        break;
                    case CHANGEPASS:
                        changePass(message);
                        break;
                    case MESSAGE:
                        sendToAll(message);
                        display(message);
                        break;
                    case YES:
                        new FileTransfer(mainFrame).start();
                    case PRIVATEMESSAGE:
                    case FILE:
                    case NO:
                        sendToOne(message);
                        break;
                }
            }
            mainFrame.imOffline(lg.getUsername());
            remove(lg.getUsername());
            updateOnlines();
            close();
        }

        private void close() {
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                mainFrame.appendError("Error in close() method. Client '" + lg.getUsername() + "':" + e);
            }
        }

        private boolean writeMessage(Message message) {
            if (!socket.isConnected()) {
                close();
                return false;
            }
            try {
                output.writeObject(message);
            } catch (IOException e) {
                mainFrame.appendError("Error in writeMsg() method. Client '" + lg.getUsername() + "':" + e);
            }
            return true;
        }

        private Message receiveMessage() {
            try {
                Message message = (Message) input.readObject();
                return message;
            } catch (IOException | ClassNotFoundException e) {
                mainFrame.appendError("Error in receiveMsg() method. Client '" + lg.getUsername() + "':" + e);
                return null;
            }
        }

        private void changeUser(Message message) {
            DatabaseConnection dt = new DatabaseConnection();
            if (!dt.changeUsername(message.getSender(), message.getMsg())) {
                writeMessage(new Message("Name already exists! Choose another one...", "SERVER", lg.getUsername(), Message.Type.ERROR));
            } else {
                lg = new Login(message.getMsg(), lg.getPassword());
                writeMessage(new Message("", "SERVER", lg.getUsername(), Message.Type.CHANGEUSER));
                mainFrame.changeOnlines(getOnlines());
                updateOnlines();
                sendToAll(new Message("'" + message.getSender() + "' changed name to '" + message.getMsg() + "'", "SERVER", "ALL", Message.Type.MESSAGE));
            }
        }

        private void changePass(Message message) {
            DatabaseConnection dt = new DatabaseConnection();
            if (!dt.changePassword(message.getSender(), message.getMsg())) {
                writeMessage(new Message("Password not available", "SERVER", lg.getUsername(), Message.Type.ERROR));
            } else {
                lg = new Login(lg.getUsername(), message.getMsg());
                writeMessage(new Message("", "SERVER", lg.getUsername(), Message.Type.CHANGEPASS));
            }
        }
    }

    class catchClients extends Thread {

        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(5000);
                while (keepGoing) {
                    Socket socket = serverSocket.accept();
                    if (!keepGoing) {
                        break;
                    }
                    ClientThread t = new ClientThread(socket);

                    if (t.flag == false) {
                        continue;
                    }
                    arrayCli.add(t);
                    t.start();
                }
                try {
                    serverSocket.close();
                    for (int i = 0; i < arrayCli.size(); ++i) {
                        ClientThread ct = arrayCli.get(i);
                        ct.close();
                    }
                } catch (IOException e) {
                    mainFrame.appendError("IOException in Server class - start() at closing server");
                }
            } catch (IOException e) {
                mainFrame.appendError("IOException in Server class - start()");
                System.exit(0);
            }
        }
    }
}
