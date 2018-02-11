package com.mmorpg.mir.model.commonactivity.resource;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CommonSPServerResource {
	public static final String SERVER_NAME_INDEX = "SERVER_NAME_INDEX";
	@Id
	private String id;

	@Index(name = SERVER_NAME_INDEX, unique = true)
	private String name;

	private String titleId;

	private String contentId;

	private String rewardChooserId;

	public String getId() {
		return id;
	}

	public String getTitleId() {
		return titleId;
	}

	public String getContentId() {
		return contentId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getRewardChooserId() {
		return rewardChooserId;
	}

	public void setRewardChooserId(String rewardChooserId) {
		this.rewardChooserId = rewardChooserId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
