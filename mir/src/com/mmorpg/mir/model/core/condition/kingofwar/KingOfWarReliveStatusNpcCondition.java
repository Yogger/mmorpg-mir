package com.mmorpg.mir.model.core.condition.kingofwar;

import java.util.Map;
import java.util.Map.Entry;

import org.h2.util.New;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.windforce.common.utility.JsonUtils;

/**
 * 咸阳战各个复活点的状态
 * 
 * @author Kuang Hao
 * @since v1.0 2014-11-11
 * 
 */
public class KingOfWarReliveStatusNpcCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if(object instanceof Player){
			player = (Player) object;
		}
		if(player == null){
			this.errorObject(object);
		}
		Map<String, String> status = JsonUtils.string2Map(code, String.class, String.class);
		for (Entry<String, String> entry : status.entrySet()) {
			if (!KingOfWarManager.getInstance().getStatusNpcs().containsKey(entry.getKey())) {
				throw new ManagedException(ManagedErrorCode.KINGOFWAR_STATUSNPC);
			}
			String s = KingOfWarManager.getInstance().getStatusNpcs().get(entry.getKey()).getStatus() + "";
			if (entry.getValue().equals("0")) {
				if (s.equals(player.getCountryValue() + "")) {
					throw new ManagedException(ManagedErrorCode.KINGOFWAR_STATUSNPC);
				}
			} else {
				if (!s.equals(player.getCountryValue() + "")) {
					throw new ManagedException(ManagedErrorCode.KINGOFWAR_STATUSNPC);
				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		Map<String, String> dd = New.hashMap();
		dd.put("KOW_rightspaw3", "0");
		dd.put("KOW_leftspaw2", "0");
		dd.put("KOW_centerspaw1", "0");

		System.out.println(JsonUtils.object2String(dd));
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

}
