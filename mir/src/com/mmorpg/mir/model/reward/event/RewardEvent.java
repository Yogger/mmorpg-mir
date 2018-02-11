package com.mmorpg.mir.model.reward.event;
//package com.wenbei.xysh.module.reward2.event;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.my9yu.common.event.Event;
//import com.wenbei.xysh.common.event.IdentityEvent;
//import com.wenbei.xysh.module.reward2.model.Reward;
//import com.wenbei.xysh.module.reward2.model.sample.ContextKey;
//
///**
// * 奖励发放事件
// * 
// * @author Kuang Hao
// * @since v1.0 2013-1-24
// * 
// */
//public class RewardEvent implements IdentityEvent {
//	/** 事件名称 */
//	public final static String EVENT_NAME = "reward:sendReward";
//	/** 所有者 */
//	private long owner;
//	/** 奖励的配置表id */
//	private String rewardSampleId;
//	/** 奖励 */
//	private Reward reward;
//	/** 自定义参数 */
//	private Map<ContextKey, Object> customs;
//	/** 模块号 */
//	private int module;
//
//	public static Event<RewardEvent> valueOf(long owner, String rewardSampleId, Reward reward, int module) {
//		RewardEvent event = new RewardEvent();
//		event.owner = owner;
//		event.rewardSampleId = rewardSampleId;
//		event.reward = reward;
//		event.module = module;
//		event.customs = new HashMap<ContextKey, Object>();
//		return Event.valueOf(EVENT_NAME, event);
//	}
//
//	@Override
//	public long getOwner() {
//		return owner;
//	}
//
//	@Override
//	public String getName() {
//		return EVENT_NAME;
//	}
//
//	public String getRewardSampleId() {
//		return rewardSampleId;
//	}
//
//	public Reward getReward() {
//		return reward;
//	}
//
//	public Map<ContextKey, Object> getCustoms() {
//		return customs;
//	}
//
//	public int getModule() {
//		return module;
//	}
//
//}
