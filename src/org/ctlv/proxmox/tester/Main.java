package org.ctlv.proxmox.tester;

import java.io.IOException;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.ctlv.proxmox.api.ProxmoxAPI;
import org.ctlv.proxmox.api.data.LXC;
import org.json.JSONException;


public class Main {

	public static void main(String[] args) throws LoginException, JSONException, IOException {

		ProxmoxAPI api = new ProxmoxAPI();		
		
		
		// Listes les CTs par serveur
		/*for (int i=1; i<=10; i++) {
			String srv ="srv-px"+i;
			System.out.println("CTs sous "+srv);
			List<LXC> cts = api.getCTs(srv);
			
			for (LXC lxc : cts) {
				System.out.println("\t" + lxc.getName());
			}
		}*/
		
		
		// Créer un CT
		//api.createCT("srv-px1", "4000", "ct-tpgei-virt-00-ct1", 512);
		
		// Supprimer un CT
		//api.deleteCT("srv-px1", "4000");
		
	}

}
