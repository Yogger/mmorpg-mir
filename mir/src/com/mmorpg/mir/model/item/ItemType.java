package com.mmorpg.mir.model.item;

public enum ItemType {
	/** 不可使用的小道具 **/
	SMALLITEM {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends AbstractItem> T create() {
			return (T) new SmallItem();
		}
	},
	/** 宝石 **/
	GEM {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends AbstractItem> T create() {
			return (T) new Gem();
		}
	},
	/** 装备 **/
	EQUIPMENT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends AbstractItem> T create() {
			return (T) new Equipment();
		}
	},
	/** 普通的可使用的道具 **/
	USEABLEITEM {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends AbstractItem> T create() {
			return (T) new UseableItem();
		}
	},
	/** 宠物道具 **/
	PETITEM {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends AbstractItem> T create() {
			return (T) new PetItem();
		}
	},
	LIFEGRID {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends AbstractItem> T create() {
			return (T) new LifeGridItem();
		}

	};

	public abstract <T extends AbstractItem> T create();
}
