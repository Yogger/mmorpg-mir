package com.mmorpg.mir.model.country.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.ClearAndMigrate;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.CountryFlag;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.model.ReserveKing;
import com.mmorpg.mir.model.country.model.ReserveTaskEnum;
import com.mmorpg.mir.model.country.packet.CM_All_CountryStatus;
import com.mmorpg.mir.model.country.packet.CM_CountryFlag_Damage_Rank;
import com.mmorpg.mir.model.country.packet.CM_CountryTechnology_Contribute_BuildItem;
import com.mmorpg.mir.model.country.packet.CM_CountryTechnology_FlagCount_Refresh;
import com.mmorpg.mir.model.country.packet.CM_CountryTechnology_Place_ArmyFlag;
import com.mmorpg.mir.model.country.packet.CM_CountryTechnology_QueryInfo;
import com.mmorpg.mir.model.country.packet.CM_Country_Action_See_All_Log;
import com.mmorpg.mir.model.country.packet.CM_Country_Appoint;
import com.mmorpg.mir.model.country.packet.CM_Country_Appoint_Guard;
import com.mmorpg.mir.model.country.packet.CM_Country_CallTogether;
import com.mmorpg.mir.model.country.packet.CM_Country_CallTogether_Guard;
import com.mmorpg.mir.model.country.packet.CM_Country_CallbackTank;
import com.mmorpg.mir.model.country.packet.CM_Country_Check_Activity_Status;
import com.mmorpg.mir.model.country.packet.CM_Country_Check_Salary_Status;
import com.mmorpg.mir.model.country.packet.CM_Country_Contribute_Shop;
import com.mmorpg.mir.model.country.packet.CM_Country_Contribution;
import com.mmorpg.mir.model.country.packet.CM_Country_Coppers;
import com.mmorpg.mir.model.country.packet.CM_Country_CreateTank;
import com.mmorpg.mir.model.country.packet.CM_Country_Depose;
import com.mmorpg.mir.model.country.packet.CM_Country_Depose_Guard;
import com.mmorpg.mir.model.country.packet.CM_Country_Diplomacy_Reilve;
import com.mmorpg.mir.model.country.packet.CM_Country_DistributeTank;
import com.mmorpg.mir.model.country.packet.CM_Country_DistributeTogetherToken;
import com.mmorpg.mir.model.country.packet.CM_Country_Door;
import com.mmorpg.mir.model.country.packet.CM_Country_Factory;
import com.mmorpg.mir.model.country.packet.CM_Country_Fete;
import com.mmorpg.mir.model.country.packet.CM_Country_Fete_Log;
import com.mmorpg.mir.model.country.packet.CM_Country_Flag;
import com.mmorpg.mir.model.country.packet.CM_Country_Flag_Reilve;
import com.mmorpg.mir.model.country.packet.CM_Country_ForbidChat;
import com.mmorpg.mir.model.country.packet.CM_Country_MovePack;
import com.mmorpg.mir.model.country.packet.CM_Country_Offical;
import com.mmorpg.mir.model.country.packet.CM_Country_OpenCivilSalary;
import com.mmorpg.mir.model.country.packet.CM_Country_OpenOfficialSalary;
import com.mmorpg.mir.model.country.packet.CM_Country_Open_Diplomacy;
import com.mmorpg.mir.model.country.packet.CM_Country_Open_Flag;
import com.mmorpg.mir.model.country.packet.CM_Country_QuestStatus;
import com.mmorpg.mir.model.country.packet.CM_Country_ReceivedCivilSalary;
import com.mmorpg.mir.model.country.packet.CM_Country_ReceivedOfficialSalary;
import com.mmorpg.mir.model.country.packet.CM_Country_Relieve_Traitor;
import com.mmorpg.mir.model.country.packet.CM_Country_SetNotice;
import com.mmorpg.mir.model.country.packet.CM_Country_SetTraitor;
import com.mmorpg.mir.model.country.packet.CM_Country_Shop;
import com.mmorpg.mir.model.country.packet.CM_Country_Shop_Buy;
import com.mmorpg.mir.model.country.packet.CM_Country_StartExpress;
import com.mmorpg.mir.model.country.packet.CM_Country_StartTemple;
import com.mmorpg.mir.model.country.packet.CM_Country_Storage;
import com.mmorpg.mir.model.country.packet.CM_Country_Store;
import com.mmorpg.mir.model.country.packet.CM_Country_Take;
import com.mmorpg.mir.model.country.packet.CM_Country_Traitor_Num;
import com.mmorpg.mir.model.country.packet.CM_Country_Traitor_Rank;
import com.mmorpg.mir.model.country.packet.CM_Country_UpgradeDoor;
import com.mmorpg.mir.model.country.packet.CM_Country_UpgradeFactory;
import com.mmorpg.mir.model.country.packet.CM_Country_UpgradeFlag;
import com.mmorpg.mir.model.country.packet.CM_Country_UpgradeShop;
import com.mmorpg.mir.model.country.packet.CM_Country_UpgrageTank;
import com.mmorpg.mir.model.country.packet.CM_Country_UseCallToken;
import com.mmorpg.mir.model.country.packet.CM_Diplomacy_Damage_Rank;
import com.mmorpg.mir.model.country.packet.CM_Flag_Quest_Info;
import com.mmorpg.mir.model.country.packet.CM_Hidden_Mission_Info;
import com.mmorpg.mir.model.country.packet.CM_Official_Mobilization;
import com.mmorpg.mir.model.country.packet.CM_Query_Mobilization;
import com.mmorpg.mir.model.country.packet.CM_ReserveKing_Become;
import com.mmorpg.mir.model.country.packet.CM_ReserveKing_CallTogether;
import com.mmorpg.mir.model.country.packet.CM_ReserveKing_GetInfo;
import com.mmorpg.mir.model.country.packet.CM_ReserveKing_InitiativeAdbicate;
import com.mmorpg.mir.model.country.packet.CM_ReserveKing_Task_Reward;
import com.mmorpg.mir.model.country.packet.SM_All_CountryStatus;
import com.mmorpg.mir.model.country.packet.SM_Country_Check_Activity_Status;
import com.mmorpg.mir.model.country.packet.SM_Country_Diplomacy_Reilve;
import com.mmorpg.mir.model.country.packet.SM_Country_Flag_Relive;
import com.mmorpg.mir.model.country.packet.SM_Country_Open_Diplomacy;
import com.mmorpg.mir.model.country.packet.SM_Country_Open_Flag;
import com.mmorpg.mir.model.country.packet.SM_Country_QuestStatus;
import com.mmorpg.mir.model.country.packet.SM_Country_Relieve_Traitor;
import com.mmorpg.mir.model.country.packet.SM_ReserveKingVO;
import com.mmorpg.mir.model.country.packet.vo.CountryTechnologyVo;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.player.event.ServerOpenEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.windforce.common.scheduler.Scheduled;
import com.windforce.common.scheduler.ValueType;
import com.windforce.common.utility.DateUtils;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class CountryFacade {

	private static Logger logger = Logger.getLogger(CountryFacade.class);
	@Autowired
	private CountryManager countryManager;

	@Autowired
	private PlayerManager playerManager;

	@HandlerAnno
	public SM_Country_Check_Activity_Status checkTimes(TSession session, CM_Country_Check_Activity_Status req) {
		SM_Country_Check_Activity_Status sm = new SM_Country_Check_Activity_Status();
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			sm = SM_Country_Check_Activity_Status.valueOf();
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看砍大臣,砍国旗的状态,是否被保护", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Country_Diplomacy_Reilve diplomacyRelive(TSession session, CM_Country_Diplomacy_Reilve req) {
		SM_Country_Diplomacy_Reilve sm = new SM_Country_Diplomacy_Reilve();
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			sm = SM_Country_Diplomacy_Reilve.valueOf();
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("大臣复活的时间", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Country_Flag_Relive flagRelive(TSession session, CM_Country_Flag_Reilve req) {
		SM_Country_Flag_Relive sm = new SM_Country_Flag_Relive();
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			sm = SM_Country_Flag_Relive.valueOf();
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("国旗复活的时间", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Country_Open_Diplomacy openDiplomacyPanel(TSession session, CM_Country_Open_Diplomacy req) {
		SM_Country_Open_Diplomacy sm = new SM_Country_Open_Diplomacy();
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			sm = countryManager.openDiplomacyPanel(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("打开砍大臣界面", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	// @HandlerAnno
	// public SM_Country_Request_Send_Message sendMessageByDiplomacy(TSession
	// session, CM_Country_Request_Send_Message req) {
	// SM_Country_Request_Send_Message sm = new
	// SM_Country_Request_Send_Message();
	// Player player = SessionUtil.getPlayerBySession(session);
	// try {
	// Map<CountryId, Country> countries = countryManager.getCountries();
	// sm = countryManager.sendMessageByDiplomacy(player,
	// countries.get(CountryId.valueOf(req.getCountryId())));
	// } catch (ManagedException e) {
	// PacketSendUtility.sendErrorMessage(player, e.getCode());
	// } catch (Exception e) {
	// logger.error("砍大臣发送集结消息", e);
	// PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
	// }
	// return sm;
	// }

	@HandlerAnno
	public SM_Country_Open_Flag openFlagPanel(TSession session, CM_Country_Open_Flag req) {
		SM_Country_Open_Flag sm = new SM_Country_Open_Flag();
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			sm = countryManager.openFlagPanel(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("打开砍国旗界面", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	// @HandlerAnno
	// public SM_Country_Request_Send_Message_Flag sendMessageByFlag(TSession
	// session,
	// CM_Country_Request_Send_Message_Flag req) {
	// SM_Country_Request_Send_Message_Flag sm = new
	// SM_Country_Request_Send_Message_Flag();
	// Player player = SessionUtil.getPlayerBySession(session);
	// try {
	// Map<CountryId, Country> countries = countryManager.getCountries();
	// sm = countryManager.sendMessageByFlag(player,
	// countries.get(CountryId.valueOf(req.getCountryId())));
	// } catch (ManagedException e) {
	// PacketSendUtility.sendErrorMessage(player, e.getCode());
	// } catch (Exception e) {
	// logger.error("砍国旗发送集结消息", e);
	// PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
	// }
	// return sm;
	// }

	@HandlerAnno
	public void appoint(TSession session, CM_Country_Appoint req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			PlayerEnt targetEnt = playerManager.getByName(req.getName());
			if (targetEnt == null) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.NOT_FOUND_PLAYER);
				return;
			}
			Player target = playerManager.getPlayer(targetEnt.getGuid());
			countryManager.appoint(player, target, req.getOfficial(), req.getIndex());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("任命官员", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void forbidChat(TSession session, CM_Country_ForbidChat req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			PlayerEnt targetEnt = playerManager.getByName(req.getName());
			if (targetEnt == null) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.NOT_FOUND_PLAYER);
				return;
			}
			Player target = playerManager.getPlayer(targetEnt.getGuid());
			countryManager.forbidChat(player, target, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("禁言", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void setTraitor(TSession session, CM_Country_SetTraitor req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			PlayerEnt targetEnt = playerManager.getByName(req.getName());
			if (targetEnt == null) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.NOT_FOUND_PLAYER);
				return;
			}
			Player target = playerManager.getPlayer(targetEnt.getGuid());
			countryManager.setTraitor(player, target, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("标记内奸", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public SM_Country_Relieve_Traitor relieveTraitor(TSession session, CM_Country_Relieve_Traitor req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Country_Relieve_Traitor sm = new SM_Country_Relieve_Traitor();
		try {
			PlayerEnt targetEnt = playerManager.getByName(req.getName());
			if (targetEnt == null) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.NOT_FOUND_PLAYER);
				return sm;
			}
			Player target = playerManager.getPlayer(targetEnt.getGuid());
			countryManager.relieveTraitor(player, target, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("解除标记内奸", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public void getCountryTraitorNum(TSession session, CM_Country_Traitor_Num req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.getCountryTraitorNum(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看国家内奸的数量", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getCountryTraitorRank(TSession session, CM_Country_Traitor_Rank req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.getCountryTraitorRank(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看内奸榜单", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void distributeTogether(TSession session, CM_Country_DistributeTogetherToken req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			Player target = player.getCountry().getCivil(req.getName());
			if (target == null || !target.isSpawned()) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.NOT_FOUND_PLAYER);
				return;
			}
			countryManager.distributeTogetherToken(player, target, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("分配虎符", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void distributeTank(TSession session, CM_Country_DistributeTank req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			Player target = player.getCountry().getCivil(req.getName());
			if (target == null || !target.isSpawned()) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.NOT_FOUND_PLAYER);
				return;
			}
			countryManager.distributeTank(player, target, req.getTankId(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("分配战车", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void callbackTank(TSession session, CM_Country_CallbackTank req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.callbackTank(player, req.getTankId(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("找回战车", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void checkSalaryStatus(TSession session, CM_Country_Check_Salary_Status req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.checkSalaryStatus(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("领取俸禄的状态", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void openCivilSalary(TSession session, CM_Country_OpenCivilSalary req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.openCivilSalary(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("发放国家福利", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void startTemple(TSession session, CM_Country_StartTemple req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.startTemple(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("开始国家搬砖", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void startExpress(TSession session, CM_Country_StartExpress req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.startExpress(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("开始国家运镖", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getCountryQuestStatus(TSession session, CM_Country_QuestStatus req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			PacketSendUtility.sendPacket(player, SM_Country_QuestStatus.valueOf(player.getCountry().getCountryQuest(),
					player.getCountry().getCourt(), !player.getCountry().getTraitorMapFixs().isEmpty()));
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("开始国家运镖", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getAllCountryQuestStatus(TSession session, CM_All_CountryStatus req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			PacketSendUtility.sendPacket(player, SM_All_CountryStatus.valueOf(countryManager.getCountries().values()));
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("开始国家运镖", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void receiveCivilSalary(TSession session, CM_Country_ReceivedCivilSalary req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.recevieCivilSalary(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("领取国家福利", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void receiveOfficialSalary(TSession session, CM_Country_ReceivedOfficialSalary req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.recevieOfficialSalary(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("领取官员福利", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void openOfficialSalary(TSession session, CM_Country_OpenOfficialSalary req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.openOfficialSalary(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("官员福利", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void appointGurad(TSession session, CM_Country_Appoint_Guard req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			PlayerEnt targetEnt = playerManager.getByName(req.getName());
			if (targetEnt == null) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.NOT_FOUND_PLAYER);
				return;
			}
			Player target = playerManager.getPlayer(targetEnt.getGuid());
			countryManager.appointGurad(player, target, req.getIndex());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("任命卫队", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void depose(TSession session, CM_Country_Depose req) {
		Player player = SessionUtil.getPlayerBySession(session);
		Player target = playerManager.getPlayer(req.getTargetId());
		try {
			countryManager.depose(player, target);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("罢免官员", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void deposeGurad(TSession session, CM_Country_Depose_Guard req) {
		Player player = SessionUtil.getPlayerBySession(session);
		Player target = playerManager.getPlayer(req.getTargetId());
		try {
			countryManager.deposeGurad(player, target);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("罢免卫队", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void upgradeCountryShop(TSession session, CM_Country_UpgradeShop req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.upgradeCountryShop(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("升级城门", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void upgradeDoor(TSession session, CM_Country_UpgradeDoor req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.upgradeDoor(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("升级城门", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void upgradeTank(TSession session, CM_Country_UpgrageTank req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.upgradeTank(player, req.getId(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("升级战车", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void createTank(TSession session, CM_Country_CreateTank req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.createTank(player, req.getResourceId(), req.getIndex(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("创建战车", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void upgradeFactory(TSession session, CM_Country_UpgradeFactory req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.upgradeFactory(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("升级军工厂", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void upgradeFlag(TSession session, CM_Country_UpgradeFlag req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.upgradeFlag(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("升级国旗", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void notice(TSession session, CM_Country_SetNotice req) {
		if (req.getNotice() == null || req.getNotice().length() > 5000) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.setNotice(player, req.getNotice());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("设置公告", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void callTogether(TSession session, CM_Country_CallTogether req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.callTogether(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("召集", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void callTogetherToken(TSession session, CM_Country_UseCallToken req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.callTogetherToken(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("召集虎符", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void callTogetherGuard(TSession session, CM_Country_CallTogether_Guard req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.callTogetherGuard(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("召集卫队", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getOfficalList(TSession session, CM_Country_Offical req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.getOfficalList(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("打开国家主界面", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getDoor(TSession session, CM_Country_Door req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.getDoor(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看城门", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getFactory(TSession session, CM_Country_Factory req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.getFactory(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看官军工厂", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getFlag(TSession session, CM_Country_Flag req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.getFlag(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看国旗", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getOffers(TSession session, CM_Country_Coppers req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.getOffers(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看国家财产", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getStorge(TSession session, CM_Country_Storage req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.getStorge(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看国库", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getShop(TSession session, CM_Country_Shop req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.getShop(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看商店", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void contributeShop(TSession session, CM_Country_Contribute_Shop req) {
		if (req.getCount() <= 0 || 100000000 <= req.getCount()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.contributionShop(player, req.getItemId(), req.getCount());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("捐献", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void contribution(TSession session, CM_Country_Contribution req) {
		if (req.getCount() <= 0 || 100000000 <= req.getCount()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.contribution(player, req.getType(), req.getCount(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("捐献", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void countryShopBuy(TSession session, CM_Country_Shop_Buy req) {
		if (req.getCount() <= 0 || 100000 <= req.getCount()) {
			return;
		}
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.buy(player, req.getId(), req.getCount());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("国家内购买", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void countryStore(TSession session, CM_Country_Store req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.store(player, req.getIndexs(), req.isInPack());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("国家商店", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void countryStore(TSession session, CM_Country_MovePack req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.countryMovePack(player, req.getType(), req.getFromIndex(), req.getToIndex(), req.getSign(),
					req.isInPack());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("权限操作放入 / 取出", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void countryTake(TSession session, CM_Country_Take req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.take(player, req.getIndexs(), req.isPack());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("去除权限", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void countrySeeAllLog(TSession session, CM_Country_Action_See_All_Log req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.seeAllLog(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看日志", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void countrySeeFeteLog(TSession session, CM_Country_Fete_Log req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.seeFeteLog(player, req.isAll());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看日志", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void countryFete(TSession session, CM_Country_Fete req) {
		Player player = SessionUtil.getPlayerBySession(session);
		if (req.getType() != 0 && req.getType() != 1) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
			return;
		}
		try {
			countryManager.sacrifice(player, req.getType(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("国家祭祀", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void mobilization(TSession session, CM_Official_Mobilization req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.mobilization(player, req.getPhrase(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("全民动员", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void queryMobilization(TSession session, CM_Query_Mobilization req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.queryMobilization(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("全民动员", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getHiddenMissionInfo(TSession session, CM_Hidden_Mission_Info req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.getHiddenMissionInfo(player, req.getType());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("获取国家战事隐藏任务信息", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getDiplomacyDamageRank(TSession session, CM_Diplomacy_Damage_Rank req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.getDiplomacyDamageRank(player, req.getCountry());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("获取国家战事隐藏任务信息", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getCountryFlagDamageRank(TSession session, CM_CountryFlag_Damage_Rank req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.getCountryFlagDamageRank(player, req.getCountry());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("获取国家战事隐藏任务信息", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void initiativeAdbicate(TSession session, CM_ReserveKing_InitiativeAdbicate req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.initiativeAdbicate(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("储君主动退位出现未知异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void becomeReserveKing(TSession session, CM_ReserveKing_Become req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.becomeReserveKing(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("成为储君出现未知异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void rewardReserveKingTask(TSession session, CM_ReserveKing_Task_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.rewardReserveKingTask(player, ReserveTaskEnum.typeOf(req.getTaskType()));
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("获取国家战事隐藏任务信息", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void reserveKingCallTogether(TSession session, CM_ReserveKing_CallTogether req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.reserveKingCallTogether(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("储君使用召集令", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public SM_ReserveKingVO getCountryReserveKingInfo(TSession session, CM_ReserveKing_GetInfo req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_ReserveKingVO result = new SM_ReserveKingVO();
		try {
			result = countryManager.getReserveKingInfo(player, CountryId.valueOf(req.getCountryId()));
		} catch (ManagedException e) {
			result.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("储君使用召集令", e);
			result.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return result;
	}

	@HandlerAnno
	public void getCountryFlagQuestInfo(TSession session, CM_Flag_Quest_Info req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.getFlagQuestInfo(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("储君使用召集令", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void contributeBuildItem(TSession session, CM_CountryTechnology_Contribute_BuildItem req) {
		Player player = SessionUtil.getPlayerBySession(session);
		if (req.getCount() <= 0) {
			return;
		}
		try {
			countryManager.contributeBuildItem(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("储君使用召集令", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void placeArmyFlag(TSession session, CM_CountryTechnology_Place_ArmyFlag req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.spawnCountryTechnologyFlag(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("放置军旗出现未知异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void queryTechnologyInfo(TSession session, CM_CountryTechnology_QueryInfo req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			PacketSendUtility.sendPacket(player, CountryTechnologyVo.valueOf(player.getCountry().getNewTechnology()));
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("储君使用召集令", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void refreshTechnologyFlagCount(TSession session, CM_CountryTechnology_FlagCount_Refresh req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			countryManager.refreshTechnologyFlagCount(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("刷新军旗数量", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@ReceiverAnno
	public void login(LoginEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		countryManager.registerPlayer(player);
		if (player.isKing()
				&& (player.getCountry().getCourt().getKing().getLastKingLoginBroadTime() + ConfigValueManager
						.getInstance().KING_LOGIN_BROAD_CD.getValue() * DateUtils.MILLIS_PER_SECOND) <= System
							.currentTimeMillis()) {
			I18nUtils i18nUtils = I18nUtils.valueOf("404001");
			i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
			i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			ChatManager.getInstance().sendSystem(71003, i18nUtils, null, player.getCountry());
			player.getCountry().getCourt().getKing().setLastKingLoginBroadTime(System.currentTimeMillis());
		}

		countryManager.loginTraitorPlayrFix(player);
	}

	@ReceiverAnno
	public void loginOut(LogoutEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		countryManager.unRegisterPlayer(player);

		ReserveKing reserveKing = player.getCountry().getReserveKing();
		if (!reserveKing.isDeprected() && reserveKing.isReserveKing(player.getObjectId())) {
			reserveKing.setLastUnlineTime(System.currentTimeMillis());
		}
	}

	@ReceiverAnno
	public void playerCountryFlagModuleOpen(ModuleOpenEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (event.getModuleResourceId().equals(CountryFlag.COUNTRY_FLAG_MODULE_OPENKEY)) {
			player.getGameStats().addModifiers(CountryFlag.COUNTRY_FLAG,
					player.getCountry().getCountryFlag().getResource().getPlayerStats());
		}
	}

	@Scheduled(name = "系统整点修正值", value = "0 0 * * * *")
	public void integralClock() {
		countryManager.countryBuildValueFix();
	}

	@Scheduled(name = "定时持久化国家", value = "0 */5 * * * *")
	public void updateCountry() {
		if (ClearAndMigrate.clear) {
			return;
		}
		for (Country country : countryManager.getCountries().values()) {
			country.upate();
		}
	}

	@Scheduled(name = "定时刷新国家20s", value = "*/20 * * * * *")
	public void refreshCountryShopSchedule() {
		// 刷新商店
		countryManager.refreshCountryShop();
		// 刷新国家控制力
		countryManager.refreshConctrol();
		// 检查俸禄发放
		countryManager.refreshSalary();
		// 刷新内奸
		countryManager.refreshTraitorPlayerFix();
		if (ServerState.getInstance().getLastGangOfWarDate() == null) {
			// 检查储君是否已经过期
			countryManager.checkAndAdbicateReserveKing();
		}
		countryManager.refreshTechnologyFlagCount(true);
	}

	@Scheduled(name = "定时刷新国家俸禄", value = "@configValueManager.civilSalryResettime", type = ValueType.SPEL)
	public void refreshSalarySchedule() {
		// 检查俸禄发放
		countryManager.refreshSalary();
	}

	@Scheduled(name = "定时刷新国家30s", value = "*/30 * * * * *")
	public void refreshCountrySchedule() {
		// 刷新禁言
		countryManager.refreshForbidChat();
	}

	@ReceiverAnno
	public void serverOpen(ServerOpenEvent event) {
		if (ServerState.getInstance().neverStartFlag()) {
			countryManager.startCountryFlag();
		}
		if (ServerState.getInstance().neverStartDiplomacy()) {
			countryManager.startDiplomacy();
		}
	}
}
