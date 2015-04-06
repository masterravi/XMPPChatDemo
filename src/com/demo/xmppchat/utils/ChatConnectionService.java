package com.demo.xmppchat.utils;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import com.demo.xmppchat.R;
import com.demo.xmppchat.common.XMPPChatDemoApplication;

public class ChatConnectionService extends IntentService {

	private XMPPChatDemoApplication application;
	private SharedPreferences pref;
	private String TAG = ChatConnectionService.class.getSimpleName();
//	private boolean isXmppConnected = false;
	
	public ChatConnectionService() {
		super("ChatConnection Service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		application = (XMPPChatDemoApplication) getApplication();
		pref = application.getSharedPreferences();
	}
	
	/**
	 * Make connection to jabber using smack
	 */
	
	private void makeChatConnection() {
		try {
			final String userId = pref.getString(getString(R.string.pref_user_id), "");
//			final String password = pref.getString(getString(R.string.pref_user_id), "");
			
				final boolean isChatConnected = ChatUtils.chatConnection(this, userId, "indianic" + userId);
				
//				Log.e(TAG, "isChatConnected : " + isChatConnected);
				 
				if ( isChatConnected ) {
					final SharedPreferences.Editor editor = pref.edit();
					editor.putBoolean("chat_login", true);
					editor.commit();
				} else {
					//Toast.makeText(this, R.string.chat_server_not_connected, Toast.LENGTH_SHORT).show();
				}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	


}