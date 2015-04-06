package com.demo.xmppchat.ui;

import org.jivesoftware.smack.XMPPConnection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.demo.xmppchat.R;

public class MainActivity extends Activity implements OnClickListener {

	private ListView listview;
	private XMPPConnection connection;
	private Button register;
	private Button deleteButton;
	private Button logoutButton;
	private Button loginButton;
	private Button messageButton;

	private EditText userName;
	private EditText userPassword;
	private EditText message;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		new XmppConnection(this);
		XmppConnection.getInstance().getConnection();

		// Set a listener to send a chat text message
		register = (Button) this.findViewById(R.id.registerBtn);
		deleteButton = (Button) this.findViewById(R.id.deleteBtn);
		loginButton = (Button) this.findViewById(R.id.loginBtn);
		logoutButton = (Button) this.findViewById(R.id.logoutBtn);
		messageButton=(Button) this.findViewById(R.id.sendMessageBtn);
		userName = (EditText) this.findViewById(R.id.userName);
		userPassword = (EditText) this.findViewById(R.id.userPassword);
		message = (EditText) this.findViewById(R.id.message);

		register.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);
		logoutButton.setOnClickListener(this);
		messageButton.setOnClickListener(this);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.registerBtn) {
			String result = XmppConnection.getInstance().register(userName.getText().toString(),userPassword.getText().toString());
			if (result.equals("1")) {
				XmppConnection.getInstance().login(userName.getText().toString(),userPassword.getText().toString());
			}

		} else if (v.getId() == R.id.deleteBtn) {
			XmppConnection.getInstance().deleteAccount();
		} else if (v.getId() == R.id.loginBtn) {
				XmppConnection.getInstance().login(userName.getText().toString(),userPassword.getText().toString());
		} else if (v.getId() == R.id.logoutBtn) {
			XmppConnection.getInstance().setPresence(5);
		} else if (v.getId() == R.id.sendMessageBtn) {
			XmppConnection.getInstance().sentMessage(message.getText().toString(), "rikin@ind282.local");;
		}

	}

}
