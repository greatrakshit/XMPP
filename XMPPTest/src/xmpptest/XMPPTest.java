/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmpptest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

/**
 *
 * @author Trinity
 */
public class XMPPTest {

    /**
     * @param args the command line arguments
     */
    XMPPConnection connection;
    String msg;
    ChatManager chatManager;
    String USER = "mobile";
    String PASSWORD = "mobile";
    private static final String DOMAIN = "redgloves.com";
    
    
    String TO_USER = "krsna";

    public void createUser() throws SmackException, IOException, XMPPException{
        ConnectionConfiguration configuration = new ConnectionConfiguration(DOMAIN, 5222);

       connection = new XMPPTCPConnection(configuration);
       connection.connect();
       
       AccountManager accountManager = AccountManager.getInstance(connection);
       accountManager.createAccount(USER, PASSWORD);        
       // connection.disconnect();
    }
    public static void main(String[] args) throws SmackException, IOException, XMPPException {
        // TODO code application logic here
        new XMPPTest().createUser();
    }    
}
