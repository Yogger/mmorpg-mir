package com.mmorpg.mir.admin.packet;

import java.util.ArrayList;

public class SGM_Reload {
	private ArrayList<String> success = new ArrayList<String>();
	private ArrayList<String> fail = new ArrayList<String>();

	public ArrayList<String> getSuccess() {
		return success;
	}

	public void setSuccess(ArrayList<String> success) {
		this.success = success;
	}

	public ArrayList<String> getFail() {
		return fail;
	}

	public void setFail(ArrayList<String> fail) {
		this.fail = fail;
	}

}
