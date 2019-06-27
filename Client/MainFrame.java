package Chatserver;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Asus
 */
public class MainFrame extends JFrame {

    private LoginFrame loginFrame;
    private JFrame jf;
    private Client cli;
    private String path;
    private String filename;
    private Color mfcolor;
    private Color mbcolor;
    private Color ofcolor;
    private Color obcolor;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        this.cli = new Client("127.0.0.1", 5000, this);
        this.loginFrame = new LoginFrame(cli, this);
        this.jf = null;
        this.path = null;
        this.filename = null;
        this.mfcolor = null;
        this.mbcolor = null;
        loginFrame.setVisible(true);
    }

    public String getPath() {
        return path;
    }

    public void resetFiles() {
        this.path = null;
        this.filename = null;
    }

    public void append(String str) {
        mainArea.append(str + "\n");
        mainArea.setCaretPosition(mainArea.getDocument().getLength());
    }

    public void showError(Message message) {
        JOptionPane.showMessageDialog(null, message.getMsg(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showOnlines(String str) {
        onlineArea.setText("");
        onlineArea.append(str);
    }

    private void sendMsg() {
        if (!cliMessage.getText().isEmpty()) {
            String msg = cliMessage.getText().trim();
            cli.sendMessage(new Message(msg, cli.getUsername(), "", Message.Type.MESSAGE));
            cliMessage.setText("");
        }
    }

    @SuppressWarnings("NonPublicExported")
    public void getKickOrBan(Message message, String kickban, Message.Type type) {
        JOptionPane.showConfirmDialog(null, message.getMsg(), kickban, JOptionPane.WARNING_MESSAGE,
                JOptionPane.PLAIN_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/images/emoji1.png")));
        cli.sendMessage(new Message(message.getMsg(), cli.getUsername(), "SERVER", type));
        this.setVisible(false);
        File f = new File(System.getProperty("java.io.tmpdir") + "mytempfileCLIENTJAVAAPP.tmp");
        if (f.exists()) {
            f.delete();
        }
        this.dispose();
    }

    public boolean receiveFile(Message message) {
        String str = "'" + message.getSender() + "' wants to send a file.\n Do you accept?";
        int opt = JOptionPane.showConfirmDialog(null, str, "File transfer", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/images/load64.png")));
        if (opt == JOptionPane.YES_OPTION) {
            JFileChooser fchooser = new JFileChooser();
            fchooser.setDialogTitle("Save file");
            fchooser.setApproveButtonToolTipText("Save file");
            fchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int n = fchooser.showOpenDialog(MainFrame.this);
            if (n == JFileChooser.APPROVE_OPTION) {
                filename = message.getMsg();
                path = fchooser.getSelectedFile().getAbsolutePath() + File.separator + filename;
            }
            cli.sendMessage(new Message("", cli.getUsername(), message.getSender(), Message.Type.YES));
            return true;
        } else {
            if (opt == JOptionPane.NO_OPTION) {
                cli.sendMessage(new Message("", cli.getUsername(), message.getSender(), Message.Type.NO));
            }
        }
        return false;
    }

    public void fileDenied(Message message) {
        JOptionPane.showMessageDialog(null, message.getSender() + " rejected your file", "File rejected", JOptionPane.WARNING_MESSAGE);
    }

    private void exit() {
        if (JOptionPane.showConfirmDialog(null, "Do you really want to go?", "Exit", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/images/sadsmile64.png"))) == JOptionPane.YES_OPTION) {
            cli.sendMessage(new Message("", cli.getUsername(), "SERVER", Message.Type.LOGOUT));
            File f = new File(System.getProperty("java.io.tmpdir") + "mytempfileCLIENTJAVAAPP.tmp");
            if (f.exists()) {
                f.delete();
            }
            this.dispose();
            System.exit(0);
        }
    }

    private void logout() {
        cli.sendMessage(new Message("", cli.getUsername(), "SERVER", Message.Type.LOGOUT));
        cli = null;
        path = null;
        filename = null;
        mfcolor = null;
        mainArea.setText("");
        onlineArea.setText("");
        cliMessage.setText("");
        cli = new Client("127.0.0.1", 5000, this);
        loginFrame = new LoginFrame(cli, this);
        this.setVisible(false);
        loginFrame.setVisible(true);
    }

    public void end() {
        JOptionPane.showConfirmDialog(null, "The server is down...", "Ops..", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/images/redcross64.png")));
        File f = new File(System.getProperty("java.io.tmpdir") + "mytempfileCLIENTJAVAAPP.tmp");
        if (f.exists()) {
            f.delete();
        }
        System.exit(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        mainArea = new javax.swing.JTextArea();
        cliMessage = new javax.swing.JTextField();
        messageLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        onlineArea = new javax.swing.JTextArea();
        sendButton = new javax.swing.JButton();
        onlineLabel = new javax.swing.JLabel();
        startPrivateChatButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        customizeWindow = new javax.swing.JMenuItem();
        logout = new javax.swing.JMenuItem();
        exit = new javax.swing.JMenuItem();
        options = new javax.swing.JMenu();
        changeUser = new javax.swing.JMenuItem();
        changePass = new javax.swing.JMenuItem();
        sendFile = new javax.swing.JMenuItem();
        help = new javax.swing.JMenu();
        instructions = new javax.swing.JMenuItem();
        about = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Chat");
        setLocation(new java.awt.Point(200, 200));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        mainArea.setEditable(false);
        mainArea.setColumns(20);
        mainArea.setLineWrap(true);
        mainArea.setRows(5);
        jScrollPane1.setViewportView(mainArea);

        cliMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cliMessageKeyPressed(evt);
            }
        });

        messageLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        messageLabel.setText("Message:");

        onlineArea.setEditable(false);
        onlineArea.setColumns(20);
        onlineArea.setLineWrap(true);
        onlineArea.setRows(5);
        jScrollPane2.setViewportView(onlineArea);

        sendButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        sendButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/send16.png"))); // NOI18N
        sendButton.setText("Send");
        sendButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        onlineLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        onlineLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        onlineLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/online24.png"))); // NOI18N
        onlineLabel.setText("Online");
        onlineLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        onlineLabel.setIconTextGap(8);

        startPrivateChatButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        startPrivateChatButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/privatechat24.png"))); // NOI18N
        startPrivateChatButton.setText("Private chat");
        startPrivateChatButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        startPrivateChatButton.setIconTextGap(8);
        startPrivateChatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startPrivateChatButtonActionPerformed(evt);
            }
        });

        jMenu1.setText("File");

        customizeWindow.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        customizeWindow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/customize.png"))); // NOI18N
        customizeWindow.setText("Customize window");
        customizeWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customizeWindowActionPerformed(evt);
            }
        });
        jMenu1.add(customizeWindow);

        logout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exit.png"))); // NOI18N
        logout.setText("Logout");
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });
        jMenu1.add(logout);

        exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/RedCross.png"))); // NOI18N
        exit.setText("Exit");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        jMenu1.add(exit);

        jMenuBar1.add(jMenu1);

        options.setText("Options");

        changeUser.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        changeUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/username.png"))); // NOI18N
        changeUser.setText("Change Username");
        changeUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeUserActionPerformed(evt);
            }
        });
        options.add(changeUser);

        changePass.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        changePass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/password.png"))); // NOI18N
        changePass.setText("Change Password");
        changePass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePassActionPerformed(evt);
            }
        });
        options.add(changePass);

        sendFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        sendFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/send_file.png"))); // NOI18N
        sendFile.setText("Send File");
        sendFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendFileActionPerformed(evt);
            }
        });
        options.add(sendFile);

        jMenuBar1.add(options);

        help.setText("Help");

        instructions.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        instructions.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Info2.png"))); // NOI18N
        instructions.setText("Instructions");
        instructions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instructionsActionPerformed(evt);
            }
        });
        help.add(instructions);

        about.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        about.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Info.png"))); // NOI18N
        about.setText("About");
        about.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutActionPerformed(evt);
            }
        });
        help.add(about);

        jMenuBar1.add(help);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(messageLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cliMessage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                    .addComponent(startPrivateChatButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(onlineLabel)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(onlineLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startPrivateChatButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sendButton)
                    .addComponent(cliMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(messageLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        sendMsg();
    }//GEN-LAST:event_sendButtonActionPerformed

    private void cliMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cliMessageKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            sendMsg();
        }
    }//GEN-LAST:event_cliMessageKeyPressed

    private void changeUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeUserActionPerformed
        JTextField olduser = new JTextField();
        JTextField newuser = new JTextField();
        Object[] obj = {
            "Current Username: ", olduser,
            "New Username: ", newuser
        };

        newuser.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (newuser.getText().length() >= 16) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        int opt = JOptionPane.showConfirmDialog(null, obj, "Change Username", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/images/people64.png")));
        if (opt == JOptionPane.OK_OPTION) {
            String oldname = olduser.getText().trim();
            String newname = newuser.getText().trim();
            if (!oldname.isEmpty() && !newname.isEmpty()) {
                if (newname.length() > 3) {

                    if (oldname.equals(cli.getUsername())) {
                        cli.sendMessage(new Message(newname, cli.getUsername(), "SERVER", Message.Type.CHANGEUSER));
                    } else {
                        JOptionPane.showMessageDialog(null, "Current password is not the same!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Username must be bigger than 4 characters.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter all fields!");
            }
        }
    }//GEN-LAST:event_changeUserActionPerformed

    private void changePassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePassActionPerformed
        JTextField oldpass = new JPasswordField();
        JTextField newpass = new JPasswordField();
        JTextField confirmpass = new JPasswordField();
        Object[] obj = {
            "Current Password: ", oldpass,
            "New Password: ", newpass,
            "Re-enter New Password: ", confirmpass
        };

        newpass.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (newpass.getText().length() >= 16) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        int opt = JOptionPane.showConfirmDialog(null, obj, "Change Password", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/images/password64.png")));
        if (opt == JOptionPane.OK_OPTION) {
            String passold = oldpass.getText().trim();
            String passnew = newpass.getText().trim();
            String passconfirm = confirmpass.getText().trim();
            if (passconfirm.equals(passnew)) {
                if (!passold.isEmpty() && !passnew.isEmpty()) {
                    if (passnew.length() > 5) {
                        if (passold.equals(cli.getPassword())) {
                            cli.sendMessage(new Message(passnew, cli.getPassword(), "SERVER", Message.Type.CHANGEPASS));
                        } else {
                            JOptionPane.showMessageDialog(null, "Current password is not the same!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Password must be bigger than 6 characters.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter all fields!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "New password does not match!");
            }
        }
    }//GEN-LAST:event_changePassActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        exit();
    }//GEN-LAST:event_exitActionPerformed

    private void startPrivateChatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startPrivateChatButtonActionPerformed
        JTextField client = new JTextField();
        Object[] obj = {
            "New chat with: ", client
        };
        int opt = JOptionPane.showConfirmDialog(null, obj, "New conversation", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/images/chat64.png")));
        if (opt == JOptionPane.OK_OPTION) {
            String str = client.getText().trim();
            if (!str.equals(cli.getUsername())) {
                if (cli.isOnline(str)) {
                    cli.newPrivateChat(str, true);
                } else {
                    JOptionPane.showMessageDialog(null, "Client '" + str + "' is not online!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "You cannot start a chat between you and you...");
            }
        }
    }//GEN-LAST:event_startPrivateChatButtonActionPerformed

    private void sendFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendFileActionPerformed
        JTextField client = new JTextField();
        JButton filechooser = new JButton("Choose file");
        JLabel label = new JLabel();
        Object[] obj = {
            "Send to: ", client,
            filechooser, label
        };
        filechooser.addActionListener((ActionEvent e) -> {
            JFileChooser fchooser = new JFileChooser();
            fchooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int n = fchooser.showOpenDialog(MainFrame.this);
            if (n == JFileChooser.APPROVE_OPTION) {
                filename = fchooser.getSelectedFile().getName();
                path = fchooser.getSelectedFile().getAbsolutePath();
                label.setText(filename);
            }
        });
        int opt = JOptionPane.showConfirmDialog(null, obj, "File transfer", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/images/load64.png")));
        if (opt == JOptionPane.OK_OPTION) {
            String str = client.getText().trim();
            if (!str.isEmpty()) {
                if (!str.equals(cli.getUsername())) {
                    if (cli.isOnline(str)) {
                        if (path != null && filename != null) {
                            cli.sendMessage(new Message(filename, cli.getUsername(), str, Message.Type.FILE));
                        } else {
                            JOptionPane.showMessageDialog(null, "No file as been chosen!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Client '" + str + "' is not online!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You don't need to send a file to yourself...");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter the client name!");
            }
        }
    }//GEN-LAST:event_sendFileActionPerformed

    private void aboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutActionPerformed
        String str = "This program was developed by Francisco Fialho \n"
                + "for his bachelor thesis of the Computer Science \n"
                + "degree in the West University of Timisoara.";
        JOptionPane.showMessageDialog(null, str, "About", JOptionPane.PLAIN_MESSAGE,
                new javax.swing.ImageIcon(getClass().getResource("/images/info64.png")));
    }//GEN-LAST:event_aboutActionPerformed

    private void instructionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instructionsActionPerformed
        jf = new JFrame("Instructions");
        jf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jf.setMaximumSize(new Dimension(1270, 696));
        jf.setMinimumSize(new Dimension(1270, 696));
        jf.setResizable(false);
        jf.add(new JLabel(new javax.swing.ImageIcon(getClass().getResource("/images/Instructions.png"))));
        jf.setVisible(true);
    }//GEN-LAST:event_instructionsActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exit();
    }//GEN-LAST:event_formWindowClosing

    private void customizeWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customizeWindowActionPerformed
        JButton button = new JButton("Choose color");
        JButton button2 = new JButton("Choose color");
        JButton button3 = new JButton("Choose color");
        JButton button4 = new JButton("Choose color");
        Object[] obj = {
            "Main area:",
            "Text color: ", button,
            "Background color: ", button2,
            "Online area:",
            "Text color: ", button3,
            "Background color: ", button4
        };
        button.addActionListener((ActionEvent e) -> {
            mfcolor = JColorChooser.showDialog(null, "Choose your color", Color.WHITE);
        });
        button2.addActionListener((e) -> {
            mbcolor = JColorChooser.showDialog(null, "Choose your color", Color.WHITE);
        });
        button3.addActionListener((ActionEvent e) -> {
            ofcolor = JColorChooser.showDialog(null, "Choose your color", Color.WHITE);
        });
        button4.addActionListener((e) -> {
            obcolor = JColorChooser.showDialog(null, "Choose your color", Color.WHITE);
        });
        int opt = JOptionPane.showConfirmDialog(null, obj, "Change color", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            mainArea.setForeground(mfcolor);
            mainArea.setBackground(mbcolor);
            onlineArea.setForeground(ofcolor);
            onlineArea.setBackground(obcolor);
        }
    }//GEN-LAST:event_customizeWindowActionPerformed

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        logout();
    }//GEN-LAST:event_logoutActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>
        File f = new File(System.getProperty("java.io.tmpdir") + "mytempfileCLIENTJAVAAPP.tmp");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                return;
            }
        } else {
            return;
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(false);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem about;
    private javax.swing.JMenuItem changePass;
    private javax.swing.JMenuItem changeUser;
    private javax.swing.JTextField cliMessage;
    private javax.swing.JMenuItem customizeWindow;
    private javax.swing.JMenuItem exit;
    private javax.swing.JMenu help;
    private javax.swing.JMenuItem instructions;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenuItem logout;
    private javax.swing.JTextArea mainArea;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JTextArea onlineArea;
    private javax.swing.JLabel onlineLabel;
    private javax.swing.JMenu options;
    private javax.swing.JButton sendButton;
    private javax.swing.JMenuItem sendFile;
    private javax.swing.JButton startPrivateChatButton;
    // End of variables declaration//GEN-END:variables
}
