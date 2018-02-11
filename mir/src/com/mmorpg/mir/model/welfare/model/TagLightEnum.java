package com.mmorpg.mir.model.welfare.model;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.manager.ClawbackManager;
import com.mmorpg.mir.model.welfare.manager.OnlineManager;
import com.mmorpg.mir.model.welfare.manager.SignManager;
import com.mmorpg.mir.model.welfare.manager.WelfareConfigValueManager;
import com.mmorpg.mir.model.welfare.resource.ClawbackResource;
import com.mmorpg.mir.model.welfare.resource.OnlineResource;
import com.mmorpg.mir.model.welfare.resource.SignResource;
import com.windforce.common.utility.DateUtils;

public enum TagLightEnum {
	/** 签到标签有奖否 */
	SIGN_TAG_ID(1) {
	    @Override
	    public boolean getStatus(Player player) {
	    	return player.getWelfare().getSign().canSign(System.currentTimeMillis());
	    }
    },
	/** 离线经验有奖否 */
	OFFLINE_TAG_ID(2) {
	    @Override
	    public boolean getStatus(Player player) {
	    	return player.getWelfare().getOfflineExp().getOfflineCountSeconds() > 0;
	    }
    },
	/** 资源追回 */
	RESOURCE_CLAWBACK_TAG_ID(3) {
	    @Override
	    public boolean getStatus(Player player) {
	    	for (ClawbackResource resource : ClawbackManager.getInstance().clawbackStorage.getAll()) {
	    		ClawbackManager.getInstance().checkTimeOut(player, resource.getEventId());
				if (resource.getType() == ClawbackType.CLAWBACK_TYPE_VALUE.getType() &&
						ClawbackManager.getInstance().canClawback(player, resource)) {
					return true;
				}
			}
		    return false;
	    }
    },
	/** 次数追回 */
	CHANCE_CLAWBACK_TAG_ID(4) {
	    @Override
	    public boolean getStatus(Player player) {
	    	for (ClawbackResource resource : ClawbackManager.getInstance().clawbackStorage.getAll()) {
	    		ClawbackManager.getInstance().checkTimeOut(player, resource.getEventId());
				if (resource.getType() == ClawbackType.CLAWBACK_TYPE_NUM.getType() &&
						ClawbackManager.getInstance().canClawback(player, resource)) {
					return true;
				}
			}
		    return false;
	    }
    },
	/** 在线奖励 */
	ONLINE_REWARD_TAG_ID(5) {
	    @Override
	    public boolean getStatus(Player player) {
	    	long timesCount = player.getWelfare().getOnlineReward().refreshTime(player);
	    	for (OnlineResource res : OnlineManager.getInstance().onlineStorage.getAll()) {
				if (timesCount >= res.getOnlineTimeMinutes() * DateUtils.MILLIS_PER_MINUTE) {
					if (!player.getWelfare().getOnlineReward().isRewarded(res.getId())) {
						return true;
					}
				}
			}
		    return false;
	    }
    },
    /** 活跃度 */
    ACTIVE_TAG_ID(6) {
		@Override
		public boolean getStatus(Player player) {
			Integer[] values = WelfareConfigValueManager.getInstance().WELFARE_VITALITY_AWARD_NUM.getValue();
	    	for (int v : values) {
	    		if (player.getWelfare().getActiveValue().getActiveValue() >= v
	    				&& !player.getWelfare().getActiveValue().isRewarded(v)) {
	    			return true;
	    		}
	    	}
			return false;
		}
	},
	/** 七日登陆 */
	FIRST_SEVEN_LOGIN(7) {
		@Override
		public boolean getStatus(Player player) {
			for (int i = 1; i <= player.getWelfare().getSevenDayReward().getDayIndex(); i++) {
				Boolean recieved = player.getWelfare().getSevenDayReward().getSevendayDrawRecord().get(i);
				if (recieved == null || (recieved != null && !recieved)) {
					return true;
				}
			}
			return false;
		}
    },
    /** 签到是否有奖励 */
    SIGN_TAG_REWARD(8) {
		@Override
		public boolean getStatus(Player player) {
	    	/** 李廷炫通过杨斌传达的改动 **/
			int signNum = player.getWelfare().getSign().getTotalSignNum();
			for (SignResource signRes: SignManager.getInstance().getAllSignResources()) {
				if (signNum < signRes.getDays()) {
					continue;
				}
				boolean hasReward = signRes.getDefaultRewadChooserGroup() != null && signRes.getDefaultRewadChooserGroup().length() != 0;
				if (hasReward && !player.getWelfare().getSign().getRewardedList().contains(signRes.getDays())) {
					return true;
				}
			}
			return false;
		}
    };
    
	
    public static TagLightEnum valueOf(int v) { 
    	for (TagLightEnum e: TagLightEnum.values()) {
    		if (e.getValue() == v) {
    			return e;
    		}
    	}
    	
    	throw new ManagedException(ManagedErrorCode.ERROR_MSG); //前端传错了类型 
    }
    
    
	private final int value;
	
	TagLightEnum(int v) {
		this.value = v;
	}
	
	public int getValue() {
		return value;
	}
	
	public abstract boolean getStatus(Player player);
}
