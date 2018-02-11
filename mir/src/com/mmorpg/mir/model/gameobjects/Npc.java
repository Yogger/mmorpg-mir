package com.mmorpg.mir.model.gameobjects;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.AIUtil;
import com.mmorpg.mir.model.ai.NpcAi;
import com.mmorpg.mir.model.ai.SkillSelector;
import com.mmorpg.mir.model.controllers.NpcController;
import com.mmorpg.mir.model.controllers.attack.AggroList;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.group.model.PlayerGroup;
import com.mmorpg.mir.model.object.ObjectManager;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.followpolicy.IFollowPolicy;
import com.mmorpg.mir.model.object.followpolicy.StateFollowPolicy;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.object.route.RouteRoad;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.world.WorldPosition;
import com.mmorpg.mir.model.world.resource.MapCountry;

public class Npc extends Creature {

	@Deprecated
	private int[] npcSkillIds;

	/** 警戒范围 */
	private int warnrange;
	/** 离家最远距离 **/
	private int homeRange;

	private RouteRoad routeRoad;

	private AI ai;

	/** 生成时间 */
	private long createTime;

	private MapCountry country;

	/** 是否可见 */
	private boolean show;

	private IFollowPolicy followPolicy;

	private SkillSelector skillSelector;

	private AggroList aggroList;

	private Map<Player, Long> damages = new NonBlockingHashMap<Player, Long>();
	
	private Map<Long, Long> onAttackTime = new NonBlockingHashMap<Long, Long>();

	public Npc(long objId, NpcController controller, WorldPosition position) {
		super(objId, controller, position);
		setAi(new NpcAi());
		setFollowPolicy(new StateFollowPolicy(this));
		aggroList = new AggroList(this);
		createTime = System.currentTimeMillis();
	}

	public Map<Integer, Player> getDamageRank() {
		Map<Integer, Player> ranks = New.hashMap();
		if (getDamages() != null && (!getDamages().isEmpty())) {
			List<Entry<Player, Long>> entrys = New.arrayList();
			for (Entry<Player, Long> entry : getDamages().entrySet()) {
				entrys.add(entry);
			}
			Collections.sort(entrys, new Comparator<Entry<Player, Long>>() {
				@Override
				public int compare(Entry<Player, Long> o1, Entry<Player, Long> o2) {
					if (o2.getValue() > o1.getValue()) {
						return 1;
					} else if (o2.getValue() < o1.getValue()) {
						return -1;
					} else {
						return 0;
					}
				}
			});
			int i = 1;
			for (Entry<Player, Long> entry : entrys) {
				ranks.put(i, entry.getKey());
				i++;
			}
		}
		return ranks;
	}

	@JsonIgnore
	public Map<Player, Long> getMostDamageCountryPlayers() {
		Map<Player, Long> playerDamage = getDamages();
		Map<Integer, Long> countryDamage = New.hashMap();
		for (CountryId id : CountryId.values()) {
			countryDamage.put(id.getValue(), 0L);
		}
		for (Entry<Player, Long> entry : playerDamage.entrySet()) {
			int countryValue = entry.getKey().getCountryValue();
			long total = countryDamage.get(countryValue) + entry.getValue();
			countryDamage.put(countryValue, total);
		}
		int mostDamageCountry = 0;
		long mostDamage = 0;
		for (Entry<Integer, Long> entry : countryDamage.entrySet()) {
			if (entry.getValue().longValue() > mostDamage) {
				mostDamage = entry.getValue().longValue();
				mostDamageCountry = entry.getKey().intValue();
			}
		}
		Map<Player, Long> result = New.hashMap();
		for (Entry<Player, Long> entry : playerDamage.entrySet()) {
			int countryValue = entry.getKey().getCountryValue();
			if (countryValue == mostDamageCountry) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}

	public Collection<Player> getMostDamagePlayers() {
		Player mostPlayer = getDamageRank().get(1);
		if (mostPlayer == null) {
			return null;
		}

		Map<PlayerGroup, Long> playerGroups = new HashMap<PlayerGroup, Long>();
		PlayerGroup mostDamageGroup = null;
		long mostDamage = 0;
		for (Entry<Player, Long> entry : damages.entrySet()) {
			if (entry.getKey().isInGroup()) {
				PlayerGroup group = entry.getKey().getPlayerGroup();
				if (mostDamageGroup == null) {
					mostDamageGroup = group;
					mostDamage = entry.getValue();
					playerGroups.put(mostDamageGroup, mostDamage);
				} else {
					if (playerGroups.containsKey(group)) {
						playerGroups.put(group, playerGroups.get(group) + entry.getValue());
					} else {
						playerGroups.put(group, entry.getValue());
					}
					if (playerGroups.get(group) > mostDamage) {
						mostDamageGroup = group;
						mostDamage = playerGroups.get(group);
					}
				}
			}
		}
		if (mostDamageGroup == null) {
			return Collections.unmodifiableCollection(Arrays.asList(mostPlayer));
		} else if (mostPlayer.getPlayerGroup() == mostDamageGroup || mostDamage > damages.get(mostPlayer)) {
			return Collections.unmodifiableCollection((mostDamageGroup.getMembers()));
		} else {
			return Collections.unmodifiableCollection(Arrays.asList(mostPlayer));
		}

	}

	public void addDamage(Creature creature, long value) {
		Player player = null;
		if (creature instanceof Summon) {
			player = ((Summon) creature).getMaster();
			if (creature instanceof Player) {
				player = (Player) creature;
			}
		}
		if (creature instanceof Player) {
			player = (Player) creature;
		}

		if (player != null) {
			if (getDamages().containsKey(player)) {
				Long oldValue = getDamages().get(player);
				if (oldValue == null) {
					return;
				}
				getDamages().put(player, oldValue + value);
			} else {
				getDamages().put(player, Long.valueOf(value));
			}
		}
	}

	public ObjectResource getObjectResource() {
		return ObjectManager.getInstance().getObjectResource(getObjectKey());
	}

	public boolean isAtSpawnLocation() {
		return MathUtil.getGridDistance(getX(), getY(), getBornX(), getBornY()) <= 4;
	}

	public boolean hasWalkRoutes() {
		return getSpawn().hasRandomWalk();
	}

	public boolean hasRouteStep() {
		return routeRoad != null && (!routeRoad.isOver());
	}

	public boolean canMove() {
		return getGameStats().getCurrentStat(StatEnum.SPEED) > 0;
	}

	public int[] getNpcSkillIds() {
		return npcSkillIds;
	}

	public void setNpcSkillIds(int[] npcSkillIds) {
		this.npcSkillIds = npcSkillIds;
	}

	public int getWarnrange() {
		return warnrange;
	}

	public void setWarnrange(int warnrange) {
		this.warnrange = warnrange;
	}

	public boolean isRestore() {
		return getObjectResource().getRestoreHp() > 0;
	}

	public RouteRoad getRouteRoad() {
		return routeRoad;
	}

	public void setRouteRoad(RouteRoad routeRoad) {
		this.routeRoad = routeRoad;
	}

	@Override
	public String getName() {
		return getObjectResource().getName();
	}

	@Override
	public NpcController getController() {
		return (NpcController) super.getController();
	}

	public long getCreateTime() {
		return createTime;
	}

	public void onDrop(Reward reward, Creature lastAttacker, Player mostDamagePlayer) {
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public boolean outWarning(int tx, int ty) {
		return followPolicy.outWarning(tx, ty);
	}

	public boolean tooFarFromHome(int tx, int ty) {
		return followPolicy.tooFarFromHome(tx, ty);
	}

	public AI getAi() {
		return ai != null ? ai : AI.dummyAi();
	}

	public void setAi(AI ai) {
		this.ai = ai;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.NPC;
	}

	public MapCountry getCountry() {
		return country;
	}

	public void setCountry(MapCountry country) {
		this.country = country;
	}

	@Override
	protected int getCountryValue() {
		return country.getValue();
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public IFollowPolicy getFollowPolicy() {
		return followPolicy;
	}

	public void setFollowPolicy(IFollowPolicy followPolicy) {
		this.followPolicy = followPolicy;
	}

	public int getHomeRange() {
		return homeRange;
	}

	public void setHomeRange(int homeRange) {
		this.homeRange = homeRange;
	}

	public void onAtSpawnLocation() {
		getDamages().clear();
	}

	public boolean findEnemy() {
		return AIUtil.addCommonHeat(this);
	}

	public SkillSelector getSkillSelector() {
		return skillSelector;
	}

	public void setSkillSelector(SkillSelector skillSelector) {
		this.skillSelector = skillSelector;
	}

	public AggroList getAggroList() {
		return this.aggroList;
	}

	@JsonIgnore
	public Map<Player, Long> getDamages() {
		return damages;
	}

	@JsonIgnore
	public void setDamages(Map<Player, Long> damages) {
		this.damages = damages;
	}

	@JsonIgnore
	public Map<Long, Long> getOnAttackTime() {
		return onAttackTime;
	}

	@JsonIgnore
	public void setOnAttackTime(Map<Long, Long> onAttackTime) {
		this.onAttackTime = onAttackTime;
	}
	
}
