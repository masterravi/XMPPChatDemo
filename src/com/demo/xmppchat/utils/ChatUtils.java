package com.demo.xmppchat.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import com.demo.xmppchat.R;
import com.demo.xmppchat.common.XMPPChatDemoApplication;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.bytestreams.ibb.provider.CloseIQProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.DataPacketProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.OpenIQProvider;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class ChatUtils {

	private final static String TAG = ChatUtils.class.getSimpleName();
	private static XmppUtils xmppUtils;
	public static String RECEIVER_USER_DELETE = "Hinter.DeleteAction";

	public static boolean chatConnection(final Context context,
			final String userId, final String password) {
		XMPPConnection connection = null;
		XMPPChatDemoApplication appStorage;

		 try {
			 //chat_uncomment
			appStorage = (XMPPChatDemoApplication) context.getApplicationContext();
			connection = appStorage.getXmppConnection();
			//Log.v(TAG, "isChatConnected connectionCheck : " + connection);
			// Log.v(TAG, "isChatConnected connectionCheck.isConnected : " +
			// connectionCheck.isConnected());
			// Log.v(TAG, "isChatConnected connectionCheck.isAuthenticated : " +
			// connectionCheck.isAuthenticated());
			if (connection != null && connection.isConnected()
					&& connection.isAuthenticated()) {
				return true;
			} else {
				xmppUtils = new XmppUtils(context);

				org.jivesoftware.smack.Connection.DEBUG_ENABLED = false;
				if (connection == null) {
					ConnectionConfiguration connConfig;

					connConfig = new ConnectionConfiguration(
							context.getString(R.string.app_name),
							Integer.parseInt(context.getString(R.string.port)));
					connConfig.setSASLAuthenticationEnabled(true);
					connConfig
							.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
					connConfig.setSendPresence(true);
					connConfig.setReconnectionAllowed(true);

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
						connConfig.setTruststoreType("AndroidCAStore");
						connConfig.setTruststorePassword(null);
						connConfig.setTruststorePath(null);
					} else {
						connConfig.setTruststoreType("JKS");
						String path = System
								.getProperty("javax.net.ssl.trustStore");
						if (path == null)
							path = System.getProperty("java.home")
									+ File.separator + "etc" + File.separator
									+ "security" + File.separator
									+ "cacerts.jks";
						connConfig.setTruststorePath(path);
					}
					connection = new XMPPConnection(connConfig);
				}

				configure(appStorage, ProviderManager.getInstance());

				if (!connection.isConnected()) {
					connection.connect();
				}

				connection.addConnectionListener(new ConnectionListener() {

					@Override
					public void reconnectionSuccessful() {
//						Log.e(TAG,
//								"===============reconnectionSuccessful===============");
					}

					@Override
					public void reconnectionFailed(Exception arg0) {
//						Log.e(TAG,
//								"===============reconnectionFailed===============");
					}

					@Override
					public void reconnectingIn(int arg0) {
//						Log.e(TAG,
//								"===============reconnectingIn==============="
//										+ arg0);
					}

					@Override
					public void connectionClosedOnError(Exception arg0) {
//						Log.e(TAG,
//								"===============connectionClosedOnError===============");
						chatConnection(context, userId, password);
					}

					@Override
					public void connectionClosed() {
//						Log.e(TAG,
//								"===============connectionClosed===============");
					}
				});

				final ServiceDiscoveryManager sdm = new ServiceDiscoveryManager(
						connection);
				sdm.addFeature("http://jabber.org/protocol/xhtml-im");
				sdm.addFeature("jabber:x:data");
				sdm.addFeature("http://jabber.org/protocol/disco#info");
				sdm.addFeature("jabber:iq:privacy");
				sdm.addFeature("http://jabber.org/protocol/si");
				sdm.addFeature("http://jabber.org/protocol/bytestreams");
				sdm.addFeature("http://jabber.org/protocol/ibb");
				sdm.addFeature("http://jabber.org/protocol/disco#items");

				if (!connection.isAuthenticated() && connection.isConnected()) {
					// SASLAuthentication.supportSASLMechanism("PLAIN", 0);
					connection.login(userId.trim(), password.trim());
				}
//				Log.e(TAG, "isChatConnected LOGIN : " + "This is true");
				
				Presence presence = new Presence(Presence.Type.available);
				// Set the highest priority
				presence.setPriority(24);
				presence.setStatus("Online");
				presence.setMode(Presence.Mode.available);

				// Set available presence mode
				appStorage.setSenderId(userId);
				connection.sendPacket(presence);

				final PacketFilter filter = new MessageTypeFilter(
						Message.Type.chat);
				 connection.getRoster().addRosterListener(xmppUtils.rosterListener);
				// connection.getChatManager().addChatListener(xmppUtils.chatManagerListener);
				MultiUserChat.addInvitationListener(connection,
						xmppUtils.invitationListener);

				connection.getRoster().addRosterListener(
						xmppUtils.rosterListener);
				appStorage.setXmppConnection(connection);
				appStorage.setXmppUtils(xmppUtils);

				
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
//			Log.e(TAG, "isChatConnected LOGIN : " + "This is false");
			return false;
		} 
	}

	public static void configure(final XMPPChatDemoApplication application,
			final ProviderManager providerManager) {

		// Private Data Storage
		providerManager.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());

		// Time
		try {
			providerManager.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
		}

		// XHTML
		providerManager.addExtensionProvider("html",
				"http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());

		// Roster Exchange
		providerManager.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());
		// Message Events
		providerManager.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());
		// Chat State
		providerManager.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		providerManager.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		providerManager.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		providerManager.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		providerManager.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		// FileTransfer
		providerManager.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());
		providerManager.addIQProvider("query",
				"http://jabber.org/protocol/bytestreams",
				new BytestreamsProvider());
		providerManager.addIQProvider("open", "http://jabber.org/protocol/ibb",
				new OpenIQProvider());
		providerManager.addIQProvider("close",
				"http://jabber.org/protocol/ibb", new CloseIQProvider());
		providerManager.addExtensionProvider("data",
				"http://jabber.org/protocol/ibb", new DataPacketProvider());

		// Group Chat Invitations
		providerManager.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());
		// Service Discovery # Items
		providerManager.addIQProvider("query",
				"http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());
		// Service Discovery # Info
		providerManager.addIQProvider("query",
				"http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());
		// Data Forms
		providerManager.addExtensionProvider("x", "jabber:x:data",
				new DataFormProvider());
		// MUC User
		providerManager.addExtensionProvider("x",
				"http://jabber.org/protocol/muc#user", new MUCUserProvider());
		// MUC Admin
		providerManager.addIQProvider("query",
				"http://jabber.org/protocol/muc#admin", new MUCAdminProvider());
		// MUC Owner
		providerManager.addIQProvider("query",
				"http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());
		// Delayed Delivery
		providerManager.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());
		// Version
		try {
			providerManager.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
		}
		// VCard
		providerManager.addIQProvider("vCard", "vcard-temp",
				new VCardProvider());
		// Offline Message Requests
		providerManager.addIQProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());
		// Offline Message Indicator
		providerManager.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());
		// Last Activity
		providerManager.addIQProvider("query", "jabber:iq:last",
				new LastActivity.Provider());
		// User Search
		providerManager.addIQProvider("query", "jabber:iq:search",
				new UserSearch.Provider());
		// SharedGroupsInfo
		providerManager.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());
		// JEP-33: Extended Stanza Addressing
		providerManager.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());

		application.setProviderManager(providerManager);
	}



	@SuppressLint("SimpleDateFormat")
	public static String getChatTime(Context context, long time) {
		String convertedTime = "";

		try {
			final DateFormat df = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss zzz yyyy");
			final Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(time);

			final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
			convertedTime = dateFormat.format(df
					.parse(cal.getTime().toString()));

			// convertedTime = getLastTime(context, time);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertedTime;
	}

	public static String convertCurrentTimeToGmt(String format) {
		final SimpleDateFormat dateFormatGmt = new SimpleDateFormat(format,
				Locale.getDefault());
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormatGmt.format(new Date());
	}

	public static long getCurrentGmtTime(String format) {
		final SimpleDateFormat dateFormatGmt = new SimpleDateFormat(format,
				Locale.getDefault());
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormatGmt.getCalendar().getTimeInMillis();
	}


}