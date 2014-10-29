package com.krsna.xmppclient;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	
	EditText editUsername, editPassword, editSendMessage, editTextToUser;
	Button buttonLogin, buttonSendMessage;
	TextView textViewMessageHistory;
	ImageView imageViewConnStatus;
	
    private static final int packetReplyTimeout = 500;
    private String server;
    private int port;
    
    private ConnectionConfiguration connectionConfiguration;
    private static XMPPConnection connection;
    
    private ChatManager chatManager;
    private MessageListener messageListener;
    
    private Chat chat;
    
    private static boolean LOGGED_IN = false;
    
    private static final String DOMAIN = "61.16.137.212";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        initializeViews();
        initializeConnection();
        
        setButtonListeners();
    }    

    private void setButtonListeners() {
		// TODO Auto-generated method stub
		buttonLogin.setOnClickListener(this);
		buttonSendMessage.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		connectToProsidy();
	}

	public void initializeConnection(){
    	connectionConfiguration = new ConnectionConfiguration(DOMAIN, 5222);
    	connection = new XMPPTCPConnection(connectionConfiguration);
    }
    
	public void login(String username, String password){
		try{
			connection.login(username, password);
			buttonLogin.setText("Successfully logged in !");
			
			chatManager = ChatManager.getInstanceFor(connection);
			messageListener = new XMPPListener();
			chatManager.addChatListener(new ChatManagerListener() {
				
				@Override
				public void chatCreated(Chat chat, boolean createdLocally) {
					// TODO Auto-generated method stub
					if(!createdLocally)
						chat.addMessageListener(messageListener);
				}
			});
		} catch(Exception e){
			e.printStackTrace();
			buttonLogin.setText("Failed to log in. Check credentials.");
            try {
                connection.disconnect();
            } catch (Exception e1) {
            	e1.printStackTrace();
            }
		}
		
	}
	
    public void connectToProsidy(){
    	try {
			connection.connect();
			Log.d(getClass().getSimpleName(), "Connected to Prosidy server");
			imageViewConnStatus.setBackgroundResource(R.drawable.online);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			imageViewConnStatus.setBackgroundResource(R.drawable.offline);
		}
    }
    
	private void initializeViews() {
		// TODO Auto-generated method stub
		editUsername = (EditText)findViewById(R.id.editUsername);
		editPassword = (EditText)findViewById(R.id.editPassword);
		editSendMessage = (EditText)findViewById(R.id.editSendMessage);
		editTextToUser = (EditText)findViewById(R.id.editTextToUser);
		
		buttonLogin = (Button)findViewById(R.id.buttonLogin);
		buttonSendMessage = (Button)findViewById(R.id.buttonSendMessage);
		
		textViewMessageHistory = (TextView)findViewById(R.id.textViewMessageHistory);
		
		imageViewConnStatus = (ImageView)findViewById(R.id.imageViewConnStatus);
		
	}
	
	class XMPPListener implements MessageListener{

		@Override
		public void processMessage(Chat chat, Message msg) {
			// TODO Auto-generated method stub
			String from = msg.getFrom().replace("@"+DOMAIN+"/Smack", "");
			String body = msg.getBody();
			Log.d(getClass().getSimpleName(), "Received :"+body+" FROM "+from);
			new addToMessageHistory().execute(from+":"+body);
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.buttonLogin:
			if(TextUtils.isEmpty(editUsername.getText().toString().trim())||TextUtils.isEmpty(editPassword.getText().toString().trim())){
				Toast.makeText(getApplicationContext(), "Please enter both - username and password.", Toast.LENGTH_LONG).show();
			} else {
				login(editUsername.getText().toString().trim(), editPassword.getText().toString().trim());
			}
			break;
		case R.id.buttonSendMessage:
			if(!TextUtils.isEmpty(editTextToUser.getText().toString().trim())){
				if(connection.isAuthenticated()){
					if(!TextUtils.isEmpty(editSendMessage.getText().toString().trim())){
						chat = chatManager.createChat(editTextToUser.getText().toString().trim()+"@"+DOMAIN, messageListener);					
						try {
							String messageToSend = editSendMessage.getText().toString().trim();
							chat.sendMessage(messageToSend);
							new addToMessageHistory().execute(messageToSend);
							editSendMessage.setText("");
						} catch (NotConnectedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (XMPPException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						Toast.makeText(getApplicationContext(), "Please enter you message.", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "Please log in..", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "Please enter recepients userID", Toast.LENGTH_LONG).show();
			}
			break;
		}
	}

	public class addToMessageHistory extends AsyncTask<String, Void, String>{
		// TODO Auto-generated method stub
		
		String newMessages;
		@Override
		protected String doInBackground(String... msg) {
			// TODO Auto-generated method stub
			newMessages = msg[0];
			Log.d(getClass().getSimpleName(), "Adding from doInBackground :"+newMessages);
			return newMessages;
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			String oldMessages = textViewMessageHistory.getText().toString().trim();
			
			String updatedText = oldMessages+"\n"+newMessages;
			Log.d(getClass().getSimpleName(), "Adding from onPostExecute :"+updatedText);
			textViewMessageHistory.setText(updatedText);
		}



	}
}
