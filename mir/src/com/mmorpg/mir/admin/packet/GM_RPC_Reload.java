package com.mmorpg.mir.admin.packet;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class GM_RPC_Reload {
	@Protobuf
	private String ip;
	@Protobuf(fieldType=FieldType.STRING)
	private List<String> resources;

	public List<String> getResources() {
		return resources;
	}

	public void setResources(List<String> resources) {
		this.resources = resources;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
