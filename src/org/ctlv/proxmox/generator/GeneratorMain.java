package org.ctlv.proxmox.generator;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.security.auth.login.LoginException;

import org.ctlv.proxmox.api.Constants;
import org.ctlv.proxmox.api.ProxmoxAPI;
import org.ctlv.proxmox.api.data.LXC;
import org.json.JSONException;

public class GeneratorMain {
	
	static Random rndTime = new Random(new Date().getTime());
	public static int getNextEventPeriodic(int period) {
		return period;
	}
	public static int getNextEventUniform(int max) {
		return rndTime.nextInt(max);
	}
	public static int getNextEventExponential(int inv_lambda) {
		float next = (float) (- Math.log(rndTime.nextFloat()) * inv_lambda);
		return (int)next;
	}
	
	public static void main(String[] args) throws InterruptedException, LoginException, JSONException, IOException {
		
	
		long baseID = Constants.CT_BASE_ID;
		long lastID = baseID;
		int lambda = 30;
		String id = Long.toString(baseID);
		
		
		Map<String, List<LXC>> myCTsPerServer = new HashMap<String, List<LXC>>();

		ProxmoxAPI api = new ProxmoxAPI();
		Random rndServer = new Random(new Date().getTime());
		Random rndRAM = new Random(new Date().getTime()); 
		
		long memAllowedOnServer1 = (long) (api.getNode(Constants.SERVER1).getMemory_total() * Constants.MAX_THRESHOLD);
		long memAllowedOnServer2 = (long) (api.getNode(Constants.SERVER2).getMemory_total() * Constants.MAX_THRESHOLD);
		
		while (true) {
			
			// 1. Calculer la quantité de RAM utilisée par mes CTs sur chaque serveur
			long memOnServer1 = 0;
			List<LXC> cts = api.getCTs(Constants.SERVER1);
			for(LXC i:cts) {
				memOnServer1 += i.getMem();
				System.out.println("Memory on server 1 : "+memOnServer1);
			}
			
			long memOnServer2 = 0;
			List<LXC> cts2 = api.getCTs(Constants.SERVER2);
			for(LXC i:cts2) {
				memOnServer2 += i.getMem();
				System.out.println("Memory on server 2 : "+memOnServer2);
			}
			
			// Mémoire autorisée sur chaque serveur
			float memRatioOnServer1 = memAllowedOnServer1;

			float memRatioOnServer2 = memAllowedOnServer2; 
			
			if (memOnServer1 < memRatioOnServer1 && memOnServer2 < memRatioOnServer2) {  // Exemple de condition de l'arrêt de la génération de CTs
				
				// choisir un serveur aléatoirement avec les ratios spécifiés 66% vs 33%
				String serverName;
				if (rndServer.nextFloat() < Constants.CT_CREATION_RATIO_ON_SERVER1) {
					serverName = Constants.SERVER1;
					System.out.println("Server 1 choosed.");
				}
				else {
					serverName = Constants.SERVER2;
					System.out.println("Server 2 choosed.");
				}
				
				lastID = (lastID + 1)%100 + baseID;
				id = Long.toString(lastID);
				System.out.println("ID = " + id);
				
				while(api.getCT(serverName, id) != null) {
					lastID = (lastID + 1)%100 + baseID;
					id = Long.toString(lastID);
					System.out.println("ID = " + id);
				}
				
				api.createCT(serverName, id, Constants.CT_BASE_NAME, Constants.RAM_SIZE[2]);
								
				// planifier la prochaine création
				int timeToWait = getNextEventExponential(lambda); // par exemple une loi expo d'une moyenne de 30sec
				
				// attendre jusqu'au prochain évènement
				Thread.sleep(1000 * timeToWait);
			}
			else {
				System.out.println("Servers are loaded, waiting ...");
				Thread.sleep(Constants.GENERATION_WAIT_TIME* 1000);
			}
		}
		
	}

}
