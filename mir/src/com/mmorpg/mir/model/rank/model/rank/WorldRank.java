package com.mmorpg.mir.model.rank.model.rank;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.rank.entity.WorldRankEnt;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rank.model.DayKey;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.mmorpg.mir.model.rank.model.RankType;
import com.mmorpg.mir.model.rank.model.rankelement.CountryRankElement;
import com.mmorpg.mir.model.rank.packet.SM_Get_Rank;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.event.IEvent;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;

public class WorldRank {
	
	public static final String RANK_SEPERATOR = "|";

	private Long refreshTime;
	
	protected RankType rankType;
	/** 排行榜 */
	protected LinkedList<RankRow> rankRows;

	@JsonIgnore
	protected WorldRankEnt rankEnt;

	@JsonIgnore
	private ReentrantReadWriteLock lock;

	public WorldRank() {
	};

	public WorldRank(RankType type) {
		rankRows = new LinkedList<RankRow>();
		lock = new ReentrantReadWriteLock();
		refreshTime = System.currentTimeMillis();
		rankType = type;
	}

	public LinkedList<RankRow> getRankRows() {
		return rankRows;
	}

	public void setRankRows(LinkedList<RankRow> rankRows) {
		this.rankRows = rankRows;
	}

	@JsonIgnore
	public void setRankRowsBySpecified(LinkedList<RankRow> rankRows) {
		try {
			lock.writeLock().lock();
			this.rankRows = rankRows;
			update();
		} finally {
			lock.writeLock().unlock();
		}
	}

	@JsonIgnore
	public LinkedList<RankRow> getRankRowsCopy() {
		try {
			lock.readLock().lock();
			return new LinkedList<RankRow>(rankRows);
		} finally {
			lock.readLock().unlock();
		}
	}

	@JsonIgnore
	public Map<Integer, RankRow> contains(long ownerId) {
		try {
			lock.readLock().lock();
			for (int i = 0; i < rankRows.size(); i++) {
				if (rankRows.get(i).getObjId() == ownerId) {
					Map<Integer, RankRow> map = New.hashMap();
					map.put(i, rankRows.get(i));
					return map;
				}
			}
			return null;
		} finally {
			lock.readLock().unlock();
		}
	}
	
	@JsonIgnore
	public int getMyRank(long ownerId) {
		try {
			lock.readLock().lock();
			for (int i = 0; i < rankRows.size(); i++) {
				if (rankRows.get(i).getObjId() == ownerId) {
					return i + 1;
				}
			}
			return 0;
		} finally {
			lock.readLock().unlock();
		}
	}
	
	@JsonIgnore
	public long getSpecifiedRankId(int rank) {
		try {
			lock.readLock().lock();
			if (rank < 0 || rank >= rankRows.size()) {
				return 0;
			}
			return rankRows.get(rank).getObjId();
		} finally {
			lock.readLock().unlock();
		}
	}

	@JsonIgnore
	public void initLock() {
		if (lock == null) {
			lock = new ReentrantReadWriteLock();
		}
	}

	public RankType getRankType() {
		return rankType;
	}

	public void setRankType(RankType rankType) {
		this.rankType = rankType;
	}

	public Long getRefreshTime() {
    	return refreshTime;
    }

	public void setRefreshTime(Long refreshTime) {
    	this.refreshTime = refreshTime;
    }

	@JsonIgnore
	public WorldRankEnt getRankEnt() {
		return rankEnt;
	}

	@JsonIgnore
	public void setRankEnt(WorldRankEnt rankEnt) {
		this.rankEnt = rankEnt;
	}

	@JsonIgnore
	public void update() {
		WorldRankManager.getInstance().updateRank(this);
	}

	@JsonIgnore
    public void submitRankRow(Player player, IEvent event) {
		int sizeLimit = WorldRankManager.getInstance().MAX_SIZE_MAP.getValue().get(rankType.getRootRank().name());
		try {
			lock.writeLock().lock();
			int index = -1;
			for (int j = 0; j < rankRows.size(); j++) {
				if (rankRows.get(j).getObjId() == event.getOwner()) {
					boolean becomeSmaller = rankRows.get(j).compareEvent(event) > 0;
					if (becomeSmaller) {
						rankRows.remove(j);
					} else {
						index = j;
					}
					break;
				}
			}
			int iter = index;
			int i = 0;
			if (index < 0) {
				i = rankRows.size() - 1;
			} else {
				i = index - 1;
			}
			for (; i >= 0; i--) {
				if (rankRows.get(i).compareEvent(event) < 0) {
					iter = i;
				} else {
					break;
				}
			}
			if (iter < 0 && rankRows.size() < sizeLimit) {
				rankRows.add(rankType.createElement(player));
			} else if (index != iter) {
				if (index >= 0) {
					rankRows.remove(index);
				}
				rankRows.add(iter, rankType.createElement(player));
			} else if (index >= 0) {
				if (rankRows.get(index).compareEvent(event) < 0) {
					rankRows.get(index).changeByEvent(event);
				}
			}
			if (rankRows.size() > sizeLimit) {
				rankRows.removeLast();
			}

			update();
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@JsonIgnore
	public void changeRankElementByEvent(IEvent event) {
		try {
			lock.writeLock().lock();
			for (int i = 0; i < rankRows.size(); i++) {
				RankRow rankRow = rankRows.get(i);
				if (rankRow.getObjId() == event.getOwner()) {
					rankRow.changeByEvent(event);
					update();
					break;
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
	}


	public void getRank(Player player, int start, int end) {
		try {
			lock.readLock().lock();
			if (start >= rankRows.size()) {
				SM_Get_Rank sm = new SM_Get_Rank();
				sm.setType(rankType.getRootRank().getValue());
				PacketSendUtility.sendPacket(player, sm);
				return;
			}
			if (end > rankRows.size()) {
				end = rankRows.size();
			}
			int myRank = -1;
			if (rankType == RankType.QI_TODAY_HERO || rankType == RankType.CHU_TODAY_HERO || rankType == RankType.ZHAO_TODAY_HERO) {//取自己杀了多少人
				DayKey key = DayKey.valueOf();
				Set<Long> killSet = player.getRankInfo().getSlaughterHistory().get(key.getLunchTime());
				if (killSet != null) {
					myRank = killSet.size() - 1;
				}
			} else if(rankType == RankType.QI_YESTERDAY_HERO || rankType == RankType.CHU_YESTERDAY_HERO || rankType == RankType.ZHAO_YESTERDAY_HERO){
				DayKey key = DayKey.valueOf();
				Long lastDay = key.getLunchTime() - DateUtils.MILLIS_PER_DAY;
				Set<Long> killSet = player.getRankInfo().getSlaughterHistory().get(lastDay);
				if (killSet != null) {
					myRank = killSet.size() - 1;
				}
			} else if (rankType == RankType.COUNTRY_POWER) {
				LinkedList<RankRow> countryPower = new LinkedList<RankRow>();
				for (int i = 0; i < rankRows.size(); i++) {
					CountryRankElement source = (CountryRankElement) rankRows.get(i);
					CountryRankElement e = new CountryRankElement();
					Player king = CountryManager.getInstance().getCountries().get(CountryId.valueOf((int) source.getObjId())).getKing();
					if (king != null) {
						e.setObjId(king.getObjectId());
						e.setKing(king.getName());
					}
					e.setPower(source.getPower());
					e.setName(source.getName());
					countryPower.add(e);
				}
				Collections.sort(countryPower);
				PacketSendUtility.sendPacket(
				        player,
				        SM_Get_Rank.valueOf(countryPower.size(), rankType.getRootRank().getValue(),
				                (AbstractList<RankRow>) countryPower.subList(start, end), myRank + 1));
				return;
			} else {
				for (int i = 0; i < rankRows.size(); i++) {
					if (rankRows.get(i).getObjId() == player.getObjectId().longValue()) {
						myRank = i;
						break;
					}
				}
			}
			
			ArrayList<RankRow> copy = new ArrayList<RankRow>(rankRows);
			PacketSendUtility.sendPacket(
			        player,
			        SM_Get_Rank.valueOf(rankRows.size(), rankType.getRootRank().getValue(),
			                (AbstractList<RankRow>) copy.subList(start, end), myRank + 1));
		} finally {
			lock.readLock().unlock();
		}
	}

	@JsonIgnore
	public boolean isPlayerInRankRange(Player player, int begin, int end) {
		try {
			lock.readLock().lock();
			if (begin < 0 || begin >= rankRows.size()) {
				return false;
			}
			if (end < 0 || begin > end) {
				return false;
			}
			if (end >= rankRows.size()) {
				end = rankRows.size() - 1;
			}
			for (int i = begin; i <= end; i++) {
				if (rankRows.get(i).getObjId() == player.getObjectId()) {
					return true;
				}
			}
			return false;
		} finally {
			lock.readLock().unlock();
		}
	}
	
	@JsonIgnore
	public static String serializeToString(WorldRank rank) {
		return JsonUtils.object2String(rank.getRankType()) + RANK_SEPERATOR + JsonUtils.object2String(rank.getRefreshTime()) + RANK_SEPERATOR + JsonUtils.object2String(rank.getRankRowsCopy());
	}
	
	@JsonIgnore
	@SuppressWarnings("unchecked")
    public static WorldRank deserializeToObject(String dbJson) {
		WorldRank rank = new WorldRank();
		String[] thirdPart = dbJson.split("[" + RANK_SEPERATOR + "]");
		RankType type = JsonUtils.string2Object(thirdPart[0], RankType.class);
		rank.setRefreshTime(JsonUtils.string2Object(thirdPart[1], Long.class));
		rank.setRankType(type);
		rank.setRankRows(JsonUtils.string2Collection(thirdPart[2], LinkedList.class, type.getElementClassType()));
		return rank;
	}
	
}
