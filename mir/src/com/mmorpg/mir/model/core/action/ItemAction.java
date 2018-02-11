package com.mmorpg.mir.model.core.action;

import java.util.List;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.packet.SM_Packet_Update;
import com.mmorpg.mir.model.item.storage.RemoveItem;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class ItemAction extends AbstractCoreAction {

	@Override
	public void act(Object object, ModuleInfo moduleInfo) {
		Player player = null;
		if (object instanceof Skill) {
			if (((Skill) object).getEffector() instanceof Player) {
				player = (Player) ((Skill) object).getEffector();
			}
		} else if (object instanceof Player) {
			player = (Player) object;
		}
		List<RemoveItem> removes = player.getPack().reduceItemByKey(code, value);
		for (RemoveItem remove : removes) {
			AbstractItem item = remove.getItem();
			LogManager.addItemLog(player, System.currentTimeMillis(), moduleInfo, -1, remove.getNum(), item, player
					.getPack().getItemSizeByKey(item.getKey()));
		}
		PacketSendUtility.sendPacket(player, SM_Packet_Update.valueOf(player));
	}

	@Override
	protected boolean check(AbstractCoreAction action) {
		return code.equals(action.code);
	}

	@Override
	public void init() {
		super.init();
		setActionType(CoreActionType.ITEM.name());
	}

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		} else if (object instanceof Skill) {
			player = (Player) ((Skill) object).getEffector();
		}
		return CoreConditionType.createItemCondition(code, value).verify(player);
	}

	@Override
	public AbstractCoreAction clone() {
		ItemAction action = new ItemAction();
		action.setCode(code);
		action.setValue(value);
		action.setActionType(getActionType());
		return action;
	}

}
