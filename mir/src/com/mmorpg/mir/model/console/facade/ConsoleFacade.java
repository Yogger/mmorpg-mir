package com.mmorpg.mir.model.console.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.admin.facade.SystemFacade;
import com.mmorpg.mir.admin.packet.GM_Identify_Treasure_Close;
import com.mmorpg.mir.admin.packet.GM_Identify_Treasure_Open;
import com.mmorpg.mir.admin.packet.GM_Reload;
import com.mmorpg.mir.model.ServerConfigValue;
import com.mmorpg.mir.model.artifact.core.ArtifactManager;
import com.mmorpg.mir.model.artifact.packet.SM_Artifact_Uplevel;
import com.mmorpg.mir.model.blackshop.model.BlackShopServer;
import com.mmorpg.mir.model.console.packet.CM_BossCenter_Console;
import com.mmorpg.mir.model.console.packet.CM_CONSOLE_ADDEXP;
import com.mmorpg.mir.model.console.packet.CM_CONSOLE_ADDMONEY;
import com.mmorpg.mir.model.console.packet.CM_CONSOLE_ADD_MILITARY;
import com.mmorpg.mir.model.console.packet.CM_CONSOLE_GM;
import com.mmorpg.mir.model.console.packet.CM_CONSOLE_ITEM;
import com.mmorpg.mir.model.console.packet.CM_CONSOLE_REWARD;
import com.mmorpg.mir.model.console.packet.CM_CONSOLE_SETPOSITION;
import com.mmorpg.mir.model.console.packet.CM_CONSOLE_SETVIP;
import com.mmorpg.mir.model.console.packet.CM_Console_BECOME_KINGOFKING;
import com.mmorpg.mir.model.console.packet.CM_Console_Kill_Myself;
import com.mmorpg.mir.model.console.packet.CM_Console_OpenModule;
import com.mmorpg.mir.model.console.packet.CM_Console_Upgrade_Artifact;
import com.mmorpg.mir.model.console.packet.CM_Console_Upgrade_Horse;
import com.mmorpg.mir.model.console.packet.CM_Console_Upgrade_Soul;
import com.mmorpg.mir.model.console.packet.CM_Get_Server_Version_Md5;
import com.mmorpg.mir.model.console.packet.CM_I_AM_KING;
import com.mmorpg.mir.model.console.packet.CM_START_GANGOFWAR;
import com.mmorpg.mir.model.console.packet.CM_START_KINGOFWAR;
import com.mmorpg.mir.model.console.packet.SM_Get_Server_Version_MD5;
import com.mmorpg.mir.model.copy.service.CopyService;
import com.mmorpg.mir.model.country.model.CountryOfficial;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWarManager;
import com.mmorpg.mir.model.horse.manager.HorseManager;
import com.mmorpg.mir.model.horse.packet.SM_HorseUpdate;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.core.ItemService;
import com.mmorpg.mir.model.item.model.EquipmentStatType;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;
import com.mmorpg.mir.model.kingofwar.event.KingOfKingAbdicateEvent;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.military.manager.MilitaryManager;
import com.mmorpg.mir.model.military.service.MilitaryService;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.moduleopen.packet.SM_Module_Open;
import com.mmorpg.mir.model.operator.manager.OperatorManager;
import com.mmorpg.mir.model.operator.model.GmPrivilegeType;
import com.mmorpg.mir.model.player.event.ServerOpenEvent;
import com.mmorpg.mir.model.player.packet.SM_Server_Open;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.purse.reward.CurrencyRewardsProvider;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.soul.core.SoulManager;
import com.mmorpg.mir.model.soul.packet.SM_Soul_Uplevel;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.welfare.service.GiftRewardService;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.service.WorldService;
import com.mmorpg.mir.transfer.manager.ClientTransferManager;
import com.mmorpg.mir.transfer.model.Ticket;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.New;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

/**
 * 前端控制台
 * 
 * @author Kuang Hao
 * @since v1.0 2012-4-10
 * 
 */
@Component
public class ConsoleFacade {

	@Autowired
	private ServerConfigValue serverConfigValue;
	@Autowired
	private RewardManager rewardManager;

	@Autowired
	private SystemFacade systemFacade;

	@Autowired
	private CopyService copyService;
	@Autowired
	private ItemService itemService;

	@Autowired
	private MilitaryService militaryService;

	@Autowired
	private WorldService worldService;
	@Autowired
	private OperatorManager operatorManager;
	@Autowired
	private GiftRewardService rewardService;

	private boolean check() {
		if (serverConfigValue.getConsole() != 1) {
			return false;
		}
		return true;
	}

	@HandlerAnno
	public void getServerVersion(TSession session, CM_Get_Server_Version_Md5 req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Get_Server_Version_MD5 res = SM_Get_Server_Version_MD5.valueOf(ServerConfigValue.versionMd5);
		PacketSendUtility.sendPacket(player, res);
	}

	@HandlerAnno
	public void becomeKingOfKing(TSession session, CM_Console_BECOME_KINGOFKING req) {
		if (!this.check()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		long kingId = KingOfWarManager.getInstance().getKingOfWarInfo().getKingOfKing();
		KingOfWarManager.getInstance().getKingOfWarInfo().becomeKing(player);
		player.getHorse().getAppearance().sendNotFinishActive(player);
		if (kingId != 0L && kingId != player.getObjectId()) {
			EventBusManager.getInstance().submit(KingOfKingAbdicateEvent.valueOf(kingId));
		}
	}

	@HandlerAnno
	public void addMoney(TSession session, CM_CONSOLE_ADDMONEY cm_CONSOLE_ADDMONEY) {
		if (!this.check()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		worldService.enterWorld(player);
		Reward reward = Reward.valueOf();
		reward.addCurrency(CurrencyType.valueOf(cm_CONSOLE_ADDMONEY.getType()), cm_CONSOLE_ADDMONEY.getValue());
		if (CurrencyType.valueOf(cm_CONSOLE_ADDMONEY.getType()) == CurrencyType.GOLD) {
			for (RewardItem item : reward.getItems()) {
				item.putParms(CurrencyRewardsProvider.VIP_REWARD, "true");
			}
		}
		// CurrencyType type =
		// CurrencyType.valueOf(cm_CONSOLE_ADDMONEY.getType());
		// if (type == CurrencyType.GOLD) {
		// player.getVip().addGoldRechargeHistory(cm_CONSOLE_ADDMONEY.getValue(),
		// System.currentTimeMillis());
		// }

		reward = rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.CONSOLE, SubModuleType.CONSOLE_ADDMONEY));
		// itemService.combiningItem(player, combiningId, addition, useGold,
		// quantity)

	}

	@HandlerAnno
	public void reward(TSession session, CM_CONSOLE_REWARD cm) {
		if (!this.check()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		rewardManager.grantReward(player, cm.getKey(),
				ModuleInfo.valueOf(ModuleType.CONSOLE, SubModuleType.CONSOLE_ADDREWARD));
	}

	@HandlerAnno
	public void addExp(TSession session, CM_CONSOLE_ADDEXP cm) {
		if (!this.check()) {
			return;
		}
		if (cm.getValue() <= 0)
			return;
		Player player = SessionUtil.getPlayerBySession(session);
		Reward reward = Reward.valueOf();
		reward.addExp(cm.getValue());
		reward = rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.CONSOLE, SubModuleType.CONSOLE_ADDEXP));
		if (cm.getValue() <= 50 && cm.getValue() >= 1) {
			int interval = cm.getValue() - 1;
			Date now = new Date(System.currentTimeMillis() - interval * DateUtils.MILLIS_PER_DAY);
			ServerState.getInstance().setOpenServerDate(now);
			SM_Server_Open openPacket = SM_Server_Open.valueOf(now, player);
			SessionManager.getInstance().sendAllIdentified(openPacket);
			EventBusManager.getInstance().submit(new ServerOpenEvent());

		}

		if (cm.getValue() == 666) {
			BlackShopServer server = ServerState.getInstance().getBlackShopServer();
			// Date beginTime = DateUtils.string2Date(DateUtils.date2String(new
			// Date(), "yyyy-MM-dd 00:00:00"),
			// "yyyy-MM-dd HH:mm:ss");
			// Date endTime = DateUtils.string2Date(DateUtils.date2String(new
			// Date(), "yyyy-MM-dd 23:59:59"),
			// "yyyy-MM-dd HH:mm:ss");
			Date beginTime = DateUtils.string2Date("2015-12-31 00:00:00", "yyyy-MM-dd HH:mm:ss");
			Date endTime = DateUtils.string2Date("2016-01-04 00:00:00", "yyyy-MM-dd HH:mm:ss");
			server.fix("NewYear", beginTime.getTime(), endTime.getTime());
		}

		if (cm.getValue() == 999) {
			BlackShopServer server = ServerState.getInstance().getBlackShopServer();
			server.close();
		}

		if (cm.getValue() == 777) {
			GM_Identify_Treasure_Open treasure = new GM_Identify_Treasure_Open();
			Date startTime = DateUtils.string2Date("2016-3-7 00:00:00", "yyyy-MM-dd HH:mm:ss");
			Date endTime = DateUtils.string2Date("2016-3-8 00:00:00", "yyyy-MM-dd HH:mm:ss");
			treasure.setStartTime(startTime.getTime());
			treasure.setEndTime(endTime.getTime());
			treasure.setActiveName("IdentifyTreasure_March");
			systemFacade.openIdentifyTreasure(session, treasure);
		}
		if (cm.getValue() == 888) {
			GM_Identify_Treasure_Close treasure = new GM_Identify_Treasure_Close();
			treasure.setActiveName("IdentifyTreasure_name_01");
			systemFacade.closeIdentifyTreasure(session, treasure);
		}

		if (cm.getValue() == 1) {
			clientTransferManager.console(1);
		}
		if (cm.getValue() == 2) {
			clientTransferManager.console(2);
		}
		if (cm.getValue() == 3) {
			clientTransferManager.console(3);
		}
		if(cm.getValue() == 111000){
			GM_Reload gm = new GM_Reload();
			gm.setResources(new String[]{"CoreConditionResource","CommonConsumeActiveResource"});
			systemFacade.reload(session, gm);
		}
	}

	@HandlerAnno
	public void addExp(TSession session, CM_BossCenter_Console cm) {
		if (!this.check()) {
			return;
		}
		clientTransferManager.console(cm.getCommand());

	}

	@Autowired
	private ClientTransferManager clientTransferManager;

	@HandlerAnno
	public void iamgm(TSession session, CM_CONSOLE_GM cm) {
		if (!this.check()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		HashSet<Integer> ss = new HashSet<Integer>();
		for (GmPrivilegeType type : GmPrivilegeType.values()) {
			ss.add(type.getValue());
		}
		operatorManager.setGmPrivilege(player, ss);
	}

	@HandlerAnno
	public void addVip(TSession session, CM_CONSOLE_SETVIP cm) {
		if (!this.check()) {
			return;
		}
		if (cm.getValue() <= 0)
			return;
		Player player = SessionUtil.getPlayerBySession(session);
		player.getVip().setLevel(cm.getValue());
	}

	@HandlerAnno
	public void addItem(TSession session, CM_CONSOLE_ITEM cm) {
		if (!this.check()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);

		if (cm.getKey().matches("soul[\\w]*")) {
			Equipment[] equips = player.getEquipmentStorage().getEquipments();
			for (int i = 0; i < equips.length; i++) {
				Equipment equip = equips[i];
				if (equip != null) {
					equip.addExtraStats(EquipmentStatType.SOUL_STAT.getValue(), cm.getKey().substring(4));
				}
			}
		} else if (cm.getKey().equals("equiptest")) {
			Reward reward = Reward.valueOf();
			for (int i = 51; i <= 71; i++) {
				reward.addItem("ic100" + i, 99);
				reward = rewardManager.grantReward(player, reward,
						ModuleInfo.valueOf(ModuleType.CONSOLE, SubModuleType.CONSOLE_ADDITEM));
			}
		} else if (cm.getKey().equals("qiushi")) {
		} else if (cm.getKey().equals("equip")) {
			for (int i = 0; i < cm.getAmount(); i++) {
				for (EquipmentType type : EquipmentType.values()) {
					if (player.getEquipmentStorage().hasEquip(type)) {
						try {
							itemService.enhanceEquipment(player, type.ordinal(), true);
						} catch (Exception e) {
							//
						}
					}
				}
			}
		} else {
			try {
				ItemManager.getInstance().getResource(cm.getKey());
				if (cm.getAmount() <= 0)
					return;
			} catch (IllegalStateException e) {
				System.out.println("别乱输入参数好么");
				return;
			}
			Reward reward = Reward.valueOf();
			reward.addItem(cm.getKey(), cm.getAmount());
			reward = rewardManager.grantReward(player, reward,
					ModuleInfo.valueOf(ModuleType.CONSOLE, SubModuleType.CONSOLE_ADDITEM));
		}
	}

	@HandlerAnno
	public void startKingOfWar(TSession session, CM_START_KINGOFWAR cm) {
		if (!this.check()) {
			return;
		}
		KingOfWarManager.getInstance().start(false);
	}

	@HandlerAnno
	public void startGangOfWar(TSession session, CM_START_GANGOFWAR cm) {
		if (!this.check()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		GangOfWarManager.getInstance().start(player.getCountryValue(), false);
	}

	@HandlerAnno
	public void setPosition(TSession session, CM_CONSOLE_SETPOSITION cm) {
		if (!this.check()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		if (player.isInCopy() && cm.getWorldId() != player.getMapId()) {
			return;
		}
		WorldMap map = World.getInstance().getWorldMap(cm.getWorldId());
		if (map != null) {
			World.getInstance().setPosition(player, cm.getWorldId(), cm.getX(), cm.getY(), player.getHeading());
			player.sendUpdatePosition();
			World.getInstance().spawn(player);
		}
	}

	@HandlerAnno
	public void setPosition(TSession session, CM_I_AM_KING cm) {
		if (!this.check()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		player.getCountry().getCourt().reset(true);
		player.getCountry().getCourt().appoint(player, CountryOfficial.KING, 0);
		player.getCountry().getCourt().setBecomeKingTime(System.currentTimeMillis());
		KingOfWarManager.getInstance().refreshSculptures(player.getCountry().getId().getValue());
	}

	@HandlerAnno
	public void addMilitaryRank(TSession session, CM_CONSOLE_ADD_MILITARY cm) {
		if (!this.check()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		if (player.getMilitary().getRank() >= MilitaryManager.getInstance().INITIAL_STAR_RANK.getValue())
			return;
		militaryService.upgradeMilitaryRank(player);
	}

	@HandlerAnno
	public void try2OpenModule(TSession session, CM_Console_OpenModule cm) {
		if (!this.check()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			if (!ModuleOpenManager.getInstance().isOpenByKey(player, cm.getOpenId())) {
				ModuleOpenManager.getInstance().getResource(cm.getOpenId());
				player.getModuleOpen().getOpeneds().put(cm.getOpenId(), true);
				ArrayList<String> opens = New.arrayList();
				opens.add(cm.getOpenId());
				EventBusManager.getInstance().submit(ModuleOpenEvent.valueOf(player.getObjectId(), cm.getOpenId()));
				PacketSendUtility.sendPacket(player, SM_Module_Open.valueOf(opens));
			}
		} catch (Exception e) {
		}
	}

	@HandlerAnno
	public void upgradeArtifact(TSession session, CM_Console_Upgrade_Artifact cm) {
		if (!this.check()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			ArtifactManager.getInstance().getArtifactResource(player.getArtifact().getLevel() + 1);
			rewardManager.grantReward(player, Reward.valueOf().addArtifact(player.getArtifact().getLevel() + 1),
					ModuleInfo.valueOf(ModuleType.CONSOLE, SubModuleType.CONSOLE_UPGRADE_ARTIFACT));
			SM_Artifact_Uplevel sm = SM_Artifact_Uplevel.valueOf(player.getArtifact());
			PacketSendUtility.sendPacket(player, sm);
		} catch (Exception e) {
		}
	}

	@HandlerAnno
	public void upgradeSoul(TSession session, CM_Console_Upgrade_Soul cm) {
		if (!this.check()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			SoulManager.getInstance().getSoulResource(player.getSoul().getLevel() + 1);
			rewardManager.grantReward(player, Reward.valueOf().addSoul(player.getSoul().getLevel() + 1),
					ModuleInfo.valueOf(ModuleType.CONSOLE, SubModuleType.CONSOLE_UPGRADE_SOUL));
			SM_Soul_Uplevel sm = SM_Soul_Uplevel.valueOf(player.getSoul());
			PacketSendUtility.sendPacket(player, sm);
		} catch (Exception e) {
		}
	}

	@HandlerAnno
	public void upgradeHorse(TSession session, CM_Console_Upgrade_Horse cm) {
		if (!this.check()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			HorseManager.getInstance().getHorseResource(player.getHorse().getGrade() + 1);
			rewardManager.grantReward(player, Reward.valueOf().addHorse(null, player.getHorse().getGrade() + 1),
					ModuleInfo.valueOf(ModuleType.CONSOLE, SubModuleType.CONSOLE_UPGRADE_HORSE));
			SM_HorseUpdate sm = player.getHorse().createVO();
			PacketSendUtility.sendPacket(player, sm);
		} catch (Exception e) {
		}
	}

	@HandlerAnno
	public void selfKill(TSession session, CM_Console_Kill_Myself cm) {
		if (!this.check()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		player.getLifeStats().reduceHp(Integer.MAX_VALUE, player, 81);
	}

}
