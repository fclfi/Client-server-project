package Chatserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Asus
 */
public class Client {

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;

    private MainFrame mainFrame;
    private ArrayList<DuoChat> arrayChat;

    private final String server;
    private final int port;
    private Login lg;
    private boolean keepgoing;
    private ListenFromServer lfs;

    Client(String server, int port, MainFrame mn) {
        this.mainFrame = mn;
        arrayChat = new ArrayList<>();
        this.server = server;
        this.port = port;
        this.keepgoing = true;
    }

    public String getUsername() {
        return lg.getUsername();
    }

    public String getPassword() {
        return lg.getPassword();
    }

    public boolean start() {
        try {
            socket = new Socket(server, port);
        } catch (IOException e) {
            //System.out.println("Error in start() method:" + e);
            return false;
        }
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            //System.out.println("Exception in start() method: " + e);
            return false;
        }
        return true;
    }

    public boolean login(String user, String pass) {
        try {
            lg = new Login(user, pass);
            output.writeObject(lg);

            lg = (Login) input.readObject();
            return lg.getValid();
        } catch (IOException | ClassNotFoundException ex) {
            //System.out.println("Error in loginGUI() method: " + ex);
        }
        return false;
    }

    public boolean signUp(String user, String pass) {
        try {
            lg = new Login(user, pass);
            lg.setValid(true);
            output.writeObject(lg);

            lg = (Login) input.readObject();
            return lg.getValid();
        } catch (IOException | ClassNotFoundException ex) {
            //System.out.println("Error in signUpGUI() method: " + ex);
        }
        return false;
    }
    
    public void logout() {
        keepgoing = false;
    }

    public void startListenServer() {
        lfs = new ListenFromServer();
        lfs.start();
    }

    private void display(Message msg) {
        mainFrame.append(new SimpleDateFormat("HH:mm").format(new Date()) + "(" + msg.getSender() + "): " + msg.getMsg());
    }

    public void sendMessage(Message msg) {
        try {
            output.writeObject(msg);
        } catch (IOException e) {
            //System.out.println("Error in sendMessage() method: " + e);
        }
    }

    public Message receiveMessage() {
        try {
            Message msg = (Message) input.readObject();
            return msg;
        } catch (IOException | ClassNotFoundException e) {
            //System.out.println("Error in receiveMessage() method: " + e);
            return null;
        }
    }

    private void disconnect() {
        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            //System.out.println("Error in disconnect() method: " + e);
        }
    }

    public boolean isOnline(String str) {
        for (int i = 0; i < arrayChat.size(); i++) {
            if (str.equals(arrayChat.get(i).getFriend())) {
                return true;
            }
        }
        return false;
    }

    private void updateOnlines(Message message) {
        if (message.getMsg().equals("")) {
            arrayChat.clear();
            return;
        }
        String[] str = message.getMsg().split("\n");
        if (arrayChat.isEmpty()) {
            for (String str1 : str) {
                arrayChat.add(new DuoChat(str1));
            }
            return;
        }
        boolean flag;
        for (String str1 : str) {
            flag = false;
            for (int i = 0; i < arrayChat.size(); i++) {
                if (str1.equals(arrayChat.get(i).getFriend())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                arrayChat.add(new DuoChat(str1));
            }
        }
        for (int i = 0; i < arrayChat.size(); i++) {
            flag = false;
            for (String str1 : str) {
                if (arrayChat.get(i).getFriend().equals(str1)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (arrayChat.get(i).getChatFrame() != null) {
                    arrayChat.get(i).getChatFrame().dispose();
                }
                arrayChat.remove(i);
            }
        }
    }

    public void newPrivateChat(String str, boolean istarted) {
        for (int i = 0; i < arrayChat.size(); i++) {
            if (arrayChat.get(i).getFriend().equals(str)) {
                if (arrayChat.get(i).getChatFrame() == null) {
                    DuoChatFrame duoChatFrame = new DuoChatFrame(this, str, istarted);
                    duoChatFrame.setVisible(true);
                    arrayChat.get(i).setChatFrame(duoChatFrame);
                    break;
                } else {
                    return;
                }
            }
        }
    }

    private void privateMessage(Message message) {
        DuoChat duochat = null;
        for (int i = 0; i < arrayChat.size(); i++) {
            if (message.getSender().equals(arrayChat.get(i).getFriend())) {
                duochat = arrayChat.get(i);
            }
        }
        if (duochat == null) {
            return;
        }
        if (duochat.getChatFrame() == null) {
            newPrivateChat(message.getSender(), false);
        }
        duochat.getChatFrame().append(new SimpleDateFormat("HH:mm").format(new Date()) + "(" + message.getSender() + "): " + message.getMsg());
    }

    public void imClosed(String name) {
        for (int i = 0; i < arrayChat.size(); i++) {
            if (arrayChat.get(i).getFriend().equals(name)) {
                arrayChat.get(i).setChatFrame(null);
            }
        }
    }

    class ListenFromServer extends Thread {

        @Override
        public void run() {
            while (keepgoing) {
                Message message = receiveMessage();
                if (message == null) {
                    mainFrame.end();
                    break;
                }
                switch (message.getType()) {
                    case BAN:
                        mainFrame.getKickOrBan(message, "Ban", Message.Type.BAN);
                        keepgoing = false;
                        disconnect();
                        System.exit(0);
                    case KICK:
                        mainFrame.getKickOrBan(message, "Kick", Message.Type.KICK);
                        keepgoing = false;
                        disconnect();
                        System.exit(0);
                    case ONLINE:
                        updateOnlines(message);
                        mainFrame.showOnlines(message.getMsg());
                        break;
                    case ERROR:
                        mainFrame.showError(message);
                        break;
                    case CHANGEUSER:
                        lg = new Login(message.getReceiver(), lg.getPassword());
                        break;
                    case CHANGEPASS:
                        lg = new Login(lg.getUsername(), message.getReceiver());
                        break;
                    case MESSAGE:
                        display(message);
                        break;
                    case PRIVATEMESSAGE:
                        privateMessage(message);
                        break;
                    case FILE:
                        if (mainFrame.receiveFile(message)) {
                            new FileTransfer(false, mainFrame.getPath(), mainFrame).start();
                            mainFrame.resetFiles();
                        }
                        break;
                    case YES:
                        new FileTransfer(true, mainFrame.getPath(), mainFrame).start();
                        mainFrame.resetFiles();
                        break;
                    case NO:
                        mainFrame.fileDenied(message);
                        mainFrame.resetFiles();
                        break;
                    case LOGOUT:
                        keepgoing = false;
                }
            }
            disconnect();
        }
    }
}
