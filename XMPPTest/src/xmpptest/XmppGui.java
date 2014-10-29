/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xmpptest;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

/**
 *
 * @author Trinity
 */
public class XmppGui extends javax.swing.JFrame {

    /**
     * Creates new form XmppGui
     */
    
    private ConnectionConfiguration connectionConfiguration;
    private XMPPConnection connection;
    
    private ChatManager chatManager;
    private MessageListener messageListener;
    
    private Chat chat;
    
    private static XmppGui XMPP;
    
    private static final String DOMAIN = "redgloves.com";
 
    public XmppGui() {
        initComponents();
    }

    public void login(String username, String password){
        try {
            connection.login(username, password);
            buttonLogin.setText("Successfully Logged In !");
            chatManager = ChatManager.getInstanceFor(connection);
            messageListener = new XmppMessageListener();
            
            chatManager.addChatListener(new ChatManagerListener() {

                @Override
                public void chatCreated(Chat chat, boolean createdLocally) {
                    if(!createdLocally)
                        chat.addMessageListener(messageListener);
                }
            });
            

        Presence presence;
        presence = new Presence(Presence.Type.available);
        presence.setStatus(username+" online");
        connection.sendPacket(presence);
        
        System.out.println("Fetching roster...");
        Roster roster = connection.getRoster();
        roster.createEntry(username, username, null);
        Collection<RosterEntry> rosterEntries = roster.getEntries();
        System.out.println("Roster size :"+rosterEntries.size());
        for(RosterEntry re : rosterEntries){
            String user = re.getUser();
            presence = roster.getPresence(user);
            System.out.println("user :"+user);
            System.out.println("Status :"+presence.getStatus());
        }           
            
        } catch (Exception ex) {
            Logger.getLogger(XmppGui.class.getName()).log(Level.SEVERE, null, ex);
            buttonLogin.setText("Failed to log in. Check credentials.");
            try {
                connection.disconnect();
            } catch (SmackException.NotConnectedException ex1) {
                Logger.getLogger(XmppGui.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } 
    }
    
    
    public void initializeConnection() {
        connectionConfiguration = new ConnectionConfiguration(DOMAIN, 5222);
        try {
            connectionConfiguration.setCustomSSLContext(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(XmppGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        connection = new XMPPTCPConnection(connectionConfiguration);
        try {
            connection.connect();
            labelConnectionStatus.setText("Connection Established");
        } catch (Exception ex) {
            Logger.getLogger(XmppGui.class.getName()).log(Level.SEVERE, null, ex);
            labelConnectionStatus.setText("Failed To Connect");
        } 
        System.out.println("Connection Established");
        
    }

    
    public void addToMessageHistory(String message){
        String oldMessages = messageHistory.getText().toString().trim();
        messageHistory.setText(oldMessages+"\n"+message);
        messageHistory.setCaretPosition(messageHistory.getLineCount()-1);
    }
    
    class XmppMessageListener implements MessageListener {
         
        @Override
        public void processMessage(Chat chat, Message message) {
            String from = message.getFrom().replace("@"+DOMAIN+"/Smack", "");
            String body = message.getBody();
            System.out.println(String.format("Received message '%1$s' from %2$s", body, from));
            addToMessageHistory(from +":"+body);
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

        textUsername = new javax.swing.JTextField();
        textPassword = new javax.swing.JTextField();
        textToUser = new javax.swing.JTextField();
        textMessage = new javax.swing.JTextField();
        buttonSendMessage = new javax.swing.JButton();
        buttonLogin = new javax.swing.JButton();
        labelConnectionStatus = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageHistory = new javax.swing.JTextArea();
        onlineUsers = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        textUsername.setText("Username");
        textUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textUsernameActionPerformed(evt);
            }
        });

        textPassword.setText("Password");
        textPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textPasswordActionPerformed(evt);
            }
        });

        textToUser.setText("To User");
        textToUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textToUserActionPerformed(evt);
            }
        });

        textMessage.setText("Message");
        textMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textMessageActionPerformed(evt);
            }
        });

        buttonSendMessage.setText("Send Message");
        buttonSendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSendMessageActionPerformed(evt);
            }
        });

        buttonLogin.setText("Login");
        buttonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoginActionPerformed(evt);
            }
        });

        labelConnectionStatus.setText("Waiting...");

        messageHistory.setEditable(false);
        messageHistory.setColumns(20);
        messageHistory.setRows(5);
        jScrollPane1.setViewportView(messageHistory);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textUsername, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textPassword, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textToUser, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonSendMessage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                    .addComponent(buttonLogin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(labelConnectionStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(textMessage)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(onlineUsers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelConnectionStatus)
                .addGap(7, 7, 7)
                .addComponent(buttonLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textToUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .addComponent(onlineUsers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(8, 8, 8)
                .addComponent(textMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonSendMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textUsernameActionPerformed

    private void textPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textPasswordActionPerformed

    private void buttonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoginActionPerformed
        // TODO add your handling code here:
        if(!connection.isConnected())
            XMPP.initializeConnection();
        
        if(connection.isConnected()){
            XMPP.login(textUsername.getText().toString().trim(), textPassword.getText().toString().trim());
        } else {
            buttonLogin.setText("Cannot log in. Not connected to server.");
        }
    }//GEN-LAST:event_buttonLoginActionPerformed

    private void buttonSendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSendMessageActionPerformed
        // TODO add your handling code here:
        if(textToUser.getText().toString().trim().equals("To User")){
            buttonSendMessage.setText("SET PROPER USER !");
        }
        if(connection.isAuthenticated()){
            chat = chatManager.createChat(textToUser.getText().toString().trim()+"@"+DOMAIN+"/Smack", messageListener);
            try {
                System.out.println("Sending message :"+textMessage.getText().toString().trim()+" to "+textToUser.getText().toString().trim()+"@"+DOMAIN);
                chat.sendMessage(textMessage.getText().toString().trim());
                addToMessageHistory("You : "+textMessage.getText().toString().trim());
                buttonSendMessage.setText("Message Sent !");
                textMessage.setText("");
            } catch (Exception ex) {
                Logger.getLogger(XmppGui.class.getName()).log(Level.SEVERE, null, ex);
                buttonSendMessage.setText("Failed to send message.");
            } 
        } else {
            buttonSendMessage.setText("Please log in !");
        }
    }//GEN-LAST:event_buttonSendMessageActionPerformed

    private void textMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textMessageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textMessageActionPerformed

    private void textToUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textToUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textToUserActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws SmackException, IOException, XMPPException {
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(XmppGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(XmppGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(XmppGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(XmppGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        XMPP = new XmppGui();
        XMPP.initializeConnection();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                XMPP.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonLogin;
    private javax.swing.JButton buttonSendMessage;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelConnectionStatus;
    private javax.swing.JTextArea messageHistory;
    private javax.swing.JLabel onlineUsers;
    private javax.swing.JTextField textMessage;
    private javax.swing.JTextField textPassword;
    private javax.swing.JTextField textToUser;
    private javax.swing.JTextField textUsername;
    // End of variables declaration//GEN-END:variables
}
