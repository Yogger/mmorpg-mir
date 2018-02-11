package com.mmorpg.mir.model.commonactivity.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.model.CoreActionResource;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CommonMarcoShopResource {
	public static final String ACTIVITY_NAME = "ACTIVITY_NAME";

	@Id
	private String id;
	@Index(name = ACTIVITY_NAME, unique = true)
	private String activityName;

	/** 开启条件 */
	private String[] openCondIds;

	/** 格子数 */
	private int gridCount;

	/** 系统刷新间隔（分钟） */
	private int intervalRefreshTime;

	/** 需要广播的道具品质 */
	private int noticeItemQuility;

	/** 消耗的份数 */
	private int actCount;

	/** 优先消耗的道具 */
	private CoreActionResource[] priItemAction;

	/** 元宝 */
	private CoreActionResource[] goldAction;

	private String tvIl18n;

	private int tvChannel;

	private String chatIl18n;

	private int chatChannel;

	@Transient
	private transient CoreConditions openConds;

	@JsonIgnore
	public CoreConditions getOpenConds() {
		if (openConds == null) {
			openConds = CoreConditionManager.getInstance().getCoreConditions(1, this.openCondIds);
		}
		return openConds;
	}

	public String getId() {
		return id;
	}

	public String[] getOpenCondIds() {
		return openCondIds;
	}

	public int getGridCount() {
		return gridCount;
	}

	public int getIntervalRefreshTime() {
		return intervalRefreshTime;
	}

	public int getNoticeItemQuility() {
		return noticeItemQuility;
	}

	public int getActCount() {
		return actCount;
	}

	public CoreActionResource[] getPriItemAction() {
		return priItemAction;
	}

	public CoreActionResource[] getGoldAction() {
		return goldAction;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public void setOpenCondIds(String[] openCondIds) {
		this.openCondIds = openCondIds;
	}

	public void setGridCount(int gridCount) {
		this.gridCount = gridCount;
	}

	public void setIntervalRefreshTime(int intervalRefreshTime) {
		this.intervalRefreshTime = intervalRefreshTime;
	}

	public void setNoticeItemQuility(int noticeItemQuility) {
		this.noticeItemQuility = noticeItemQuility;
	}

	public void setActCount(int actCount) {
		this.actCount = actCount;
	}

	public void setPriItemAction(CoreActionResource[] priItemAction) {
		this.priItemAction = priItemAction;
	}

	public void setGoldAction(CoreActionResource[] goldAction) {
		this.goldAction = goldAction;
	}

	public String getActivityName() {
		return activityName;
	}

	public String getTvIl18n() {
		return tvIl18n;
	}

	public String getChatIl18n() {
		return chatIl18n;
	}

	public void setTvIl18n(String tvIl18n) {
		this.tvIl18n = tvIl18n;
	}

	public void setChatIl18n(String chatIl18n) {
		this.chatIl18n = chatIl18n;
	}

	public int getTvChannel() {
		return tvChannel;
	}

	public int getChatChannel() {
		return chatChannel;
	}

	public void setTvChannel(int tvChannel) {
		this.tvChannel = tvChannel;
	}

	public void setChatChannel(int chatChannel) {
		this.chatChannel = chatChannel;
	}

}
