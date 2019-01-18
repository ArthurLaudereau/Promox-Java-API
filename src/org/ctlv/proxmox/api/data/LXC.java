package org.ctlv.proxmox.api.data;

import org.json.JSONObject;

public class LXC {


	String template;
	String vmid;
	String name;	
	String status;	
	String type;	
	
	long disk;		
	long maxdisk;
	long diskwrite;
	long diskread;
	
	long netin;	
	long netout;	

	long cpu;
	int cpus;
	long uptime;
	long lock;
	long pid;

	long mem;
	long maxmem;	
	long swap;
	long maxswap;
	
	public LXC(JSONObject data) {
		
		template = data.optString("template");
		vmid = data.optString("vmid");
		name = data.optString("name");
		status = data.optString("status");
		type = data.optString("type");
		
		disk = data.optLong("disk");
		maxdisk = data.optLong("maxdisk");
		diskwrite = data.optLong("diskwrite");
		diskread = data.optLong("diskread");
		
		netin = data.optLong("netin");
		netout = data.optLong("netout");
		
		cpu = data.optLong("cpu");
		cpus = data.optInt("cpus");
		uptime = data.optLong("uptime");
		lock = data.optLong("lock");
		
		mem = data.optLong("mem");
		maxmem = data.optLong("maxmem");
		swap = data.optLong("swap");
		maxswap = data.optLong("maxswap");
		
	}
	
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getVmid() {
		return vmid;
	}

	public void setVmid(String vmid) {
		this.vmid = vmid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getDisk() {
		return disk;
	}

	public void setDisk(long disk) {
		this.disk = disk;
	}

	public long getMaxdisk() {
		return maxdisk;
	}

	public void setMaxdisk(long maxdisk) {
		this.maxdisk = maxdisk;
	}

	public long getDiskwrite() {
		return diskwrite;
	}

	public void setDiskwrite(long diskwrite) {
		this.diskwrite = diskwrite;
	}

	public long getDiskread() {
		return diskread;
	}

	public void setDiskread(long diskread) {
		this.diskread = diskread;
	}

	public long getNetin() {
		return netin;
	}

	public void setNetin(long netin) {
		this.netin = netin;
	}

	public long getNetout() {
		return netout;
	}

	public void setNetout(long netout) {
		this.netout = netout;
	}

	public long getCpu() {
		return cpu;
	}

	public void setCpu(long cpu) {
		this.cpu = cpu;
	}

	public int getCpus() {
		return cpus;
	}

	public void setCpus(int cpus) {
		this.cpus = cpus;
	}

	public long getUptime() {
		return uptime;
	}

	public void setUptime(long uptime) {
		this.uptime = uptime;
	}

	public long getLock() {
		return lock;
	}

	public void setLock(long lock) {
		this.lock = lock;
	}
	
	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public long getMem() {
		return mem;
	}

	public void setMem(long mem) {
		this.mem = mem;
	}

	public long getMaxmem() {
		return maxmem;
	}

	public void setMaxmem(long maxmem) {
		this.maxmem = maxmem;
	}

	public long getSwap() {
		return swap;
	}

	public void setSwap(long swap) {
		this.swap = swap;
	}

	public long getMaxswap() {
		return maxswap;
	}

	public void setMaxswap(long maxswap) {
		this.maxswap = maxswap;
	}

}
