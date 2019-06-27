package Chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 *
 * @author Asus
 */
public class FileTransfer extends Thread {

    private Socket socket;
    private String path;
    private boolean flag;       //if(true) - im the sender, else, im the receiver
    private MainFrame mn;

    public FileTransfer(boolean flag, String path, MainFrame mn) {
        this.flag = flag;
        this.path = path;
        this.mn = mn;
    }

    @Override
    public void run() {
        try {
            socket = new Socket("127.0.0.1", 5001);
            if (flag) {
                try (DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
                    try {
                        FileInputStream fs = new FileInputStream(path);
                        long size = new File(path).length();
                        try {
                            dos.writeLong(size);
                        } catch (IOException e) {
                        }
                        long chunks = size / 1024;
                        int lastChunk = (int) (size - (chunks * 1024));
                        byte[] buf = new byte[1024];
                        for (long i = 0; i < chunks; i++) {
                            try {
                                fs.read(buf);
                            } catch (IOException e) {
                            }
                            dos.write(buf, 0, 1024);
                        }
                        try {
                            fs.read(buf, 0, lastChunk);
                        } catch (IOException e) {
                        }
                        dos.write(buf, 0, lastChunk);
                        fs.close();
                    } catch (FileNotFoundException e) {
                    }
                }
                mn.append("File successefully sent.");
            } else {
                try (DataInputStream dis = new DataInputStream(socket.getInputStream())) {
                    try {
                        FileOutputStream fs = new FileOutputStream(path);
                        long size = 0;
                        try {
                            size = dis.readLong();
                        } catch (IOException e) {
                        }
                        long chunks = size / 1024;
                        int lastChunk = (int) (size - (chunks * 1024));
                        byte[] buf = new byte[1024];
                        for (long i = 0; i < chunks; i++) {
                            dis.read(buf, 0, 1024);
                            try {
                                fs.write(buf);
                            } catch (IOException e) {
                            }
                        }
                        dis.read(buf, 0, lastChunk);
                        try {
                            fs.write(buf, 0, lastChunk);
                        } catch (IOException e) {
                        }
                        fs.close();
                    } catch (FileNotFoundException e) {
                    }
                }
                mn.append("File received with success.");
            }
        } catch (IOException e) {
        }
    }
}
