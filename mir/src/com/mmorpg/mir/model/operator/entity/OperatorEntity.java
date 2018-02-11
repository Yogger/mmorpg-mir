package com.mmorpg.mir.model.operator.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.mmorpg.mir.model.operator.model.ForbidChatList;
import com.mmorpg.mir.model.operator.model.GmList;
import com.mmorpg.mir.model.operator.model.LoginBanList;
import com.mmorpg.mir.model.operator.model.SuperVipPool;
import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;
import com.windforce.common.utility.JsonUtils;

@Entity
@Cached(size = "ten", persister = @Persister("30s"))
public class OperatorEntity implements IEntity<Long> {
	@Id
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private Long id;
	@Lob
	private String forbidChatJson;

	@Transient
	private ForbidChatList forbidChatList;

	@Lob
	private String loginBanListJson;

	@Transient
	private LoginBanList loginBanList;

	@Lob
	private String gmListJson;

	@Transient
	private GmList gmList;

	@Lob
	private String superVipPoolJson;

	@Transient
	private SuperVipPool superVipPool;

	// PS:ADD serialize()

	public static OperatorEntity valueOf() {
		OperatorEntity oe = new OperatorEntity();
		oe.forbidChatJson = JsonUtils.object2String(ForbidChatList.valueOf());
		oe.loginBanListJson = JsonUtils.object2String(LoginBanList.valueOf());
		oe.gmListJson = JsonUtils.object2String(GmList.valueOf());
		oe.superVipPoolJson = JsonUtils.object2String(SuperVipPool.valueOf());
		oe.id = 1L;
		return oe;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getForbidChatJson() {
		return forbidChatJson;
	}

	public void setForbidChatJson(String forbidChatJson) {
		this.forbidChatJson = forbidChatJson;
	}

	public ForbidChatList getForbidChatList() {
		if (forbidChatList == null) {
			forbidChatList = JsonUtils.string2Object(forbidChatJson, ForbidChatList.class);
		}
		return forbidChatList;
	}

	public void setForbidChatList(ForbidChatList forbidChatList) {
		this.forbidChatList = forbidChatList;
	}

	@Override
	public boolean serialize() {
		if (forbidChatList != null) {
			forbidChatJson = JsonUtils.object2String(forbidChatList);
		}
		if (loginBanList != null) {
			loginBanListJson = JsonUtils.object2String(loginBanList);
		}
		if (gmList != null) {
			gmListJson = JsonUtils.object2String(gmList);
		}
		if (superVipPool != null) {
			superVipPoolJson = JsonUtils.object2String(superVipPool);
		}
		return true;
	}

	public String getLoginBanListJson() {
		return loginBanListJson;
	}

	public void setLoginBanListJson(String loginBanListJson) {
		this.loginBanListJson = loginBanListJson;
	}

	public LoginBanList getLoginBanList() {
		if (loginBanList == null) {
			loginBanList = JsonUtils.string2Object(loginBanListJson, LoginBanList.class);
		}
		return loginBanList;
	}

	public void setLoginBanList(LoginBanList loginBanList) {
		this.loginBanList = loginBanList;
	}

	public String getGmListJson() {
		return gmListJson;
	}

	public void setGmListJson(String gmListJson) {
		this.gmListJson = gmListJson;
	}

	public GmList getGmList() {
		if (gmList == null) {
			gmList = JsonUtils.string2Object(gmListJson, GmList.class);
		}
		return gmList;
	}

	public void setGmList(GmList gmList) {
		this.gmList = gmList;
	}

	public String getSuperVipPoolJson() {
		return superVipPoolJson;
	}

	public void setSuperVipPoolJson(String superVipPoolJson) {
		this.superVipPoolJson = superVipPoolJson;
	}

	public SuperVipPool getSuperVipPool() {
		if (superVipPool == null) {
			superVipPool = JsonUtils.string2Object(superVipPoolJson, SuperVipPool.class);
		}
		return superVipPool;
	}

	public void setSuperVipPool(SuperVipPool superVipPool) {
		this.superVipPool = superVipPool;
	}

}
