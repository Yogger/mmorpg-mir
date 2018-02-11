package com.mmorpg.mir.model.chat.packet;

public class SM_Big_Face {

	private long targetId;
	private int faceId;

	public static SM_Big_Face valueOf(long objId, int faceId) {
		SM_Big_Face sm = new SM_Big_Face();
		sm.targetId = objId;
		sm.faceId = faceId;
		return sm;
	}
	
	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public int getFaceId() {
		return faceId;
	}

	public void setFaceId(int faceId) {
		this.faceId = faceId;
	}

}
