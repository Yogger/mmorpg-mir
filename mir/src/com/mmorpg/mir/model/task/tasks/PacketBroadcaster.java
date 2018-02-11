package com.mmorpg.mir.model.task.tasks;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.controllers.packet.SM_ATTACK_STATUS;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.PlayerGameStats;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWarManager;
import com.mmorpg.mir.model.gangofwar.model.PlayerGangWarInfo;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.kingofwar.model.PlayerWarInfo;
import com.mmorpg.mir.model.purse.packet.SM_Currency;
import com.mmorpg.mir.model.task.taskmanager.AbstractFIFOPeriodicTaskManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;

@Component
public final class PacketBroadcaster extends AbstractFIFOPeriodicTaskManager<Creature> {

	private static final Logger logger = Logger.getLogger(BroadcastMode.class);
	private static PacketBroadcaster self;

	public static PacketBroadcaster getInstance() {
		return self;
	}

	private PacketBroadcaster() {
		super(500);
	}

	@PostConstruct
	public void init() {
		super.init();
		self = this;
	}

	public static enum BroadcastMode {

		/** 通知货币变化 **/
		UPDATE_PLAYER_PURSE {
			@Override
			public void sendPacket(Creature creature) {
				Player player = (Player) creature;
				PacketSendUtility.sendPacket(player, SM_Currency.valueOf(player));
			}
		},
		/** 通知伤害结果 **/
		BORADCAST_DAMAGE_STAT {
			@Override
			public void sendPacket(Creature creature) {
				PacketSendUtility.broadcastPacket(creature, SM_ATTACK_STATUS.valueOf(creature));
			}
		},
		/** 更新hp状态 **/
		UPDATE_PLAYER_HP_STAT {
			@Override
			public void sendPacket(Creature creature) {
				((Player) creature).getLifeStats().sendHpPacketUpdateImpl();
			}
		},
		/** 更新mp状态 **/
		UPDATE_PLAYER_MP_STAT {
			@Override
			public void sendPacket(Creature creature) {
				((Player) creature).getLifeStats().sendMpPacketUpdateImpl();
			}
		},
		UPDATE_PLAYER_DP_STAT {
			@Override
			public void sendPacket(Creature creature) {
				((Player) creature).getLifeStats().sendDpPacketUpdateImpl();
			}
		},
		UPDATE_PLAYER_BARRIER_STAT {
			@Override
			public void sendPacket(Creature creature) {
				creature.getLifeStats().sendBarrierPacketUpdateImpl();
			}
		},
		UPDATE_PLAYER_RP_STAT {
			@Override
			public void sendPacket(Creature creature) {
				((Player) creature).getRp().sendRpPacketUpdateImpl();
			}
		},
		BROAD_CAST_EFFECTS {
			@Override
			public void sendPacket(Creature creature) {
				creature.getEffectController().broadCastEffectsImp();
			}
		},
		SEND_KINGOFWAR_INFO {
			@Override
			public void sendPacket(Creature creature) {
				if (KingOfWarManager.getInstance().isWarring()) {
					PlayerWarInfo playerWarInfo = KingOfWarManager.getInstance().getPlayerWarInfos()
							.get(creature.getObjectId());
					if (playerWarInfo != null) {
						playerWarInfo.sendUpdateImp();
					}
				}
			}
		},
		SEND_GANGOFWAR_INFO {
			@Override
			public void sendPacket(Creature creature) {
				Player player = (Player) creature;
				if (GangOfWarManager.getInstance().getGangOfwars().get(player.getCountryId()).isWarring()) {
					PlayerGangWarInfo playerWarInfo = GangOfWarManager.getInstance().getGangOfwars()
							.get(player.getCountryId()).getPlayerMap().get(creature.getObjectId());
					if (playerWarInfo != null) {
						playerWarInfo.sendUpdateImp();
					}
				}
			}
		},
		SEND_BATTLE_SCORE {
			@Override
			public void sendPacket(Creature creature) {
				PlayerGameStats playerGameStats = (PlayerGameStats) ((Player) creature).getGameStats();
				playerGameStats.sendBattleScoreImp();
			}
		};

		private final int MASK;

		private BroadcastMode() {
			MASK = (int) (1 << ordinal());
		}

		public int mask() {
			return MASK;
		}

		protected abstract void sendPacket(Creature creature);

		protected final void trySendPacket(final Creature creature, int mask) {
			if ((mask & mask()) == mask()) {
				try {
					sendPacket(creature);
				} catch (Exception e) {
					logger.error("推送队列报错", e);
				}
				creature.removePacketBroadcastMask(this);
			}
		}
	}

	private static final BroadcastMode[] VALUES = BroadcastMode.values();

	@Override
	protected void callTask(Creature creature) {
		for (int mask; (mask = creature.getPacketBroadcastMask()) != 0;) {
			for (BroadcastMode mode : VALUES) {
				mode.trySendPacket(creature, mask);
			}
		}
	}

	@Override
	protected String getCalledMethodName() {
		return "packetBroadcast()";
	}
}
