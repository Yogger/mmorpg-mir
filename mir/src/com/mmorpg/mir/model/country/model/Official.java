package com.mmorpg.mir.model.country.model;

import java.sql.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.country.model.vo.OfficialVO;
import com.mmorpg.mir.model.country.packet.SM_Country_AutorityHistory;
import com.mmorpg.mir.model.country.packet.SM_Country_OfficalContexts;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.DateUtils;

public class Official {
	private long playerId;
	private CountryOfficial official;
	private int index;
	private Map<String, Integer> useAuthorityHistory;
	private Map<Integer, String> contexts;
	@Transient
	private Map<Long, Integer> calltogeterhCount = new ConcurrentHashMap<Long, Integer>();
	@Transient
	private long lastKingLoginBroadTime;

	public static Official valueOf(Player player, CountryOfficial official, int index) {
		Official offical = new Official();
		offical.playerId = player.getObjectId();
		offical.official = official;
		offical.index = index;
		offical.useAuthorityHistory = New.hashMap();
		offical.contexts = New.hashMap();
		return offical;
	}

	@JsonIgnore
	public void addCallCount(long playerId) {
		if (calltogeterhCount.containsKey(playerId)) {
			calltogeterhCount.put(playerId, calltogeterhCount.get(playerId) + 1);
		} else {
			calltogeterhCount.put(playerId, 1);
		}
	}

	@JsonIgnore
	public int getCallCount(long playerId) {
		if (calltogeterhCount.containsKey(playerId)) {
			return calltogeterhCount.get(playerId);
		} else {
			return 0;
		}
	}

	@JsonIgnore
	public boolean calltogetherCd() {
		if (!contexts.containsKey(OfficalCtxKey.CALLTOGETHER_CD_END.getValue())) {
			return false;
		}
		long endTime = Long.valueOf(contexts.get(OfficalCtxKey.CALLTOGETHER_CD_END.getValue()));
		if (System.currentTimeMillis() < endTime) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public void calltogether(long cd) {
		putContext(OfficalCtxKey.CALLTOGETHER_CD_END.getValue(), (System.currentTimeMillis() + cd) + "");
	}

	@JsonIgnore
	public void putContext(int key, String value) {
		contexts.put(key, value);
		// 通知前端
		PacketSendUtility.sendPacket(PlayerManager.getInstance().getPlayer(playerId),
				SM_Country_OfficalContexts.valueOf(key, value));

	}

	public static void main(String[] args) {
		System.out.println(DateUtils.date2String(new Date(1412778769021l), DateUtils.PATTERN_DATE_TIME));
	}

	public OfficialVO creatVO() {
		return OfficialVO.valueOf(PlayerManager.getInstance().getPlayer(playerId), this);
	}

	@JsonIgnore
	public void clearUseAuthorityHistory() {
		useAuthorityHistory.clear();
	}

	@JsonIgnore
	public void addUseAuthorityHistory(String id, int count) {
		if (useAuthorityHistory.containsKey(id)) {
			useAuthorityHistory.put(id, useAuthorityHistory.get(id) + count);
		} else {
			useAuthorityHistory.put(id, count);
		}
		// 通知前端
		PacketSendUtility.sendPacket(PlayerManager.getInstance().getPlayer(playerId),
				SM_Country_AutorityHistory.valueOf(id, useAuthorityHistory.get(id)));
	}

	@JsonIgnore
	public int getUseAuthorityHistory(String id) {
		if (useAuthorityHistory.containsKey(id)) {
			return useAuthorityHistory.get(id);
		} else {
			return 0;
		}
	}

	public boolean authority(Player player, String id) {
		return official.authority(id, player);
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public CountryOfficial getOfficial() {
		return official;
	}

	public void setOfficial(CountryOfficial official) {
		this.official = official;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Map<String, Integer> getUseAuthorityHistory() {
		return useAuthorityHistory;
	}

	public void setUseAuthorityHistory(Map<String, Integer> useAuthorityHistory) {
		this.useAuthorityHistory = useAuthorityHistory;
	}

	public Map<Integer, String> getContexts() {
		return contexts;
	}

	public void setContexts(Map<Integer, String> contexts) {
		this.contexts = contexts;
	}

	@JsonIgnore
	public Map<Long, Integer> getCalltogeterhCount() {
		return calltogeterhCount;
	}

	public void setCalltogeterhCount(Map<Long, Integer> calltogeterhCount) {
		this.calltogeterhCount = calltogeterhCount;
	}

	@JsonIgnore
	public long getLastKingLoginBroadTime() {
		return lastKingLoginBroadTime;
	}

	@JsonIgnore
	public void setLastKingLoginBroadTime(long lastKingLoginBroadTime) {
		this.lastKingLoginBroadTime = lastKingLoginBroadTime;
	}

}
