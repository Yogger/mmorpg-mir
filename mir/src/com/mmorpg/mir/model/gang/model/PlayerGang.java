package com.mmorpg.mir.model.gang.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gang.manager.GangManager;
import com.mmorpg.mir.model.player.manager.PlayerManager;

public class PlayerGang {
	private long gangId;
	/** 申请 */
	private Set<PlayerApply> applies = New.hashSet();
	/** 邀请 */
	private Set<PlayerInvite> invites = New.hashSet();

	private transient Player owner;

	private long lastQuitGangTime;

	@JsonIgnore
	public void clear() {
		applies.clear();
		invites.clear();
		lastQuitGangTime = 0;
	}

	@JsonIgnore
	public void cancelApply(long gang) {
		List<PlayerApply> removes = New.arrayList();
		for (PlayerApply pa : applies) {
			if (pa.getGangId() == gang) {
				removes.add(pa);
			}
		}
		for (PlayerApply r : removes) {
			applies.remove(r);
		}
	}

	@JsonIgnore
	public boolean containApply(long gang) {
		for (PlayerApply pa : applies) {
			if (pa.getGangId() == gang) {
				return true;
			}
		}
		return false;
	}

	@JsonIgnore
	public PlayerGangVO createVO(Gang gang) {
		PlayerGangVO vo = new PlayerGangVO();
		vo.setGangInfo(GangInfo.valueOf(gang));
		vo.setApplies(new ArrayList<PlayerApplyVO>());
		vo.setInvites(new ArrayList<PlayerInviteVO>());
		for (PlayerApply pa : applies) {
			vo.getApplies().add(pa.createVO());
		}
		for (PlayerInvite pi : invites) {
			vo.getInvites().add(pi.createVO());
		}
		vo.setLastQuitGangTime(lastQuitGangTime + GangManager.getInstance().ENTER_NEW_GANG_CD.getValue());
		return vo;
	}

	public static PlayerGang valueOf(long gangId) {
		PlayerGang playerGang = new PlayerGang();
		playerGang.gangId = gangId;
		return playerGang;
	}

	@JsonIgnore
	public PlayerInvite getPlayerInvite(long gangId) {
		for (PlayerInvite pi : invites) {
			if (pi.getGangId() == gangId) {
				return pi;
			}
		}
		return null;
	}

	@JsonIgnore
	public void setGangAndUpdate(long gangId) {
		this.gangId = gangId;
		PlayerManager.getInstance().updatePlayer(owner);
	}

	@JsonIgnore
	public void removeInvite(long gangId) {
		List<PlayerInvite> removes = New.arrayList();
		for (PlayerInvite pi : invites) {
			if (pi.getGangId() == gangId) {
				removes.add(pi);
			}
		}
		for (PlayerInvite pi : removes) {
			invites.remove(pi);
		}
	}

	public long getGangId() {
		return gangId;
	}

	public void setGangId(long gangId) {
		this.gangId = gangId;
	}

	public Set<PlayerApply> getApplies() {
		return applies;
	}

	public void setApplies(Set<PlayerApply> applies) {
		this.applies = applies;
	}

	public Set<PlayerInvite> getInvites() {
		return invites;
	}

	public void setInvites(Set<PlayerInvite> invites) {
		this.invites = invites;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	@JsonIgnore
	public long getLastQuitGangTime() {
		return lastQuitGangTime;
	}

	@JsonIgnore
	public void setLastQuitGangTime(long lastQuitGangTime) {
		this.lastQuitGangTime = lastQuitGangTime;
	}

}
