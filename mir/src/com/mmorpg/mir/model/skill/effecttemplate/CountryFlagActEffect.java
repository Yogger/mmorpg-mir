package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.CountryFlag;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.model.countryact.CountryFlagQuestType;
import com.mmorpg.mir.model.country.model.countryact.HiddenMissionType;
import com.mmorpg.mir.model.country.packet.SM_Attend_Reward;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.utility.New;

public class CountryFlagActEffect extends EffectTemplate{

	protected int checktime;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		this.checktime = resource.getChecktime();
	}
	
	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}

	@Override
	public void onPeriodicAction(Effect effect) {
		Creature effected = effect.getEffected();
		if (effected.getObjectType() != ObjectType.COUNTRY_NPC) {
			return;
		}
		CountryNpc flagNpc = (CountryNpc) effected;
		CountryFlag flag = CountryManager.getInstance().getCountries().get(CountryId.valueOf(flagNpc.getCountryValue())).getCountryFlag();
		int width = ConfigValueManager.getInstance().ATTEND_REWARD_SCOPE_WIDTH.getValue();
		int height = ConfigValueManager.getInstance().ATTEND_REWARD_SCOPE_HEIGHT.getValue();

		boolean hasDefend = flag.getFlagQuestType() == CountryFlagQuestType.DEFENCE;
		Iterator<Player> ite = World.getInstance().getWorldMap(flagNpc.getMapId()).getInstances().get(flagNpc.getInstanceId()).playerIterator();
		while (ite.hasNext()) {
			Player player = ite.next();
			boolean isInRange = MathUtil.isInRange(flagNpc, player, width, height); 
			boolean notDead = !player.getLifeStats().isAlreadyDead();
			boolean openModule = ConfigValueManager.getInstance().filterFlagAttend(player);
			boolean notFinished = !player.getPlayerCountryHistory().hiddenMissionFinished(HiddenMissionType.DEFEND_FLAG);
			if (isInRange && notDead && openModule) {
				String chooserRewardId = null;
				if (notFinished && hasDefend && player.getCountryValue() == flagNpc.getCountryValue()) {
					chooserRewardId = ConfigValueManager.getInstance().DEFEND_ATTEND_REWARDID.getValue();
				} else if (notFinished && player.getCountry().getCountryFlag().getTarget() == flagNpc.getCountryValue()
						&& player.getCountryValue() != flagNpc.getCountryValue()) {
					chooserRewardId = ConfigValueManager.getInstance().ATTACK_ATTEND_REWARDID.getValue();
				}
				Reward reward = Reward.valueOf();
				if (chooserRewardId != null) {
					Map<String, Object> params = New.hashMap();
					params.put("LEVEL", player.getLevel());
					params.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player));
					params.put("STANDARD_HONOR", PlayerManager.getInstance().getStandardHonor(player));
					List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player, chooserRewardId);
					reward.addReward(RewardManager.getInstance().creatReward(player, rewardIds, params));
					RewardManager.getInstance().grantReward(player, reward, 
							ModuleInfo.valueOf(ModuleType.FLAG_QUEST, SubModuleType.FLAG_QUEST_ATTEND));
				}
				Reward result = flag.logAttendReward(player, reward);
				if (!result.isEmpty()) {
					PacketSendUtility.sendPacket(player, SM_Attend_Reward.valueOf(result));
				}
			} else if (isInRange && openModule && notFinished) {
				PacketSendUtility.sendPacket(player, SM_Attend_Reward.valueOf(flag.getAttendRewardStatus(player)));
			}
		}
	}
	
	@Override
	public void startEffect(final Effect effect) {
		Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable() {
			@Override
			public void run() {
				onPeriodicAction(effect);
			}
		}, 0, checktime);
		effect.addEffectTask(task);
	}
}
