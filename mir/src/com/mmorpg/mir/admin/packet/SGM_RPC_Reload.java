package com.mmorpg.mir.admin.packet;

import java.util.ArrayList;
import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class SGM_RPC_Reload {
	@Protobuf(fieldType=FieldType.STRING)
	private List<String> success = new ArrayList<String>();
	@Protobuf(fieldType=FieldType.STRING)
	private List<String> fail = new ArrayList<String>();
	public List<String> getSuccess() {
		return success;
	}
	public void setSuccess(List<String> success) {
		this.success = success;
	}
	public List<String> getFail() {
		return fail;
	}
	public void setFail(List<String> fail) {
		this.fail = fail;
	}
}