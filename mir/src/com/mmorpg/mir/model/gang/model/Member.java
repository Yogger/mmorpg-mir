package com.mmorpg.mir.model.gang.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.session.SessionManager;

public class Member {
	private long playerId;
	private String name;
	private GangPosition position;
	private long joinTime;
	private int level;
	private long lastLoginTime;
	private int role;
	private long battlePoints;
	private int vip;
	private int promotionId;

	public static Member valueOf(Player player, GangPosition positon) {
		Member member = new Member();
		member.playerId = player.getObjectId();
		member.position = positon;
		member.name = player.getName();
		member.level = player.getLevel();
		member.role = player.getPlayerEnt().getRole();
		member.battlePoints = player.getGameStats().calcBattleScore();
		member.lastLoginTime = player.getPlayerStat().getLastLogin().getTime();
		member.vip = player.getVip().getLevel();
		member.promotionId = player.getPromotion().getStage();
		return member;
	}
	
	public boolean updateBattlePoints(Gang gang, long points, Player player) {
		vip = player.getVip().getLevel();
		if (points != battlePoints) {
			gang.decBattlePoints(battlePoints);
			gang.addBattlePoints(points);
			battlePoints = points;
			return true;
		}
		
		return false;
	}

	public MemberVO createVO() {
		MemberVO vo = new MemberVO();
		vo.setLastLoginTime(lastLoginTime);
		vo.setLevel(level);
		vo.setName(name);
		vo.setOnline(SessionManager.getInstance().isOnline(playerId) ? (byte) 1 : 0);
		vo.setPlayerId(playerId);
		vo.setPosition(position.name());
		vo.setRole(role);
		vo.setBattle(battlePoints);
		vo.setPromotionId(promotionId);
		vo.setVip(vip);
		return vo;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public GangPosition getPosition() {
		return position;
	}

	public void setPosition(GangPosition position) {
		this.position = position;
	}

	public long getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(long joinTime) {
		this.joinTime = joinTime;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	@JsonIgnore
	public boolean isMaster() {
		return position == GangPosition.Master;
	}

	@JsonIgnore
	public boolean isAssistant() {
		return position == GangPosition.Assistant;
	}

	@JsonIgnore
	public boolean isMember() {
		return position == GangPosition.Member;
	}

	public long getBattlePoints() {
		return battlePoints;
	}

	public void setBattlePoints(long battlePoints) {
		this.battlePoints = battlePoints;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

}
