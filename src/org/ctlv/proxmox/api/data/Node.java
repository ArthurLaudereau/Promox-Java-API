package org.ctlv.proxmox.api.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Node {
	private float cpu;
	private int uptime;
	
	private long memory_used;
	private long memory_free;
	private long memory_total;
	
	private long rootfs_free;
	private long rootfs_total;
	private long rootfs_used;

	public Node(JSONObject data) throws JSONException {
		JSONObject o;
		
		cpu = (float) data.getDouble("cpu");
		uptime = data.getInt("uptime");
		
		o = data.getJSONObject("memory");
		memory_free = o.getLong("free");
		memory_total = o.getLong("total");
		memory_used = o.getLong("used");
		
		o = data.getJSONObject("rootfs");
		rootfs_free = o.getLong("free");
		rootfs_total = o.getLong("total");
		rootfs_used = o.getLong("used");
	}

	public float getCpu() {
		return cpu;
	}

	public int getUptime() {
		return uptime;
	}

	public long getMemory_free() {
		return memory_free;
	}

	public long getMemory_total() {
		return memory_total;
	}

	public long getRootfs_free() {
		return rootfs_free;
	}

	public long getRootfs_total() {
		return rootfs_total;
	}

	public long getRootfs_used() {
		return rootfs_used;
	}

	public long getMemory_used() {
		return memory_used;
	}
}
