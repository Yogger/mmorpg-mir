package com.mmorpg.mir.model.mergeactive.model;

public class MergeActive {
	public static Object valueOf() {
		MergeActive active = new MergeActive();
		active.loginGift = LoginGift.valueOf();
		active.cheapGiftBag = CheapGiftBag.valueOf();
		active.consumeCompete = ConsumeCompete.valueOf();
		return active;
	}
	
	private LoginGift loginGift;
	
	private CheapGiftBag cheapGiftBag;
	
	private ConsumeCompete consumeCompete; 
	
	public LoginGift getLoginGift() {
		return loginGift;
	}

	public void setLoginGift(LoginGift loginGift) {
		this.loginGift = loginGift;
	}

	public CheapGiftBag getCheapGiftBag() {
		return cheapGiftBag;
	}

	public void setCheapGiftBag(CheapGiftBag cheapGiftBag) {
		this.cheapGiftBag = cheapGiftBag;
	}

	public ConsumeCompete getConsumeCompete() {
		return consumeCompete;
	}

	public void setConsumeCompete(ConsumeCompete consumeCompete) {
		this.consumeCompete = consumeCompete;
	}
	
}
