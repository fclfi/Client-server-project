package Chatserver;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 *
 * @author Asus
 */
public class LoginFrame extends JFrame {

    private Client cli;
    private MainFrame mn;
    private boolean flag;

    /**
     * Creates new form LoginFrame
     *
     * @param cli
     * @param mn
     */
    public LoginFrame(Client cli, MainFrame mn) {
        initComponents();
        this.cli = cli;
        this.mn = mn;
        this.flag = true;
        Timer timer = new Timer(500, (ActionEvent e) -> {
            if (flag) {
                image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/red-talk-icon.png")));
            } else {
                image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/green-talk-icon.png")));
            }
            flag = !flag;
        });
        timer.start();
    }

    private void login() {
        char[] password = passField.getPassword();
        String name = userField.getText().trim();
        StringBuilder aux = new StringBuilder();
        for (int i = 0; i < password.length; i++) {
            aux.append(password[i]);
        }
        String pass = aux.toString();
        if (name.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter all fields!");
        } else {
            if (!cli.start()) {
                JOptionPane.showMessageDialog(null, "Connection failed");
                return;
            }
            if (!cli.login(name, pass)) {
                JOptionPane.showMessageDialog(null, "Incorrect username or password", "", JOptionPane.INFORMATION_MESSAGE,
                        new javax.swing.ImageIcon(getClass().getResource("/images/emoji1.png")));
            } else {
                JOptionPane.showMessageDialog(null, "Welcome back " + name + "!!", "", JOptionPane.INFORMATION_MESSAGE,
                        new javax.swing.ImageIcon(getClass().getResource("/images/emoji.png")));
                mn.setVisible(true);
                this.setVisible(false);
                cli.startListenServer();
            }
        }
    }

    private void signUp() {
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        Object[] message = {
            "Username: ", username,
            "Password: ", password
        };
        boolean flg = true;
        int opt;

        username.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (username.getText().length() >= 16) {
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

        password.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char[] pass = password.getPassword();
                StringBuilder aux = new StringBuilder();
                for (int i = 0; i < pass.length; i++) {
                    aux.append(pass[i]);
                }
                String pass2 = aux.toString();
                if (pass2.length() >= 16) {
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

        while (flg) {
            opt = JOptionPane.showConfirmDialog(null, message, "Sign up", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/images/signup64.png")));
            if (opt == JOptionPane.OK_OPTION) {
                char[] pass = password.getPassword();
                String name = username.getText().trim();
                StringBuilder aux = new StringBuilder();
                for (int i = 0; i < pass.length; i++) {
                    aux.append(pass[i]);
                }
                String pass2 = aux.toString();
                if (name.contains("server")) {
                    JOptionPane.showMessageDialog(null, "Invalid username!");
                } else {
                    if (name.isEmpty() || pass2.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter all fields!");
                    } else {
                        if (name.length() < 3 || pass2.length() < 6) {
                            JOptionPane.showMessageDialog(null, "Your name or password is too small!"
                                    + "\nName must be bigger than 4 characters.\nPassword must be bigger than 6 characters.");
                        } else {

                            if (name.contains(pass2)) {
                                JOptionPane.showMessageDialog(null, "Your name shound not contain your password!");
                            } else {
                                if (!cli.start()) {
                                    JOptionPane.showMessageDialog(null, "Connection failed");
                                    return;
                                }
                                if (!cli.signUp(name, pass2)) {
                                    JOptionPane.showMessageDialog(null, "Username already exists");
                                    username.setText("");
                                    password.setText("");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Welcome to the chat " + name + "!! Enjoy!", "", JOptionPane.INFORMATION_MESSAGE,
                                            new javax.swing.ImageIcon(getClass().getResource("/images/emoji.png")));
                                    mn.setVisible(true);
                                    this.setVisible(false);
                                    cli.startListenServer();
                                    return;
                                }
                            }
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        userField = new javax.swing.JTextField();
        loginButton = new javax.swing.JButton();
        passField = new javax.swing.JPasswordField();
        signUpButton = new javax.swing.JButton();
        image = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setForeground(java.awt.Color.gray);
        setLocation(new java.awt.Point(200, 200));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("Username:");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("Password:");

        jLabel3.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        jLabel3.setText("Welcome to the Family Chat!");

        userField.setForeground(new java.awt.Color(60, 63, 65));
        userField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                userFieldKeyTyped(evt);
            }
        });

        loginButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        passField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextPasswordField1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                passFieldKeyTyped(evt);
            }
        });

        signUpButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        signUpButton.setText("Sign up");
        signUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signUpButtonActionPerformed(evt);
            }
        });

        image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/green-talk-icon.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(passField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(userField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(loginButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(signUpButton)))))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(userField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(passField, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(loginButton)
                            .addComponent(signUpButton))
                        .addGap(73, 73, 73))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        login();
    }//GEN-LAST:event_loginButtonActionPerformed

    private void signUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signUpButtonActionPerformed
        signUp();
    }//GEN-LAST:event_signUpButtonActionPerformed

    private void jTextPasswordField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPasswordField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            login();
        }
    }//GEN-LAST:event_jTextPasswordField1KeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        File f = new File(System.getProperty("java.io.tmpdir") + "mytempfileCLIENTJAVAAPP.tmp");
        if (f.exists()) {
            f.delete();
        }
    }//GEN-LAST:event_formWindowClosing

    private void userFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_userFieldKeyTyped
        if (userField.getText().length() >= 16) {
            evt.consume();
        }
    }//GEN-LAST:event_userFieldKeyTyped

    private void passFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passFieldKeyTyped
        char[] password = passField.getPassword();
        StringBuilder aux = new StringBuilder();
        for (int i = 0; i < password.length; i++) {
            aux.append(password[i]);
        }
        String pass = aux.toString();
        if (pass.length() >= 16) {
            evt.consume();
        }
    }//GEN-LAST:event_passFieldKeyTyped
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel image;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton loginButton;
    private javax.swing.JPasswordField passField;
    private javax.swing.JButton signUpButton;
    private javax.swing.JTextField userField;
    // End of variables declaration//GEN-END:variables
}
