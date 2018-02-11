package com.mmorpg.mir.model.console.packet;

public class SM_Get_Server_Version_MD5 {
	private String versionMd5;

	public static SM_Get_Server_Version_MD5 valueOf(String versionMd5) {
		SM_Get_Server_Version_MD5 result = new SM_Get_Server_Version_MD5();
		result.versionMd5 = versionMd5;
		return result;
	}

	public String getVersionMd5() {
		return versionMd5;
	}

	public void setVersionMd5(String versionMd5) {
		this.versionMd5 = versionMd5;
	}

}
