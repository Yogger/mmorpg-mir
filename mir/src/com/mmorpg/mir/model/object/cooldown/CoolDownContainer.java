package com.mmorpg.mir.model.object.cooldown;

import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Transient;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

public final class CoolDownContainer {
	private NonBlockingHashMap<Integer, Long> skillCoolDowns;
	private NonBlockingHashMap<Integer, Long> publicCoolDowns;
	@Transient
	private GatherEntry gatherCoolDown;
	@Transient
	private BackHomeEntry backHomeEntry;
	@Transient
	private TempelBrickEntry templeBrickEntry;

	public boolean isSkillDisabled(int skillId) {
		if (skillCoolDowns == null)
			return false;

		Long coolDown = skillCoolDowns.get(skillId);
		if (coolDown == null)
			return false;

		if (coolDown <= System.currentTimeMillis()) {
			skillCoolDowns.remove(skillId);
			return false;
		}

		return true;
	}

	public boolean isPublicDisabled(int group) {
		if (publicCoolDowns == null)
			return false;

		Long coolDown = publicCoolDowns.get(group);
		if (coolDown == null)
			return false;

		if (coolDown <= System.currentTimeMillis()) {
			publicCoolDowns.remove(group);
			return false;
		}

		return true;
	}

	public long getSkillCoolDown(int skillId) {
		if (skillCoolDowns == null || !skillCoolDowns.containsKey(skillId))
			return 0;

		return skillCoolDowns.get(skillId);
	}

	public long getPublicCoolDown(int group) {
		if (skillCoolDowns == null || !publicCoolDowns.containsKey(group))
			return 0;

		return publicCoolDowns.get(group);
	}

	@JsonIgnore
	public void setGatherCoolDown(long objId, long time) {
		gatherCoolDown = new GatherEntry();
		gatherCoolDown.setKey(objId);
		gatherCoolDown.setObject(time);
	}

	@JsonIgnore
	public boolean isGatherDisable(long objId) {
		if (gatherCoolDown != null) {
			if (gatherCoolDown.getKey() == objId) {
				if (System.currentTimeMillis() >= gatherCoolDown.getObject()) {
					gatherCoolDown = null;
					return false;
				}
			}
		}
		return true;
	}

	@JsonIgnore
	public void setBackHomeSing(long endTime) {
		backHomeEntry = new BackHomeEntry();
		backHomeEntry.setEndTime(endTime);
	}

	@JsonIgnore
	public void setTempleBrick(long endTime, int country) {
		templeBrickEntry = new TempelBrickEntry();
		templeBrickEntry.setEndTime(endTime);
		templeBrickEntry.setCountry(country);
	}

	@JsonIgnore
	public boolean isBackHomeDisable() {
		if (backHomeEntry != null) {
			if (backHomeEntry.getEndTime() != 0L && backHomeEntry.getEndTime() <= System.currentTimeMillis()) {
				return false;
			}
		}
		return true;
	}

	@JsonIgnore
	public boolean isTempleBrickDisable(int country) {
		if (templeBrickEntry != null && templeBrickEntry.getCountry() == country) {
			if (templeBrickEntry.getEndTime() != 0L && templeBrickEntry.getEndTime() <= System.currentTimeMillis()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param skillId
	 * @param time
	 */
	public void setSkillCoolDown(int skillId, long time) {
		if (skillCoolDowns == null)
			skillCoolDowns = new NonBlockingHashMap<Integer, Long>();

		skillCoolDowns.put(skillId, time);
	}

	public void setPulibcCoolDown(int group, long time) {
		if (publicCoolDowns == null) {
			publicCoolDowns = new NonBlockingHashMap<Integer, Long>();
		}
		publicCoolDowns.put(group, time);
	}

	/**
	 * @return the skillCoolDowns
	 */
	public Map<Integer, Long> getSkillCoolDowns() {
		return skillCoolDowns;
	}

	public Map<Integer, Long> getPublicCoolDowns() {
		return publicCoolDowns;
	}
	
	@JsonIgnore
	public Map<Integer, Long> getSpecialSkillCD() {
		Map<Integer, Long> skillCd = New.hashMap();
		if (skillCoolDowns != null) {
			for (Entry<Integer, Long> entry: skillCoolDowns.entrySet()) {
				if (entry.getValue() > System.currentTimeMillis() + 4000L) {
					skillCd.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return skillCd;
	}

	/**
	 * 
	 * @param skillId
	 */
	public void removeSkillCoolDown(int skillId) {
		if (skillCoolDowns == null)
			return;
		skillCoolDowns.remove(skillId);
	}

	public void removePublicCoolDown(int group) {
		if (publicCoolDowns == null)
			return;
		publicCoolDowns.remove(group);
	}

	public void removeGatherCoolDown() {
		gatherCoolDown = null;
	}

	public void removeBackHome() {
		backHomeEntry = null;
	}

	public void removeTempleBrick() {
		templeBrickEntry = null;
	}

	@JsonIgnore
	public TempelBrickEntry getTempleBrickEntry() {
		return templeBrickEntry;
	}

	@JsonIgnore
	public void setTempleBrickEntry(TempelBrickEntry templeBrickEntry) {
		this.templeBrickEntry = templeBrickEntry;
	}

	private static class GatherEntry {
		private long key;
		private long Object;

		public long getKey() {
			return key;
		}

		public void setKey(long key) {
			this.key = key;
		}

		public long getObject() {
			return Object;
		}

		public void setObject(long object) {
			Object = object;
		}

	}

	private static class BackHomeEntry {
		private long endTime;

		public long getEndTime() {
			return endTime;
		}

		public void setEndTime(long endTime) {
			this.endTime = endTime;
		}

	}

	private static class TempelBrickEntry {
		private int country;
		private long endTime;

		public long getEndTime() {
			return endTime;
		}

		public void setEndTime(long endTime) {
			this.endTime = endTime;
		}

		public int getCountry() {
			return country;
		}

		public void setCountry(int country) {
			this.country = country;
		}

	}
}
