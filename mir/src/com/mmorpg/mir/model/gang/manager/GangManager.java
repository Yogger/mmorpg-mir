package com.mmorpg.mir.model.gang.manager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mmorpg.mir.model.boss.config.BossConfig;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.complexstate.ComplexStateType;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.dirtywords.model.WordsType;
import com.mmorpg.mir.model.dirtywords.service.DirtyWordsManager;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.RequestHandlerType;
import com.mmorpg.mir.model.gameobjects.RequestResponseHandler;
import com.mmorpg.mir.model.gang.entity.GangEnt;
import com.mmorpg.mir.model.gang.model.Apply;
import com.mmorpg.mir.model.gang.model.Gang;
import com.mmorpg.mir.model.gang.model.GangPosition;
import com.mmorpg.mir.model.gang.model.Member;
import com.mmorpg.mir.model.gang.model.PlayerApply;
import com.mmorpg.mir.model.gang.model.PlayerInvite;
import com.mmorpg.mir.model.gang.model.log.GangLogFactory;
import com.mmorpg.mir.model.gang.packet.SM_ApplyResult_Gang;
import com.mmorpg.mir.model.gang.packet.SM_Apply_List;
import com.mmorpg.mir.model.gang.packet.SM_Disband_Gang;
import com.mmorpg.mir.model.gang.packet.SM_Expel_Gang;
import com.mmorpg.mir.model.gang.packet.SM_GangInfo;
import com.mmorpg.mir.model.gang.packet.SM_GangInvite;
import com.mmorpg.mir.model.gang.packet.SM_Gang_Ask_Help;
import com.mmorpg.mir.model.gang.packet.SM_Gang_List;
import com.mmorpg.mir.model.gang.packet.SM_Gang_Login;
import com.mmorpg.mir.model.gang.packet.SM_IMPEACH_GANG;
import com.mmorpg.mir.model.gang.packet.SM_PlayerApply_Gang;
import com.mmorpg.mir.model.gang.packet.SM_Self_Gang_Change;
import com.mmorpg.mir.model.gang.packet.SM_Set_AutoDeal;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.util.IdentifyManager;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.resource.MapResource;
import com.mmorpg.mir.utils.CharCheckUtil;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.orm.Querier;
import com.windforce.common.ramcache.service.EntityBuilder;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.JsonUtils;
import com.windforce.common.utility.collection.ConcurrentHashSet;

@Component
public class GangManager {

	private static Logger logger = Logger.getLogger(GangManager.class);
	@Inject
	private EntityCacheService<Long, GangEnt> gangEntDbService;

	private ConcurrentHashMap<Long, Gang> gangs = new ConcurrentHashMap<Long, Gang>();

	@Autowired
	private IdentifyManager identifyManager;
	@Autowired
	private Querier querier;
	@Autowired
	private CoreActionManager coreActionManager;
	private Set<String> gangNameExists = new ConcurrentHashSet<String>();

	private ReentrantLock lock = new ReentrantLock();

	/** 驱逐出帮条件 */
	@Static("GANG:GANG_EXPEL_CONDITION")
	public ConfigValue<String[]> GANG_EXPEL_CONDITION;

	/** 邀请入帮条件 */
	@Static("GANG:GANG_DEAL_INVITE_CONDITION")
	public ConfigValue<String[]> GANG_DEAL_INVITE_CONDITION;

	/** 申请入帮条件 */
	@Static("GANG:GANG_APPLY_CONDITION")
	public ConfigValue<String[]> GANG_APPLY_CONDITION;

	/** 更换帮会成员职位条件 */
	@Static("GANG:GANG_CHANGEPOSITION")
	public ConfigValue<String[]> GANG_CHANGEPOSITION;

	/** 解散帮会条件 */
	@Static("GANG:GANG_DISBAND")
	public ConfigValue<String[]> GANG_DISBAND;

	/** 退出帮会条件 */
	@Static("GANG:GANG_EXIT_CONDITION")
	public ConfigValue<String[]> GANG_EXIT_CONDITION;

	/** 创建帮会的条件 */
	@Static("GANG:GANG_CREATE_CONDITION")
	public ConfigValue<String[]> GANG_CREATE_CONDITION;

	/** 创建帮会需要扣除的钱 */
	@Static("GANG:GANG_CREATE_CURRENCY")
	public ConfigValue<String> GANG_CREATE_CURRENCY;

	/** 家族名字长度限制 - 字符个数 */
	@Static("GANG:GANG_CREATE_NAME_LENGTH")
	public ConfigValue<Integer> GANG_CREATE_NAME_LENGTH;

	@Static("GANG:GANG_APPLY_MAXSIZE")
	public ConfigValue<Integer> GANG_APPLY_MAXSIZE;

	@Static("GANG:GANG_MEMBER_MAXSIZE")
	public ConfigValue<Integer> GANG_MEMBER_MAXSIZE;

	@Static("GANG:GANG_IMPEACH_INTERVAL")
	public ConfigValue<Integer> GANG_IMPEACH_INTERVAL;

	@Static("GANG:GANG_MEMBER_IMPEACH_INTERVAL")
	public ConfigValue<Integer> GANG_MEMBER_IMPEACH_INTERVAL;

	@Static("GANG:CRY_FOR_HELP_ACT")
	public ConfigValue<String[]> CRY_FOR_HELP_ACT;

	@Static("GANG:ACCEPT_HELP_COND")
	public ConfigValue<String[]> ACCEPT_HELP_COND;

	@Static("GANG:JOIN_CHAT_CD")
	public ConfigValue<Integer> JOIN_CHAT_CD;

	@Static("GANG:FUZZY_SEARCH_LIMIT")
	public ConfigValue<Integer> FUZZY_SEARCH_LIMIT;

	@Static("GANG:CRY4HELP_DEPRECATE_DURATION")
	public ConfigValue<Integer> CRY4HELP_DEPRECATE_DURATION;

	@Static("GANG:ENTER_NEW_GANG_CD")
	public ConfigValue<Integer> ENTER_NEW_GANG_CD;

	@Autowired
	private PlayerManager playerManager;
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private GangLogFactory gangLogFactory;

	private static GangManager instance;

	@PostConstruct
	public void init() {
		setInstance(this);
		List<GangEnt> gangEnts = querier.all(GangEnt.class);
		for (GangEnt ent : gangEnts) {
			gangs.put(ent.getId(), ent.getGang());
			gangNameExists.add(ent.getName());
		}

		for (Gang gang : gangs.values()) {
			long delay = System.currentTimeMillis() - gang.getStartImpeachTime();
			if (gang.getMemberImpeachId() != 0L) {
				if (delay >= GANG_MEMBER_IMPEACH_INTERVAL.getValue()) {
					delay = GANG_MEMBER_IMPEACH_INTERVAL.getValue();
				}
				submitImpeach(gang, delay);
			}
			gang.refreshBattlePoints();
		}

	}

	public void setAutoDeal(Player player, boolean autoDeal) {
		try {
			lock.lock();
			Gang gang = this.load(player.getPlayerGang().getGangId());
			gang.isRight(player, GangPosition.Assistant);
			gang.setAutoDeal(autoDeal);
			PacketSendUtility.sendPacket(player, SM_Set_AutoDeal.valueOf(autoDeal));
		} finally {
			lock.unlock();
		}
	}

	public Gang apply(Player player, long id, boolean byPlayerId) {
		try {
			lock.lock();
			CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
					GANG_APPLY_CONDITION.getValue());
			if (!conditions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.SYS_ERROR);
			}
			if (player.getPlayerGang().getGangId() != 0) {
				throw new ManagedException(ManagedErrorCode.GANG_JOIN);
			}
			if (player.getPlayerGang().getLastQuitGangTime() + ENTER_NEW_GANG_CD.getValue() >= System
					.currentTimeMillis()) {
				throw new ManagedException(ManagedErrorCode.APPLY_GANG_IN_QUIT_CD);
			}

			Gang gang = null;
			if (byPlayerId) {
				if (!SessionManager.getInstance().isOnline(id)) {
					throw new ManagedException(ManagedErrorCode.PLAYER_INLINE);
				}
				Player target = PlayerManager.getInstance().getPlayer(id);
				gang = target.getGang();
				if (gang == null) {
					throw new ManagedException(ManagedErrorCode.APPLY_GANG_NOT_EXIST);
				}
			} else {
				gang = gangs.get(id);
				if (gang == null) {
					throw new ManagedException(ManagedErrorCode.GANG_NOT_EXIST);
				}
			}
			if (player.getPlayerGang().getApplies().size() >= GANG_APPLY_MAXSIZE.getValue()) {
				throw new ManagedException(ManagedErrorCode.GANG_APPLY_MAXSIZE);
			}
			gang.addApply(player);
			player.getPlayerGang().getApplies().add(PlayerApply.valueOf(id));
			if (gang.isAutoDeal()) {
				doDealApply(gang, player.getObjectId(), true);
			} else {
				// 通知在线的长老
				gang.send(SM_PlayerApply_Gang.valueOf(player), GangPosition.Assistant, sessionManager);
			}
			this.update(gang);
			return gang;
		} finally {
			lock.unlock();
		}

	}

	public Gang applyCancel(Player player, long id) {
		try {
			lock.lock();
			Gang gang = this.load(id);
			gang.cancelApply(player.getObjectId());
			player.getPlayerGang().cancelApply(id);
			this.update(gang);
			return gang;
		} finally {
			lock.unlock();
		}
	}

	public void changePosition(Player master, long targetId, int position) {
		try {
			lock.lock();
			CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
					GANG_CHANGEPOSITION.getValue());
			if (!conditions.verify(master, true)) {
				throw new ManagedException(ManagedErrorCode.KINGOFWAR_FIGHTING);
			}
			Gang gang = this.load(master.getPlayerGang().getGangId());
			gang.isRight(master, GangPosition.Master);
			Member operate = gang.load(master.getObjectId());
			Member target = gang.load(targetId);
			if (master.getObjectId().longValue() == targetId) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}
			Player targetPlayer = playerManager.getPlayer(targetId);

			if (position == GangPosition.Assistant.getValue()) {
				// 长老转让
				Member member = gang.getAssistant();
				if (member != null) {
					gang.setMemberPosition(member.getPlayerId(), GangPosition.Member);
				}

				if (operate.isMaster()) {
					// notice
					I18nUtils utils = I18nUtils.valueOf("304003").addParm("user",
							I18nPack.valueOf(master.createSimple()));
					utils.addParm("targetuser", I18nPack.valueOf(targetPlayer.createSimple()));
					ChatManager.getInstance().sendSystem(2, utils, null, gang);
				}
			}

			if (target.getPosition() == GangPosition.Assistant && position > GangPosition.Assistant.getValue()) {
				I18nUtils utils = I18nUtils.valueOf("304004").addParm("user", I18nPack.valueOf(master.createSimple()));
				utils.addParm("targetuser", I18nPack.valueOf(targetPlayer.createSimple()));
				ChatManager.getInstance().sendSystem(2, utils, null, gang);
			}

			if (position == GangPosition.Master.getValue()) {
				// 族长转让
				gang.setMemberPosition(master.getObjectId(), GangPosition.Member);
				gang.addGangLog(gangLogFactory.transferLog(master, target));

				// notice
				I18nUtils utils = I18nUtils.valueOf("304002").addParm("user", I18nPack.valueOf(master.createSimple()));
				utils.addParm("targetuser", I18nPack.valueOf(targetPlayer.createSimple()));

				ChatManager.getInstance().sendSystem(2, utils, null, gang);

				gang.setMemberImpeachId(0);
			}
			gang.setMemberPosition(targetId, GangPosition.valueOf(position));
			this.update(gang);
		} finally {
			lock.unlock();
		}

	}

	public Gang createGang(Player player, String gangName) {
		try {
			lock.lock();

			if (gangName == null || gangName.trim().isEmpty()) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_NAME_IS_NULL);
			}
			String checkName = gangName.substring(("s" + player.getPlayerEnt().getServer()).length() + 1);
			if (StringUtils.containsWhitespace(checkName) || !CharCheckUtil.checkString(checkName)
					|| DirtyWordsManager.getInstance().containsWords(checkName, WordsType.ROLEWORDS)) {
				throw new ManagedException(ManagedErrorCode.WORDS_SENSITIVE);
			}
			// 名字长度限制
			int length = GANG_CREATE_NAME_LENGTH.getValue();
			char[] chars = checkName.toCharArray();
			if (chars.length > length) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_NAME_LENGTH_2_LANG);
			}
			if (gangNameExists.contains(gangName)) {
				throw new ManagedException(ManagedErrorCode.GANG_NAME_REPEAT);
			}
			if (player.getPlayerGang().getGangId() != 0) {
				throw new ManagedException(ManagedErrorCode.GANG_JOIN);
			}
			// 帮会创建条件
			String[] keys = GANG_CREATE_CONDITION.getValue();
			CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1, keys);
			if (!conditions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}

			boolean firstGang = isCountryHasUniqueGang(player);
			// 本国第一个家族免费
			if (false == firstGang) {
				// 扣钱
				CoreActions actions = coreActionManager.getCoreActions(GANG_CREATE_CURRENCY.getValue());
				actions.verify(player, true);
				ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.GANG, SubModuleType.CREATE_GANG);
				actions.act(player, moduleInfo);
			}
			// 创建
			final Gang gang = Gang.valueOf(identifyManager.getNextIdentify(IdentifyType.GANG), gangName,
					player.getCountryId(), player);
			gangNameExists.add(gangName);
			gang.join(player, GangPosition.Master, false);
			GangEnt gangEnt = gangEntDbService.loadOrCreate(gang.getId(), new EntityBuilder<Long, GangEnt>() {
				@Override
				public GangEnt newInstance(Long id) {
					return GangEnt.valueOf(id, gang.getName());
				}
			});
			gangEnt.setGang(gang);

			gang.setGangEnt(gangEnt);
			gangs.put(gang.getId(), gang);
			gang.addGangLog(gangLogFactory.createLog(gang.getMaster().getName()));

			refreshRank();
			this.update(gang);

			I18nUtils utils = I18nUtils.valueOf("304000")
					.addParm(I18NparamKey.FAMILY, I18nPack.valueOf(gang.getName()))
					.addParm(I18NparamKey.FAMILYID, I18nPack.valueOf(String.valueOf(gang.getId())));
			ChatManager.getInstance().sendSystem(6, utils, null, player.getCountry());

			if (true == firstGang) {
				I18nUtils firstUtils = I18nUtils.valueOf("405007").addParm("family", I18nPack.valueOf(gang.getName()))
						.addParm("name", I18nPack.valueOf(player.getName()))
						.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
				ChatManager.getInstance().sendSystem(71001, firstUtils, null);
			}

			return gang;
		} finally {
			lock.unlock();
		}
	}

	private boolean isCountryHasUniqueGang(Player player) {
		boolean firstGang = true;
		for (Gang g : gangs.values()) {
			if (g.getCountry() == player.getCountryId()) {
				firstGang = false;
				break;
			}
		}
		return firstGang;
	}

	private void doDealApply(Gang gang, long id, boolean ok) {
		Apply apply = gang.getApplies().get(id);
		Player applyer = playerManager.getPlayer(id);
		if (apply == null && applyer.isInGang()) {
			throw new ManagedException(ManagedErrorCode.GANG_JOIN);
		} else if (apply == null) {
			throw new ManagedException(ManagedErrorCode.GANG_APPLIER_REJECT);
		}
		SM_ApplyResult_Gang sm = new SM_ApplyResult_Gang();
		sm.setGangId(gang.getId());
		sm.setType(0);
		if (applyer.getPlayerGang().getGangId() != 0) {
			throw new ManagedException(ManagedErrorCode.GANG_JOIN);
		}
		if (!ok) {
			sm.setResult((byte) 0);
			PacketSendUtility.sendPacket(applyer, sm);
			gang.getApplies().remove(apply.getPlayerId());
			applyer.getPlayerGang().cancelApply(gang.getId());
			this.update(gang);
			return;
		}
		if (gang.getMembers().size() >= GANG_MEMBER_MAXSIZE.getValue()) {
			throw new ManagedException(ManagedErrorCode.GANG_MAX_SIZE);
		}
		gang.getApplies().remove(apply.getPlayerId());
		gang.join(applyer, GangPosition.Member, true);
		gang.addGangLog(gangLogFactory.joinLog(applyer.getName()));
		sm.setServer(gang.getServer());
		sm.setGangName(gang.getName());
		sm.setResult((byte) 1);
		sm.setApplyPlayerName(apply.getName());
		PacketSendUtility.sendPacket(applyer, sm);
		this.update(gang);
	}

	public void dealApply(Player player, long id, boolean ok) {
		try {
			lock.lock();
			CoreConditions conditions = CoreConditionManager.getInstance()
					.getCoreConditions(1, GANG_DISBAND.getValue());
			if (!conditions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.KINGOFWAR_FIGHTING);
			}
			Gang gang = load(player.getPlayerGang().getGangId());
			gang.isRight(player, GangPosition.Assistant);
			this.doDealApply(gang, id, ok);
		} finally {
			lock.unlock();
		}
	}

	public void disband(Player master) {
		try {
			lock.lock();
			// 如果在咸阳争夺战中不允许解散家族
			CoreConditions conditions = CoreConditionManager.getInstance()
					.getCoreConditions(1, GANG_DISBAND.getValue());
			if (!conditions.verify(master, true)) {
				throw new ManagedException(ManagedErrorCode.KINGOFWAR_FIGHTING);
			}
			Gang gang = this.load(master.getPlayerGang().getGangId());
			gang.isRight(master, GangPosition.Master);

			// notice
			I18nUtils utils = I18nUtils.valueOf("304009")
					.addParm(I18NparamKey.FAMILY, I18nPack.valueOf(gang.getName()))
					.addParm("user", I18nPack.valueOf(master.createSimple()));
			ChatManager.getInstance().sendSystem(2, utils, null, gang);

			for (Member m : gang.getMembers().values()) {
				Player gangPlayer = playerManager.getPlayer(m.getPlayerId());
				gangPlayer.getPlayerGang().setGangAndUpdate(0);
				PacketSendUtility.broadcastPacket(gangPlayer,
						SM_Self_Gang_Change.valueOf(gangPlayer.getObjectId(), null));
			}
			gang.send(new SM_Disband_Gang(), GangPosition.Member, sessionManager);
			gangs.remove(gang.getId());
			gangNameExists.remove(gang.getName());
			gangEntDbService.remove(gang.getId());
			this.update(gang);
		} finally {
			lock.unlock();
		}
	}

	public void doRemoveGang(Gang gang) {
		gangs.remove(gang.getId());
		gangNameExists.remove(gang.getName());
		gangEntDbService.remove(gang.getId());
	}

	public void expel(Player master, long targetId) {
		try {
			lock.lock();
			CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
					GANG_EXPEL_CONDITION.getValue());
			if (!conditions.verify(master, true)) {
				throw new ManagedException(ManagedErrorCode.SYS_ERROR);
			}
			Gang gang = this.load(master.getPlayerGang().getGangId());
			gang.isRight(master, GangPosition.Assistant);

			Member targetMember = gang.load(targetId);
			Member operater = gang.load(master.getObjectId());
			if (master.getObjectId().longValue() == targetId) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}
			if (targetMember.isMaster()) {
				throw new ManagedException(ManagedErrorCode.GANG_EXPEL_MASTER_ERROR);
			}

			Player targetPlayer = playerManager.getPlayer(targetMember.getPlayerId());
			// notice
			I18nUtils utils = null;
			if (operater.getPosition() == GangPosition.Assistant) {
				utils = I18nUtils.valueOf("304010").addParm("user", I18nPack.valueOf(master.createSimple()))
						.addParm("targetuser", I18nPack.valueOf(targetPlayer.createSimple()));
			} else {
				utils = I18nUtils.valueOf("304008").addParm("user", I18nPack.valueOf(master.createSimple()))
						.addParm("targetuser", I18nPack.valueOf(targetPlayer.createSimple()));
			}
			ChatManager.getInstance().sendSystem(2, utils, null, gang);

			// do remove
			targetPlayer.getPlayerGang().setGangAndUpdate(0);
			gang.getMembers().remove(targetPlayer.getObjectId());
			PacketSendUtility.sendPacket(targetPlayer, SM_Expel_Gang.valueOf(targetId));
			gang.addGangLog(gangLogFactory.expelLog(targetPlayer));

			this.update(gang);
		} finally {
			lock.unlock();
		}
	}

	private Gang load(long gangId) {
		Gang gang = this.gangs.get(gangId);
		if (gang == null) {
			throw new ManagedException(ManagedErrorCode.GANG_NOT_JOIN);
		}
		return gang;
	}

	public Gang get(long gangId) {
		return gangs.get(gangId);
	}

	public void sendMessage(Object message, long gangId, long senderId) {
		Gang gang = load(gangId);
		gang.sendByFilter(message, senderId, GangPosition.Member, sessionManager);
	}

	public void getGangApplyList(Player player) {
		Gang gang = this.gangs.get(player.getPlayerGang().getGangId());
		gang.isRight(player, GangPosition.Assistant);
		if (gang != null) {
			PacketSendUtility.sendPacket(player, SM_Apply_List.valueOf(gang));
		}
	}

	public void getGangDetailInfo(Player player) {
		refreshRank();
		Gang gang = this.gangs.get(player.getPlayerGang().getGangId());
		if (gang != null) {
			PacketSendUtility.sendPacket(player, gang.creatVO());
		}
	}

	public SM_Gang_List getGangSimpleList() {
		refreshRank();
		SM_Gang_List sm = new SM_Gang_List();
		for (Gang gang : gangs.values()) {
			sm.getVos().add(gang.creatSimpleVO());
		}
		return sm;
	}

	public SM_Gang_List getGangSimpleList(Country country) {
		refreshRank();
		SM_Gang_List sm = new SM_Gang_List();
		for (Gang gang : gangs.values()) {
			if (gang.getCountry() == country.getId()) {
				sm.getVos().add(gang.creatSimpleVO());
			}
		}
		return sm;
	}

	private void refreshRank() {
		Map<CountryId, List<Gang>> ranks = new HashMap<CountryId, List<Gang>>();
		for (CountryId countryId : CountryId.values()) {
			ranks.put(countryId, new ArrayList<Gang>());
		}
		for (Gang gang : gangs.values()) {
			ranks.get(gang.getCountry()).add(gang);
		}

		for (List<Gang> gangs : ranks.values()) {
			Collections.sort(gangs, new Comparator<Gang>() {
				@Override
				public int compare(Gang o1, Gang o2) {
					long dif = o2.getBattle() - o1.getBattle();
					if (dif < 0) {
						return -1;
					} else {
						return 1;
					}
				}
			});
			int i = 1;
			for (Gang gang : gangs) {
				gang.setRank(i);
				i++;
			}
		}
	}

	public SM_Gang_List getGangSimpleList(Country country, String gangName) {
		refreshRank();
		SM_Gang_List sm = new SM_Gang_List();
		for (Gang gang : gangs.values()) {
			if (gang.getCountry() == country.getId() && gang.getName().contains(gangName)) {
				sm.getVos().add(gang.creatSimpleVO());
			}
		}
		return sm;
	}

	public SM_Gang_List getOtherCountryGangSimpleList(Player player) {
		refreshRank();
		SM_Gang_List sm = new SM_Gang_List();
		for (Gang gang : gangs.values()) {
			if (gang.getCountry() == player.getCountry().getId()) {
				continue;
			}
			sm.getVos().add(gang.creatSimpleVO());
		}
		return sm;
	}

	public SM_Gang_List getOtherCountryGangSimpleList(Player player, String gangName) {
		refreshRank();
		SM_Gang_List sm = new SM_Gang_List();
		for (Gang gang : gangs.values()) {
			if (gang.getCountry() == player.getCountry().getId()) {
				continue;
			}
			if (gang.getName().contains(gangName)) {
				sm.getVos().add(gang.creatSimpleVO());
			}
		}
		return sm;
	}

	private void submitImpeach(final Gang gang, long delay) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				try {
					lock.lock();
					long impeachId = gang.getMemberImpeachId();
					if (impeachId != 0) {
						Member master = gang.getMaster();
						Member impeacher = gang.load(impeachId);
						Member replaceMember = gang.impeachMaster(impeacher, GANG_IMPEACH_INTERVAL.getValue() * 1000);
						if (replaceMember != null) {
							Player impeacherPlayer = playerManager.getPlayer(impeachId);
							I18nUtils utils = I18nUtils.valueOf("304005")
									.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(master.getName()))
									.addParm("targetuser", I18nPack.valueOf(impeacherPlayer.createSimple()));
							ChatManager.getInstance().sendSystem(2, utils, null, gang);
							PacketSendUtility.sendPacket(impeacherPlayer, new SM_IMPEACH_GANG());
						}
						gang.setMemberImpeachId(0L);
						update(gang);
					}
				} finally {
					lock.unlock();
				}
			}

		}, delay);
	}

	public void invite(Player master, Player target) {
		try {
			lock.lock();
			Gang gang = this.load(master.getPlayerGang().getGangId());
			gang.isRight(master, GangPosition.Assistant);
			if (gang.getMembers().size() >= GANG_MEMBER_MAXSIZE.getValue()) {
				throw new ManagedException(ManagedErrorCode.GANG_MAX_SIZE);
			}
			if (target.getPlayerGang().getGangId() != 0) {
				throw new ManagedException(ManagedErrorCode.GANG_JOIN);
			}

			if (target.getPlayerGang().getLastQuitGangTime() + ENTER_NEW_GANG_CD.getValue() >= System
					.currentTimeMillis()) {
				throw new ManagedException(ManagedErrorCode.INVITE_GANG_IN_QUIT_CD);
			}

			if (target.getComplexState().isState(ComplexStateType.GUILD)) {
				throw new ManagedException(ManagedErrorCode.GANG_FORBIT_INVITE);
			}

			if (target.getComplexState().isState(ComplexStateType.GANG_INVITE)) {
				// 自动加入
				PlayerInvite playerInvite = PlayerInvite.valueOf(master.getObjectId(), master.getName(), gang);
				if (!target.getPlayerGang().getInvites().contains(playerInvite)) {
					target.getPlayerGang().getInvites().add(playerInvite);
				}
				dealInvite(target, gang.getId(), true);
			} else {
				// 手动，发送消息
				PlayerInvite playerInvite = PlayerInvite.valueOf(master.getObjectId(), master.getName(), gang);
				if (target.getPlayerGang().getInvites().contains(playerInvite)) {
					throw new ManagedException(ManagedErrorCode.GANG_INVITED);
				}
				target.getPlayerGang().getInvites().add(playerInvite);
				PacketSendUtility.sendPacket(target, SM_GangInvite.valueOf(playerInvite));
				this.update(gang);
			}
		} finally {
			lock.unlock();
		}

	}

	public void dealInvite(Player player, long gangId, boolean ok) {
		try {
			lock.lock();
			CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
					GANG_DEAL_INVITE_CONDITION.getValue());
			if (!conditions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.SYS_ERROR);
			}
			PlayerInvite pi = player.getPlayerGang().getPlayerInvite(gangId);
			if (pi == null) {
				throw new ManagedException(ManagedErrorCode.GANG_NOT_INVITED);
			}
			Gang gang = this.gangs.get(pi.getGangId());
			if (ok) {
				if (player.getPlayerGang().getGangId() != 0) {
					throw new ManagedException(ManagedErrorCode.GANG_JOIN);
				}
				if (gang == null) {
					throw new ManagedException(ManagedErrorCode.GANG_NOT_JOIN);
				}
				if (gang.getMembers().size() >= GANG_MEMBER_MAXSIZE.getValue()) {
					player.getPlayerGang().removeInvite(gangId);
					throw new ManagedException(ManagedErrorCode.GANG_MAX_SIZE);
				}
				gang.join(player, GangPosition.Member, true);
				gang.addGangLog(gangLogFactory.joinLog(player.getName()));
				this.update(gang);
			}
			player.getPlayerGang().removeInvite(gangId);
			SM_ApplyResult_Gang sm = new SM_ApplyResult_Gang();
			sm.setGangId(player.getPlayerGang().getGangId());
			sm.setServer(gang.getServer());
			sm.setGangName(gang.getName());
			sm.setType(1);
			sm.setApplyPlayerName(player.getName());
			if (ok) {
				sm.setResult((byte) (1));
				PacketSendUtility.sendPacket(player, sm);
			} else {
				sm.setResult((byte) (0));
				if (SessionManager.getInstance().isOnline(pi.getInvitorId())) {
					Player invitor = playerManager.getPlayer(pi.getInvitorId());
					PacketSendUtility.sendPacket(invitor, sm);
				}
			}
		} finally {
			lock.unlock();
		}
	}

	public void playerLogin(Player player) {
		if (!this.getGangs().containsKey(player.getPlayerGang().getGangId())) {
			logger.error(String.format("玩家account[%s],name[%s]帮会gangId[%s]丢失！", player.getPlayerEnt().getAccountName(),
					player.getName(), player.getPlayerGang().getGangId()));
			player.getPlayerGang().setGangAndUpdate(0l);
			return;
		}
		Gang gang = this.load(player.getPlayerGang().getGangId());
		if (!gang.getMembers().containsKey(player.getObjectId())) {
			logger.error(String.format("玩家account[%s],name[%s]帮会gangId[%s]丢失！gang[%s]", player.getPlayerEnt()
					.getAccountName(), player.getName(), player.getPlayerGang().getGangId(), JsonUtils
					.object2String(gang)));
			player.getPlayerGang().setGangAndUpdate(0l);
			return;
		}
		gang.send(SM_Gang_Login.valueOf(player.getName()), GangPosition.Member, sessionManager);
	}

	public long quit(Player player) {
		long quitTime = System.currentTimeMillis();
		try {
			lock.lock();
			CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
					GANG_EXIT_CONDITION.getValue());
			if (!conditions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.SYS_ERROR);
			}
			Gang gang = this.load(player.getPlayerGang().getGangId());
			if (gang.getMembers().size() <= 1) {
				// 帮会只有一人的时候不能退出，只能解散
				throw new ManagedException(ManagedErrorCode.GANG_MASTER_CANNOT_QUIT);
			}
			gang.quit(player);
			gang.addGangLog(gangLogFactory.quitLog(player));
			player.getPlayerGang().setLastQuitGangTime(quitTime);
			this.update(gang);
			return quitTime + ENTER_NEW_GANG_CD.getValue();
		} finally {
			lock.unlock();
		}
	}

	public void refreshLastLoginOut(Player player) {
		if (player.getPlayerGang().getGangId() != 0) {
			Gang gang = this.load(player.getPlayerGang().getGangId());
			gang.load(player.getObjectId()).setLastLoginTime(System.currentTimeMillis());
			this.update(gang);
		}
	}

	public void setGangInfo(Player master, String infor) throws UnsupportedEncodingException {
		if (DirtyWordsManager.getInstance().containsWords(infor, WordsType.ROLEWORDS)) {
			PacketSendUtility.sendErrorMessage(master, ManagedErrorCode.WORDS_SENSITIVE);
			return;
		}
		Gang gang = this.load(master.getPlayerGang().getGangId());
		gang.isRight(master, GangPosition.Assistant);
		String filterUtfInfo = CharCheckUtil.filterOffUtf8Mb4(infor);
		gang.setInfo(filterUtfInfo);
		gang.setLastModifyInfoTime(System.currentTimeMillis());
		gang.send(SM_GangInfo.valueOf(filterUtfInfo), GangPosition.Member, sessionManager);
		this.update(gang);
	}

	private void update(Gang gang) {
		gangEntDbService.writeBack(gang.getId(), gang.getGangEnt());
	}

	public void updateAll() {
		for (Gang gang : gangs.values()) {
			update(gang);
		}
	}

	public void impeachGangMaster(Player player) {
		try {
			lock.lock();

			Gang gang = this.load(player.getPlayerGang().getGangId());
			Member master = gang.getMaster();
			Member member = gang.load(player.getObjectId());

			if (sessionManager.isOnline(master.getPlayerId())) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.GANG_MASTER_STILL_ONLINE);
				return;
			}
			if ((System.currentTimeMillis() - master.getLastLoginTime()) < GANG_IMPEACH_INTERVAL.getValue() * 1000) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.MASTER_STILL_ACTIVE);
				return;
			}

			if (member.isAssistant()) {
				I18nUtils utils = I18nUtils.valueOf("304005")
						.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(master.getName()))
						.addParm("targetuser", I18nPack.valueOf(player.createSimple()));
				gang.impeachMaster(member, GANG_IMPEACH_INTERVAL.getValue() * 1000);
				gang.setMemberImpeachId(0L);
				ChatManager.getInstance().sendSystem(2, utils, null, gang);
				PacketSendUtility.sendPacket(player, new SM_IMPEACH_GANG());
				update(gang);
			} else {
				if (gang.getMemberImpeachId() == 0L) {
					gang.setMemberImpeachId(player.getObjectId());
					gang.setStartImpeachTime(System.currentTimeMillis());
					submitImpeach(gang, GANG_MEMBER_IMPEACH_INTERVAL.getValue());
					update(gang);
				} else {
					throw new ManagedException(ManagedErrorCode.SOMEONE_ALREADY_IMPEACH);
				}
			}
		} finally {
			lock.unlock();
		}
	}

	public void doLevelUpRefresh(long gangId, Player player) {
		Gang gang = this.load(gangId);
		Member member = gang.load(player.getObjectId());
		member.setLevel(player.getLevel());
		this.update(gang);
	}

	public void doVipRefresh(long gangId, Player player) {
		Gang gang = this.load(gangId);
		Member member = gang.load(player.getObjectId());
		member.setVip(player.getVip().getLevel());
		this.update(gang);
	}

	public void doPromotionRefresh(long gangId, Player player) {
		Gang gang = this.load(gangId);
		Member member = gang.load(player.getObjectId());
		member.setPromotionId(player.getPromotion().getStage());
		this.update(gang);
	}

	public static GangManager getInstance() {
		return instance;
	}

	public static void setInstance(GangManager instance) {
		GangManager.instance = instance;
	}

	public ConcurrentHashMap<Long, Gang> getGangs() {
		return gangs;
	}

	public void askForHelp(final Player player, int sign) {
		if (player.isInCopy()) {
			throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
		}

		if (player.getLifeStats().isAlreadyDead()) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}

		if (BossConfig.getInstance().isInBossHomeMap(player.getMapId())) {
			throw new ManagedException(ManagedErrorCode.BOSS_HOME_CANNOT_CALLTOGETHER);
		}

		CoreActions actions = CoreActionManager.getInstance().getCoreActions(1, CRY_FOR_HELP_ACT.getValue());
		actions.verify(player, true);
		ItemResource item = ItemManager.getInstance().getResource(actions.getFirstItemKey());
		CoreConditions useCondition = item.getItemConditions(1);
		if (!useCondition.verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		actions.act(player, ModuleInfo.valueOf(ModuleType.GANG, SubModuleType.GANG_HELP_ACT));

		final int instanceId = player.getInstanceId();
		final int x = player.getX();
		final int y = player.getY();
		final int mapId = player.getMapId();
		final long time = System.currentTimeMillis();
		final MapResource mapResource = World.getInstance().getMapResource(mapId);

		String[] conditions = ACCEPT_HELP_COND.getValue();
		CoreConditions filterConditions = CoreConditionManager.getInstance().getCoreConditions(1, conditions);
		for (Player p : CountryManager.getInstance().getCountry(player).getCivils().values()) {
			if (SessionManager.getInstance().isOnline(p.getObjectId())) {
				boolean inSpecialMap = (player.getMapId() == ConfigValueManager.getInstance().CALLED_SPECIAL_MAPID
						.getValue());
				boolean verify = true;
				if (inSpecialMap) {
					verify = ConfigValueManager.getInstance().getCalledSpecialCondsOne().verify(p)
							|| ConfigValueManager.getInstance().getCalledSpecialCondsTwo().verify(p);
				} else {
					verify = filterConditions.verify(p);
				}
				if (!World.getInstance().canEnterMap(p, mapId, false)) {
					continue;
				}
				if (verify && (!p.equals(player))) {
					boolean result = p.getRequester().putRequest(RequestHandlerType.ASK_GANG_FOR_HELP,
							new RequestResponseHandler(p) {
								@Override
								public boolean deprecated() {
									return time + CRY4HELP_DEPRECATE_DURATION.getValue() < System.currentTimeMillis();
								}

								@Override
								public void denyRequest(Creature requester, Player responder) {
								}

								@Override
								public void acceptRequest(Creature requester, Player responder) {
									if (responder.isInCopy()) {
										PacketSendUtility.sendErrorMessage(responder, ManagedErrorCode.PLAYER_IN_COPY);
										return;
									}
									if (responder.getLifeStats().isAlreadyDead()) {
										PacketSendUtility.sendErrorMessage(responder, ManagedErrorCode.DEAD_ERROR);
										return;
									}
									try {
										World.getInstance().canEnterMap(responder, mapId);
										responder.getMoveController().stopMoving();
									} catch (ManagedException e) {
										PacketSendUtility.sendErrorMessage(responder, e.getCode());
										return;
									}
									if (responder.getPosition().getMapId() == mapId
											&& responder.getPosition().getInstanceId() == instanceId) {
										World.getInstance().updatePosition(responder, x, y, responder.getHeading());
									} else {
										World.getInstance().setPosition(responder, mapId, instanceId, x, y,
												responder.getHeading());
									}
									responder.sendUpdatePosition();

									// 召集无敌BUFF
									if (!mapResource.isCopy()) {
										Skill godSKill = SkillEngine.getInstance().getSkill(null,
												ConfigValueManager.getInstance().BEEN_CALLED_BUFF.getValue(),
												responder.getObjectId(), 0, 0, responder, null);
										godSKill.noEffectorUseSkill();
									}
								}
							});
					if (result) {
						PacketSendUtility.sendPacket(p, SM_Gang_Ask_Help.valueOf(player, time));
					}
				}
			}
		}
		PacketSendUtility.sendSignMessage(player, sign);
	}
}
