package org.ctlv.proxmox.manager;

import java.io.IOException;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.ctlv.proxmox.api.ProxmoxAPI;
import org.ctlv.proxmox.api.data.LXC;
import org.json.JSONException;

public class Controller {

	ProxmoxAPI api;
	public Controller(ProxmoxAPI api){
		this.api = api;
	}
	
	// migrer un conteneur du serveur "srcServer" vers le serveur "dstServer"
	public void migrateFromTo(String srcServer, String ctID, String dstServer)  {
		try {
			api.stopCT(srcServer, ctID);
			api.migrateCT(srcServer, ctID, dstServer);
			api.startCT(dstServer, ctID);
		} catch (LoginException e) {
			System.out.println("Login exception for migration");
			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("JSON exception for migration");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO exception for migration");
			e.printStackTrace();
		}
		System.out.println("Migrate CT : " + ctID + " from " + srcServer + " to " + dstServer); 
	}

	// arrêter le plus vieux conteneur sur le serveur "server"
	public void offLoad(String server) throws LoginException, JSONException, IOException {
		long olderTime = Long.MAX_VALUE;
		String olderID = null;
		List<LXC> cts = api.getCTs(server);
		for(LXC i : cts) {
			if (i.getUptime() < olderTime) {
				olderTime = i.getUptime();
				olderID = i.getVmid();
			}
		}
		if (olderID != null) {
			api.deleteCT(server, olderID);
			System.out.println("Delete cnt : " + olderID);
		}
	}

}
