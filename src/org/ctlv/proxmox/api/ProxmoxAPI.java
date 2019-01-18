package org.ctlv.proxmox.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import org.ctlv.proxmox.api.data.LXC;
import org.ctlv.proxmox.api.data.Node;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProxmoxAPI {

	private String Ticket;
	private String Token;
	private Date TicketTimestamp;
	
	public ProxmoxAPI() {
		this.TicketTimestamp = null;
	}
	
	public void login() throws JSONException, LoginException, IOException {
		RestClient client = new RestClient("https://" + Constants.HOST + ":8006/api2/json/access/ticket");
		client.addParam("username", Constants.USER_NAME);
		client.addParam("password", Constants.PASS_WORD);
		client.addParam("realm", Constants.REALM);
		try {
			client.execute(RestClient.RequestMethod.POST);
		} catch (Exception e) {
			throw new IOException(e);
		}
		if (client.getResponseCode() == HttpURLConnection.HTTP_OK) {
			JSONObject jObj = new JSONObject(client.getResponse());
			JSONObject data = jObj.getJSONObject("data");
			this.Ticket = data.getString("ticket");
			this.Token = data.getString("CSRFPreventionToken");
			this.TicketTimestamp = new Date(); 
			return;
		} else if (client.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
			throw new LoginException("Login failed. Please try again");
		} else {
			throw new IOException(client.getErrorMessage());
		}
	}

	public void checkLoginTicket() throws LoginException, JSONException, IOException {
		if (TicketTimestamp == null || TicketTimestamp.getTime() >= (new Date()).getTime() - 3600) {
			login(); // login again
		}
	}

	public JSONObject doAction(String Path, RestClient.RequestMethod method, Map<String, String> data) 
			throws JSONException, LoginException, IOException {
		
		checkLoginTicket();
		RestClient client = new RestClient("https://" + Constants.HOST + ":8006/api2/json" + Path);
		if (!method.equals(RestClient.RequestMethod.GET))
			client.addHeader("CSRFPreventionToken", this.Token);
		client.addHeader("Cookie", "PVEAuthCookie=" + this.Ticket);
		if (data != null)
			for (Map.Entry<String, String> entry : data.entrySet()) {
				client.addParam(entry.getKey(), entry.getValue());
			}
		try {
			client.execute(method);
		} catch (Exception e) {
			throw new IOException(e);
		}
		if (client.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return new JSONObject(client.getResponse());
		} else if (client.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
			throw new LoginException(client.getErrorMessage());
		} else if (client.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
			throw new IOException(client.getErrorMessage() + "  " + client.getResponse());
		} else {
			throw new IOException(client.getErrorMessage());
		}
	}


	// Clusters Nodes
	public List<String> getNodes() throws LoginException, JSONException, IOException {		
		List<String> res = new ArrayList<String>();
		JSONObject jObj = doAction("/nodes", RestClient.RequestMethod.GET, null);
		JSONArray data = jObj.getJSONArray("data");
		for (int i = 0; i < data.length(); i++) {
			JSONObject item = data.getJSONObject(i);
			res.add(item.getString("node"));
		}
		return res;
	}
	
	public Node getNode(String name) throws LoginException, JSONException, IOException {
		JSONObject obj = doAction("/nodes/" + name + "/status", RestClient.RequestMethod.GET, null);
		return new Node(obj.getJSONObject("data"));
	}

	// LXC Containers
	public void createCT(String node, String ctID, String ctName, long ctMemory) throws LoginException, JSONException, IOException {
		Map<String, String> data = new HashMap<String, String>();
		data.put("ostemplate", Constants.CT_TEMPLATE);
		data.put("vmid", ctID);
		data.put("hostname", ctName);
		data.put("memory", Long.toString(ctMemory));
		data.put("storage", "vm");
		data.put("password",Constants.CT_PASSWORD);
		data.put("swap", Long.toString(ctMemory));
		data.put("rootfs", Constants.CT_HDD);
		data.put("cpulimit", "1");
		data.put("net0", Constants.CT_NETWORK);
		JSONObject obj = doAction("/nodes/" + node + "/lxc", RestClient.RequestMethod.POST, data);
		System.out.println(obj.toString());
	}
	public void startCT(String node, String ctID) throws LoginException, JSONException, IOException {
		@SuppressWarnings("unused")
		JSONObject obj = doAction("/nodes/" + node + "/lxc/" + ctID + "/status/start", RestClient.RequestMethod.POST, null);
	}
	public void stopCT(String node, String ctID) throws LoginException, JSONException, IOException {
		@SuppressWarnings("unused")
		JSONObject obj = doAction("/nodes/" + node + "/lxc/" + ctID + "/status/stop", RestClient.RequestMethod.POST, null);
	}	
	public void deleteCT(String node, String ctID) throws LoginException, JSONException, IOException {
		@SuppressWarnings("unused")
		JSONObject obj = doAction("/nodes/" + node + "/lxc/" + ctID, RestClient.RequestMethod.DELETE, null);
	}
	public void migrateCT(String srcNode, String ctID, String dstNode) throws LoginException, JSONException, IOException {
		Map<String, String> data = new HashMap<String, String>();
		data.put("target", dstNode);
		@SuppressWarnings("unused")
		JSONObject obj = doAction("/nodes/" + srcNode + "/lxc/" + ctID + "/migrate", RestClient.RequestMethod.POST, data);
	}
	
	public LXC getCT(String node, String ctID) throws LoginException, JSONException, IOException {
		LXC res = null;
		JSONObject obj = doAction("/nodes/" + node + "/lxc", RestClient.RequestMethod.GET, null);
		JSONArray data = obj.getJSONArray("data");
		for (int i = 0; i < data.length(); i++) {
			JSONObject item = data.getJSONObject(i);
			if (item.getString("vmid").equals(ctID)) {
				res = new LXC(item);
				break;
			}
		}
		return res;
	}
	public List<String> getCTList(String node) throws LoginException, JSONException, IOException {
		List<String> res = new ArrayList<String>();
		JSONObject jObj = doAction("/nodes/" + node + "/lxc", RestClient.RequestMethod.GET, null);
		JSONArray data = jObj.getJSONArray("data");
		for (int i = 0; i < data.length(); i++) {
			JSONObject item = data.getJSONObject(i);
			res.add(item.getString("vmid"));
		}
		return res;
	}

	
	public List<LXC> getCTs(String node) throws LoginException, JSONException, IOException {
		List<LXC> res = new ArrayList<LXC>();
		JSONObject jObj = doAction("/nodes/" + node + "/lxc", RestClient.RequestMethod.GET, null);
		JSONArray data = jObj.getJSONArray("data");
		for (int i = 0; i < data.length(); i++) {
			JSONObject item = data.getJSONObject(i);
			res.add(new LXC(item));
		}
		return res;
	}




}
