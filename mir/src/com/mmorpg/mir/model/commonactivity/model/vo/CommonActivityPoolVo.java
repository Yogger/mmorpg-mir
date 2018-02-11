package com.mmorpg.mir.model.commonactivity.model.vo;

import java.util.HashMap;
import java.util.HashSet;

import com.mmorpg.mir.model.commonactivity.manager.CommonActivityManager;
import com.mmorpg.mir.model.gameobjects.Player;

public class CommonActivityPoolVo {

	private HashMap<String, CommonLoginGiftVo> loginGiftActives;

	private HashMap<String, CommonCheapGiftBagVo> cheapGiftBagActives;

	private HashMap<String, CommonRechargeVo> rechargeActives;

	private HashMap<String, CommonConsumeActiveVo> consumeActives;

	private HashSet<String> aliveBoss;

	private HashMap<String, CommonIdentifyTreasureVo> identifyTreasureVos;

	private HashMap<String, CommonFirstPayVo> firstPayVos;

	private HashMap<String, CommonTreasureActiveVo> treasureActiveVos;

	private HashMap<String, CommonRedPackVo> redPackActiveVos;
	
	private LuckyDrawVo luckyDrawVo;
	
	private HashMap<String, CommonGoldTreasuryVo> goldTreasuryVos;
	
	private HashMap<String, CommonConsumeGiftVo> consumeGiftVos;
	
	public static CommonActivityPoolVo valueOf(Player player) {
		CommonActivityPoolVo vo = new CommonActivityPoolVo();
		vo.loginGiftActives = player.getCommonActivityPool().getCommonLoginGiftVos();
		vo.cheapGiftBagActives = player.getCommonActivityPool().getCommonCheapGiftBagVos();
		vo.rechargeActives = player.getCommonActivityPool().getCommonRechargeVos();
		vo.consumeActives = player.getCommonActivityPool().getCommonConsumeVos(player);
		vo.aliveBoss = CommonActivityManager.getInstance().getAliveBoss();
		vo.identifyTreasureVos = player.getCommonActivityPool().getIdentifyTreasureVos(player);
		vo.firstPayVos = player.getCommonActivityPool().getFirstPayVos(player);
		vo.treasureActiveVos = player.getCommonActivityPool().getCommonTreasureActiveVos();
		vo.redPackActiveVos = player.getCommonActivityPool().getCommonRedPackVos();
		vo.luckyDrawVo = player.getCommonActivityPool().getLuckyDrawVo();
		vo.goldTreasuryVos = player.getCommonActivityPool().getGoldTreasuryVos(player);
		vo.consumeGiftVos = player.getCommonActivityPool().getCommonConsumeGiftVos(player);
		return vo;
	}

	public HashMap<String, CommonLoginGiftVo> getLoginGiftActives() {
		return loginGiftActives;
	}

	public void setLoginGiftActives(HashMap<String, CommonLoginGiftVo> loginGiftActives) {
		this.loginGiftActives = loginGiftActives;
	}

	public HashMap<String, CommonCheapGiftBagVo> getCheapGiftBagActives() {
		return cheapGiftBagActives;
	}

	public void setCheapGiftBagActives(HashMap<String, CommonCheapGiftBagVo> cheapGiftBagActives) {
		this.cheapGiftBagActives = cheapGiftBagActives;
	}

	public HashMap<String, CommonRechargeVo> getRechargeActives() {
		return rechargeActives;
	}

	public void setRechargeActives(HashMap<String, CommonRechargeVo> rechargeActives) {
		this.rechargeActives = rechargeActives;
	}

	public HashMap<String, CommonConsumeActiveVo> getConsumeActives() {
		return consumeActives;
	}

	public void setConsumeActives(HashMap<String, CommonConsumeActiveVo> consumeActives) {
		this.consumeActives = consumeActives;
	}

	public HashSet<String> getAliveBoss() {
		return aliveBoss;
	}

	public void setAliveBoss(HashSet<String> aliveBoss) {
		this.aliveBoss = aliveBoss;
	}

	public HashMap<String, CommonIdentifyTreasureVo> getIdentifyTreasureVos() {
		return identifyTreasureVos;
	}

	public void setIdentifyTreasureVos(HashMap<String, CommonIdentifyTreasureVo> identifyTreasureVos) {
		this.identifyTreasureVos = identifyTreasureVos;
	}

	public HashMap<String, CommonFirstPayVo> getFirstPayVos() {
		return firstPayVos;
	}

	public void setFirstPayVos(HashMap<String, CommonFirstPayVo> firstPayVos) {
		this.firstPayVos = firstPayVos;
	}

	public HashMap<String, CommonTreasureActiveVo> getTreasureActiveVos() {
		return treasureActiveVos;
	}

	public void setTreasureActiveVos(HashMap<String, CommonTreasureActiveVo> treasureActiveVos) {
		this.treasureActiveVos = treasureActiveVos;
	}

	public HashMap<String, CommonRedPackVo> getRedPackActiveVos() {
		return redPackActiveVos;
	}

	public void setRedPackActiveVos(HashMap<String, CommonRedPackVo> redPackActiveVos) {
		this.redPackActiveVos = redPackActiveVos;
	}

	public LuckyDrawVo getLuckyDrawVo() {
		return luckyDrawVo;
	}

	public void setLuckyDrawVo(LuckyDrawVo luckyDrawVo) {
		this.luckyDrawVo = luckyDrawVo;
	}

	public HashMap<String, CommonGoldTreasuryVo> getGoldTreasuryVos() {
		return goldTreasuryVos;
	}

	public void setGoldTreasuryVos(HashMap<String, CommonGoldTreasuryVo> goldTreasuryVos) {
		this.goldTreasuryVos = goldTreasuryVos;
	}

	public HashMap<String, CommonConsumeGiftVo> getConsumeGiftVos() {
		return consumeGiftVos;
	}

	public void setConsumeGiftVos(HashMap<String, CommonConsumeGiftVo> consumeGiftVos) {
		this.consumeGiftVos = consumeGiftVos;
	}
}
