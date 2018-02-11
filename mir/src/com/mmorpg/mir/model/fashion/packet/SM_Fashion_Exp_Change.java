package com.mmorpg.mir.model.fashion.packet;

public class SM_Fashion_Exp_Change {

	private int exp;

	public static SM_Fashion_Exp_Change valueOf(int exp) {
		SM_Fashion_Exp_Change result = new SM_Fashion_Exp_Change();
		result.exp = exp;
		return result;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

}
