package org.ctlv.proxmox.manager;

import java.util.List;
import java.util.Map;

import org.ctlv.proxmox.api.ProxmoxAPI;
import org.ctlv.proxmox.api.data.LXC;

public class ManagerMain {

	public static void main(String[] args) throws Exception {
		ProxmoxAPI api = new ProxmoxAPI();
		Controller controller = new Controller(api);
		Analyzer analyser = new Analyzer(api, controller);
		Monitor monitor = new Monitor(api, analyser);
		monitor.run();
	}

}