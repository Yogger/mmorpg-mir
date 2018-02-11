package com.mmorpg.mir.model.openactive.model.vo;

import java.util.Date;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.model.ArtifactActive;
import com.mmorpg.mir.model.openactive.model.EnhancePowerActive;
import com.mmorpg.mir.model.openactive.model.EquipActive;
import com.mmorpg.mir.model.openactive.model.EveryDayRecharge;
import com.mmorpg.mir.model.openactive.model.ExpActive;
import com.mmorpg.mir.model.openactive.model.HorseUpgradeActive;
import com.mmorpg.mir.model.openactive.model.SoulActive;

public class OpenActiveVo {

	private long todayRecharge;

	private EquipActive equipActive;

	private ExpActive expActive;

	private HorseUpgradeActive horseUpgrageActive;

	private ArtifactActive artifactActive;

	private EveryDayRecharge everyDayActive;
	
	private EnhancePowerActive enhancePowerActive;
	
	private SoulActive soulActive;

	public static OpenActiveVo valueOf(Player player) {
		OpenActiveVo result = new OpenActiveVo();
		result.todayRecharge = player.getVip().dayTotalCharge(new Date());
		result.equipActive = player.getOpenActive().getEquipActive();
		result.expActive = player.getOpenActive().getExpActive();
		result.everyDayActive = player.getOpenActive().getEveryDayRecharge();
		result.horseUpgrageActive = player.getOpenActive().getHorseUpgradeActive();
		result.artifactActive = player.getOpenActive().getArtifactActive();
		result.enhancePowerActive = player.getOpenActive().getEnhancePowerActive();
		result.soulActive = player.getOpenActive().getSoulActive();
		return result;
	}

	public EquipActive getEquipActive() {
		return equipActive;
	}

	public void setEquipActive(EquipActive equipActive) {
		this.equipActive = equipActive;
	}

	public ExpActive getExpActive() {
		return expActive;
	}

	public void setExpActive(ExpActive expActive) {
		this.expActive = expActive;
	}

	public long getTodayRecharge() {
		return todayRecharge;
	}

	public void setTodayRecharge(long todayRecharge) {
		this.todayRecharge = todayRecharge;
	}

	public HorseUpgradeActive getHorseUpgrageActive() {
		return horseUpgrageActive;
	}

	public void setHorseUpgrageActive(HorseUpgradeActive horseUpgrageActive) {
		this.horseUpgrageActive = horseUpgrageActive;
	}

	public EveryDayRecharge getEveryDayActive() {
		return everyDayActive;
	}

	public void setEveryDayActive(EveryDayRecharge everyDayActive) {
		this.everyDayActive = everyDayActive;
	}

	public ArtifactActive getArtifactActive() {
		return artifactActive;
	}

	public void setArtifactActive(ArtifactActive artifactActive) {
		this.artifactActive = artifactActive;
	}

	public EnhancePowerActive getEnhancePowerActive() {
		return enhancePowerActive;
	}

	public void setEnhancePowerActive(EnhancePowerActive enhancePowerActive) {
		this.enhancePowerActive = enhancePowerActive;
	}

	public SoulActive getSoulActive() {
		return soulActive;
	}

	public void setSoulActive(SoulActive soulActive) {
		this.soulActive = soulActive;
	}

}
