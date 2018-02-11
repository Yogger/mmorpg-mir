package com.mmorpg.mir.model.openactive.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Transient;

import org.cliffc.high_scale_lib.NonBlockingHashSet;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 开服活动
 * 
 * @author Kuang Hao
 * @since v1.0 2015-6-4
 * 
 */
public class OpenActive {
	/** 经验大放送 */
	private ExpActive expActive;

	/** 橙色灵魂礼包 */
	private EquipActive equipActive;

	/** 全民强化 */
	private EquipEnhanceActive enhanceActive;

	/** 军衔礼包 */
	private MilitaryActive militaryActive;

	/** 消费礼包 */
	private ConsumeActive consumeActive;

	/** 等级竞技 */
	private LevelActive levelActive;

	/** 每日充值 */
	private EveryDayRecharge everyDayRecharge;

	/** 坐骑竞技 */
	private HorseUpgradeActive horseUpgradeActive;

	/** 神兵 */
	private ArtifactActive artifactActive;

	/** 战斗力 */
	private FightPowerActive fightPowerActive;

	/** 星级套装 */
	private StarItemActive starItemActive;

	/** 国旗争夺 */
	private CountryFlagActive countryFlagActive;

	/** 装备强化战斗力 */
	private EnhancePowerActive enhancePowerActive;

	/** 公测献礼 */
	private GiftActive giftActive;
	
	/** 英魂活动 */
	private SoulActive soulActive;
	
	/** 老服英魂活动 */
	private OldSoulActive oldSoulActive;
	
	/** 国家英雄活动 */
	private CountryHeroActive countryHeroActive;

	@Transient
	private NonBlockingHashSet<String> canRecieved = new NonBlockingHashSet<String>();
	
	@Transient
	private NonBlockingHashSet<String> oldCanRecieved = new NonBlockingHashSet<String>();

	@Transient
	private Map<Integer, CompeteRankActivity> competeStorage;

	public static OpenActive valueOf() {
		OpenActive o = new OpenActive();
		o.expActive = ExpActive.valueOf();
		o.equipActive = EquipActive.valueOf();
		o.enhanceActive = EquipEnhanceActive.valueOf();
		o.militaryActive = new MilitaryActive();
		o.consumeActive = new ConsumeActive();
		o.levelActive = new LevelActive();
		o.everyDayRecharge = EveryDayRecharge.valueOf();
		o.horseUpgradeActive = HorseUpgradeActive.valueOf();
		o.artifactActive = ArtifactActive.valueOf();
		o.fightPowerActive = FightPowerActive.valueOf();
		o.starItemActive = StarItemActive.valueOf();
		o.countryFlagActive = CountryFlagActive.valueOf();
		o.enhancePowerActive = EnhancePowerActive.valueOf();
		o.giftActive = GiftActive.valueOf();
		o.soulActive = SoulActive.valueOf();
		o.oldSoulActive = OldSoulActive.valueOf();
		o.countryHeroActive = CountryHeroActive.valueOf();
		return o;
	}
	
	public ExpActive getExpActive() {
		return expActive;
	}

	public void setExpActive(ExpActive expActive) {
		this.expActive = expActive;
	}

	public EquipActive getEquipActive() {
		return equipActive;
	}

	public void setEquipActive(EquipActive equipActive) {
		this.equipActive = equipActive;
	}

	public EquipEnhanceActive getEnhanceActive() {
		return enhanceActive;
	}

	public void setEnhanceActive(EquipEnhanceActive enhanceActive) {
		this.enhanceActive = enhanceActive;
	}

	public EveryDayRecharge getEveryDayRecharge() {
		return everyDayRecharge;
	}

	public void setEveryDayRecharge(EveryDayRecharge everyDayRecharge) {
		this.everyDayRecharge = everyDayRecharge;
	}

	public MilitaryActive getMilitaryActive() {
		return militaryActive;
	}

	public void setMilitaryActive(MilitaryActive militaryActive) {
		this.militaryActive = militaryActive;
	}

	public ConsumeActive getConsumeActive() {
		return consumeActive;
	}

	public void setConsumeActive(ConsumeActive consumeActive) {
		this.consumeActive = consumeActive;
	}

	public LevelActive getLevelActive() {
		return levelActive;
	}

	public void setLevelActive(LevelActive levelActive) {
		this.levelActive = levelActive;
	}

	public HorseUpgradeActive getHorseUpgradeActive() {
		return horseUpgradeActive;
	}

	public void setHorseUpgradeActive(HorseUpgradeActive horseUpgradeActive) {
		this.horseUpgradeActive = horseUpgradeActive;
	}

	public ArtifactActive getArtifactActive() {
		return artifactActive;
	}

	public void setArtifactActive(ArtifactActive artifactActive) {
		this.artifactActive = artifactActive;
	}

	public FightPowerActive getFightPowerActive() {
		return fightPowerActive;
	}

	public void setFightPowerActive(FightPowerActive fightPowerActive) {
		this.fightPowerActive = fightPowerActive;
	}

	public StarItemActive getStarItemActive() {
		return starItemActive;
	}

	public void setStarItemActive(StarItemActive starItemActive) {
		this.starItemActive = starItemActive;
	}

	public CountryFlagActive getCountryFlagActive() {
		return countryFlagActive;
	}

	public void setCountryFlagActive(CountryFlagActive countryFlagActive) {
		this.countryFlagActive = countryFlagActive;
	}

	public EnhancePowerActive getEnhancePowerActive() {
		return enhancePowerActive;
	}

	public void setEnhancePowerActive(EnhancePowerActive enhancePowerActive) {
		this.enhancePowerActive = enhancePowerActive;
	}

	public GiftActive getGiftActive() {
		return giftActive;
	}

	public void setGiftActive(GiftActive giftActive) {
		this.giftActive = giftActive;
	}

	@JsonIgnore
	public CompeteRankActivity getCompeteRankActivity(int type) {
		return competeStorage.get(type);
	}

	@JsonIgnore
	public void registerCompeteRankTypeActivity() {
		if (competeStorage == null) {
			competeStorage = new HashMap<Integer, CompeteRankActivity>();
		}
		competeStorage.put(artifactActive.getCompeteRankTypeValue(), artifactActive);
		competeStorage.put(consumeActive.getCompeteRankTypeValue(), consumeActive);
		competeStorage.put(enhanceActive.getCompeteRankTypeValue(), enhanceActive);
		competeStorage.put(fightPowerActive.getCompeteRankTypeValue(), fightPowerActive);
		competeStorage.put(horseUpgradeActive.getCompeteRankTypeValue(), horseUpgradeActive);
		competeStorage.put(levelActive.getCompeteRankTypeValue(), levelActive);
		competeStorage.put(militaryActive.getCompeteRankTypeValue(), militaryActive);
		competeStorage.put(soulActive.getCompeteRankTypeValue(), soulActive);
		competeStorage.put(oldSoulActive.getCompeteRankTypeValue(), oldSoulActive);
		competeStorage.put(countryHeroActive.getCompeteRankTypeValue(), countryHeroActive);
	}

	@JsonIgnore
	public NonBlockingHashSet<String> getCanRecieved() {
		return canRecieved;
	}

	@JsonIgnore
	public void setCanRecieved(NonBlockingHashSet<String> canRecieved) {
		this.canRecieved = canRecieved;
	}
	
	@JsonIgnore
	public NonBlockingHashSet<String> getOldCanRecieved() {
		return oldCanRecieved;
	}

	@JsonIgnore
	public void setOldCanRecieved(NonBlockingHashSet<String> oldCanRecieved) {
		this.oldCanRecieved = oldCanRecieved;
	}
	
	public SoulActive getSoulActive() {
		return soulActive;
	}

	public void setSoulActive(SoulActive soulActive) {
		this.soulActive = soulActive;
	}

	public OldSoulActive getOldSoulActive() {
		return oldSoulActive;
	}

	public void setOldSoulActive(OldSoulActive oldSoulActive) {
		this.oldSoulActive = oldSoulActive;
	}

	public CountryHeroActive getCountryHeroActive() {
		return countryHeroActive;
	}

	public void setCountryHeroActive(CountryHeroActive countryHeroActive) {
		this.countryHeroActive = countryHeroActive;
	}

}
