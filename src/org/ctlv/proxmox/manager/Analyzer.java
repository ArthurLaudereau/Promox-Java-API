package org.ctlv.proxmox.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.login.LoginException;

import org.ctlv.proxmox.api.Constants;
import org.ctlv.proxmox.api.ProxmoxAPI;
import org.ctlv.proxmox.api.data.LXC;
import org.json.JSONException;

public class Analyzer {
	ProxmoxAPI api;
	Controller controller;
	
	public Analyzer(ProxmoxAPI api, Controller controller) {
		this.api = api;
		this.controller = controller;
	}
	
	public void analyze(Map<String, List<LXC>> myCTsPerServer) throws LoginException, JSONException, IOException  {

		// Calculer la quantité de RAM utilisée par mes CTs sur chaque serveur
		long memOnServer1 = 0;
		long memOnServer2 = 0;
		
		long halfMemAllowedOnServer1 = (long) (api.getNode(Constants.SERVER1).getMemory_total() * Constants.MIGRATION_THRESHOLD);
		long halfMemAllowedOnServer2 = (long) (api.getNode(Constants.SERVER2).getMemory_total() * Constants.MIGRATION_THRESHOLD);
		System.out.println("Mem allowed : " + halfMemAllowedOnServer1 + " " + halfMemAllowedOnServer2);

		// halfMemAllowedOnServer1 = halfMemAllowedOnServer1/100; //Test pour migrer les CT entre les serveurs sans surpeupler les serveurs
		// halfMemAllowedOnServer2 = halfMemAllowedOnServer2/100; //On simule une RAM plus petite pour que l'appel de la fonction fonctionne
		
		System.out.println("Mem allowed : " + halfMemAllowedOnServer1 + " " + halfMemAllowedOnServer2);

		
		Set<String> keys = myCTsPerServer.keySet();
		for(int i = 0; i < myCTsPerServer.size(); i++) {
			String serverName = (String) keys.toArray()[(i+1)%2];
			System.out.println(serverName);
			for(LXC lxc : myCTsPerServer.get(serverName)) {
				if (i==0) {
					if(lxc.getMem() > halfMemAllowedOnServer1) {
						controller.migrateFromTo(Constants.SERVER1, lxc.getVmid(), Constants.SERVER2);
						memOnServer2 += lxc.getMem();
						System.out.println("Migation, Memory of the cnt : "+lxc.getMem());
					} else {
						memOnServer1 += lxc.getMem();
						System.out.println("Memory on server " + (i + 1)  + " : "+lxc.getMem());
					}
				} else {
					if(lxc.getMem() > halfMemAllowedOnServer2) {
						controller.migrateFromTo(Constants.SERVER2, lxc.getVmid(), Constants.SERVER1);
						memOnServer1 += lxc.getMem();
						System.out.println("Migation, Memory of the cnt : "+lxc.getMem());
					} else {
						memOnServer2 += lxc.getMem();
						System.out.println("Memory on server " + (i+1) + " : "+lxc.getMem());
					}
				}
			}
		}
		
		System.out.println("Total Mem on server 1 : " + memOnServer1);
		System.out.println("Total Mem on server 2 : " + memOnServer2);
		System.out.println("Half mem allowed : " + halfMemAllowedOnServer1 + " " + halfMemAllowedOnServer2);

		
		// Mémoire autorisée sur chaque serveur
		long memAllowedOnServer1 = (long) (api.getNode(Constants.SERVER1).getMemory_total() * Constants.DROPPING_THRESHOLD);
		long memAllowedOnServer2 = (long) (api.getNode(Constants.SERVER2).getMemory_total() * Constants.DROPPING_THRESHOLD);
		
		// Analyse et Actions
		if (memOnServer1 > memAllowedOnServer1) {
			controller.offLoad(Constants.SERVER1);
			System.out.println("Delete server 1 oldest cnt");
		}
		if (memOnServer2 > memAllowedOnServer2) {
			controller.offLoad(Constants.SERVER2);
			System.out.println("Delete server 2 oldest cnt");
		}
		
	}
}

