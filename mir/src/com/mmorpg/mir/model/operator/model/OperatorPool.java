package com.mmorpg.mir.model.operator.model;

public class OperatorPool {

	public static OperatorPool valueOf() {
		OperatorPool op = new OperatorPool();
		op.mobilePhone = MobilePhone.valueOf();
		op.operatorVip = OperatorVip.valueOf();
		op.subInformation = SubInformation.valueOf();
		op.gmPrivilege = GmPrivilege.valueOf();
		op.qiHuPrivilege = QiHu360PrivilegePlayer.valueOf();
		op.qiHuSpeedPrivilege = QiHu360SpeedPrivilegePlayer.valueOf();
		return op;
	}

	// PS:add valueOf(),setOwner
	private MobilePhone mobilePhone;

	private OperatorVip operatorVip;

	private SubInformation subInformation;

	private GmPrivilege gmPrivilege;
	/** 是否领取过微信礼包 */
	private boolean wechatGiftRewarded;
	/** 是否领取过360加速礼包 */
	private boolean qihuSpeedBallGiftRewarded;
	/** 运营商客户端存取数据 */
	private String operatorClientInfo;
	/** 360特权*/
	private QiHu360PrivilegePlayer qiHuPrivilege;
	/** 360加速特权*/
	private QiHu360SpeedPrivilegePlayer qiHuSpeedPrivilege;
	/** 2345王牌浏览器*/
	private boolean browser2345Rewarded;
	
	
	public MobilePhone getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(MobilePhone mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public OperatorVip getOperatorVip() {
		return operatorVip;
	}

	public void setOperatorVip(OperatorVip operatorVip) {
		this.operatorVip = operatorVip;
	}

	public SubInformation getSubInformation() {
		return subInformation;
	}

	public void setSubInformation(SubInformation subInformation) {
		this.subInformation = subInformation;
	}

	public GmPrivilege getGmPrivilege() {
		return gmPrivilege;
	}

	public void setGmPrivilege(GmPrivilege gmPrivilege) {
		this.gmPrivilege = gmPrivilege;
	}

	public boolean isWechatGiftRewarded() {
		return wechatGiftRewarded;
	}

	public void setWechatGiftRewarded(boolean wechatGiftRewarded) {
		this.wechatGiftRewarded = wechatGiftRewarded;
	}

	public boolean isQihuSpeedBallGiftRewarded() {
		return qihuSpeedBallGiftRewarded;
	}

	public void setQihuSpeedBallGiftRewarded(boolean qihuSpeedBallGiftRewarded) {
		this.qihuSpeedBallGiftRewarded = qihuSpeedBallGiftRewarded;
	}

	public String getOperatorClientInfo() {
		return operatorClientInfo;
	}

	public void setOperatorClientInfo(String operatorClientInfo) {
		this.operatorClientInfo = operatorClientInfo;
	}
	
	public QiHu360PrivilegePlayer getQiHuPrivilege() {
		return qiHuPrivilege;
	}

	public void setQiHuPrivilege(QiHu360PrivilegePlayer qiHuPrivilege) {
		this.qiHuPrivilege = qiHuPrivilege;
	}
	public QiHu360SpeedPrivilegePlayer getQiHuSpeedPrivilege() {
		return qiHuSpeedPrivilege;
	}

	public void setQiHuSpeedPrivilege(QiHu360SpeedPrivilegePlayer qiHuSpeedPrivilege) {
		this.qiHuSpeedPrivilege = qiHuSpeedPrivilege;
	}

	public boolean isBrowser2345Rewarded() {
		return browser2345Rewarded;
	}

	public void setBrowser2345Rewarded(boolean browser2345Rewarded) {
		this.browser2345Rewarded = browser2345Rewarded;
	}
}
