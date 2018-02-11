package com.mmorpg.mir.model.commonactivity.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.commonactivity.CommonActivityConfig;
import com.mmorpg.mir.model.commonactivity.model.vo.CommonCheapGiftBagVo;
import com.mmorpg.mir.model.commonactivity.model.vo.CommonConsumeActiveVo;
import com.mmorpg.mir.model.commonactivity.model.vo.CommonConsumeGiftVo;
import com.mmorpg.mir.model.commonactivity.model.vo.CommonFirstPayVo;
import com.mmorpg.mir.model.commonactivity.model.vo.CommonGoldTreasuryVo;
import com.mmorpg.mir.model.commonactivity.model.vo.CommonIdentifyTreasureVo;
import com.mmorpg.mir.model.commonactivity.model.vo.CommonLoginGiftVo;
import com.mmorpg.mir.model.commonactivity.model.vo.CommonRechargeVo;
import com.mmorpg.mir.model.commonactivity.model.vo.CommonRedPackVo;
import com.mmorpg.mir.model.commonactivity.model.vo.CommonTreasureActiveVo;
import com.mmorpg.mir.model.commonactivity.model.vo.LuckyDrawVo;
import com.mmorpg.mir.model.commonactivity.resource.CommonGoldTreasuryResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.serverstate.ServerState;

public class CommonActivityPool {
	/** 登陆有礼， key为活动的名字， value为活动记录 */
	private Map<String, CommonLoginGift> commonLoginActives;
	/** 特惠礼包， key为活动的名字， value为活动记录 */
	private Map<String, CommonCheapGiftBag> commonCheapActives;
	/** 消费活动， key为活动的名字， value为活动记录 */
	private Map<String, CommonConsumeActive> consumeActives;
	/** 充值献礼， key为活动的名字， value为活动记录 */
	private Map<String, CommonRechargeCelebrate> rechargeActives;
	/** 鉴宝， key为活动的名字， value为活动记录 */
	private Map<String, CommonIdentifyTreasure> identifyTreasures;
	/** 活动神秘商店， key为活动的名字， value为活动记录 */
	private Map<String, CommonMarcoShop> commonMarcoShop;
	/** 首充， key为活动的名字， value为活动记录 */
	private Map<String, CommonFirstPay> firstPays;
	/** 全民探宝， key为活动的名字， value为活动记录 */
	private Map<String, CommonTreasureActive> treasurueActives;
	/** 红包抢不停， key为活动的名字， value为活动记录 */
	private Map<String, CommonRedPack> redPacketActives;
	/** 找回， key为活动的名字， value为活动记录 */
	private Map<String, CommonRecollectActive> recollectActives;
	/** 幸运抽奖 */
	private LuckyDraw luckyDraw;
	/** 黄金宝库 */
	private Map<String, CommonGoldTreasury> goldTreasurys;
	/** 消费献礼 */
	private Map<String, CommonConsumeGift> consumeGifts;
	@Transient
	private Player owner;

	public static CommonActivityPool valueOf() {
		CommonActivityPool pool = new CommonActivityPool();
		pool.consumeActives = new HashMap<String, CommonConsumeActive>();
		pool.rechargeActives = new HashMap<String, CommonRechargeCelebrate>();
		pool.commonLoginActives = new HashMap<String, CommonLoginGift>();
		pool.commonCheapActives = new HashMap<String, CommonCheapGiftBag>();
		pool.identifyTreasures = new HashMap<String, CommonIdentifyTreasure>();
		pool.firstPays = new HashMap<String, CommonFirstPay>();
		pool.commonMarcoShop = new HashMap<String, CommonMarcoShop>();
		pool.treasurueActives = new HashMap<String, CommonTreasureActive>();
		pool.redPacketActives = new HashMap<String, CommonRedPack>();
		pool.recollectActives = new HashMap<String, CommonRecollectActive>();
		pool.setLuckyDraw(new LuckyDraw());
		pool.goldTreasurys = new HashMap<String, CommonGoldTreasury>();
		pool.consumeGifts = new HashMap<String, CommonConsumeGift>();
		return pool;
	}

	public Map<String, CommonLoginGift> getCommonLoginActives() {
		return commonLoginActives;
	}

	public void setCommonLoginActives(Map<String, CommonLoginGift> commonLoginActives) {
		this.commonLoginActives = commonLoginActives;
	}

	public Map<String, CommonCheapGiftBag> getCommonCheapActives() {
		return commonCheapActives;
	}

	public void setCommonCheapActives(Map<String, CommonCheapGiftBag> commonCheapActives) {
		this.commonCheapActives = commonCheapActives;
	}

	public Map<String, CommonConsumeActive> getConsumeActives() {
		return consumeActives;
	}

	public void setConsumeActives(Map<String, CommonConsumeActive> consumeActives) {
		this.consumeActives = consumeActives;
	}

	public Map<String, CommonRechargeCelebrate> getRechargeActives() {
		return rechargeActives;
	}

	public void setRechargeActives(Map<String, CommonRechargeCelebrate> rechargeActives) {
		this.rechargeActives = rechargeActives;
	}

	@JsonIgnore
	public HashMap<String, CommonLoginGiftVo> getCommonLoginGiftVos() {
		HashMap<String, CommonLoginGiftVo> vos = new HashMap<String, CommonLoginGiftVo>();
		for (String name : commonLoginActives.keySet()) {
			vos.put(name, CommonLoginGiftVo.valueOf(commonLoginActives.get(name).getRewarded()));
		}
		return vos;
	}

	@JsonIgnore
	public HashMap<String, CommonCheapGiftBagVo> getCommonCheapGiftBagVos() {
		HashMap<String, CommonCheapGiftBagVo> vos = new HashMap<String, CommonCheapGiftBagVo>();
		for (String name : commonCheapActives.keySet()) {
			vos.put(name, CommonCheapGiftBagVo.valueOf(commonCheapActives.get(name).getRewarded()));
		}
		return vos;
	}

	@JsonIgnore
	public HashMap<String, CommonRechargeVo> getCommonRechargeVos() {
		HashMap<String, CommonRechargeVo> results = new HashMap<String, CommonRechargeVo>();
		for (CommonRechargeCelebrate active : getRechargeActives().values()) {
			results.put(active.getAcitityName(), CommonRechargeVo.valueOf(active));
		}
		return results;
	}

	@JsonIgnore
	public HashMap<String, CommonConsumeActiveVo> getCommonConsumeVos(Player player) {
		HashMap<String, CommonConsumeActiveVo> results = new HashMap<String, CommonConsumeActiveVo>();
		for (CommonConsumeActive active : getConsumeActives().values()) {
			results.put(active.getActivityName(), CommonConsumeActiveVo.valueOf(player, active));
		}
		return results;
	}

	@JsonIgnore
	public HashMap<String, CommonIdentifyTreasureVo> getIdentifyTreasureVos(Player player) {
		HashMap<String, CommonIdentifyTreasureVo> results = new HashMap<String, CommonIdentifyTreasureVo>();
		CommonIdentifyTreasureTotalServers totalServers = ServerState.getInstance()
				.getCommonIdentifyTreasureTotalServers();
		for (String activeName : totalServers.getTreasureServers().keySet()) {
			CommonIdentifyTreasure treasure = identifyTreasures.get(activeName);
			if (treasure == null) {
				treasure = CommonIdentifyTreasure.valueOf(totalServers.getTreasureServerByActiveName(activeName)
						.getVersion());
			}
			results.put(activeName,
					CommonIdentifyTreasureVo.valueOf(totalServers.getTreasureServerByActiveName(activeName), treasure));
		}
		//
		// for (String activeName : identifyTreasures.keySet()) {
		// results.put(
		// activeName,
		// CommonIdentifyTreasureVo.valueOf(ServerState.getInstance().getCommonIdentifyTreasureTotalServers()
		// .getTreasureServerByActiveName(activeName),
		// identifyTreasures.get(activeName)));
		// }
		return results;
	}

	@JsonIgnore
	public HashMap<String, CommonFirstPayVo> getFirstPayVos(Player player) {
		HashMap<String, CommonFirstPayVo> results = new HashMap<String, CommonFirstPayVo>();
		for (String activeName : firstPays.keySet()) {
			results.put(activeName, CommonFirstPayVo.valueOf(firstPays.get(activeName)));
		}
		return results;
	}

	@JsonIgnore
	public HashMap<String, CommonRedPackVo> getCommonRedPackVos() {
		HashMap<String, CommonRedPackVo> result = new HashMap<String, CommonRedPackVo>();
		for (String activeName : redPacketActives.keySet()) {
			result.put(activeName, CommonRedPackVo.valueOf(redPacketActives.get(activeName)));
		}
		return result;
	}

	@JsonIgnore
	public LuckyDrawVo getLuckyDrawVo() {
		return LuckyDrawVo.valueOf(luckyDraw);
	}

	@JsonIgnore
	public HashMap<String, CommonTreasureActiveVo> getCommonTreasureActiveVos() {
		HashMap<String, CommonTreasureActiveVo> result = new HashMap<String, CommonTreasureActiveVo>();
		for (String activeName : treasurueActives.keySet()) {
			result.put(activeName, CommonTreasureActiveVo.valueOf(treasurueActives.get(activeName)));
		}
		return result;
	}

	public HashMap<String, CommonGoldTreasuryVo> getGoldTreasuryVos(Player player) {
		HashMap<String, CommonGoldTreasuryVo> result = new HashMap<String, CommonGoldTreasuryVo>();
		for (String activeName : goldTreasurys.keySet()) {
			CommonGoldTreasuryVo vo = CommonGoldTreasuryVo.valueOf(goldTreasurys.get(activeName));
			List<CommonGoldTreasuryResource> resources = CommonActivityConfig.getInstance().goldTreasuryStorage
					.getIndex(CommonGoldTreasuryResource.ACTIVE_NAME, activeName);
			for (CommonGoldTreasuryResource resource : resources) {
				if (!vo.getTreasurys().containsKey(resource.getGroupId())) {
					vo.getTreasurys().put(resource.getGroupId(), GoldTreasuryLog.valueOf());
				}
			}
			result.put(activeName, vo);
		}
		return result;
	}

	public HashMap<String, CommonConsumeGiftVo> getCommonConsumeGiftVos(Player player) {
		HashMap<String, CommonConsumeGiftVo> result = new HashMap<String, CommonConsumeGiftVo>();
		for (String activeName : consumeGifts.keySet()) {
			result.put(activeName, CommonConsumeGiftVo.ValueOf(consumeGifts.get(activeName)));
		}
		return result;
	}

	public Map<String, CommonIdentifyTreasure> getIdentifyTreasures() {
		return identifyTreasures;
	}

	public void setIdentifyTreasures(Map<String, CommonIdentifyTreasure> identifyTreasures) {
		this.identifyTreasures = identifyTreasures;
	}

	public CommonIdentifyTreasure getIdentifyTreasureByActiveName(String activeName) {
		return identifyTreasures.get(activeName);
	}

	public Map<String, CommonFirstPay> getFirstPays() {
		return firstPays;
	}

	public void setFirstPays(Map<String, CommonFirstPay> firstPays) {
		this.firstPays = firstPays;
	}

	public Map<String, CommonMarcoShop> getCommonMarcoShop() {
		return commonMarcoShop;
	}

	public void setCommonMarcoShop(Map<String, CommonMarcoShop> commonMarcoShop) {
		this.commonMarcoShop = commonMarcoShop;
	}

	public Map<String, CommonTreasureActive> getTreasurueActives() {
		return treasurueActives;
	}

	public void setTreasurueActives(Map<String, CommonTreasureActive> treasurueActives) {
		this.treasurueActives = treasurueActives;
	}

	public Map<String, CommonRedPack> getRedPacketActives() {
		return redPacketActives;
	}

	public void setRedPacketActives(Map<String, CommonRedPack> redPacketActives) {
		this.redPacketActives = redPacketActives;
	}

	public Map<String, CommonRecollectActive> getRecollectActives() {
		return recollectActives;
	}

	public void setRecollectActives(Map<String, CommonRecollectActive> recollectActives) {
		this.recollectActives = recollectActives;
	}

	@JsonIgnore
	public CommonRecollectActive getCurrentRecollectActive() {
		String activityName = CommonActivityConfig.getInstance().getCurrentCommonRecollectResources().iterator().next()
				.getActivityName();
		CommonRecollectActive active = recollectActives.get(activityName);
		if (active == null) {
			active = CommonRecollectActive.valueOf(owner);
			recollectActives.put(activityName, active);
		}
		return active;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public LuckyDraw getLuckyDraw() {
		return luckyDraw;
	}

	public void setLuckyDraw(LuckyDraw luckyDraw) {
		this.luckyDraw = luckyDraw;
	}

	public Map<String, CommonGoldTreasury> getGoldTreasurys() {
		return goldTreasurys;
	}

	public void setGoldTreasurys(Map<String, CommonGoldTreasury> goldTreasurys) {
		this.goldTreasurys = goldTreasurys;
	}

	public Map<String, CommonConsumeGift> getConsumeGifts() {
		return consumeGifts;
	}

	public void setConsumeGifts(Map<String, CommonConsumeGift> consumeGifts) {
		this.consumeGifts = consumeGifts;
	}

}
