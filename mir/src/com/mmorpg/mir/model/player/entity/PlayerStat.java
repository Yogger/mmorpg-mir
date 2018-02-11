package com.mmorpg.mir.model.player.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;

@Entity
@Cached(size = "thousand", persister = @Persister("30s"))
public class PlayerStat implements IEntity<Long> {

	/** uid */
	@Id
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private long guid;
	/** 创建ip **/
	private String createIp;
	/** 登录ip **/
	private String loginIp;
	/** 创建时间 */
	private Date createdOn;
	/** 最后登录时间 */
	private Date lastLogin;
	/** 连续登陆天数 */
	@Column(columnDefinition = "int default 0", nullable = false)
	private int continuousLogin;
	/** 上次刷新连续登陆天数的时间 */
	private Date refreshContinuousLoginTime;
	/** 今天连续在线时间 */
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private long onlineTime;
	/** 下线连续时间 */
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private long unlineTime;
	/** 上次刷新在线时间 */
	private Date lastRefreshOnlineTime;
	/** 上次登出时间 */
	private Date lastLogoutTime;
	/** 累积消费元宝 */
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private long accConsumeGold;
	/** 累积充值 */
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private long accRechargeGold;
	/** 最后充值时间 */
	private Date lastRechargeTime;
	/** 最后充值金币 */
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private long lastRechargeGold;
	/** 最高单笔充值时间 */
	private Date maxOnceRechargeTime;
	/** 最高单笔充值金币 */
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private long maxOnceRechargeGold;
	/** 累计登陆天数 */
	@Column(columnDefinition = "int default 0", nullable = false)
	private int accLoginNumber;
	/** 累计登陆天数时间标示 */
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private long lastAccLoginNumberTime;
	/** 当前元宝数量 */
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private long currentGold;
	/** 当前内币数量 */
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private long currentInter;

	/**
	 * 唯一的构造方法
	 * 
	 * @param guid
	 * @param name
	 * @param account
	 * @param op
	 * @param server
	 * @return
	 */
	public static PlayerStat valueOf(long guid) {
		PlayerStat ent = new PlayerStat();
		ent.guid = guid;
		Date now = new Date();
		ent.refreshContinuousLoginTime = now;
		ent.lastLogin = now;
		ent.createdOn = now;
		ent.lastLogoutTime = now;
		ent.lastRefreshOnlineTime = now;
		return ent;
	}

	@JsonIgnore
	public void campareAndSetmaxOnceRechargeGold(long gold) {
		if (gold > maxOnceRechargeGold) {
			maxOnceRechargeGold = gold;
			maxOnceRechargeTime = new Date();
		}
	}

	@JsonIgnore
	public void addAccRechargeGold(long gold) {
		accRechargeGold += gold;
		lastRechargeTime = new Date();
		lastRechargeGold = gold;
	}

	@JsonIgnore
	public void addAccConsumeGold(long gold) {
		accConsumeGold += gold;
	}

	public long getGuid() {
		return guid;
	}

	public void setGuid(long guid) {
		this.guid = guid;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public int getContinuousLogin() {
		return continuousLogin;
	}

	public void setContinuousLogin(int continuousLogin) {
		this.continuousLogin = continuousLogin;
	}

	public Date getRefreshContinuousLoginTime() {
		return refreshContinuousLoginTime;
	}

	public void setRefreshContinuousLoginTime(Date refreshContinuousLoginTime) {
		this.refreshContinuousLoginTime = refreshContinuousLoginTime;
	}

	public long getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(long onlineTime) {
		this.onlineTime = onlineTime;
	}

	public long getUnlineTime() {
		return unlineTime;
	}

	public void setUnlineTime(long unlineTime) {
		this.unlineTime = unlineTime;
	}

	public Date getLastRefreshOnlineTime() {
		return lastRefreshOnlineTime;
	}

	public void setLastRefreshOnlineTime(Date lastRefreshOnlineTime) {
		this.lastRefreshOnlineTime = lastRefreshOnlineTime;
	}

	public Date getLastLogoutTime() {
		return lastLogoutTime;
	}

	public void setLastLogoutTime(Date lastLogoutTime) {
		this.lastLogoutTime = lastLogoutTime;
	}

	public long getAccConsumeGold() {
		return accConsumeGold;
	}

	public void setAccConsumeGold(long accConsumeGold) {
		this.accConsumeGold = accConsumeGold;
	}

	public long getAccRechargeGold() {
		return accRechargeGold;
	}

	public void setAccRechargeGold(long accRechargeGold) {
		this.accRechargeGold = accRechargeGold;
	}

	public Date getLastRechargeTime() {
		return lastRechargeTime;
	}

	public void setLastRechargeTime(Date lastRechargeTime) {
		this.lastRechargeTime = lastRechargeTime;
	}

	public long getLastRechargeGold() {
		return lastRechargeGold;
	}

	public void setLastRechargeGold(long lastRechargeGold) {
		this.lastRechargeGold = lastRechargeGold;
	}

	public Date getMaxOnceRechargeTime() {
		return maxOnceRechargeTime;
	}

	public void setMaxOnceRechargeTime(Date maxOnceRechargeTime) {
		this.maxOnceRechargeTime = maxOnceRechargeTime;
	}

	public long getMaxOnceRechargeGold() {
		return maxOnceRechargeGold;
	}

	public void setMaxOnceRechargeGold(long maxOnceRechargeGold) {
		this.maxOnceRechargeGold = maxOnceRechargeGold;
	}

	@Override
	public Long getId() {
		return guid;
	}

	public int getAccLoginNumber() {
		return accLoginNumber;
	}

	public void setAccLoginNumber(int accLoginNumber) {
		this.accLoginNumber = accLoginNumber;
	}

	public long getLastAccLoginNumberTime() {
		return lastAccLoginNumberTime;
	}

	public void setLastAccLoginNumberTime(long lastAccLoginNumberTime) {
		this.lastAccLoginNumberTime = lastAccLoginNumberTime;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@Override
	public boolean serialize() {
		return true;
	}

	public long getCurrentGold() {
		return currentGold;
	}

	public void setCurrentGold(long currentGold) {
		this.currentGold = currentGold;
	}

	public long getCurrentInter() {
		return currentInter;
	}

	public void setCurrentInter(long currentInter) {
		this.currentInter = currentInter;
	}

}