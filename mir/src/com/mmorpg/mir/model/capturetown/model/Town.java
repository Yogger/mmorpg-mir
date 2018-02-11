package com.mmorpg.mir.model.capturetown.model;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.capturetown.config.TownConfig;
import com.mmorpg.mir.model.capturetown.packet.SM_Town_Acc_Feats;
import com.mmorpg.mir.model.capturetown.resource.TownResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.windforce.common.utility.DateUtils;


public class Town {

	private String id;

	private long captureTime;
	
	private AtomicLong capturePlayerId = new AtomicLong();
	
	private String captureName;
	
	private int captureCountryValue;
	
	private int captureRole;
	
	private volatile int accFeats;
	
	@Transient
	private transient Future<?>  accFeatsFuture;
	
	@JsonIgnore
	public void startAccumulateFeats() {
		final TownResource resource = TownConfig.getInstance().getTownResource(id);
		final int maxBaseFeats = resource.getFeatsPerMin() * resource.getFeatsProduceMaxLimit();
		if (accFeatsFuture == null || accFeatsFuture.isCancelled()) {
			accFeatsFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
				
				@Override
				public void run() {
					if (accFeats >= maxBaseFeats) {
						// lock?????????????????????????/
						// accFeatsFuture.cancel(false);
					} else {
						int addValue = resource.getFeatsPerMin() * resource.getFeatsCalcRate();
						if (accFeats + addValue > maxBaseFeats) {
							accFeats = maxBaseFeats;
						} else {
							accFeats += addValue;
						}
						
						long pid = capturePlayerId.get();
						Player owner = PlayerManager.getInstance().getPlayer(pid);
						if (owner != null) {
							PacketSendUtility.sendPacket(owner, SM_Town_Acc_Feats.valueOf(accFeats));
						}
					}
				}
			}, resource.getFeatsCalcRate() * DateUtils.MILLIS_PER_MINUTE, 
				resource.getFeatsCalcRate() * DateUtils.MILLIS_PER_MINUTE);
		}
	}
	
	@JsonIgnore
	public void stopAccumulateFeats() {
		if (accFeatsFuture != null) {
			accFeatsFuture.cancel(false);
		}
	}
	
	@JsonIgnore
	public void clearTownOwner() {
		capturePlayerId = new AtomicLong();
		captureName = null;
		captureCountryValue = 0;
		captureRole = 0;
	}
	
	@JsonIgnore
	public void becomeTownOwner(Player player, int original) {
		captureName = player.getName();
		captureCountryValue = player.getCountryValue();
		captureRole = player.getRole();
		accFeats = original;
		capturePlayerId = new AtomicLong(player.getObjectId());
	}
	
	@JsonIgnore
	public boolean catpureThisTown(Player winner, long targetPlayerId) {
		return capturePlayerId.compareAndSet(targetPlayerId, winner.getObjectId());
	}
	
	public static Town valueOf(String key) {
		Town town = new Town();
		town.id = key;
		return town;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCaptureTime() {
		return captureTime;
	}

	public void setCaptureTime(long captureTime) {
		this.captureTime = captureTime;
	}

	public AtomicLong getCapturePlayerId() {
		return capturePlayerId;
	}

	public void setCapturePlayerId(AtomicLong capturePlayerId) {
		this.capturePlayerId = capturePlayerId;
	}

	public int getCaptureCountryValue() {
		return captureCountryValue;
	}

	public void setCaptureCountryValue(int captureCountryValue) {
		this.captureCountryValue = captureCountryValue;
	}

	@JsonIgnore
	public int clearAccFeats() {
		int ret = accFeats;
		accFeats = 0;
		return ret;
	}

	public int getAccFeats() {
		return accFeats;
	}

	public void setAccFeats(int accFeats) {
		this.accFeats = accFeats;
	}

	public int getCaptureRole() {
		return captureRole;
	}

	public void setCaptureRole(int captureRole) {
		this.captureRole = captureRole;
	}

	public String getCaptureName() {
		return captureName;
	}

	public void setCaptureName(String captureName) {
		this.captureName = captureName;
	}

}
