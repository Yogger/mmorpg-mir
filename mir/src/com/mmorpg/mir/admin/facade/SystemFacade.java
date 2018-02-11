//package com.mmorpg.mir.admin.facade;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.concurrent.ConcurrentMap;
//
//import org.apache.commons.lang.ArrayUtils;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.helpers.MessageFormatter;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.stereotype.Component;
//
//import com.mmorpg.mir.admin.bean.IpGroupBean;
//import com.mmorpg.mir.admin.bean.PlayerInfoBean;
//import com.mmorpg.mir.admin.bean.PlayerInfoBeanNew;
//import com.mmorpg.mir.admin.bean.ServerStat;
//import com.mmorpg.mir.admin.bean.StatEventBean;
//import com.mmorpg.mir.admin.bean.StatNetReceiveBean;
//import com.mmorpg.mir.admin.bean.StatNetSendBean;
//import com.mmorpg.mir.admin.bean.StatTaskBean;
//import com.mmorpg.mir.model.addication.AntiAddictionManager;
//import com.mmorpg.mir.model.blackshop.BlackShopConfig;
//import com.mmorpg.mir.model.blackshop.model.BlackShopServer;
//import com.mmorpg.mir.model.capturetown.manager.TownManager;
//import com.mmorpg.mir.model.chat.manager.ChatManager;
//import com.mmorpg.mir.model.common.ResourceReload;
//import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
//import com.mmorpg.mir.model.common.exception.ManagedException;
//import com.mmorpg.mir.model.commonactivity.manager.CommonActivityManager;
//import com.mmorpg.mir.model.commonactivity.model.CommonIdentifyTreasureServer;
//import com.mmorpg.mir.model.commonactivity.model.CommonIdentifyTreasureTotalServers;
//import com.mmorpg.mir.model.core.condition.CoreConditionType;
//import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
//import com.mmorpg.mir.model.country.manager.CountryManager;
//import com.mmorpg.mir.model.country.model.Country;
//import com.mmorpg.mir.model.country.model.CountryId;
//import com.mmorpg.mir.model.dirtywords.manager.NodeManager;
//import com.mmorpg.mir.model.gameobjects.Player;
//import com.mmorpg.mir.model.gang.manager.GangManager;
//import com.mmorpg.mir.model.gang.model.Gang;
//import com.mmorpg.mir.model.gang.packet.SM_Gang_List;
//import com.mmorpg.mir.model.log.ModuleInfo;
//import com.mmorpg.mir.model.log.ModuleType;
//import com.mmorpg.mir.model.log.SubModuleType;
//import com.mmorpg.mir.model.mail.manager.MailManager;
//import com.mmorpg.mir.model.mail.model.Mail;
//import com.mmorpg.mir.model.mail.model.MailGroup;
//import com.mmorpg.mir.model.openactive.model.ActivityEnum;
//import com.mmorpg.mir.model.openactive.model.ActivityInfo;
//import com.mmorpg.mir.model.openactive.packet.SM_Celebrate_Activity_Change;
//import com.mmorpg.mir.model.operator.manager.OperatorManager;
//import com.mmorpg.mir.model.operator.model.SuperVip;
//import com.mmorpg.mir.model.operator.packet.SM_Server_Maintenance;
//import com.mmorpg.mir.model.operator.packet.SM_SubInformation_Confirmation;
//import com.mmorpg.mir.model.player.entity.PlayerEnt;
//import com.mmorpg.mir.model.player.facade.PlayerFacade;
//import com.mmorpg.mir.model.player.manager.PlayerManager;
//import com.mmorpg.mir.model.purse.model.CurrencyType;
//import com.mmorpg.mir.model.purse.reward.CurrencyRewardsProvider;
//import com.mmorpg.mir.model.rank.manager.WorldRankManager;
//import com.mmorpg.mir.model.reward.manager.RewardManager;
//import com.mmorpg.mir.model.reward.model.Reward;
//import com.mmorpg.mir.model.reward.model.RewardItem;
//import com.mmorpg.mir.model.serverstate.ServerState;
//import com.mmorpg.mir.model.session.FirewallFilter;
//import com.mmorpg.mir.model.session.ManagementFilter;
//import com.mmorpg.mir.model.session.SessionManager;
//import com.mmorpg.mir.model.utils.PacketSendUtility;
//import com.mmorpg.mir.model.utils.SessionUtil;
//import com.mmorpg.mir.model.utils.ThreadPoolManager;
//import com.mmorpg.mir.transfer.facade.ClientTransferFacade;
//import com.mmorpg.mir.transfer.manager.ClientCenterSessionManager;
//import com.windforce.common.event.event.IEvent;
//import com.windforce.common.event.jmx.Stat.EventStat;
//import com.windforce.common.event.monitor.MonitorEventBusManager;
//import com.windforce.common.resource.Storage;
//import com.windforce.common.resource.StorageManager;
//import com.windforce.common.utility.JsonUtils;
//import com.windforce.common.utility.ZlibUtils;
//import com.xiaosan.dispatcher.anno.HandlerAnno;
//import com.xiaosan.dispatcher.anno.InSessionAnno;
//import com.xiaosan.dispatcher.core.MonitorActionDispatcher;
//import com.xiaosan.socket.core.TSession;
//import com.xiaosan.socket.monitor.ReceivePacketStat;
//import com.xiaosan.socket.monitor.SendPacketStat;
//
//@Component
//public class SystemFacade implements ApplicationContextAware {
//
//	private static Logger logger = LoggerFactory.getLogger(SystemFacade.class);
//	@Autowired
//	private StorageManager storageManager;
//
//	@Autowired
//	private MailManager mailManager;
//
//	@Autowired
//	private PlayerManager playerManager;
//
//	@Autowired
//	private RewardManager rewardManager;
//
//	@Autowired
//	private OperatorManager operatorManager;
//
//	@Autowired
//	private GangManager gangManager;
//
//	@Autowired
//	private CountryManager countryManager;
//
//	@Autowired
//	private WorldRankManager worldRankManager;
//
//	@Autowired
//	private NodeManager nodeManager;
//
//	@Autowired
//	private ServerState serverState;
//
//	@Autowired
//	private TownManager townManager;
//
//	@Autowired
//	private CommonActivityManager commonActivityManager;
//
//
//	public SGM_Stop stop(TSession session, GM_Stop req) {
//		String message = MessageFormatter.format("管理后台[{}]请求关闭服务器", session.getInetIp()).getMessage();
//		logger.error(message);
//		try {
//			FirewallFilter.setOpen(false);
//			// 请求踢全部玩家下线,确保发出登出事件
//			this.kick(session, null);
//			// 保存所有帮会
//			gangManager.updateAll();
//			// 保存国家
//			countryManager.updateCountry();
//			// 排行榜
//			worldRankManager.updateAllRank();
//			// 服务器全局变量
//			serverState.update();
//			// 保存封禁对象
//			operatorManager.updateOperator();
//			// 城池
//			townManager.updateCaptureTownEnt();
//			// 写入防火墙状态
//			consoleManager.shutdown();
//			Thread thread = new Thread("停止服务器线程") {
//				public void run() {
//					try {
//						Thread.sleep(5000); // 延时5秒再关闭服务器，以便通信信息可正确返回给客户端
//						applicationContext.close();
//					} catch (Exception e) {
//						logger.error("停止服务器线程", e);
//					}
//				};
//			};
//			thread.setDaemon(true);
//			thread.start();
//		} catch (Exception e) {
//			logger.error("停止服务器线程", e);
//		}
//		return new SGM_Stop();
//	}
//
//	@Autowired
//	private SessionManager sessionManager;
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Kick kick(TSession session, GM_Kick req) {
//		String message = MessageFormatter.format("管理后台[{}]请求踢全部玩家下线", session.getInetIp()).getMessage();
//		logger.error(message);
//		try {
//			// 通知所有玩家维护
//			sessionManager.kickAllAndSendPacket(new SM_Server_Maintenance(), session);
//		} catch (Exception e) {
//			logger.error("踢全部玩家下线", e);
//		}
//		return new SGM_Kick();
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Count count(TSession session, GM_Count req) {
//		// logger.error("管理后台[{}]请求获取在线用户数量", session.getInetIp());
//		SGM_Count result = new SGM_Count();
//		try {
//			result.setSize(sessionManager.count(false));
//		} catch (Exception e) {
//			String message = MessageFormatter.format("获取在线用户数量", session.getInetIp()).getMessage();
//			logger.error(message);
//		}
//		return result;
//	}
//
//	@Autowired
//	private FirewallFilter firewallFilter;
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Block block(TSession session, GM_Block req) {
//		String message = MessageFormatter.format("管理后台[{}]请求开启防火墙阻止功能", session.getInetIp()).getMessage();
//		logger.error(message);
//		try {
//			firewallFilter.blockAll();
//			consoleManager.block();
//		} catch (Exception e) {
//			logger.error("开启防火墙", e);
//		}
//		return new SGM_Block();
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_UnBlock unblock(TSession session, GM_UnBlock req) {
//		String message = MessageFormatter.format("管理后台[{}]请求关闭防火墙阻止功能", session.getInetIp()).getMessage();
//		logger.error(message);
//		try {
//			firewallFilter.unblockAll();
//			consoleManager.unBlock();
//		} catch (Exception e) {
//			logger.error("关闭防火墙", e);
//		}
//		return new SGM_UnBlock();
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Recharge recharge(TSession session, GM_Recharge req) {
//		SGM_Recharge sgm = new SGM_Recharge();
//		try {
//			Player player = playerManager.getPlayerByAccount(req.getAccount(), req.getOp(), req.getServerId());
//			if (player == null) {
//				sgm.setCode(ManagedErrorCode.NOT_FOUND_PLAYER);
//				return sgm;
//			}
//			if (player.getVip().orderRepeat(req.getOrderId())) {
//				sgm.setCode(ManagedErrorCode.RECHARGE_ORDER_REPEAT);
//				return sgm;
//			}
//
//			Reward reward = Reward.valueOf();
//			reward.addCurrency(CurrencyType.GOLD, req.getMoney());
//			for (RewardItem item : reward.getItems()) {
//				item.putParms(CurrencyRewardsProvider.VIP_REWARD, "true");
//			}
//			rewardManager.grantReward(player, reward,
//					ModuleInfo.valueOf(ModuleType.RECHARGE, SubModuleType.DAILYCHARGE_REWARD));
//			player.getVip().addRechargeHistory(req.getOrderId(), req.getMoney(), req.getTime());
//			boolean isFirst = (player.getPlayerStat().getAccRechargeGold() == 0L);
//			player.getPlayerStat().addAccRechargeGold(req.getMoney());
//			player.getPlayerStat().campareAndSetmaxOnceRechargeGold(req.getMoney());
//			LogManager.addRecharge(player.getPlayerEnt().getAccountName(), player.getObjectId(), player.getName(), req
//					.getTime(), req.getMoney(), req.getRmb(), req.getOrderId(), isFirst, player.getLevel(), player
//					.getPlayerEnt().getSource());
//			playerManager.updatePlayer(player);
//		} catch (Exception e) {
//			logger.error("玩家充值", e);
//		}
//		return sgm;
//	}
//
//	@Autowired
//	private ManagementFilter managementFilter;
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Allow allow(TSession session, GM_Allow req) {
//		String message = MessageFormatter.format("管理后台[{}]请求添加后台许可IP[{}:{}]",
//				new Object[] { session.getInetIp(), req.getIp(), req.getName() }).getMessage();
//		logger.error(message);
//		try {
//			managementFilter.addAllowIp(req.getIp().trim(), req.getName());
//			firewallFilter.allow(req.getIp());
//		} catch (Exception e) {
//			logger.error("添加后台许可IP", e);
//		}
//		return new SGM_Allow();
//	}
//
//	@SuppressWarnings("deprecation")
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_SuperVip setSuperVip(TSession session, GM_SuperVip req) {
//		String message = MessageFormatter.format("管理后台[{}]管理后台设置超级VIP[{}]", session.getInetIp(),
//				JsonUtils.object2String(req.getSuperVips())).getMessage();
//		logger.error(message);
//		try {
//			for (SuperVip sv : req.getSuperVips()) {
//				operatorManager.buildSuperVip(sv.getName(), sv.getServerId(), sv.getContact(), sv.getMinRecharge(),
//						sv.getLevel(), sv.isOpen(), sv.getPicturePath(), sv.getCircleDay());
//			}
//		} catch (Exception e) {
//			logger.error("管理后台设置超级VIP", e);
//		}
//		return new SGM_SuperVip();
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Set_AntiAddiction setAntiAddiction(TSession session, GM_Set_AntiAddiction req) {
//		String message = MessageFormatter.format("管理后台[{}]管理后台设置防沉迷[{}]", session.getInetIp(), req.isOpen())
//				.getMessage();
//		logger.error(message);
//		AntiAddictionManager.openAnti = req.isOpen();
//		return new SGM_Set_AntiAddiction();
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_MobilePhoneGift mobilephone(TSession session, GM_MobilePhoneGift req) {
//		SGM_MobilePhoneGift sgm = new SGM_MobilePhoneGift();
//		try {
//			Player player = playerManager.getPlayerByAccount(req.getAccount(), req.getOp(), req.getServer());
//			if (player != null) {
//				player.getOperatorPool().getMobilePhone().confirmation();
//			} else {
//				sgm.setCode(ManagedErrorCode.NOT_FOUND_PLAYER);
//			}
//
//		} catch (Exception e) {
//			logger.error("玩家通过手机验证", e);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_SubInformation subInformation(TSession session, GM_SubInformation req) {
//		SGM_SubInformation sgm = new SGM_SubInformation();
//		try {
//			Player player = playerManager.getPlayerByAccount(req.getAccount(), req.getOp(), req.getServer());
//			if (player != null) {
//				player.getOperatorPool().getSubInformation().confirmation();
//				operatorManager.rewardInformation(player);
//				PacketSendUtility.sendPacket(player, new SM_SubInformation_Confirmation());
//			} else {
//				sgm.setCode(ManagedErrorCode.NOT_FOUND_PLAYER);
//			}
//
//		} catch (Exception e) {
//			logger.error("玩家通过手机验证", e);
//		}
//		return sgm;
//	}
//
//	// 实现 Spring 的接口
//
//	private ClassPathXmlApplicationContext applicationContext;
//
//	@Override
//	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//		this.applicationContext = (ClassPathXmlApplicationContext) applicationContext;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Reload reload(TSession session, final GM_Reload req) {
//		logger.error(String.format("管理后台请求重新加载配置文件!resources[%s]", JsonUtils.object2String(req.getResources())));
//		final SGM_Reload sm = new SGM_Reload();
//		final String ip = session.getInetIp();
//		ThreadPoolManager.getInstance().schedule(new Runnable() {
//			@Override
//			public void run() {
//				if (!ArrayUtils.isEmpty(req.getResources())) {
//					String head = req.getResources()[0];
//					String[] notHeadString = new String[req.getResources().length - 2];
//					for (int i = 2; i < req.getResources().length; i++) {
//						notHeadString[i - 2] = req.getResources()[i];
//					}
//					req.setResources(notHeadString);
//					Map<String, ResourceReload> reloadManager = applicationContext.getBeansOfType(ResourceReload.class);
//					Map<String, ResourceReload> reloadHandles = new HashMap<String, ResourceReload>();
//					for (ResourceReload rr : reloadManager.values()) {
//						reloadHandles.put(rr.getResourceClass().getSimpleName(), rr);
//					}
//					Map<String, Storage<?, ?>> all = new HashMap<String, Storage<?, ?>>();
//					for (Storage<?, ?> stroge : storageManager.listStorages()) {
//						all.put(stroge.getClz().getSimpleName(), stroge);
//					}
//					for (String reload : req.getResources()) {
//						if (reload.equals("reload")) {
//							continue;
//						}
//						if (reload.equals("words_chat.jour")) {
//							try {
//								nodeManager.reloadChat();
//								sm.getSuccess().add(reload);
//							} catch (Exception e) {
//								logger.error("配置表reload错误.", e);
//								sm.getFail().add(reload);
//							}
//							continue;
//						}
//						if (reload.equals("words_role.jour")) {
//							try {
//								nodeManager.reloadRole();
//								sm.getSuccess().add(reload);
//							} catch (Exception e) {
//								logger.error("配置表reload错误.", e);
//								sm.getFail().add(reload);
//							}
//							continue;
//						}
//						if (reload.equals("RPResource")) {
//							PlayerFacade.masterOpen = false;
//						}
//						if (all.containsKey(reload)) {
//							try {
//								all.get(reload).reload();
//								if (reloadHandles.containsKey(reload)) {
//									reloadHandles.get(reload).reload();
//								}
//								sm.getSuccess().add(reload);
//							} catch (Exception e) {
//								logger.error("配置表reload错误.", e);
//								sm.getFail().add(reload);
//								continue;
//							}
//						} else {
//							sm.getFail().add(reload);
//						}
//					}
//					consoleManager
//							.log2DB(head, "reload", JsonUtils.object2String(req), JsonUtils.object2String(sm), ip);
//				}
//				logger.error(String.format("重新加载配置文件完成!成功加载[%s],失败加载[%s]", JsonUtils.object2String(sm.getSuccess()),
//						JsonUtils.object2String(sm.getFail())));
//
//			}
//		}, 0);
//
//		return sm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_GetBlockList getBlockList(TSession session, GM_GetBlockList req) {
//		SGM_GetBlockList result = new SGM_GetBlockList();
//		result.setResult(firewallFilter.getBlockList());
//		return result;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_BlockIP blockIp(TSession session, GM_BlockIP req) {
//		String message = MessageFormatter.format("管理后台[{}]封禁ip[{}]列表", session.getInetIp(), req.getIp()).getMessage();
//		logger.error(message);
//		SGM_BlockIP result = new SGM_BlockIP();
//		try {
//			if (!StringUtils.isEmpty(req.getIp())) {
//				firewallFilter.block(req.getIp());
//			}
//		} catch (Exception e) {
//			logger.error("封IP异常", e);
//		}
//		return result;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_BlockIP blockIpAndBlock(TSession session, GM_BlockIP_And_BlockAccount req) {
//		String message = MessageFormatter.format("管理后台[{}]封禁ip[{}]列表", session.getInetIp(), req.getIp()).getMessage();
//		logger.error(message);
//		SGM_BlockIP result = new SGM_BlockIP();
//		try {
//			if (!StringUtils.isEmpty(req.getIp())) {
//				for (Country country : countryManager.getCountries().values()) {
//					for (Player player : country.getCivils().values()) {
//						if (player.getSession().getInetIp().equals(req.getIp())) {
//							operatorManager.addBanPlayer(player);
//							PlayerManager.getInstance().updatePlayer(player);
//						}
//					}
//				}
//				firewallFilter.block(req.getIp());
//			}
//		} catch (Exception e) {
//			logger.error("封IP异常", e);
//		}
//		return result;
//	}
//
//	@Autowired
//	private ClientCenterSessionManager clientCenterSessionManager;
//
//	@Autowired
//	private ClientTransferFacade clientTransferFacade;
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Change_CenterAds changeCenterAds(TSession session, GM_Change_CenterAds ads) {
//		String message = MessageFormatter.format("管理后台[{}]更改跨服服务器地址[{}]", session.getInetIp(),
//				JsonUtils.object2String(ads)).getMessage();
//		logger.error(message);
//		SGM_Change_CenterAds result = new SGM_Change_CenterAds();
//		try {
//			clientCenterSessionManager.changeSession(ads);
//			clientTransferFacade.doConnect();
//		} catch (Exception e) {
//			logger.error("更改跨服服务器地址异常", e);
//		}
//		return result;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_UnBlockIP unBlockIp(TSession session, GM_UnBlockIP req) {
//		SGM_UnBlockIP result = new SGM_UnBlockIP();
//		firewallFilter.unblock(req.getIp());
//		return result;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Online gmOnline(TSession session, GM_Online req) {
//		// logger.error("管理后台[{}]请求获取ip列表", session.getInetIp());
//		Map<Long, TSession> identyfyMap = sessionManager.getIdentities();
//		List<String> ips = new ArrayList<String>(identyfyMap.size());
//		for (Entry<Long, TSession> entry : identyfyMap.entrySet()) {
//			ips.add(entry.getValue().getInetIp());
//		}
//		SGM_Online res = new SGM_Online();
//		String json = JsonUtils.object2String(ips);
//		res.setIps(ZlibUtils.zip(json));
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Post post(TSession session, GM_Post req) {
//		// logger.error("管理后台[{}]请求发送全服公告[{}]", new Object[] {
//		// session.getInetIp(), req });
//		ChatManager.getInstance().sendSystem(71001, req.getContent(), null, null, null);
//		ChatManager.getInstance().sendSystem(0, req.getContent(), null, null, null);
//		return new SGM_Post();
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_SendMail gmSendMail(TSession session, GM_SendMail req) {
//		// logger.error("管理后台[{}]发送个人邮件", session.getInetIp());
//		Player player = playerManager.getPlayer(req.getPlayerId());
//		SGM_SendMail res = new SGM_SendMail();
//		if (player == null) {
//			res.setCode(ManagedErrorCode.NOT_FOUND_PLAYER);
//			return res;
//		}
//		Reward reward = null;
//		if (req.getReward() != null) {
//			reward = req.getReward().init();
//		}
//		Mail mail = Mail.valueOf(req.getTitle(), req.getSender(), req.getContext(), reward);
//		mailManager.sendMail(mail, req.getPlayerId());
//
//		res.setMailId(mail.getId());
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_SendMailGroup gmSendMail(TSession session, GM_SendMailGroup req) {
//		String message = MessageFormatter.format("管理后台[{}]发送全服邮件", session.getInetIp()).getMessage();
//		logger.error(message);
//		Reward reward = null;
//		if (req.getReward() != null) {
//			reward = req.getReward().init();
//		}
//		MailGroup mailGroup = MailGroup.valueOf(req.getTitle(), req.getSender(), req.getContext(), reward,
//				req.getReceiveCondition(), req.getExpriedTime());
//		if (mailGroup.getReceivedCondtionResources() != null) {
//			for (CoreConditionResource ccr : mailGroup.getReceivedCondtionResources()) {
//				ccr.setType(CoreConditionType.valueOf(ccr.getTypeString()));
//			}
//		}
//		MailManager.getInstance().addMailGroup(mailGroup);
//		for (long playerId : sessionManager.getOnlineIdentities()) {
//			mailManager.receiveGroupMail(playerId);
//		}
//		SGM_SendMailGroup res = new SGM_SendMailGroup();
//		res.setMailId(mailGroup.getId());
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_GetIpGroup getIpGroup(TSession session, GM_GetIpGroup req) {
//		String message = MessageFormatter.format("管理后台[{}]获取ip对应的在线信息", session.getInetIp()).getMessage();
//		logger.error(message);
//		Map<Long, TSession> identities = SessionManager.getInstance().getIdentities();
//		Map<String, IpGroupBean> result = new HashMap<String, IpGroupBean>();
//		for (Entry<Long, TSession> entry : identities.entrySet()) {
//			TSession t = entry.getValue();
//			Player player = SessionUtil.getPlayerBySession(t);
//			if (player != null) {
//				String ip = t.getInetIp();
//				if (!result.containsKey(ip)) {
//					IpGroupBean bean = new IpGroupBean();
//					bean.setIp(ip);
//					result.put(ip, bean);
//				}
//				IpGroupBean bean = result.get(ip);
//				bean.getAccounts().add(player.getPlayerEnt().getAccountName());
//				bean.getNames().add(player.getName());
//			}
//		}
//		SGM_GetIpGroup res = new SGM_GetIpGroup();
//
//		String json = JsonUtils.object2String(result.values());
//		res.setIpGroupBeans(ZlibUtils.zip(json));
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_BlockById blockById(TSession session, GM_BlockById req) {
//		String message = MessageFormatter.format("管理后台[{}]封禁用户", session.getInetIp()).getMessage();
//		logger.error(message);
//		Player player = PlayerManager.getInstance().getPlayer(req.getGuid());
//		if (player != null) {
//			operatorManager.addBanPlayer(player);
//			PlayerManager.getInstance().updatePlayer(player);
//		}
//		SGM_BlockById res = new SGM_BlockById();
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_BlockById blockByAccount(TSession session, GM_BlockByAccount req) {
//		String message = MessageFormatter.format("管理后台[{}]封禁用户", session.getInetIp()).getMessage();
//		logger.error(message);
//		Player player = PlayerManager.getInstance()
//				.getPlayerByAccount(req.getAccount(), req.getOp(), req.getServerId());
//		if (player != null) {
//			operatorManager.addBanPlayer(player);
//			PlayerManager.getInstance().updatePlayer(player);
//		}
//		SGM_BlockById res = new SGM_BlockById();
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_UnBlockById unBlockById(TSession session, GM_UnBlockById req) {
//		String message = MessageFormatter.format("管理后台[{}]解除封禁用户", session.getInetIp()).getMessage();
//		logger.error(message);
//		Player player = PlayerManager.getInstance().getPlayer(req.getGuid());
//		SGM_UnBlockById res = new SGM_UnBlockById();
//		if (player != null) {
//			operatorManager.unBanPlayer(player);
//			PlayerManager.getInstance().updatePlayer(player);
//		} else {
//			res.setCode(ManagedErrorCode.NOT_FOUND_PLAYER);
//		}
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_UnBlockById unBlockByAccount(TSession session, GM_UnBlockByAccount req) {
//		String message = MessageFormatter.format("管理后台[{}]解除封禁用户", session.getInetIp()).getMessage();
//		logger.error(message);
//		Player player = PlayerManager.getInstance()
//				.getPlayerByAccount(req.getAccount(), req.getOp(), req.getServerId());
//		SGM_UnBlockById res = new SGM_UnBlockById();
//		if (player != null) {
//			operatorManager.unBanPlayer(player);
//			PlayerManager.getInstance().updatePlayer(player);
//		} else {
//			res.setCode(ManagedErrorCode.NOT_FOUND_PLAYER);
//		}
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_ForbidChatById forbidChatById(TSession session, GM_ForbidChatById req) {
//		// logger.error("管理后台[{}]禁言用户", session.getInetIp());
//		SGM_ForbidChatById res = new SGM_ForbidChatById();
//		Player player = PlayerManager.getInstance().getPlayer(req.getGuid());
//		if (player != null) {
//			operatorManager.addChat(player, req.getEndTime());
//			PlayerManager.getInstance().updatePlayer(player);
//		} else {
//			res.setCode(ManagedErrorCode.NOT_FOUND_PLAYER);
//		}
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_ForbidChatById forbidChatByAccount(TSession session, GM_ForbidChatByAccount req) {
//		String message = MessageFormatter.format("管理后台[{}]禁言用户", session.getInetIp()).getMessage();
//		logger.error(message);
//		SGM_ForbidChatById res = new SGM_ForbidChatById();
//		Player player = playerManager.getPlayerByAccount(req.getAccount(), req.getOp(), req.getServerId());
//		if (player != null) {
//			operatorManager.addChat(player, req.getEndTime());
//			PlayerManager.getInstance().updatePlayer(player);
//		} else {
//			res.setCode(ManagedErrorCode.NOT_FOUND_PLAYER);
//		}
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_GetForbidChatList getForbidChatList(TSession session, GM_GetForbidChatList req) {
//		String message = MessageFormatter.format("管理后台[{}]查询禁言用户列表", session.getInetIp()).getMessage();
//		logger.error(message);
//		SGM_GetForbidChatList res = new SGM_GetForbidChatList();
//		res.setForbidChatList(operatorManager.getForbidChatList());
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_GetGmList getGmList(TSession session, GM_GetGmList req) {
//		String message = MessageFormatter.format("管理后台[{}]查询GM用户列表", session.getInetIp()).getMessage();
//		logger.error(message);
//		SGM_GetGmList res = SGM_GetGmList.valueOf();
//		for (Long playerId : operatorManager.getGmList().getGmList()) {
//			Player player = playerManager.getPlayer(playerId);
//			res.add(player);
//		}
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_GetLoginBanList getForbidChatList(TSession session, GM_GetLoginBanList req) {
//		String message = MessageFormatter.format("管理后台[{}]查询禁止登录用户列表", session.getInetIp()).getMessage();
//		logger.error(message);
//		SGM_GetLoginBanList res = new SGM_GetLoginBanList();
//		res.setLoginBanList(operatorManager.getLoginBanList());
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_UnForbidChatById unForbidChatById(TSession session, GM_UnForbidChatById req) {
//		String message = MessageFormatter.format("管理后台[{}]解除禁言用户", session.getInetIp()).getMessage();
//		logger.error(message);
//		SGM_UnForbidChatById res = new SGM_UnForbidChatById();
//		Player player = PlayerManager.getInstance().getPlayer(req.getGuid());
//		if (player != null) {
//			operatorManager.removeChat(player.getObjectId());
//			PlayerManager.getInstance().updatePlayer(player);
//		} else {
//			res.setCode(ManagedErrorCode.NOT_FOUND_PLAYER);
//		}
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_UnForbidChatById unForbidChatByAccount(TSession session, GM_UnForbidChatByAccount req) {
//		String message = MessageFormatter.format("管理后台[{}]解除禁言用户", session.getInetIp()).getMessage();
//		logger.error(message);
//		SGM_UnForbidChatById res = new SGM_UnForbidChatById();
//		Player player = PlayerManager.getInstance()
//				.getPlayerByAccount(req.getAccount(), req.getOp(), req.getServerId());
//		if (player != null) {
//			operatorManager.removeChat(player.getObjectId());
//			PlayerManager.getInstance().updatePlayer(player);
//		} else {
//			res.setCode(ManagedErrorCode.NOT_FOUND_PLAYER);
//		}
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_SetGmPrivilege setGmPrivilege(TSession session, GM_SetGmPrivilege req) {
//		String message = MessageFormatter.format("管理后台[{}]设置GM权限", session.getInetIp()).getMessage();
//		logger.error(message);
//		SGM_SetGmPrivilege res = new SGM_SetGmPrivilege();
//		Player player = PlayerManager.getInstance()
//				.getPlayerByAccount(req.getAccount(), req.getOp(), req.getServerId());
//		if (player != null) {
//			operatorManager.setGmPrivilege(player, req.getPrivileges());
//			PlayerManager.getInstance().updatePlayer(player);
//		} else {
//			res.setCode(ManagedErrorCode.NOT_FOUND_PLAYER);
//		}
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Wechat wechatReward(TSession session, GM_Wechat req) {
//		SGM_Wechat res = new SGM_Wechat();
//		Player player = PlayerManager.getInstance().getPlayerByAccount(req.getAccount(), req.getOp(), req.getServer());
//		if (player != null) {
//			operatorManager.wechatReward(player);
//			PlayerManager.getInstance().updatePlayer(player);
//		} else {
//			res.setCode(ManagedErrorCode.NOT_FOUND_PLAYER);
//		}
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_GetPlayerInfo getPlayerInfo(TSession session, GM_GetPlayerInfo req) {
//		String message = MessageFormatter.format("管理后台[{}]查询用户详细信息", session.getInetIp()).getMessage();
//		logger.error(message);
//		Player player = playerManager.getPlayer(req.getGuid());
//		SGM_GetPlayerInfo res = new SGM_GetPlayerInfo();
//		if (player != null) {
//			res.setPlayerInfoBean(PlayerInfoBean.valueOf(player, operatorManager.isForbidChat(player.getObjectId()),
//					operatorManager.isBan(player.getObjectId())));
//		}
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_GetPlayerInfo_New getPlayerInfo(TSession session, GM_GetPlayerInfo_New req) {
//		String message = MessageFormatter.format("管理后台[{}]查询用户详细信息", session.getInetIp()).getMessage();
//		logger.error(message);
//		Player player = playerManager.getPlayer(req.getGuid());
//		SGM_GetPlayerInfo_New res = new SGM_GetPlayerInfo_New();
//		if (player != null) {
//			res = SGM_GetPlayerInfo_New.valueOf(JsonUtils.object2String(PlayerInfoBeanNew.valueOf(player,
//					operatorManager.isForbidChat(player.getObjectId()), operatorManager.isBan(player.getObjectId()))));
//		}
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_GetSimplePlayerInfoByName getPlayerSimpleInfoByName(TSession session, GM_GetSimplePlayerInfoByName req) {
//		String message = MessageFormatter.format("管理后台[{}]查询用户简单信息", session.getInetIp()).getMessage();
//		logger.error(message);
//		PlayerEnt playerEnt = playerManager.getByName(req.getName());
//		SGM_GetSimplePlayerInfoByName res = new SGM_GetSimplePlayerInfoByName();
//		if (playerEnt != null) {
//			Player player = playerManager.getPlayer(playerEnt.getGuid());
//			res.setPlayerSimpleInfo(player.createSimple());
//		}
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Gang_List getGangList(TSession session, GM_Gang_List req) {
//		SGM_Gang_List sgm = new SGM_Gang_List();
//		try {
//			String message = MessageFormatter.format("管理后台[{}]查询帮会信息", session.getInetIp()).getMessage();
//			logger.error(message);
//			if (req.getCountry() != 0 && req.getGangName() != null) {
//				SM_Gang_List sm = gangManager.getGangSimpleList(
//						CountryManager.getInstance().getCountries().get(CountryId.valueOf(req.getCountry())),
//						req.getGangName());
//				sgm.setVos(sm.getVos());
//				return sgm;
//			}
//			if (req.getCountry() != 0) {
//				SM_Gang_List sm = gangManager.getGangSimpleList(CountryManager.getInstance().getCountries()
//						.get(CountryId.valueOf(req.getCountry())));
//
//				sgm.setVos(sm.getVos());
//				return sgm;
//			}
//
//			SM_Gang_List sm = gangManager.getGangSimpleList();
//			sgm.setVos(sm.getVos());
//		} catch (Exception e) {
//			logger.error("查询帮会信息", e);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Gang_DetailInfo getGangDetailInfo(TSession session, GM_Gang_DetailInfo req) {
//		String message = MessageFormatter.format("管理后台[{}]查询帮会详细信息", session.getInetIp()).getMessage();
//		logger.error(message);
//		SGM_Gang_DetailInfo sgm = new SGM_Gang_DetailInfo();
//		try {
//			Gang gang = gangManager.getGangs().get(req.getGangId());
//			if (gang != null) {
//				sgm.setGangVO(gang.creatVO());
//			}
//		} catch (Exception e) {
//			logger.error("查询帮会信息", e);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Gang_SetInfo setGangInfo(TSession session, GM_Gang_SetInfo req) {
//		String message = MessageFormatter.format("管理后台[{}]设置帮会详细信息", session.getInetIp()).getMessage();
//		logger.error(message);
//		SGM_Gang_SetInfo sgm = new SGM_Gang_SetInfo();
//		try {
//			Gang gang = gangManager.getGangs().get(req.getGangId());
//			if (gang != null) {
//				gang.setInfo(req.getInfo());
//				sgm.setGangVO(gang.creatVO());
//			}
//		} catch (Exception e) {
//			logger.error("查询帮会信息", e);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Gang_Disband disbandGang(TSession session, GM_Gang_Disband req) {
//		String message = MessageFormatter.format("管理后台[{}]解散帮会帮会[{}]", session.getInetIp(), req.getGangId())
//				.getMessage();
//		logger.error(message);
//		Gang gang = gangManager.getGangs().get(req.getGangId());
//		SGM_Gang_Disband sgm = new SGM_Gang_Disband();
//		try {
//			if (gang != null) {
//				Player master = playerManager.getPlayer(gang.getMaster().getPlayerId());
//				try {
//					gangManager.disband(master);
//				} catch (ManagedException e) {
//					sgm.setCode(e.getCode());
//				} catch (Exception e) {
//					sgm.setCode(ManagedErrorCode.SYS_ERROR);
//				}
//			} else {
//				sgm.setCode(ManagedErrorCode.GANG_NOT_EXIST);
//			}
//		} catch (Exception e) {
//			logger.error("查询帮会信息", e);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Gang_ChangePosition changePositionGang(TSession session, GM_Gang_ChangePosition req) {
//		String message = MessageFormatter.format("管理后台[{}]改变帮会成员[{}]职务", session.getInetIp(), req.getTargetId())
//				.getMessage();
//		logger.error(message);
//		Gang gang = gangManager.getGangs().get(req.getGangId());
//		SGM_Gang_ChangePosition sgm = new SGM_Gang_ChangePosition();
//		try {
//			if (gang != null) {
//				Player master = playerManager.getPlayer(gang.getMaster().getPlayerId());
//				try {
//					gangManager.changePosition(master, req.getTargetId(), req.getPosition());
//				} catch (ManagedException e) {
//					sgm.setCode(e.getCode());
//				} catch (Exception e) {
//					sgm.setCode(ManagedErrorCode.SYS_ERROR);
//				}
//			} else {
//				sgm.setCode(ManagedErrorCode.GANG_NOT_EXIST);
//			}
//		} catch (Exception e) {
//			logger.error("查询帮会信息", e);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Gang_Expel expelGang(TSession session, GM_Gang_Expel req) {
//		String message = MessageFormatter.format("管理后台[{}]开除帮会成员[{}]职务", session.getInetIp(), req.getTargetId())
//				.getMessage();
//		logger.error(message);
//		Gang gang = gangManager.getGangs().get(req.getGangId());
//		SGM_Gang_Expel sgm = new SGM_Gang_Expel();
//		try {
//			if (gang != null) {
//				Player master = playerManager.getPlayer(gang.getMaster().getPlayerId());
//				try {
//					gangManager.expel(master, req.getTargetId());
//				} catch (ManagedException e) {
//					sgm.setCode(e.getCode());
//				} catch (Exception e) {
//					sgm.setCode(ManagedErrorCode.SYS_ERROR);
//				}
//			} else {
//				sgm.setCode(ManagedErrorCode.GANG_NOT_EXIST);
//			}
//		} catch (Exception e) {
//			logger.error("查询帮会信息", e);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_GetSimplePlayerInfoByAccount getPlayerSimpleInfoByAccount(TSession session,
//			GM_GetSimplePlayerInfoByAccount req) {
//		String message = MessageFormatter.format("管理后台[{}]查询用户简单信息", session.getInetIp()).getMessage();
//		logger.error(message);
//		Player player = playerManager.getPlayerByAccount(req.getAccount(), req.getOp(), req.getServerId());
//		SGM_GetSimplePlayerInfoByAccount res = new SGM_GetSimplePlayerInfoByAccount();
//		if (player != null) {
//			res.setPlayerSimpleInfo(player.createSimple());
//		}
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_GetSimplePlayerInfoByAccountJson getPlayerSimpleInfoByAccountJson(TSession session,
//			GM_GetSimplePlayerInfoByAccountJson req) {
//		String message = MessageFormatter.format("管理后台[{}]查询用户简单信息", session.getInetIp()).getMessage();
//		logger.error(message);
//		Player player = playerManager.getPlayerByAccount(req.getAccount(), req.getOp(), req.getServerId());
//		SGM_GetSimplePlayerInfoByAccountJson res = new SGM_GetSimplePlayerInfoByAccountJson();
//		if (player != null) {
//			res.setPlayerSimpleInfoJson(JsonUtils.object2String(player.createSimple()));
//		}
//		return res;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_GetServerInfo getServerInfo(TSession session, GM_GetServerInfo req) {
//		String message = MessageFormatter.format("管理后台[{}]查询服务器详细信息", session.getInetIp()).getMessage();
//		logger.error(message);
//		ServerStat res = new ServerStat();
//		res.setAllowIp(firewallFilter.getAllowList());
//		res.setBlockIp(firewallFilter.getBlockList());
//		res.setBlockAll(firewallFilter.isBlockAll());
//		res.setManagedIp(managementFilter.getList());
//		res.setMaxConnectionSize(firewallFilter.getMaxClients());
//		res.setOnLine(SessionManager.getInstance().getOnlineSize());
//		res.setTotalMemory(Runtime.getRuntime().totalMemory());
//		res.setFreeMemory(Runtime.getRuntime().freeMemory());
//		res.setUseMemory(res.getTotalMemory() - res.getFreeMemory());
//		Date openDate = ServerState.getInstance().getOpenServerDate();
//		res.setOpenDate(openDate == null ? -1 : openDate.getTime());
//
//		// 通讯层基本数据信息
//		com.xiaosan.socket.monitor.Stat stat = MonitorActionDispatcher.getStat();
//		res.setStartTime(stat.getStartTime());
//		res.setNetConnectionSize(stat.getConnectionSize().get());
//		res.setNetCreateTimes(stat.getCreateTimes().get());
//		res.setNetCloseTimes(stat.getCloseTimes().get());
//		res.setNetReceivePackets(stat.getReceivePackets().get());
//		res.setNetReceiveLengths(stat.getReceiveLengths().get());
//		res.setNetSendPackets(stat.getSendPackets().get());
//		res.setNetSendLengths(stat.getSendLengths().get());
//
//		// 通讯层接收消息信息
//		ConcurrentMap<String, ReceivePacketStat> receiveMap = stat.getReceivePacketMap();
//		Map<String, StatNetReceiveBean> netReceiveMap = new HashMap<String, StatNetReceiveBean>();
//		for (Entry<String, ReceivePacketStat> entry : receiveMap.entrySet()) {
//			String key = entry.getKey();
//			ReceivePacketStat value = entry.getValue();
//			StatNetReceiveBean bean = new StatNetReceiveBean();
//			bean.setKey(key);
//			bean.setTotalTimes(value.getTotalTimes().get() / 1000000);
//			bean.setOverTimes(value.getOverTimes().get());
//			bean.setPacketTimes(value.getPacketTimes().get());
//			bean.setPacketLengths(value.getPacketLengths().get());
//			bean.setDecodeTimes(value.getDecodeTimes().get() / 1000000);
//			netReceiveMap.put(key, bean);
//		}
//		res.setNetReceiveMap(netReceiveMap);
//
//		// 通讯层发送消息信息
//		ConcurrentMap<String, SendPacketStat> sendMap = stat.getSendPacketMap();
//		Map<String, StatNetSendBean> netSendMap = new HashMap<String, StatNetSendBean>();
//		for (Entry<String, SendPacketStat> entry : sendMap.entrySet()) {
//			String key = entry.getKey();
//			SendPacketStat value = entry.getValue();
//			StatNetSendBean bean = new StatNetSendBean();
//			bean.setKey(key);
//			bean.setPacketTimes(value.getPacketTimes().get());
//			bean.setPacketLengths(value.getPacketLengths().get());
//			bean.setEncodeTimes(value.getEncodeTimes().get() / 1000000);
//			netSendMap.put(key, bean);
//		}
//		res.setNetSendMap(netSendMap);
//
//		// 业务处理信息
//		com.mmorpg.mir.jmx.Stat tStat = JourMBean.getStat();
//		res.setTaskTotalPackets(tStat.getTotalPackets().get());
//		res.setTaskTotalTimes(tStat.getTotalTimes().get() / 1000000);
//
//		ConcurrentMap<String, TaskStat> map = tStat.getPacketMap();
//		Map<String, StatTaskBean> taskMap = new HashMap<String, StatTaskBean>();
//		for (Entry<String, TaskStat> entry : map.entrySet()) {
//			String key = entry.getKey();
//			TaskStat value = entry.getValue();
//			StatTaskBean bean = new StatTaskBean();
//			bean.setKey(key);
//			bean.setPacketTimes(value.getPacketTimes().get());
//			bean.setTotalTimes(value.getTotalTimes().get() / 1000000);
//			bean.setOverTimes(value.getOverTimes().get());
//			taskMap.put(key, bean);
//		}
//		res.setTaskMap(taskMap);
//
//		// 事件处理信息
//		com.my9yu.common.event.jmx.Stat eStat = MonitorEventBusManager.getStat();
//		res.setEventTotalPackets(eStat.getTotalPackets().get());
//		res.setEventTotalTimes(eStat.getTotalTimes().get() / 1000000);
//
//		ConcurrentMap<Class<? extends IEvent>, EventStat> eMap = eStat.getEventStatMap();
//		Map<String, StatEventBean> eventMap = new HashMap<String, StatEventBean>();
//		for (Entry<Class<? extends IEvent>, EventStat> entry : eMap.entrySet()) {
//			String key = entry.getKey().getSimpleName();
//			EventStat value = entry.getValue();
//			StatEventBean bean = new StatEventBean();
//			bean.setKey(key);
//			bean.setEventOverTimes(value.getEventOverTimes().get());
//			bean.setEventTimes(value.getEventTimes().get());
//			bean.setEventTotalTime(value.getEventTotalTime().get() / 1000000);
//			eventMap.put(key, bean);
//		}
//		res.setEventMap(eventMap);
//
//		SGM_GetServerInfo info = new SGM_GetServerInfo();
//
//		String json = JsonUtils.object2String(res);
//		info.setStat(ZlibUtils.zip(json));
//		return info;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_AddWhiteIp addWhiteIp(TSession session, GM_AddWhiteIp req) {
//		String message = MessageFormatter.format("管理后台[{}]添加白名单[{}]",
//				new Object[] { session.getInetIp(), req.getIp().trim() }).getMessage();
//		logger.error(message);
//		firewallFilter.allow(req.getIp());
//		SGM_AddWhiteIp res = new SGM_AddWhiteIp();
//		return res;
//	}
//
//	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_QiHu360Open openQiHu360(TSession session, GM_QiHu360Open req) {
//		String message = MessageFormatter.format("管理后台[{}]开启奇虎特权活动。开始时间[{}]结束时间[{}]",
//				new Object[] { session.getInetIp(), req.getStartTime(), req.getEndTime() }).getMessage();
//		logger.error(message);
//		SGM_QiHu360Open sgm = new SGM_QiHu360Open();
//		try {
//			if (StringUtils.isEmpty(req.getStartTime()) || StringUtils.isEmpty(req.getEndTime())) {
//				throw new ManagedException(ManagedErrorCode.QIHU_PRIVILEGE_TIME_NULL);
//			}
//			Date startTime = format.parse(req.getStartTime());
//			Date endTime = format.parse(req.getEndTime());
//			if (startTime.getTime() > endTime.getTime()) {
//				throw new ManagedException(ManagedErrorCode.QIHU_PRIVILEGE_START_TIME_THAN_END);
//			}
//			serverState.getQiHu360PrivilegeServer().refresh(startTime, endTime);
//			operatorManager.notifyAllPalyerQiHu360PrivilegeServerChanged();
//		} catch (ManagedException e) {
//			sgm.setCode(e.getCode());
//		} catch (Exception e) {
//			logger.error("奇虎350设置特权错误！", e);
//			sgm.setCode(ManagedErrorCode.SYS_ERROR);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Get_QiHu360Open getQiHu360(TSession session, GM_Get_QiHu360Open req) {
//		String message = MessageFormatter.format("管理后台[{}]获取奇虎特权活动时间。", new Object[] { session.getInetIp() })
//				.getMessage();
//		logger.error(message);
//		SGM_Get_QiHu360Open sgm = new SGM_Get_QiHu360Open();
//		try {
//			Date startTime = serverState.getQiHu360PrivilegeServer().getstartTime();
//			Date endTime = serverState.getQiHu360PrivilegeServer().getEndTime();
//			sgm.setStartTime(format.format(startTime));
//			sgm.setEndTime(format.format(endTime));
//		} catch (Exception e) {
//			logger.error("奇虎350设置特权错误！", e);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Celebrate_Set_ActivityOpen openActivity(TSession session, GM_Celebrate_Set_ActivityOpen req) {
//		String message = MessageFormatter.format("管理后台[{}]设置活动类型[{}]开始时间[{}]结束时间[{}]。",
//				new Object[] { session.getInetIp(), req.getType(), req.getBeginTime(), req.getEndTime() }).getMessage();
//		logger.error(message);
//		SGM_Celebrate_Set_ActivityOpen sgm = new SGM_Celebrate_Set_ActivityOpen();
//		try {
//			/*
//			 * if (req.getBeginTime() < System.currentTimeMillis()) { throw new
//			 * ManagedException(ManagedErrorCode.ERROR_MSG); }
//			 */
//			if (req.getEndTime() < System.currentTimeMillis()) {
//				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
//			}
//			if (req.getBeginTime() > req.getEndTime()) {
//				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
//			}
//			ActivityEnum type = ActivityEnum.typeOf(req.getType());
//			Map<Integer, ActivityInfo> activityInfos = ServerState.getInstance().getCelebrateActivityInfos();
//			ActivityInfo activity = null;
//			if (!activityInfos.containsKey(req.getType())) {
//				activity = ActivityInfo.valueOf(req.getBeginTime(), req.getEndTime());
//				activityInfos.put(type.getValue(), activity);
//			} else {
//				activity = activityInfos.get(type.getValue());
//				if (activity.isOpenning()) {
//					throw new ManagedException(ManagedErrorCode.ACTIVITY_OPENNING);
//				}
//				activity.reset(req.getBeginTime(), req.getEndTime());
//			}
//			for (Long pid : SessionManager.getInstance().getOnlineIdentities()) {
//				Player player = PlayerManager.getInstance().getPlayer(pid);
//				PacketSendUtility.sendPacket(player, SM_Celebrate_Activity_Change.valueOf(type, activity));
//			}
//			sgm = SGM_Celebrate_Set_ActivityOpen.valueOf(type.getValue(), req.getBeginTime(), req.getEndTime());
//		} catch (ManagedException e) {
//			sgm.setCode(e.getCode());
//		} catch (Exception e) {
//			logger.error("设置活动开启错误", e);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Celebrate_Query_ActivityState queryCelebrateStates(TSession session,
//			GM_Celebrate_Query_ActivityStates req) {
//		String message = MessageFormatter.format("管理后台[{}]查询战国庆典活动状态", new Object[] { session.getInetIp() })
//				.getMessage();
//		logger.error(message);
//		SGM_Celebrate_Query_ActivityState sgm = new SGM_Celebrate_Query_ActivityState();
//		try {
//			sgm = SGM_Celebrate_Query_ActivityState.valueOf();
//		} catch (ManagedException e) {
//			sgm.setCode(e.getCode());
//		} catch (Exception e) {
//			logger.error("查询战国庆典活动状态错误", e);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Get_QiHu360SpeedOpen getQiHu360Speed(TSession session, GM_Get_QiHu360SpeedOpen req) {
//		String message = MessageFormatter.format("管理后台[{}]获取奇虎特权活动时间。", new Object[] { session.getInetIp() })
//				.getMessage();
//		logger.error(message);
//		SGM_Get_QiHu360SpeedOpen sgm = new SGM_Get_QiHu360SpeedOpen();
//		try {
//			Date startTime = serverState.getQiHu360SpeedPrivilegeServer().getStartTime();
//			Date endTime = serverState.getQiHu360SpeedPrivilegeServer().getEndTime();
//			sgm.setStartTime(format.format(startTime));
//			sgm.setEndTime(format.format(endTime));
//		} catch (Exception e) {
//			logger.error("奇虎350设置特权错误！", e);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_QiHu360_Privilege_Open openQiHu360_Privilge(TSession session, GM_QiHu360_Privilege_Open req) {
//		String message = MessageFormatter.format("管理后台[{}]开启奇虎特权活动。开始时间[{}]结束时间[{}]",
//				new Object[] { session.getInetIp(), req.getStartTime(), req.getEndTime() }).getMessage();
//		logger.error(message);
//		SGM_QiHu360_Privilege_Open sgm = new SGM_QiHu360_Privilege_Open();
//		try {
//			if (StringUtils.isEmpty(req.getStartTime()) || StringUtils.isEmpty(req.getEndTime())) {
//				throw new ManagedException(ManagedErrorCode.QIHU_PRIVILEGE_TIME_NULL);
//			}
//			Date startTime = format.parse(req.getStartTime());
//			Date endTime = format.parse(req.getEndTime());
//			if (startTime.getTime() > endTime.getTime()) {
//				throw new ManagedException(ManagedErrorCode.QIHU_PRIVILEGE_START_TIME_THAN_END);
//			}
//			serverState.getQiHu360SpeedPrivilegeServer().refresh(startTime, endTime);
//			operatorManager.notifyAllPalyerQiHu360SpeedPrivilegeServerChanged();
//		} catch (ManagedException e) {
//			sgm.setCode(e.getCode());
//		} catch (Exception e) {
//			logger.error("奇虎360设置特权错误！", e);
//			sgm.setCode(ManagedErrorCode.SYS_ERROR);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_BlackShop_Set_Open openBlackShop(TSession session, GM_BlackShop_Set_Open req) {
//		SGM_BlackShop_Set_Open sgm = new SGM_BlackShop_Set_Open();
//		try {
//			long now = System.currentTimeMillis();
//			if (req.getEndTime() < now) {
//				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
//			}
//			if (req.getBeginTime() >= req.getEndTime()) {
//				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
//			}
//
//			BlackShopServer blackShopServer = ServerState.getInstance().getBlackShopServer();
//			if (blackShopServer.isActivityOpen()) {
//				throw new ManagedException(ManagedErrorCode.BLACKSHOP_ACTIVITY_HAS_OPEN);
//			}
//			if (!BlackShopConfig.getInstance().isExistGoodGroupId(req.getGoodGroupId())) {
//				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
//			}
//			blackShopServer.fix(req.getGoodGroupId(), req.getBeginTime(), req.getEndTime());
//		} catch (ManagedException e) {
//			sgm.setCode(e.getCode());
//		} catch (Exception e) {
//			logger.error("设置黑市开启错误！", e);
//			sgm.setCode(ManagedErrorCode.SYS_ERROR);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_BlackShop_Close closeBlackShop(TSession session, GM_BlackShop_Close req) {
//		SGM_BlackShop_Close sgm = new SGM_BlackShop_Close();
//		try {
//			BlackShopServer blackShopServer = ServerState.getInstance().getBlackShopServer();
//			if (!blackShopServer.isActivityOpen()) {
//				throw new ManagedException(ManagedErrorCode.BLACKSHOP_ACTIVITY_NOT_OPEN);
//			}
//
//			blackShopServer.close();
//		} catch (ManagedException e) {
//			sgm.setCode(e.getCode());
//		} catch (Exception e) {
//			logger.error("关闭黑市错误！", e);
//			sgm.setCode(ManagedErrorCode.SYS_ERROR);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_BlackShop_Query queryBlackShop(TSession session, GM_BlackShop_Query req) {
//		SGM_BlackShop_Query sgm = new SGM_BlackShop_Query();
//		try {
//			BlackShopServer blackShopServer = ServerState.getInstance().getBlackShopServer();
//			sgm = SGM_BlackShop_Query.valueOf(blackShopServer);
//		} catch (ManagedException e) {
//			sgm.setCode(e.getCode());
//		} catch (Exception e) {
//			logger.error("查询黑市错误！", e);
//			sgm.setCode(ManagedErrorCode.SYS_ERROR);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Identify_Treasure_Open openIdentifyTreasure(TSession session, GM_Identify_Treasure_Open req) {
//		String message = MessageFormatter.format("管理后台[{}]开启[{}]鉴宝活动。开始时间[{}]结束时间[{}]",
//				new Object[] { session.getInetIp(), req.getActiveName(), req.getStartTime(), req.getEndTime() })
//				.getMessage();
//		logger.error(message);
//		SGM_Identify_Treasure_Open sgm = SGM_Identify_Treasure_Open.valueOf(req);
//		try {
//			CommonIdentifyTreasureTotalServers treasureTotalServers = ServerState.getInstance()
//					.getCommonIdentifyTreasureTotalServers();
//			CommonIdentifyTreasureServer treasureServer = treasureTotalServers.getTreasureServerByActiveName(req
//					.getActiveName());
//			// 不存在对应的鉴宝活动， 那么就直接开启
//			if (treasureServer == null) {
//				treasureServer = CommonIdentifyTreasureServer.valueOf(req.getActiveName());
//				treasureTotalServers.addTreasureServer(req.getActiveName(), treasureServer);
//				treasureServer.refresh(req.getActiveName(), req.getStartTime(), req.getEndTime());
//				commonActivityManager.notifyAllPalyerCommonIdentifyTreasureServerChange(req.getActiveName());
//			} else {
//				if (treasureServer.isOpeningIdentifyTreasure()) {
//					throw new ManagedException(ManagedErrorCode.COMMON_IDENTITY_TREASURE_IS_OPENING);
//				}
//				treasureServer.refresh(req.getActiveName(), req.getStartTime(), req.getEndTime());
//				commonActivityManager.notifyAllPalyerCommonIdentifyTreasureServerChange(req.getActiveName());
//			}
//		} catch (ManagedException e) {
//			sgm.setCode(e.getCode());
//		} catch (Exception e) {
//			logger.error("鉴宝活动开启错误！", e);
//			sgm.setCode(ManagedErrorCode.SYS_ERROR);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Identify_Treasure_Close closeIdentifyTreasure(TSession session, GM_Identify_Treasure_Close req) {
//		logger.error("管理后台关闭鉴宝活动.");
//		SGM_Identify_Treasure_Close sgm = SGM_Identify_Treasure_Close.valueOf(req.getActiveName());
//		try {
//			CommonIdentifyTreasureTotalServers treasureTotalServers = ServerState.getInstance()
//					.getCommonIdentifyTreasureTotalServers();
//			CommonIdentifyTreasureServer treasureServer = treasureTotalServers.getTreasureServerByActiveName(req
//					.getActiveName());
//			if (treasureServer == null) {
//				throw new ManagedException(ManagedErrorCode.COMMON_IDENTITY_TREASURE_NOT_EXIST);
//			} else {
//				if (!treasureServer.isOpeningIdentifyTreasure()) {
//					treasureServer.refresh(null, 0L, 0L);
//					throw new ManagedException(ManagedErrorCode.COMMON_IDENTITY_TREASURE_NOT_OPENING);
//				} else {
//					treasureServer.refresh(null, 0L, 0L);
//					commonActivityManager.notifyAllPalyerCommonIdentifyTreasureServerChange(req.getActiveName());
//				}
//			}
//		} catch (ManagedException e) {
//			sgm.setCode(e.getCode());
//		} catch (Exception e) {
//			logger.error("鉴宝活动关闭错误！", e);
//			sgm.setCode(ManagedErrorCode.SYS_ERROR);
//		}
//		return sgm;
//	}
//
//	@InSessionAnno("ATT_MANAGEMENT")
//	@HandlerAnno
//	public SGM_Identify_Treasure_Query queryIdentifyTreasure(TSession session, GM_Identify_Treasure_Query req) {
//		logger.error("管理查看鉴宝活动.");
//
//		SGM_Identify_Treasure_Query sgm = new SGM_Identify_Treasure_Query();
//		try {
//			CommonIdentifyTreasureTotalServers treasureTotalServers = ServerState.getInstance()
//					.getCommonIdentifyTreasureTotalServers();
//			CommonIdentifyTreasureServer treasureServer = treasureTotalServers.getTreasureServerByActiveName(req
//					.getActiveName());
//			if (treasureServer == null) {
//				throw new ManagedException(ManagedErrorCode.COMMON_IDENTITY_TREASURE_NOT_EXIST);
//			} else {
//				sgm = SGM_Identify_Treasure_Query.valueOf(req.getActiveName(), treasureServer);
//			}
//		} catch (ManagedException e) {
//			sgm.setCode(e.getCode());
//		} catch (Exception e) {
//			logger.error("鉴宝活动查询错误！", e);
//			sgm.setCode(ManagedErrorCode.SYS_ERROR);
//		}
//		return sgm;
//	}
//}
