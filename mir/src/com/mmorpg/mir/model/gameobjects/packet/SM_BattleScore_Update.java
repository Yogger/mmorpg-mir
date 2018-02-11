package com.mmorpg.mir.model.gameobjects.packet;

public class SM_BattleScore_Update {

	private int battleScore;
	private int enhanceScore;
	private int horseEnhanceScore;

	public static SM_BattleScore_Update valueOf(int score, int enhanceScore, int horseEnhanceScore) {
		SM_BattleScore_Update update = new SM_BattleScore_Update();
		update.battleScore = score;
		update.enhanceScore = enhanceScore;
		update.horseEnhanceScore = horseEnhanceScore;
		return update;
	}

	public int getBattleScore() {
		return battleScore;
	}

	public void setBattleScore(int battleScore) {
		this.battleScore = battleScore;
	}

	public int getEnhanceScore() {
		return enhanceScore;
	}

	public void setEnhanceScore(int enhanceScore) {
		this.enhanceScore = enhanceScore;
	}

	public int getHorseEnhanceScore() {
		return horseEnhanceScore;
	}

	public void setHorseEnhanceScore(int horseEnhanceScore) {
		this.horseEnhanceScore = horseEnhanceScore;
	}

}
