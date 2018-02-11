package com.mmorpg.mir.model.gameobjects;

import com.mmorpg.mir.model.boss.model.BossScheme;
import com.mmorpg.mir.model.controllers.BossController;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.world.WorldPosition;

public class Boss extends Monster {

	private BossScheme bossScheme;

	public Boss(long objId, BossController controller, WorldPosition position) {
		super(objId, controller, position);
	}

	@Override
	public BossController getController() {
		return (BossController) super.getController();
	}

	@Override
	public boolean isPlayerEnemy(Player player) {
		if (getController().inActivity()) {
			return getController().isActivityEnemy(player);
		}
		return true;
	}

	@Override
	protected boolean isMonsterEnemy(Monster monster) {
		return false;
	}

	public boolean isAtSpawnLocation() {
		if (getX() == getBornX() && getY() == getBornY()) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean isCountryNpcEnemy(CountryNpc countryNpc) {
		return getCountry() != countryNpc.getCountry();
	}

	@Override
	protected boolean canSeeCountryNpc(VisibleObject visibleObject) {
		return true;
	}

	@Override
	protected boolean canSeeRobot(VisibleObject visibleObject) {
		return true;
	}

	@Override
	protected boolean canSeeSummon(VisibleObject visibleObject) {
		return true;
	}

	@Override
	protected boolean canSeeBigBrother(VisibleObject visibleObject) {
		return true;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.BOSS;
	}

	public BossScheme getBossScheme() {
		return bossScheme;
	}

	public void setBossScheme(BossScheme bossScheme) {
		this.bossScheme = bossScheme;
	}

	@Override
	public void onDrop(Reward reward, Creature lastAttacker, Player mostDamagePlayer) {
		super.onDrop(reward, lastAttacker, mostDamagePlayer);
		if (bossScheme != null) {
			if (lastAttacker instanceof Summon) {
				bossScheme.getBossHistory().addDropInfo(((Summon) lastAttacker).getMaster(), reward, this);
			} else if (lastAttacker instanceof Player) {
				bossScheme.getBossHistory().addDropInfo((Player) lastAttacker, reward, this);
			}
		}
	}

}
