package Chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Asus
 */
public class FileTransfer extends Thread {

    private ServerSocket serverSocket;
    private Socket senderSocket;
    private Socket receiverSocket;
    private MainFrame mn;

    public FileTransfer(MainFrame mn) {
        this.mn = mn;
    }
    
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(5001);
            receiverSocket = serverSocket.accept();
            senderSocket = serverSocket.accept();

            DataOutputStream dosReceiver = new DataOutputStream(receiverSocket.getOutputStream());
            DataInputStream disSender = new DataInputStream(senderSocket.getInputStream());

            long size = disSender.readLong();
            dosReceiver.writeLong(size);
            long chunks = size / 1024;
            int lastChunk = (int) (size - (chunks * 1024));
            byte[] buf = new byte[1024];
            for (long i = 0; i < chunks; i++) {
                disSender.read(buf);
                dosReceiver.write(buf);
            }
            disSender.read(buf, 0, lastChunk);
            dosReceiver.write(buf, 0, lastChunk);
        } catch (IOException e) {
            mn.appendError("Error in FileTransfer():" + e);
        }
        try {
            serverSocket.close();
            senderSocket.close();
            receiverSocket.close();
        } catch (IOException e) {
            mn.appendError("Error in closing FileTransfer():" + e);
        }
    }
}
