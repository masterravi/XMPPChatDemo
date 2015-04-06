package com.demo.xmppchat.utils;

import android.content.Context;

import com.demo.xmppchat.common.XMPPChatDemoApplication;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.ChatState;
import org.jivesoftware.smackx.ChatStateListener;
import org.jivesoftware.smackx.muc.InvitationListener;

import java.util.Collection;

public class XmppUtils {

	private final String TAG = getClass().getSimpleName();
	private Context context;
	// private final int SINGLE_CHAT = 0, GROUP_CHAT = 1;
	private XMPPChatDemoApplication application;
	private String receivedMessage = "";
	private String receiverName = "";
	private String strFromName, strToName, strMessageBody;
	private String[] arrFromName, arrToName;
	private boolean isTyping = false;

	public XmppUtils(final Context context) {
		this.context = context;
		this.application = (XMPPChatDemoApplication) context.getApplicationContext();
	}

	public RosterListener rosterListener = new RosterListener() {

		@Override
		public void presenceChanged(Presence presence) {
			// Log.e(TAG, "Presence Changed : " + presence.toXML());
		}

		@Override
		public void entriesUpdated(Collection<String> arg0) {

		}

		@Override
		public void entriesDeleted(Collection<String> arg0) {

		}

		@Override
		public void entriesAdded(Collection<String> arg0) {

		}
	};

	public ConnectionListener connectionListener = new ConnectionListener() {

		@Override
		public void reconnectionSuccessful() {

		}

		@Override
		public void reconnectionFailed(Exception arg0) {

		}

		@Override
		public void reconnectingIn(int arg0) {

		}

		@Override
		public void connectionClosedOnError(Exception arg0) {

		}

		@Override
		public void connectionClosed() {

		}
	};

	public class MessageListenerImpl implements MessageListener,
			ChatStateListener {

		@Override
		public void stateChanged(Chat chat, ChatState chatState) {
			if (ChatState.active.equals(chatState)) {
//				Log.e(TAG, "State Changed is ACTIVE");

			} else if (ChatState.inactive.equals(chatState)) {
//				Log.e(TAG, "State Changed is INACTIVE");

			} else if (ChatState.composing.equals(chatState)) {
//				Log.e(TAG, "State Changed is COMPOSING");

			} else if (ChatState.paused.equals(chatState)) {
//				Log.e(TAG, "State Changed is PAUSED");

			} else if (ChatState.gone.equals(chatState)) {
//				Log.e(TAG, "State Changed is GONE");
			} else {
//				Log.e(TAG, "Else Part....");
			}
		}

		@Override
		public void processMessage(Chat chat, Message message) {
			try {
				chat.sendMessage(message.getBody());
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
	}

	public InvitationListener invitationListener = new InvitationListener() {

		@Override
		public void invitationReceived(Connection connection, String room,
				String invitor, String reason, String unknown, Message message) {

			final Presence groupPresence = new Presence(Presence.Type.available);
			groupPresence.setTo(room + "/" + connection.getUser());
			connection.sendPacket(groupPresence);


			receivedMessage = invitor + " has added you in group " + room;

			// notificationTask(context, SingleChatFragment.class);
		}
	};

}