package org.ctlv.proxmox.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import org.ctlv.proxmox.api.Constants;
import org.ctlv.proxmox.api.ProxmoxAPI;
import org.ctlv.proxmox.api.data.LXC;
import org.json.JSONException;

public class Monitor implements Runnable {

	Analyzer analyzer;
	ProxmoxAPI api;
	
	public Monitor(ProxmoxAPI api, Analyzer analyzer) {
		this.api = api;
		this.analyzer = analyzer;
	}
	

	@Override
	public void run() {
		
		while(true) {
			
			// Récupérer les données sur les serveurs
			Map<String, List<LXC>> myCTsPerServer = new HashMap();
			List<LXC> cts = null;
			try {
				cts = api.getCTs(Constants.SERVER1);
			} catch (LoginException | JSONException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("Impossible to get cnt");
			}
			
			myCTsPerServer.put(Constants.SERVER1, cts);
			
			try {
				cts = api.getCTs(Constants.SERVER2);
			} catch (LoginException | JSONException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("Impossible to get cnt");
			}
			
			myCTsPerServer.put(Constants.SERVER2, cts);
			
			// Lancer l'analyse
			try {
				analyzer.analyze(myCTsPerServer);
			} catch (LoginException | JSONException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			
			// attendre une certaine période
			try {
				Thread.sleep(Constants.MONITOR_PERIOD * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
