package com.mmorpg.mir.model.reward.event;
//package com.wenbei.xysh.module.reward2.event;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.my9yu.common.event.AbstractReceiver;
//import com.wenbei.xysh.module.reward2.manager.RewardManager;
//
///**
// * 接受创建用户事件
// * 
// * @author Kuang Hao
// * @since v1.0 2012-2-22
// * 
// */
//@Component
//public class RewardReceiver extends AbstractReceiver<RewardEvent> {
//
//	@Autowired
//	private RewardManager rewardManager;
//
//	@Override
//	public void doEvent(RewardEvent event) {
//		if (event.getRewardSampleId() != null) {
//			rewardManager.grantReward(event.getOwner(), event.getRewardSampleId(), event.getModule(),
//					event.getCustoms());
//		}
//
//		if (event.getReward() != null) {
//			rewardManager.grantReward(event.getOwner(), event.getReward(), event.getModule());
//		}
//	}
//
//	@Override
//	public String[] getEventNames() {
//		return new String[] { RewardEvent.EVENT_NAME };
//	}
//
//}
