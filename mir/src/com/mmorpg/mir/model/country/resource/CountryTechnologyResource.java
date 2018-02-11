package com.mmorpg.mir.model.country.resource;

import java.util.Comparator;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CountryTechnologyResource {
	/** 科技等级 */
	@Id
	private int grade;

	/** 升级到该等级需要的建设值 */
	private int buildValue;

	private String mailTitleIl18n;

	private String mailContentIl18n;

	private String rewardId;

	public static class GradeComparator implements Comparator<CountryTechnologyResource> {

		@Override
		public int compare(CountryTechnologyResource o1, CountryTechnologyResource o2) {
			return o1.getGrade() - o2.getGrade();
		}
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getBuildValue() {
		return buildValue;
	}

	public void setBuildValue(int buildValue) {
		this.buildValue = buildValue;
	}

	public String getMailTitleIl18n() {
		return mailTitleIl18n;
	}

	public void setMailTitleIl18n(String mailTitleIl18n) {
		this.mailTitleIl18n = mailTitleIl18n;
	}

	public String getMailContentIl18n() {
		return mailContentIl18n;
	}

	public void setMailContentIl18n(String mailContentIl18n) {
		this.mailContentIl18n = mailContentIl18n;
	}

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

}
