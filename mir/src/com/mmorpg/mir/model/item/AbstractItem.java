package com.mmorpg.mir.model.item;

import java.util.concurrent.Future;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.mmorpg.mir.model.gameobjects.JourObject;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.config.ItemConfig;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.core.ItemService;
import com.mmorpg.mir.model.item.packet.SM_Item_Deprecated;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.scheduler.ScheduledTask;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
public abstract class AbstractItem extends JourObject implements Comparable<AbstractItem> {
	protected String key;
	protected int size;
	protected int state;
	/** 过期时间 */
	protected long deprecatedTime;

	private transient Future<?> deprecatedFuture;

	public String getKey() {
		return key;
	}

	@JsonIgnore
	public AbstractItem copy() {
		AbstractItem item = getType().create();
		item.setObjectId(objectId);
		item.setKey(key);
		item.setSize(size);
		item.setState(state);
		item.setDeprecatedTime(deprecatedTime);
		return this;
	}

	@JsonIgnore
	public void startDeprecatedFuture(final Player owner, boolean addTime) {
		if (deprecatedTime == 0) {
			stopDeprecatedFuture();
		} else {
			if (addTime) {
				stopDeprecatedFuture();
			}
			if (deprecatedFuture == null || deprecatedFuture.isCancelled()) {
				deprecatedFuture = ItemConfig.getInstance().schedule(new ScheduledTask() {
					@Override
					public void run() {
						if (getResource().getItemType() == ItemType.PETITEM) {
							ItemService.getInstance().callBackPet(owner, getObjectId());
						}
						PacketSendUtility.sendPacket(owner,
								SM_Item_Deprecated.valueOf(getResource().getItemType().ordinal(), objectId));
					}

					@Override
					public String getName() {
						return "道具过期倒计时";
					}
				}, deprecatedTime);
			}
		}
	}

	@JsonIgnore
	public void stopDeprecatedFuture() {
		if (deprecatedFuture != null && !deprecatedFuture.isCancelled()) {
			deprecatedFuture.cancel(true);
		}
		deprecatedFuture = null;
	}

	/**
	 * 检查是否过期
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean checkDeprecated() {
		if (!hasDeprecated()) {
			return false;
		}
		long now = System.currentTimeMillis();
		if (now > deprecatedTime) {
			openState(ItemState.DEPRECATED.getMark());
			return true;
		}
		return false;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void openState(int state) {
		this.state |= state;
	}

	public void closeState(int state) {
		this.state &= ~state;
	}

	@JsonIgnore
	public boolean isState(int state) {
		int result = this.state & state;
		return result > 0;
	}

	@JsonIgnore
	public boolean isBind() {
		return isState(ItemState.BIND.getMark());
	}

	@JsonIgnore
	public boolean isDeprecated() {
		return isState(ItemState.DEPRECATED.getMark());
	}

	@JsonIgnore
	public boolean hasDeprecated() {
		return deprecatedTime > 0;
	}

	@JsonIgnore
	public int getOverLimit() {
		return getResource().getOverLimit();
	}

	@JsonIgnore
	public ItemResource getResource() {
		return ItemManager.getInstance().getResource(key);
	}

	@JsonIgnore
	public String getName() {
		return getResource().getName();
	}

	public AddResult add(AbstractItem item) {
		int overLimit = getOverLimit();
		// 同种类型的物品才可以叠加
		AddResult result = new AddResult();
		if (overLimit > 1) {
			if (check(item)) {
				int size = this.getSize() + item.getSize();
				if (size <= overLimit) {
					this.setSize(size);
				} else {
					size -= overLimit;
					this.setSize(overLimit);
					item.setSize(size);
					result.setResult(item);
				}
				result.setSuccess(true);
				return result;
			}
		}
		return result;
	}

	public boolean check(AbstractItem item) {
		return item.getKey().equals(key) && this.state == item.state && deprecatedTime == item.deprecatedTime;
	}

	@JsonIgnore
	public boolean canBuy() {
		return !isState(ItemState.CANTBUY.getMark());
	}

	@JsonIgnore
	public boolean canTrade() {
		return !isState(ItemState.CANTTRADE.getMark());
	}

	@JsonIgnore
	public boolean canBuyBack() {
		return getResource().canBuyBack();
	}

	@JsonIgnore
	public boolean canDevour() {
		return !isState(ItemState.LOCK.getMark());
	}

	@JsonIgnore
	public int getSellPrice() {
		return getResource().getSellPrice();
	}

	public class AddResult {
		private boolean success;
		private AbstractItem result;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public AbstractItem getResult() {
			return result;
		}

		public void setResult(AbstractItem result) {
			this.result = result;
		}

	}

	public static enum ItemState {
		/** 绑定状态 **/
		BIND(1 << 0),

		CANTBUY(BIND.getMark()),

		CANTTRADE(BIND.getMark()),
		/** 过期 */
		DEPRECATED(1 << 1),

		LOCK(1 << 2);

		private final int mark;

		private ItemState(int mark) {
			this.mark = mark;
		}

		public int getMark() {
			return mark;
		}

	}

	public void init(ItemResource itemResource) {
		this.key = itemResource.getKey();
	}

	@JsonIgnore
	public ItemType getType() {
		return getResource().getItemType();
	}

	@JsonIgnore
	public String[] getTriggerIds() {
		return getResource().getTriggerIds();
	}

	@Override
	public int compareTo(AbstractItem o) {
		if (o == this) {
			return 0;
		}

		long longResult = getResource().getSortIndex() - o.getResource().getSortIndex();
		int result = 0;
		if (longResult > 0) {
			return -1;
		} else if (longResult < 0) {
			return 1;
		}
		if (result == 0) {
			result = key.compareTo(o.key);
		}
		if (result == 0) {
			result = size - o.size;
		}
		if (result == 0) {
			// 这里类型的道具根据自己的特殊规则，来进行判断
			result = compare(o);
		}
		return result;
	}

	protected int compare(AbstractItem o) {
		return 0;
	}

	public long getDeprecatedTime() {
		return deprecatedTime;
	}

	public void setDeprecatedTime(long deprecatedTime) {
		this.deprecatedTime = deprecatedTime;
	}

	@Override
	public String toString() {
		return state + "|" + deprecatedTime;
	}
}
