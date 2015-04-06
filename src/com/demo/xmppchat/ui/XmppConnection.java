package com.demo.xmppchat.ui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.demo.xmppchat.R;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.OfflineMessageManager;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.RosterExchangeManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class XmppConnection {

	public static final String HOST = "10.2.4.162";
	public static final int PORT = 5222;
	public static final String SERVER_NAME = "ind282.local";
	private boolean userState = false;

	private XMPPConnection connection;
	private ArrayList<String> messages = new ArrayList<String>();
	private Handler mHandler = new Handler();

	private static XmppConnection instance;
	private Context context;

	public XmppConnection(Context context) {
		instance = this;
		this.context = context;
	}

	public void sentMessage(String message,String to) {
        Message msg = new Message(to, Message.Type.chat);
        msg.setBody(message);
		if (connection != null) {
			connection.sendPacket(msg);
			messages.add(connection.getUser() + ":  " + message);
			// messages.add(connection.getUser() + ":");
			// messages.add(text);
			Log.v("text send", connection.getUser().toString());
			Toast.makeText(context, "txt send", Toast.LENGTH_SHORT)
					.show();
			Roster roster = connection.getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) {
				Log.d("XMPPChatDemoActivity",
						"--------------------------------------");
				Log.d("XMPPChatDemoActivity", "RosterEntry " + entry);
				Log.d("XMPPChatDemoActivity", "User: " + entry.getUser());
				Log.d("XMPPChatDemoActivity", "Name: " + entry.getName());
				Log.d("XMPPChatDemoActivity", "Status: " + entry.getStatus());
				Log.d("XMPPChatDemoActivity", "Type: " + entry.getType());
				Presence entryPresence = roster.getPresence(entry.getUser());
				Log.d("XMPPChatDemoActivity", "Presence Status: "
						+ entryPresence.getStatus());
				Log.d("XMPPChatDemoActivity",
						"Presence Type: " + entryPresence.getType());
				Presence.Type type = entryPresence.getType();
				if (type == Presence.Type.available)
					Log.d("XMPPChatDemoActivity", "Presence AVIALABLE");
				Log.d("XMPPChatDemoActivity", "Presence : " + entryPresence);
			}

		} else {
			Log.i("XMPPChatDemoActivity", "no massage send");
			Log.e("XMPPChatDemoActivity",
					"Failed to log in as " + connection.getUser());
		}
	}

	/**
	 * Called by Settings dialog when a connection is establised with the XMPP
	 * server
	 * 
	 * @param connection
	 */
	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
		if (connection != null) {
			// Add a packet listener to get messages sent to us
			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
			connection.addPacketListener(new PacketListener() {
				@Override
				public void processPacket(Packet packet) {
					Message message = (Message) packet;
					if (message.getBody() != null) {
						String fromName = StringUtils.parseBareAddress(message
								.getFrom());
						Log.i("XMPPChatDemoActivity", "Text Recieved "
								+ message.getBody() + " from " + fromName);
						messages.add(fromName + ":");
						messages.add(message.getBody());
						// Add the incoming message to the list view
						mHandler.post(new Runnable() {
							public void run() {
								// setListAdapter();
							}
						});
						
						// Open NotificationView Class on Notification Click
						Intent intent = new Intent(context, NotificationView.class);
						// Send data to NotificationView Class
						intent.putExtra("title", "Chat Message");
						intent.putExtra("text", message.getBody());
						// Open NotificationView.java Activity
						PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
								PendingIntent.FLAG_UPDATE_CURRENT);
						
						//Set mnessage in notification 
						// Create Notification using NotificationCompat.Builder
						NotificationCompat.Builder builder = new NotificationCompat.Builder(
								context)
								// Set Icon
								.setSmallIcon(R.drawable.ic_launcher)
								// Set Ticker Message
								.setTicker(message.getBody())
								// Set Title
								.setContentTitle(message.getBody())
								// Set Text
								.setContentText(message.getBody())
								// Add an Action Button below Notification
								.addAction(R.drawable.ic_launcher, "Action Button", pIntent)
								// Set PendingIntent into Notification
								.setContentIntent(pIntent)
								// Dismiss Notification
								.setAutoCancel(true);
				 
						// Create Notification Manager
						NotificationManager notificationmanager = (NotificationManager) context
								.getSystemService(Context.NOTIFICATION_SERVICE);
						// Build Notification with Notification Manager
						notificationmanager.notify(0, builder.build());
					}
				}
			}, filter);
		}
	}

	public XMPPConnection getConnection() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// Create a connection
				ConnectionConfiguration config = new ConnectionConfiguration(
						HOST, PORT, SERVER_NAME);
				config.setReconnectionAllowed(true);
				config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
				config.setSendPresence(true); // The state is set to offline, in
												// order to get offline message
				config.setSASLAuthenticationEnabled(false); // Whether to enable
															// the security
															// verification
				config.setTruststorePath("/system/etc/security/cacerts.bks");
				config.setTruststorePassword("changeit");
				config.setTruststoreType("bks");
				connection = new XMPPConnection(config);
				// setConnection(connection);

				try {
					connection.connect();
					Log.i("XMPPChatDemoActivity",
							"Connected to " + connection.getHost());
				} catch (XMPPException ex) {
					Log.e("XMPPChatDemoActivity", "Failed to connect to "
							+ connection.getHost());
					Log.e("XMPPChatDemoActivity", ex.toString());
					setConnection(null);
				}
			}
		});
		t.start();
		return connection;
	}

	public XMPPConnection getConnectionobject() {
		return connection;
	}

	/**
	 * Close the connection
	 */
	public void closeConnection() {
		if (connection != null) {
			// Remove the connection monitor
			// connection.removeConnectionListener(connectionListener);
			if (connection.isConnected())
				connection.disconnect();
			Toast.makeText(context, "Successfully Logout", Toast.LENGTH_SHORT)
					.show();
			connection = null;

		}
		Log.i("XmppConnection", "Close the connection");
	}

	/**
	 * Sign in
	 * 
	 * @param account
	 *            The logon account
	 * @param password
	 *            Login password
	 * @return
	 */
	public boolean login(String account, String password) {
		if (connection == null) {
			Toast.makeText(context, "Please connect again to server.",
					Toast.LENGTH_SHORT).show();
		}
		if (connection.getUser() != null) {
			Toast.makeText(context, "User already login.Please logout first.",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		// //////////////////////////////////////////////////
		try {
			// SASLAuthentication.supportSASLMechanism("PLAIN", 0);
			connection.login(account, password);
			Log.i("XMPPChatDemoActivity",
					"Logged in as " + connection.getUser());

			// Set the status to available
			Presence presence = new Presence(Presence.Type.available);
			connection.sendPacket(presence);
			setConnection(connection);
			//
			Roast roster = connection.getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) {
				Log.d("XMPPChatDemoActivity",
						"--------------------------------------");
				Log.d("XMPPChatDemoActivity", "RosterEntry " + entry);
				Log.d("XMPPChatDemoActivity", "User: " + entry.getUser());
				Log.d("XMPPChatDemoActivity", "Name: " + entry.getName());
				Log.d("XMPPChatDemoActivity", "Status: " + entry.getStatus());
				Log.d("XMPPChatDemoActivity", "Type: " + entry.getType());
				Presence entryPresence = roster.getPresence(entry.getUser());

				Log.d("XMPPChatDemoActivity", "Presence Status: "
						+ entryPresence.getStatus());
				Log.d("XMPPChatDemoActivity",
						"Presence Type: " + entryPresence.getType());
				Presence.Type type = entryPresence.getType();
				if (type == Presence.Type.available)
					Log.d("XMPPChatDemoActivity", "Presence AVIALABLE");
				Log.d("XMPPChatDemoActivity", "Presence : " + entryPresence);

			}
		} catch (XMPPException ex) {
			Log.e("XMPPChatDemoActivity", "Failed to log in as " + account);
			Log.e("XMPPChatDemoActivity", ex.toString());
			setConnection(null);
		}
		// //////////////////////////////////////////////////
		// connection.login(account, password);
		// // Change your status
		// Presence presence = new Presence(Presence.Type.available);
		// connection.sendPacket(presence);
		Toast.makeText(context, account + "Successfully Login into system",
				Toast.LENGTH_SHORT).show();
		// Add connection monitor
		return true;
	}

	/**
	 * Registration `
	 * 
	 * @param account
	 *            A registered account
	 * @param password
	 *            The login password
	 * @return 1, Successfully registered 0, the server returned no results in
	 *         2, the 3, failed to register account already exists
	 */
	public String register(String account, String password) {
		if (getConnection() == null)
			return "0";
		Registration reg = new Registration();
		reg.setType(IQ.Type.SET);
		reg.setTo(getConnection().getServiceName());
		// Note the createAccount registration, parameter is UserName, not Jid,
		// is " @" the front part of the.
		reg.setUsername(account);
		reg.setPassword(password);
		// This addAttribute cannot be empty, otherwise an error. So do mark is
		// Android mobile phone created！！！！！
		reg.addAttribute("android", "geolo_createUser_android");
		PacketFilter filter = new AndFilter(new PacketIDFilter(
				reg.getPacketID()), new PacketTypeFilter(IQ.class));
		PacketCollector collector = getConnection().createPacketCollector(
				filter);
		getConnection().sendPacket(reg);
		IQ result = (IQ) collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		// Stop queuing Results stop request results (whether successful
		// results)
		collector.cancel();
		if (result == null) {
			Toast.makeText(context, "No Response from server",
					Toast.LENGTH_SHORT).show();
			return "0";
		} else if (result.getType() == IQ.Type.RESULT) {
			Toast.makeText(context, "Registration Successful",
					Toast.LENGTH_SHORT).show();
			return "1";
		} else { // if (result.getType() == IQ.Type.ERROR)
			Toast.makeText(context, "User Already Exist", Toast.LENGTH_SHORT)
					.show();
			if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
				Log.e("regist", "IQ.Type.ERROR: "
						+ result.getError().toString());
				return "2";
			} else {
				Log.e("regist", "IQ.Type.ERROR: "
						+ result.getError().toString());
				return "3";
			}
		}
	}

	/**
	 * Change user state
	 */
	public void setPresence(int code) {
		XMPPConnection con = getConnection();
		if (con == null)
			return;
		Presence presence;
		switch (code) {
		case 0:
			presence = new Presence(Presence.Type.available);
			con.sendPacket(presence);
			Log.v("state", "Set online");
			break;
		case 1:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.chat);
			con.sendPacket(presence);
			Log.v("state", "Set Q to me");
			break;
		case 2:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.dnd);
			con.sendPacket(presence);
			Log.v("state", "Set the busy");
			break;
		case 3:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.away);
			con.sendPacket(presence);
			Log.v("state", "Set off");
			break;
		case 4:
			Roster roster = con.getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) {
				presence = new Presence(Presence.Type.unavailable);
				presence.setPacketID(Packet.ID_NOT_AVAILABLE);
				presence.setFrom(con.getUser());
				presence.setTo(entry.getUser());
				con.sendPacket(presence);
				Log.v("state", presence.toXML());
			}
			// To the other client sends the same user stealth
			presence = new Presence(Presence.Type.unavailable);
			presence.setPacketID(Packet.ID_NOT_AVAILABLE);
			presence.setFrom(con.getUser());
			presence.setTo(StringUtils.parseBareAddress(con.getUser()));
			con.sendPacket(presence);
			Log.v("state", "Set up stealth");
			break;
		case 5:
			presence = new Presence(Presence.Type.unavailable);
			con.sendPacket(presence);
			Log.v("state", "Set offline");
			break;
		default:
			break;
		}
	}

	/**
	 * Get all the group
	 * 
	 * @return All set
	 */
	public List<RosterGroup> getGroups() {

		if (getConnection() == null)
			return null;
		List<RosterGroup> grouplist = new ArrayList<RosterGroup>();
		Collection<RosterGroup> rosterGroup = getConnection().getRoster()
				.getGroups();
		Iterator<RosterGroup> i = rosterGroup.iterator();
		while (i.hasNext()) {
			grouplist.add(i.next());
		}
		return grouplist;
	}

	/**
	 * Gets all of a group of friends
	 * 
	 * @param roster
	 * @param groupName
	 *            Group name
	 * @return
	 */
	public List<RosterEntry> getEntriesByGroup(String groupName) {
		if (getConnection() == null)
			return null;
		List<RosterEntry> Entrieslist = new ArrayList<RosterEntry>();
		RosterGroup rosterGroup = getConnection().getRoster().getGroup(
				groupName);
		Collection<RosterEntry> rosterEntry = rosterGroup.getEntries();
		Iterator<RosterEntry> i = rosterEntry.iterator();
		while (i.hasNext()) {
			Entrieslist.add(i.next());
		}
		return Entrieslist;
	}

	/**
	 * Get all the friends of information
	 * 
	 * @return
	 */
	public List<RosterEntry> getAllEntries() {
		if (getConnection() == null)
			return null;
		List<RosterEntry> Entrieslist = new ArrayList<RosterEntry>();
		Collection<RosterEntry> rosterEntry = getConnection().getRoster()
				.getEntries();
		Iterator<RosterEntry> i = rosterEntry.iterator();
		while (i.hasNext()) {
			Entrieslist.add(i.next());
		}
		return Entrieslist;
	}

	/**
	 * To obtain the user VCard information
	 * 
	 * @param connection
	 * @param user
	 * @return
	 * @throws XMPPException
	 */
	public VCard getUserVCard(String user) {
		if (getConnection() == null)
			return null;
		VCard vcard = new VCard();
		try {
			vcard.load(getConnection(), user);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return vcard;
	}

	/**
	 * Gets the user avatar information
	 * 
	 * @param connection
	 * @param user
	 * @return
	 */
	public Drawable getUserImage(String user) {
		if (getConnection() == null)
			return null;
		ByteArrayInputStream bais = null;
		try {
			VCard vcard = new VCard();
			// Add this code, No VCard for
			ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp",
					new org.jivesoftware.smackx.provider.VCardProvider());
			if (user == "" || user == null || user.trim().length() <= 0) {
				return null;
			}
			vcard.load(getConnection(), user + "@"
					+ getConnection().getServiceName());

			if (vcard == null || vcard.getAvatar() == null)
				return null;
			bais = new ByteArrayInputStream(vcard.getAvatar());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Bitmap b = BitmapFactory.decodeStream(bais);
		b.setDensity(Bitmap.DENSITY_NONE);
		Drawable d = new BitmapDrawable(b);
		return d;
	}

	/**
	 * Add a group
	 * 
	 * @param groupName
	 * @return
	 */
	public boolean addGroup(String groupName) {
		if (getConnection() == null)
			return false;
		try {
			getConnection().getRoster().createGroup(groupName);
			Log.v("addGroup", groupName + "Successfully created");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Delete group
	 * 
	 * @param groupName
	 * @return
	 */
	public boolean removeGroup(String groupName) {
		return true;
	}

	/**
	 * Add a friend no packet
	 * 
	 * @param userName
	 * @param name
	 * @return
	 */
	public boolean addUser(String userName, String name) {
		if (getConnection() == null)
			return false;
		try {
			getConnection().getRoster().createEntry(userName, name, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Add a friend packets
	 * 
	 * @param userName
	 * @param name
	 * @param groupName
	 * @return
	 */
	public boolean addUser(String userName, String name, String groupName) {
		if (getConnection() == null)
			return false;
		try {
			Presence subscription = new Presence(Presence.Type.subscribed);
			subscription.setTo(userName);
			userName += "@" + getConnection().getServiceName();
			getConnection().sendPacket(subscription);
			getConnection().getRoster().createEntry(userName, name,
					new String[] { groupName });
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Delete friends
	 * 
	 * @param userName
	 * @return
	 */
	public boolean removeUser(String userName) {
		if (getConnection() == null)
			return false;
		try {
			RosterEntry entry = null;
			if (userName.contains("@"))
				entry = getConnection().getRoster().getEntry(userName);
			else
				entry = getConnection().getRoster().getEntry(
						userName + "@" + getConnection().getServiceName());
			if (entry == null)
				entry = getConnection().getRoster().getEntry(userName);
			getConnection().getRoster().removeEntry(entry);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Query user
	 * 
	 * @param userName
	 * @return
	 * @throws XMPPException
	 */
	public List<HashMap<String, String>> searchUsers(String userName) {
		if (getConnection() == null)
			return null;
		HashMap<String, String> user = null;
		List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
		try {
			new ServiceDiscoveryManager(getConnection());

			UserSearchManager usm = new UserSearchManager(getConnection());

			Form searchForm = usm.getSearchForm(getConnection()
					.getServiceName());
			Form answerForm = searchForm.createAnswerForm();
			answerForm.setAnswer("userAccount", true);
			answerForm.setAnswer("userPhote", userName);
			org.jivesoftware.smackx.ReportedData data = usm.getSearchResults(
					answerForm, "search" + getConnection().getServiceName());

			Iterator<Row> it = data.getRows();
			Row row = null;
			while (it.hasNext()) {
				user = new HashMap<String, String>();
				row = it.next();
				user.put("userAccount", row.getValues("userAccount").next()
						.toString());
				user.put("userPhote", row.getValues("userPhote").next()
						.toString());
				results.add(user);
				// If there is, is returned, UserName must be non empty, the
				// other two if there is a non empty set
			}
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * Modify the mood
	 * 
	 * @param connection
	 * @param status
	 */
	public void changeStateMessage(String status) {
		if (getConnection() == null)
			return;
		Presence presence = new Presence(Presence.Type.available);
		presence.setStatus(status);
		getConnection().sendPacket(presence);
	}

	/**
	 * Modify user Avatar
	 * 
	 * @param file
	 */
	public boolean changeImage(File file) {
		if (getConnection() == null)
			return false;
		try {
			VCard vcard = new VCard();
			vcard.load(getConnection());

			byte[] bytes;

			bytes = getFileBytes(file);
			String encodedImage = StringUtils.encodeBase64(bytes);
			vcard.setAvatar(bytes, encodedImage);
			vcard.setField("PHOTO", "<TYPE>image/jpg</TYPE><BINVAL>"
					+ encodedImage + "</BINVAL>", true);

			ByteArrayInputStream bais = new ByteArrayInputStream(
					vcard.getAvatar());

			vcard.save(getConnection());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * File to byte
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private byte[] getFileBytes(java.io.File file) throws IOException {
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			int bytes = (int) file.length();
			byte[] buffer = new byte[bytes];
			int readBytes = bis.read(buffer);
			if (readBytes != buffer.length) {
				throw new IOException("Entire file not read");
			}
			return buffer;
		} finally {
			if (bis != null) {
				bis.close();
			}
		}
	}

	/**
	 * Delete the current user
	 * 
	 * @return
	 */
	public boolean deleteAccount() {
		if (getConnection() == null)
			return false;
		try {
			getConnection().getAccountManager().deleteAccount();
			return true;
		} catch (XMPPException e) {
			return false;
		}
	}

	/**
	 * Change password
	 * 
	 * @return
	 */
	public boolean changePassword(String pwd) {
		if (getConnection() == null)
			return false;
		try {
			getConnection().getAccountManager().changePassword(pwd);
			return true;
		} catch (XMPPException e) {
			return false;
		}
	}

	/**
	 * Initialization list of meeting rooms
	 */
	public List<HostedRoom> getHostRooms() {
		if (getConnection() == null)
			return null;
		Collection<HostedRoom> hostrooms = null;
		List<HostedRoom> roominfos = new ArrayList<HostedRoom>();
		try {
			new ServiceDiscoveryManager(getConnection());
			hostrooms = MultiUserChat.getHostedRooms(getConnection(),
					getConnection().getServiceName());
			for (HostedRoom entry : hostrooms) {
				roominfos.add(entry);
				Log.i("room",
						"Name: " + entry.getName() + " - ID:" + entry.getJid());
			}
			Log.i("room", "Service meeting number:" + roominfos.size());
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return roominfos;
	}

	/**
	 * Create a room
	 * 
	 * @param roomName
	 *            The name of the room
	 */
	public MultiUserChat createRoom(String user, String roomName,
			String password) {
		if (getConnection() == null)
			return null;

		MultiUserChat muc = null;
		try {
			// Create a MultiUserChat
			muc = new MultiUserChat(getConnection(), roomName + "@conference."
					+ getConnection().getServiceName());
			// Create a chat room
			muc.create(roomName);
			// To obtain the chat room configuration form
			Form form = muc.getConfigurationForm();
			// Create a new form to submit the original form according to the.
			Form submitForm = form.createAnswerForm();
			// To submit the form to add a default reply
			for (Iterator<FormField> fields = form.getFields(); fields
					.hasNext();) {
				FormField field = (FormField) fields.next();
				if (!FormField.TYPE_HIDDEN.equals(field.getType())
						&& field.getVariable() != null) {
					// Set default values for an answer
					submitForm.setDefaultAnswer(field.getVariable());
				}
			}
			// Set the chat room of the new owner
			List<String> owners = new ArrayList<String>();
			owners.add(getConnection().getUser());// The user JID
			submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			// Set the chat room is a long chat room, soon to be preserved
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);
			// Only members of the open room
			submitForm.setAnswer("muc#roomconfig_membersonly", false);
			// Allows the possessor to invite others
			submitForm.setAnswer("muc#roomconfig_allowinvites", true);
			if (!password.equals("")) {
				// Enter the password if needed
				submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",
						true);
				// Set to enter the password
				submitForm.setAnswer("muc#roomconfig_roomsecret", password);
			}
			// Can be found in possession of real JID role
			// submitForm.setAnswer("muc#roomconfig_whois", "anyone");
			// Login room dialogue
			submitForm.setAnswer("muc#roomconfig_enablelogging", true);
			// Only allow registered nickname log
			submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
			// Allows the user to modify the nickname
			submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
			// Allows the user to register the room
			submitForm.setAnswer("x-muc#roomconfig_registration", false);
			// Send the completed form (the default) to the server to configure
			// the chat room
			muc.sendConfigurationForm(submitForm);
		} catch (XMPPException e) {
			e.printStackTrace();
			return null;
		}
		return muc;
	}

	/**
	 * To join the conference room
	 * 
	 * @param user
	 *            Nickname
	 * @param password
	 *            The conference room password
	 * @param roomsName
	 *            The meeting room
	 */
	public MultiUserChat joinMultiUserChat(String user, String roomsName,
			String password) {
		if (getConnection() == null)
			return null;
		try {
			// Create a MultiUserChat window using XMPPConnection
			MultiUserChat muc = new MultiUserChat(getConnection(), roomsName
					+ "@conference." + getConnection().getServiceName());
			// The number of chat room services will decide to accept the
			// historical record
			DiscussionHistory history = new DiscussionHistory();
			history.setMaxChars(0);
			// history.setSince(new Date());
			// Users to join in the chat room
			muc.join(user, password, history,
					SmackConfiguration.getPacketReplyTimeout());
			Log.i("MultiUserChat", "The conference room [" + roomsName
					+ "] success....");
			return muc;
		} catch (XMPPException e) {
			e.printStackTrace();
			Log.i("MultiUserChat", "The conference room " + roomsName
					+ " join failure....");
			return null;
		}
	}

	/**
	 * Query the conference room member name
	 * 
	 * @param muc
	 */
	public List<String> findMulitUser(MultiUserChat muc) {
		if (getConnection() == null)
			return null;
		List<String> listUser = new ArrayList<String>();
		Iterator<String> it = muc.getOccupants();
		// Traverse the chat room name
		while (it.hasNext()) {
			// Chat room members name
			String name = StringUtils.parseResource(it.next());
			listUser.add(name);
		}
		return listUser;
	}

	/**
	 * Send the file
	 * 
	 * @param user
	 * @param filePath
	 */
	public void sendFile(String user, String filePath) {
		if (getConnection() == null)
			return;
		// Create a file transfer manager
		FileTransferManager manager = new FileTransferManager(getConnection());

		// To create the output file transfer
		OutgoingFileTransfer transfer = manager
				.createOutgoingFileTransfer(user);

		// Send the file
		// try {
		// transfer.sendFile(new File(filePath), "You won't believe this!");
		// } catch (XMPPException e) {
		// e.printStackTrace();
		// }
	}

	/**
	 * Get offline message
	 * 
	 * @return
	 */
	public Map<String, List<HashMap<String, String>>> getHisMessage() {
		if (getConnection() == null)
			return null;
		Map<String, List<HashMap<String, String>>> offlineMsgs = null;

		try {
			OfflineMessageManager offlineManager = new OfflineMessageManager(
					getConnection());
			Iterator<Message> it = offlineManager.getMessages();

			int count = offlineManager.getMessageCount();
			if (count <= 0)
				return null;
			offlineMsgs = new HashMap<String, List<HashMap<String, String>>>();

			while (it.hasNext()) {
				Message message = it.next();
				String fromUser = StringUtils.parseName(message.getFrom());
				;
				HashMap<String, String> histrory = new HashMap<String, String>();
				histrory.put("useraccount",
						StringUtils.parseName(getConnection().getUser()));
				histrory.put("friendaccount", fromUser);
				histrory.put("info", message.getBody());
				histrory.put("type", "left");
				if (offlineMsgs.containsKey(fromUser)) {
					offlineMsgs.get(fromUser).add(histrory);
				} else {
					List<HashMap<String, String>> temp = new ArrayList<HashMap<String, String>>();
					temp.add(histrory);
					offlineMsgs.put(fromUser, temp);
				}
			}
			offlineManager.deleteMessages();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return offlineMsgs;
	}

	/**
	 * Determine the OpenFire user state strUrl : URL format - ;
	 * /status?jid=user1@SERVER_NAME&type=xml Return value: 0 - user does not
	 * exist; 1 - user online; 2 - user is offline Description: must; OpenFire
	 * load presence plug-in, and anyone can access
	 */
	public int IsUserOnLine(String user) {
		String url = "http://" + HOST + ":9090/plugins/presence/status?"
				+ "jid=" + user + "@" + SERVER_NAME + "&type=xml";
		int shOnLineState = 0; // Does not exist
		try {
			URL oUrl = new URL(url);
			URLConnection oConn = oUrl.openConnection();
			if (oConn != null) {
				BufferedReader oIn = new BufferedReader(new InputStreamReader(
						oConn.getInputStream()));
				if (null != oIn) {
					String strFlag = oIn.readLine();
					oIn.close();
					System.out.println("strFlag" + strFlag);
					if (strFlag.indexOf("type=\"unavailable\"") >= 0) {
						shOnLineState = 2;
					}
					if (strFlag.indexOf("type=\"error\"") >= 0) {
						shOnLineState = 0;
					} else if (strFlag.indexOf("priority") >= 0
							|| strFlag.indexOf("id=\"") >= 0) {
						shOnLineState = 1;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return shOnLineState;
	}

	/**
	 * Join the providers function ASmack is missing a smack.providers file in
	 * /META-INF
	 * 
	 * @param pm
	 */
	public void configureConnection(ProviderManager pm) {

		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());

		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
			Log.w("TestClient",
					"Can't load class for org.jivesoftware.smackx.packet.Time");
		}

		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeManager());

		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());

		// Chat State
		pm.addExtensionProvider("active", null,
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("composing", null,
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("paused", null,
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("inactive", null,
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("gone", null, new ChatStateExtension.Provider());

		// XHTML
		pm.addExtensionProvider("html", null, new XHTMLExtensionProvider());

		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());

		// Service Discovery # Items
		pm.addIQProvider("query", null, new DiscoverItemsProvider());

		// Service Discovery # Info
		pm.addIQProvider("query", null, new DiscoverInfoProvider());

		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

		// MUC User
		pm.addExtensionProvider("x", null, new MUCUserProvider());

		// MUC Admin
		pm.addIQProvider("query", null, new MUCAdminProvider());

		// MUC Owner
		pm.addIQProvider("query", null, new MUCOwnerProvider());

		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());

		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
		}

		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

		// Offline Message Requests
		pm.addIQProvider("offline", null, new OfflineMessageRequest.Provider());

		// Offline Message Indicator
		pm.addExtensionProvider("offline", null,
				new OfflineMessageInfo.Provider());

		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup", null, new SharedGroupsInfo.Provider());

		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses", null,
				new MultipleAddressesProvider());

		// FileTransfer
		pm.addIQProvider("si", null, new StreamInitiationProvider());

		pm.addIQProvider("query", null, new BytestreamsProvider());

		// Privacy
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
		pm.addIQProvider("command", null, new AdHocCommandDataProvider());
		pm.addExtensionProvider("malformed-action", null,
				new AdHocCommandDataProvider.MalformedActionError());
		pm.addExtensionProvider("bad-locale", null,
				new AdHocCommandDataProvider.BadLocaleError());
		pm.addExtensionProvider("bad-payload", null,
				new AdHocCommandDataProvider.BadPayloadError());
		pm.addExtensionProvider("bad-sessionid", null,
				new AdHocCommandDataProvider.BadSessionIDError());
		pm.addExtensionProvider("session-expired", null,
				new AdHocCommandDataProvider.SessionExpiredError());
	}

	public static XmppConnection getInstance() {
		return instance;
	}

}