package com.demo.xmppchat.common;

import java.util.ArrayList;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.app.Application;
import android.app.Fragment;
import android.content.SharedPreferences;

import com.demo.xmppchat.utils.XmppUtils;

public class XMPPChatDemoApplication extends Application {

	private String TAG = XMPPChatDemoApplication.class.getSimpleName();
	private final int KEYBOARD_DEFAULT_HEIGHT = 344;
	private String title;
	private SharedPreferences pref;
	private String errMessage;
	private boolean isTwitterLogin = false;
	private XMPPConnection xmppConnection;
	private PacketListener packetListener;
	private ProviderManager providerManager;
	private String senderId, receiverId;
	private Fragment fragment = null;
	private String groupChatUser;
	private MultiUserChat multiUserChat;
	private XmppUtils xmppUtils;

	@Override
	public void onCreate() {
		super.onCreate();
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setXmppConnection(XMPPConnection xmppConnection) {
		this.xmppConnection = xmppConnection;
	}

	public XMPPConnection getXmppConnection() {
		return xmppConnection;
	}

	public void setPacketListener(PacketListener packetListener) {
		this.packetListener = packetListener;
	}

	public PacketListener getPacketListener() {
		return packetListener;
	}

	public void setProviderManager(ProviderManager providerManager) {
		this.providerManager = providerManager;
	}

	public ProviderManager getProviderManager() {
		return providerManager;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	public Fragment getFragment() {
		return fragment;
	}

	public String getGroupChatUser() {
		return groupChatUser;
	}

	public MultiUserChat getMultiUserChat() {
		return multiUserChat;
	}

	public void setMultiUserChat(MultiUserChat multiUserChat) {
		this.multiUserChat = multiUserChat;
	}

	public void setXmppUtils(XmppUtils xmppUtils) {
		this.xmppUtils = xmppUtils;
	}

	public XmppUtils getXmppUtils() {
		return xmppUtils;
	}

	/**
	 * @return the sharedPreferences
	 */
	public SharedPreferences getSharedPreferences() {
		return pref;
	}

}
