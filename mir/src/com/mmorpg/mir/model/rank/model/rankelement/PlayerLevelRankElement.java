package com.mmorpg.mir.model.rank.model.rankelement;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.windforce.common.event.event.IEvent;

public class PlayerLevelRankElement extends RankRow {

	/** 玩家等级 */
	private int level;
	/** 角色 */
	private int role;
	/** 国家 */
	private int country;
	/** 转职等级 */
	private int promotionId;

	public static PlayerLevelRankElement valueOf(Player player) {
		PlayerLevelRankElement element = new PlayerLevelRankElement();
		element.objId = player.getObjectId();
		element.name = player.getName();
		element.role = player.getRole();
		element.country = player.getCountryValue();
		element.level = player.getLevel();
		element.promotionId = player.getPromotion().getStage();
		return element;
	}

	@Override
    public void changeByEvent(IEvent event) {
		if (event instanceof LevelUpEvent){
			LevelUpEvent up = (LevelUpEvent) event;
			level = up.getLevel();
		} else if (event instanceof PromotionEvent) {
			PromotionEvent e = (PromotionEvent) event;
			promotionId = e.getStage();
		}
	}
	
	@Override
    public int compareEvent(IEvent event) {
		LevelUpEvent e = (LevelUpEvent) event;
		return level - e.getLevel();
    }
	
	@Override
	public int compareTo(RankRow o) {
		final PlayerLevelRankElement other = (PlayerLevelRankElement) o;
		return other.level - level;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public static void main(String[] args) {
		List<RankRow> arrs = new ArrayList<RankRow>();
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			PlayerLevelRankElement e = new PlayerLevelRankElement();
			e.name = i + "";
			e.level = r.nextInt(250);
			arrs.add(e);
		}
		Collections.sort(arrs);
		for (int i = 0; i < 100; i++) {
			PlayerLevelRankElement rxx = (PlayerLevelRankElement) arrs.get(i);
			System.out.print(rxx.level + " ");
		}
		
		Class<?> clazz = PlayerLevelRankElement.class;
		for (Field f: clazz.getDeclaredFields()) {
			System.out.println(f.getName());
		}
	}

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

}
