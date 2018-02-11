package com.mmorpg.mir.model.country.model;

import java.util.List;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.spawn.SpawnManager;

/**
 * 太庙
 * 
 * @author Kuang Hao
 * @since v1.0 2014-10-20
 * 
 */
public class Temple {

	private Country country;
	/** 砖块 */
	private int brick;
	/** 映射到的地图表现 */
	@Transient
	private StatusNpc statusNpc;
	/** 被搬走的砖块 */
	private List<Long> takedBricks = New.arrayList();

	@JsonIgnore
	public void initStatusNpc(SpawnManager spawnManager, ConfigValueManager configValueManager) {
		String spawnId = ConfigValueManager.getInstance().COUNTRY_TEMPLE_LOCAL.getValue().get(country.getId().getValue() + "");
		statusNpc = (StatusNpc) spawnManager.spawnObject(spawnId, 1);
		brick = ConfigValueManager.getInstance().COUNTRY_TEMPLE_INIT_BRICK.getValue();
		this.addBrick(takedBricks.size(), configValueManager.COUNTRY_TEMPLE_MAX_BRICK.getValue());
		takedBricks.clear();
	}

	/**
	 * 添加砖块
	 * 
	 * @param max
	 * @return
	 */
	@JsonIgnore
	public int addBrick(int max) {
		return addBrick(1, max);
	}

	@JsonIgnore
	public int addBrick(int count, int max) {
		brick += count;
		if (brick > max) {
			brick = max;
		}
		if (statusNpc.getStatus() != brick) {
			statusNpc.setStatus(brick);
		}
		return brick;
	}

	@JsonIgnore
	public int reduceBrick() {
		brick--;
		if (brick < 1) {
			brick = 1;
		}
		if (statusNpc.getStatus() != brick) {
			statusNpc.setStatus(brick);
		}
		return brick;
	}

	public int getBrick() {
		return brick;
	}

	public void setBrick(int brick) {
		this.brick = brick;
	}

	@JsonIgnore
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public List<Long> getTakedBricks() {
		return takedBricks;
	}

	public void setTakedBricks(List<Long> takedBricks) {
		this.takedBricks = takedBricks;
	}

	@JsonIgnore
	public StatusNpc getStatusNpc() {
		return statusNpc;
	}
}
