package com.mmorpg.mir.model.player.packet;

import com.mmorpg.mir.transfer.packet.TransferAddress;

public class SM_CheckExist {

	private int code = 0;
	private int result;
	private byte country;
	private byte role;
	private byte forbidCountry;
	private TransferAddress transferAddress;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

	public byte getRole() {
		return role;
	}

	public void setRole(byte role) {
		this.role = role;
	}

	public byte getForbidCountry() {
		return forbidCountry;
	}

	public void setForbidCountry(byte forbidCountry) {
		this.forbidCountry = forbidCountry;
	}

	public TransferAddress getTransferAddress() {
		return transferAddress;
	}

	public void setTransferAddress(TransferAddress transferAddress) {
		this.transferAddress = transferAddress;
	}

}
