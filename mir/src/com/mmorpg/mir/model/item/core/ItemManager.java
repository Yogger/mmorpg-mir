package com.mmorpg.mir.model.item.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.FormulaParmsUtil;
import com.mmorpg.mir.model.common.ResourceReload;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.group.model.PlayerGroup;
import com.mmorpg.mir.model.group.packet.SM_Member_EnhanceLevel_Change;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.ItemType;
import com.mmorpg.mir.model.item.LifeGridItem;
import com.mmorpg.mir.model.item.model.EquipmentStat;
import com.mmorpg.mir.model.item.model.EquipmentStatType;
import com.mmorpg.mir.model.item.model.EquipmentStorageType;
import com.mmorpg.mir.model.item.model.extrastat.EquipmentExtraStatHandle;
import com.mmorpg.mir.model.item.packet.SM_EnhanceLevel_Change;
import com.mmorpg.mir.model.item.resource.CombingExResource;
import com.mmorpg.mir.model.item.resource.CombiningResource;
import com.mmorpg.mir.model.item.resource.EquipmentComposeResource;
import com.mmorpg.mir.model.item.resource.EquipmentCreateSoulResource;
import com.mmorpg.mir.model.item.resource.EquipmentEnhanceExResource;
import com.mmorpg.mir.model.item.resource.EquipmentEnhanceResource;
import com.mmorpg.mir.model.item.resource.EquipmentGemGatherResource;
import com.mmorpg.mir.model.item.resource.EquipmentGodResource;
import com.mmorpg.mir.model.item.resource.EquipmentRareResource;
import com.mmorpg.mir.model.item.resource.EquipmentSmeltResource;
import com.mmorpg.mir.model.item.resource.EquipmentSoulResource;
import com.mmorpg.mir.model.item.resource.EquipmentSuitResource;
import com.mmorpg.mir.model.item.resource.EquipmentTurnResource;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.item.resource.TreasureItemFixResource;
import com.mmorpg.mir.model.item.resource.TreasureResource;
import com.mmorpg.mir.model.item.storage.EquipmentStorage;
import com.mmorpg.mir.model.item.storage.ItemStorage;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;
import com.mmorpg.mir.model.util.IdentifyManager;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public final class ItemManager implements ResourceReload, IItemManager {

	private static ItemManager self;

	@Static
	public Storage<String, TreasureItemFixResource> treasureFixStorage;

	@Static
	public Storage<String, Formula> formulaStorage;

	@Static
	public Storage<String, ItemResource> itemResources;

	@Static
	public Storage<String, TreasureResource> treasureResource;

	@Static
	public Storage<Integer, EquipmentEnhanceResource> equipEnhanceResource;

	@Static
	public Storage<String, EquipmentEnhanceExResource> equipEnhanceExReource;

	@Static
	private Storage<String, EquipmentComposeResource> equipComposeResource;

	@Static
	private Storage<String, EquipmentSoulResource> equipSoulResource;

	@Static
	private Storage<String, EquipmentRareResource> equipRareResource;

	@Static
	private Storage<Integer, EquipmentSmeltResource> equipSmeltResource;

	@Static
	private Storage<String, EquipmentSuitResource> equipSuitResource;

	@Static
	private Storage<String, CombiningResource> combiningResource;

	@Static
	private Storage<String, EquipmentCreateSoulResource> equipCreateSoulResources;

	@Static
	public Storage<String, EquipmentGemGatherResource> equipGemGatherResources;

	@Static
	public Storage<String, EquipmentGodResource> equipmentGodResources;

	@Static
	public Storage<String, CombingExResource> combingExResources;

	@Static
	public Storage<String, EquipmentTurnResource> equipTurnResources;

	private Map<String, String> equipExtendsFound;

	private Map<String, String> godResourceIndex;

	private Map<String, String> equipTurnIndex;

	@Static("DEPOT:MAX_SIZE")
	public ConfigValue<Integer> DEPOT_MAX_SIZE;

	@Static("DEPOT:INIT_SIZE")
	public ConfigValue<Integer> DEPOT_INIT_SIZE;

	@Static("DEPOT:OPEN_CELL_TIME")
	public Formula DEPOT_OPEN_TIME;

	@Static("BAG:MAX_SIZE")
	public ConfigValue<Integer> BAG_MAX_SIZE;

	@Static("BAG:INIT_SIZE")
	public ConfigValue<Integer> BAG_INIT_SIZE;

	@Static("BAG:OPEN_CELL_TIME")
	public Formula BAG_OPEN_TIME;

	@Static("BAG:OPEN_CELL_MONEY")
	public ConfigValue<Integer[]> BAG_OPEN_CELL_MONEY;

	@Static("DEPOT:OPEN_CELL_MONEY")
	public ConfigValue<Integer[]> DEPOT_OPEN_CELL_MONEY;

	@Static("EQUIPMENT:CALCULATE_SMELT_GOLD")
	private Formula CALCULATE_SMELT_GOLD;

	@Static("BAG:OPEN_CELL_EXP")
	private Formula BAG_OPEN_CELL_EXP;

	@Static("DEPOT:OPEN_CELL_EXP")
	private Formula DEPOT_OPEN_CELL_EXP;

	@Static("BAG:OPEN_CELL_STAT")
	public ConfigValue<Stat[]> BAG_OPEN_CELL_STAT;

	@Static("DEPOT:OPEN_CELL_STAT")
	public ConfigValue<Stat[]> DEPOT_OPEN_CELL_STAT;

	@Static("EQUIPMENT:SMELT_GOLD_ACTION")
	public Formula SMELT_GOLD_ACTION;

	@Static("EQUIPMENT:SMELT_GIFT_ACTION")
	public Formula SMELT_GIFT_ACTION;

	@Static("EQUIPMENT:SMELT_GOLD_VALUE")
	public Formula SMELT_GOLD_VALUE;

	@Static("EQUIPMENT:SMELT_GIFT_VALUE")
	public Formula SMELT_GIFT_VALUE;

	@Static("EQUIPMENT:SUPER_FORGE_GOLD")
	public Formula SUPER_FORGE_GOLD;

	@Static("EQUIPMENT:SMELT_USEGOLD_ADD")
	public ConfigValue<Integer> SMELT_USEGOLD_ADD;

	@Static("EQUIPMENT:ROLETYPE_EQUPMENT_GROUP")
	public ConfigValue<Integer[]> ROLETYPE_EQUPMENT_GROUP;

	@Static("EQUIPMENT:ATTACK_EQUIPMENT_GROUP")
	public ConfigValue<Integer[]> ATTACK_EQUIPMENT_GROUP;

	@Static("EQUIPMENT:DEFENSE_EQUIPMENT_GROUP")
	public ConfigValue<Integer[]> DEFENSE_EQUIPMENT_GROUP;

	@Static("EQUIPMENT:GEM_ATTACK_EQUIPMENT_GROUP")
	public ConfigValue<Integer[]> GEM_ATTACK_EQUIPMENT_GROUP;

	@Static("EQUIPMENT:GEM_DEFENSE_EQUIPMENT_GROUP")
	public ConfigValue<Integer[]> GEM_DEFENSE_EQUIPMENT_GROUP;

	@Static("EQIUPMENT:SOUL_ACTIVATE_RELATION")
	public ConfigValue<Map<String, String>> SOUL_ACTIVATE_RELATION;

	@Static("EQUIPMENT:ENHANCE_RATIO")
	public ConfigValue<Double[]> ENHANCE_RATIO;

	@Static("EQUIPMENT:HORSE_ENHANCE_RATIO")
	public ConfigValue<Double[]> HORSE_ENHANCE_RATIO;

	@Static("EQUIPMENT:RARE_CHOOSERGROUP")
	public ConfigValue<Map<String, String>> EQUIPMENT_RARE_CHOOSERGROUP;

	@Static("EQUIP:QUALITY_COEFFICIENT")
	public ConfigValue<String[]> QUALITY_COEFFICIENT;

	@Static("EQUIP:STRENGTHEN_COEFFICIENT")
	public ConfigValue<String[]> STRENGTHEN_COEFFICIENT;

	/** 这个修改成map */
	@Static("EQUIP:SMELT_DALIYEXP_LIMIT")
	public ConfigValue<Long> SMELT_DALIYEXP_LIMIT;

	@Static("EQIUPMENT:COMBAT_SPIRIT_INIT")
	public ConfigValue<Map<String, String>> COMBAT_SPIRIT_INIT;

	@Static("ITEM:ITEM_QUALITY_COLOR")
	public ConfigValue<Map<String, String>> ITEM_QUALITY_COLOR;

	@Static("EQUIP:QUALITY_FACTOR_MAP")
	public ConfigValue<Map<String, Double>> QUALITY_FACTOR_MAP;

	@Static("EQUIP:LEVEL_FACTOR_MAP")
	public ConfigValue<Map<String, Double>> LEVEL_FACTOR_MAP;

	@Static("EQUIP:EQUIPTYPE_FACTOR_MAP")
	public ConfigValue<Map<String, Double>> EQUIPTYPE_FACTOR_MAP;

	@Static("EQUIP:EQUIP_GEM_MAX_SIZE")
	public ConfigValue<Integer> EQUIP_GEM_MAX_SIZE;

	@Static("EQUIP:EQUIP_TRANSFER_LEVEL_GEM_TITLE")
	public ConfigValue<String> EQUIP_TRANSFER_LEVEL_GEM_TITLE;

	@Static("EQUIP:EQUIP_TRANSFER_LEVEL_GEM_CONTENT")
	public ConfigValue<String> EQUIP_TRANSFER_LEVEL_GEM_CONTENT;

	@Static("EQUIP:EQUIP_DESTROY_GEM_TITLE")
	public ConfigValue<String> EQUIP_DESTROY_GEM_TITLE;

	@Static("EQUIP:EQUIP_DESTROY_GEM_CONTENT")
	public ConfigValue<String> EQUIP_DESTROY_GEM_CONTENT;

	/** 探宝全服记录数量 */
	@Static("EQUIP:TREASURE_STORAGE_SERVER_COLLECT_RECORD_COUNT")
	public ConfigValue<Integer> TREASURE_STORAGE_SERVER_COLLECT_RECORD_COUNT;

	/** 探宝仓库初始大小 */
	@Static("EQUIP:TREASURE_STORAGE_INIT_SIZE")
	public ConfigValue<Integer> TREASURE_STORAGE_INIT_SIZE;

	/** 探宝记录的装备最低品质 */
	@Static("EQUIP:TREASURE_STORAGE_SERVER_COLLECT_QUALITY_LIMIT")
	public ConfigValue<Integer> TREASURE_STORAGE_SERVER_COLLECT_QULITY_LIMIT;

	@Static("EQUIP:TREASURE_TYPE_MODULE_IDS")
	public ConfigValue<Map<String, String>> TREASURE_MODULE_OPEN_IDS;

	/** 探宝个人记录数量 */
	@Static("EQUIP:TREASURE_STORAGE_COLLECT_RECORD_COUNT")
	public ConfigValue<Integer> TREASURE_STORAGE_COLLECT_RECORD_COUNT;

	@Static("EQUIP:COMBING_EX_GEM_MAIL_TITLE")
	public ConfigValue<String> COMBING_EX_GEM_MAIL_TITLE;

	@Static("EQUIP:COMBING_EX_GEM_MAIL_CONTENT")
	public ConfigValue<String> COMBING_EX_GEM_MAIL_CONTENT;

	@Static("EQUIP:MIN_ENHANCE_LEVEL_INDEX")
	private ConfigValue<Map<String, Integer>> MIN_ENHANCE_LEVEL_INDEX;

	/** 装备激活灵魂属性需要什么MAP */
	private Map<EquipmentType, EquipmentType> soulActivateRelation;

	/** 装备影响另一个装备的MAP */
	private Map<EquipmentType, EquipmentType> soulEffectActivate;

	private Map<String, String> soulIndexMap;

	public Map<EquipmentType, EquipmentType> getActivateMap() {
		return soulActivateRelation;
	}

	public String getEquipmentRareChooserGroup(String quality) {
		return EQUIPMENT_RARE_CHOOSERGROUP.getValue().get(quality);
	}

	public Map<EquipmentType, EquipmentType> getEffectActivateMap() {
		return soulEffectActivate;
	}

	public static ItemManager getInstance() {
		return self;
	}

	@PostConstruct
	protected void init() throws Exception {
		self = this;
		reload();
	}

	@Override
	public void reload() {
		// 合成的HASH
		Map<String, String> tempEquipmentExtends = New.hashMap();
		for (ItemResource resource : getItemResources().getAll()) {
			if (resource.getItemType() == ItemType.EQUIPMENT && (!resource.isCannotForged())) {
				String key = resource.getRoletype() + "_" + resource.getEquipmentType().ordinal() + "_"
						+ resource.getLevel() + "_" + resource.getQuality() + "_" + resource.getSpecialType();
				if (tempEquipmentExtends.put(key, resource.getKey()) != null)
					throw new IllegalStateException("装备HASH重复!!!");
			}
		}
		equipExtendsFound = tempEquipmentExtends;

		Map<String, String> tempEquipmentGod = New.hashMap();
		for (EquipmentGodResource resource : equipmentGodResources.getAll()) {
			String key = resource.getRole() + "_" + resource.getEquipmentType().ordinal() + "_" + resource.getLevel()
					+ "_" + resource.getQuality() + "_" + resource.getSpecialType();
			tempEquipmentGod.put(key, resource.getId());
		}
		godResourceIndex = tempEquipmentGod;

		Map<String, String> tempEquipTurn = New.hashMap();
		for (EquipmentTurnResource resource : equipTurnResources.getAll()) {
			StringBuilder sb = new StringBuilder();
			sb.append(resource.getTurn()).append('_').append(resource.getRole()).append('_')
					.append(resource.getEquipmentType().getIndex()).append('_').append(resource.getLevel()).append('_')
					.append(resource.getQuality()).append('_').append(resource.getSpecialType());
			tempEquipTurn.put(sb.toString(), resource.getId());
		}
		equipTurnIndex = tempEquipTurn;
		if(equipTurnIndex.containsKey("1_1_8_210_8_1")){
			System.out.println(equipTurnIndex.get("1_1_8_210_8_1"));
		}

		// 灵魂的激活及影响的 相应关系
		Map<String, String> soulMap = SOUL_ACTIVATE_RELATION.getValue();
		Map<EquipmentType, EquipmentType> tempSoul = New.hashMap();
		Map<EquipmentType, EquipmentType> tempEffectSoul = New.hashMap();

		for (Map.Entry<String, String> entry : soulMap.entrySet()) {
			tempSoul.put(EquipmentType.valueOf(entry.getKey()), EquipmentType.valueOf(entry.getValue()));
			tempEffectSoul.put(EquipmentType.valueOf(entry.getValue()), EquipmentType.valueOf(entry.getKey()));
		}
		soulActivateRelation = tempSoul;
		soulEffectActivate = tempEffectSoul;

		// 灵魂的索引
		Map<String, String> soulKeyHashIndex = New.hashMap();
		for (EquipmentSoulResource resource : equipSoulResource.getAll()) {
			String key = resource.getEquipmentType().ordinal() + "_" + resource.getSoulStatLevel() + "_"
					+ resource.getStatType();
			if (soulKeyHashIndex.put(key, resource.getId()) != null)
				throw new IllegalStateException("灵魂属性索引重复!!!");
		}
		soulIndexMap = soulKeyHashIndex;

	}

	public String getSoulIndexKey(EquipmentType type, String soulLevel, String soulType) {
		StringBuilder sb = new StringBuilder();
		sb.append(type.ordinal()).append('_').append(soulLevel).append('_').append(soulType);
		return soulIndexMap.get(sb.toString());
	}

	public String getSoulIndexKey(EquipmentType type, int soulLevel, int soulType) {
		StringBuilder sb = new StringBuilder();
		sb.append(type.ordinal()).append('_').append(soulLevel).append('_').append(soulType);
		return soulIndexMap.get(sb.toString());
	}

	public ItemResource getResource(String key) {
		return getItemResources().get(key, true);
	}

	public EquipmentEnhanceResource getEquipEnhanceResource(Integer level) {
		return equipEnhanceResource.get(level, true);
	}

	public EquipmentEnhanceExResource getEquipEnhaceResourceEx(Integer level, int equipStorageType) {
		return equipEnhanceExReource.getUnique(EquipmentEnhanceExResource.EQUIPSTORAGE_LEVEL,
				EquipmentEnhanceExResource.toStorageLevelIndex(level, equipStorageType));
	}

	public EquipmentComposeResource getEquipComposeResource(String key) {
		return equipComposeResource.get(key, true);
	}

	public EquipmentRareResource getEquipRaraResource(String key) {
		return equipRareResource.get(key, true);
	}

	public EquipmentSoulResource getEquipmentSoulReousrce(String key) {
		return equipSoulResource.get(key, true);
	}

	public EquipmentSmeltResource getEquipmentSmeltResource(Integer key) {
		return equipSmeltResource.get(key, true);
	}

	public EquipmentSuitResource getStarSuitResource(int star) {
		if (star == 0) {
			return null;
		}
		List<EquipmentSuitResource> resources = equipSuitResource.getIndex(EquipmentSuitResource.STAR, star);
		if (resources.isEmpty()) {
			return null;
		}
		return equipSuitResource.getIndex(EquipmentSuitResource.STAR, star).get(0);
	}

	public EquipmentSuitResource getStarSuitResource(int equipStorageType, int star) {
		if (star == 0) {
			return null;
		}

		List<EquipmentSuitResource> resources = equipSuitResource.getIndex(EquipmentSuitResource.STAR, star);
		if (resources.isEmpty()) {
			return null;
		}
		for (EquipmentSuitResource r : resources) {
			if (r.getEquipStorageType() == equipStorageType) {
				return r;
			}
		}
		return null;
	}

	public List<EquipmentSuitResource> getSoulSuitResource(int soulType) {
		return equipSuitResource.getIndex(EquipmentSuitResource.SOULTYPE, soulType);
	}

	public List<EquipmentSuitResource> getNormalEquipmentSuitResource(int levelIndex) {
		return equipSuitResource.getIndex(EquipmentSuitResource.SUITLEVEL, levelIndex);
	}

	public List<EquipmentSuitResource> getEquipmentSuitResource(EquipmentStatType type) {
		return equipSuitResource.getIndex(EquipmentSuitResource.TYPE, type);
	}

	public EquipmentSuitResource getEquipmentSuitResourceById(String key) {
		return equipSuitResource.get(key, true);
	}

	public String getItemResourceKeyByHash(int roleType, int equipmentType, int level, int quality, int specialType) {
		StringBuilder sb = new StringBuilder();
		sb.append(roleType).append('_').append(equipmentType).append('_').append(level).append('_').append(quality)
				.append('_').append(specialType);
		return equipExtendsFound.get(sb.toString());
	}

	public String getEquipmentGodResource(int role, int equipmentType, int level, int quality, int specialType) {
		StringBuilder sb = new StringBuilder();
		sb.append(role).append('_').append(equipmentType).append('_').append(level).append('_').append(quality)
				.append('_').append(specialType);
		return godResourceIndex.get(sb.toString());
	}

	public String getEquipmentTurnResource(int turn, int role, int equipmentType, int level, int quality,
			int specialType) {
		StringBuilder sb = new StringBuilder();
		sb.append(turn).append('_').append(role).append('_')
		.append(equipmentType).append('_').append(level).append('_')
		.append(quality).append('_').append(specialType);
		return equipTurnIndex.get(sb.toString());
	}

	public double getSmeltUseGoldRate() {
		return SMELT_USEGOLD_ADD.getValue() * 1.0 / 10000;
	}

	public int smeltConsumeGift(int count) {
		return (Integer) FormulaParmsUtil.valueOf(SMELT_GIFT_ACTION).addParm("n", count).getValue();
	}

	public int smeltConsumeGold(int count) {
		return (Integer) FormulaParmsUtil.valueOf(SMELT_GOLD_ACTION).addParm("n", count).getValue();
	}

	public int addSmeltValueByGift(int consumeValue, int level) {
		return (Integer) FormulaParmsUtil.valueOf(SMELT_GIFT_VALUE).addParm("x", consumeValue).addParm("y", level)
				.getValue();
	}

	public int addSmeltValueByGold(int consumeValue, int level) {
		return (Integer) FormulaParmsUtil.valueOf(SMELT_GOLD_VALUE).addParm("x", consumeValue).addParm("y", level)
				.getValue();
	}

	public int getItemLength(String key, int num) {
		if (num <= 0) {
			throw new IllegalArgumentException("num must bigger than zero Exception");
		}

		ItemResource itemResource = getItemResources().get(key, true);
		int overLyLimit = itemResource.getOverLimit();
		if (overLyLimit < 1) {
			overLyLimit = 1;
		}

		int size = (int) (num / overLyLimit + (num % overLyLimit == 0 ? 0 : 1));
		return size;
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractItem> T[] createItems(String key, int num) {
		if (num <= 0) {
			throw new IllegalArgumentException("num must bigger than zero Exception");
		}
		ItemResource itemResource = getItemResources().get(key, true);
		int overLyLimit = itemResource.getOverLimit();
		if (overLyLimit < 1) {
			overLyLimit = 1;
		}
		int size = (int) (num / overLyLimit + (num % overLyLimit == 0 ? 0 : 1));
		AbstractItem[] result = new AbstractItem[size];
		for (int i = 0; i < size - 1; i++) {
			result[i] = doCreateItem(itemResource, overLyLimit);
			num -= result[i].getSize();
		}
		result[size - 1] = doCreateItem(itemResource, num);
		return (T[]) result;
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractItem> T createItem(String key) {
		return (T) createItems(key, 1)[0];
	}

	private AbstractItem doCreateItem(ItemResource itemResource, int num) {
		AbstractItem item = itemResource.getItemType().create();
		try {
			long guid = createGuid();
			item.setObjectId(guid);
			item.init(itemResource);
			item.setSize(num);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		return item;
	}

	private long createGuid() {
		return IdentifyManager.getInstance().getNextIdentify(IdentifyType.ITEM);
	}

	public int getInitSize(byte type) {
		if (type == 0) {
			return BAG_INIT_SIZE.getValue();
		}
		return DEPOT_INIT_SIZE.getValue();
	}

	public int getMaxSize(byte type) {
		if (type == 0) {
			return BAG_MAX_SIZE.getValue();
		} else {
			return DEPOT_MAX_SIZE.getValue();
		}
	}

	public long getReaminTime(ItemStorage storage, byte type) {
		int n = storage.getSize() - getInitSize(type);
		Long remainTime = 0L;
		if (type == 0) {
			remainTime = (Long) FormulaParmsUtil.valueOf(BAG_OPEN_TIME).addParm("n", n).getValue() * 1000L;
		} else {
			remainTime = (Long) FormulaParmsUtil.valueOf(DEPOT_OPEN_TIME).addParm("n", n).getValue() * 1000L;
		}

		return remainTime;
	}

	public int getAddPackPrice(int size, byte type) {
		Integer price = 0;
		if (type == 0) {
			price = BAG_OPEN_CELL_MONEY.getValue()[size - BAG_INIT_SIZE.getValue()];
		} else {
			price = DEPOT_OPEN_CELL_MONEY.getValue()[size - DEPOT_INIT_SIZE.getValue()];
		}
		return price;
	}

	public int getAddPackExp(int index, byte type) {
		Integer exp = 0;
		if (type == 0) {
			exp = (Integer) FormulaParmsUtil.valueOf(BAG_OPEN_CELL_EXP).addParm("n", index - BAG_INIT_SIZE.getValue())
					.getValue();
		} else {
			exp = (Integer) FormulaParmsUtil.valueOf(DEPOT_OPEN_CELL_EXP)
					.addParm("n", index - DEPOT_INIT_SIZE.getValue()).getValue();
		}

		if (exp < 0)
			exp = 0;
		return exp;
	}

	public Stat replaceAddPackStat(int index, byte type) {
		int i = 0;
		if (type == 0) {
			i = index - BAG_INIT_SIZE.getValue();
			return BAG_OPEN_CELL_STAT.getValue()[i];
		} else {
			i = index - DEPOT_INIT_SIZE.getValue();
			return DEPOT_OPEN_CELL_STAT.getValue()[i];
		}
	}

	public int calcSmeltGoldActionValue(int n) {
		return (Integer) FormulaParmsUtil.valueOf(SMELT_GOLD_ACTION).addParm("n", n).getValue();
	}

	public int calcSmeltGiftActionValue(int n) {
		return (Integer) FormulaParmsUtil.valueOf(SMELT_GIFT_ACTION).addParm("n", n).getValue();
	}

	public static final StatEffectId OPEN_PACK = StatEffectId.valueOf("OPEN_PACK", StatEffectType.PACK);
	public static final StatEffectId OPEN_WAREHOUSE = StatEffectId.valueOf("OPEN_WAREHOUSE", StatEffectType.PACK);

	public int getEquipmentValue(int quality, int enhanceLevel) {
		return Integer.parseInt(QUALITY_COEFFICIENT.getValue()[quality - 1])
				+ Integer.parseInt(STRENGTHEN_COEFFICIENT.getValue()[enhanceLevel]);
		// return
		// (Integer)FormulaParmsUtil.valueOf(CALCULATE_VALUE).addParm("q",
		// QUALITY_COEFFICIENT.getValue()[quality-1]). //quality 1-8,
		// enhance0-15
		// addParm("e",
		// STRENGTHEN_COEFFICIENT.getValue()[enhanceLevel]).getValue();
	}

	private Map<EquipmentStatType, EquipmentExtraStatHandle> equipmentExtraStatHandle = new HashMap<EquipmentStatType, EquipmentExtraStatHandle>();

	public void registerExtralStatHandle(EquipmentExtraStatHandle equipmentExtalStatHandle) {
		equipmentExtraStatHandle.put(equipmentExtalStatHandle.getType(), equipmentExtalStatHandle);
	}

	public List<Stat> calculateStat(Player player, Equipment equip, EquipmentStat equipStat) {
		if (equipStat == null || equipStat.getType() == EquipmentStatType.SUIT_STAT.getValue()) {
			return null;
		}
		return equipmentExtraStatHandle.get(EquipmentStatType.valueOf(equipStat.getType())).calcStat(player, equip,
				equipStat);
	}

	public int calGoldConsumption(int totalAddValue) {
		return (Integer) FormulaParmsUtil.valueOf(CALCULATE_SMELT_GOLD).addParm("x", totalAddValue).getValue();
	}

	public Integer[] getDefenseEquipGroup() {
		return DEFENSE_EQUIPMENT_GROUP.getValue();
	}

	public Integer[] getAttackEquipGroup() {
		return ATTACK_EQUIPMENT_GROUP.getValue();
	}

	public Integer[] getRoleEquipGroup() {
		return ROLETYPE_EQUPMENT_GROUP.getValue();
	}

	public Integer[] getGemAttackEquipGroup() {
		return GEM_ATTACK_EQUIPMENT_GROUP.getValue();
	}

	public Integer[] getGemDefenseEquipGroup() {
		return GEM_DEFENSE_EQUIPMENT_GROUP.getValue();
	}

	public void calOnAddSuitStatOnOtherEquipStorage(Player player, boolean recompute) {
		for (EquipmentStorageType equipStorageType : EquipmentStorageType.values()) {
			if (equipStorageType == EquipmentStorageType.PLAYER) {
				continue;
			}
			calculateStarSuit(player, recompute, equipStorageType);
		}

	}

	public void calOnAddSuitStat(Player player, boolean recompute) {
		calculateSoulSuit(player, recompute);
		calculateStarSuit(player, recompute, EquipmentStorageType.PLAYER);
		calculateGemSuit(player, recompute);
		calculateSuit(player, recompute);
	}

	public void calculateSuit(Player player, boolean recompute) {
		int[] equipmentSuitLevel = new int[player.getEquipmentStorage().getEquipments().length];
		for (Integer index : getRoleEquipGroup()) {
			Equipment equip = player.getEquipmentStorage().getEquip(index);
			if (equip == null || !equip.hasSpecifiedTypeStat(EquipmentStatType.SUIT_STAT)) {
				continue;
			}
			equipmentSuitLevel[index] = equip.getResource().getLevel();
		}
		for (EquipmentSuitResource resource : getEquipmentSuitResource(EquipmentStatType.SUIT_STAT)) {
			int count = 0;
			for (Integer child : resource.getChild()) {
				if (equipmentSuitLevel[child] == resource.getLevel()) {
					count++;
				}
			}
			boolean canActive = resource.getActiveCoreCondition().verify(player, false);
			if (canActive) {
				StatEffectId id = StatEffectId.valueOf(EquipmentStatType.SUIT_STAT.name() + resource.getId(),
						StatEffectType.EQUIPMENT);
				if (count >= resource.getNeedNum()) {
					player.getGameStats().addModifiers(id, resource.getStats(), false);
				} else {
					player.getGameStats().endModifiers(id, false);
				}
			}
		}
		if (recompute) {
			player.getGameStats().recomputeStats();
		}
	}

	public void calculateGemSuit(Player player, boolean recompute) {
		int levelSum = 0;
		String gatherMaxId = null;
		int maxSatisfy = 0;
		for (Equipment equip : player.getEquipmentStorage().getEquipments()) {
			if (equip == null || !equip.hasSpecifiedTypeStat(EquipmentStatType.GEM_STAT)) {
				continue;
			}
			for (String gemKey : equip.getExtraStats().get(EquipmentStatType.GEM_STAT.getValue()).getContext()) {
				if (gemKey == null || gemKey.equals(EquipmentStat.GEM_NOT_EXIST)) {
					continue;
				}
				levelSum += getResource(gemKey).getLevel();
			}
		}

		for (EquipmentGemGatherResource res : equipGemGatherResources.getAll()) {
			if (levelSum >= res.getLevelSum() && res.getLevelSum() > maxSatisfy) {
				maxSatisfy = res.getLevelSum();
				gatherMaxId = res.getId();
			}
		}
		if (gatherMaxId == null) {
			player.getGameStats()
					.endModifiers(
							StatEffectId.valueOf(EquipmentStatType.GEM_GATHER_STAT.name(), StatEffectType.EQUIPMENT),
							recompute);
			return;
		}
		player.getGameStats().replaceModifiers(
				StatEffectId.valueOf(EquipmentStatType.GEM_GATHER_STAT.name(), StatEffectType.EQUIPMENT),
				equipGemGatherResources.get(gatherMaxId, true).getStats(), recompute);
	}

	public void calculateSoulSuit(Player player, boolean recompute) {
		player.getGameStats().replaceModifiers(Equipment.ATTACK_SOUL_SUIT,
				calculateSoulSuit(player, player.getEquipmentStorage().getEquipments(), getAttackEquipGroup()),
				recompute);
		player.getGameStats().replaceModifiers(Equipment.DEFENSE_SOUL_SUIT,
				calculateSoulSuit(player, player.getEquipmentStorage().getEquipments(), getDefenseEquipGroup()),
				recompute);
	}

	private List<Stat> calculateSoulSuit(Player player, Equipment[] equipments, Integer[] group) {
		Map<Integer, Integer> soulNums = New.hashMap();
		List<Stat> stats = New.arrayList();

		for (int i = 0; i < group.length; i++) {
			Equipment equip = equipments[group[i]];
			if (equip == null || equip.getSoulType() == 0) {
				continue;
			}
			Integer count = soulNums.get(equip.getSoulType());
			if (count == null) {
				soulNums.put(equip.getSoulType(), 1);
			} else {
				soulNums.put(equip.getSoulType(), 1 + count);
			}
		}

		for (Entry<Integer, Integer> entry : soulNums.entrySet()) {
			List<EquipmentSuitResource> resources = getSoulSuitResource(entry.getKey());
			if (resources != null) {
				for (EquipmentSuitResource resource : resources) {
					if (entry.getValue() >= resource.getNeedNum()
							&& resource.getActiveCoreCondition().verify(player, false)) {
						stats.addAll(Arrays.asList(resource.getStats()));
					}
				}
			}
		}

		return stats;
	}

	public void calculateStarSuit(Player player, boolean recompute, EquipmentStorageType equipStorageType) {
		// 星级套装
		int minEnhanceLevel = getEnhanceMinLevel(player, equipStorageType);
		EquipmentStorage targetEquipStorage = equipStorageType.getEquipmentStorage(player);
		boolean changeSuitStar = targetEquipStorage.getSuitStarLevel() != minEnhanceLevel;

		refreshStarSuitStats(player, equipStorageType, minEnhanceLevel, recompute);
		if (changeSuitStar) {
			boolean firstChangeStar = targetEquipStorage.updateSuitStarLevel(minEnhanceLevel);
			EquipmentSuitResource starResource = getStarSuitResource(equipStorageType.getWhere(), minEnhanceLevel);
			if (starResource != null && firstChangeStar && starResource.getI18nNotice() != null) {
				for (Entry<String, Integer> entry : starResource.getI18nNotice().entrySet()) {
					I18nUtils utils = I18nUtils.valueOf(entry.getKey());
					utils.addParm("name", I18nPack.valueOf(player.getName()));
					utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
					utils.addParm("n", I18nPack.valueOf(minEnhanceLevel));
					ChatManager.getInstance().sendSystem(entry.getValue(), utils, null, player.getObjectId(),
							player.getObjectId());
				}
			}
			PacketSendUtility.broadcastPacket(player,
					SM_EnhanceLevel_Change.valueOf(player.getObjectId(), minEnhanceLevel, equipStorageType));
			if (player.isInGroup()) {
				player.getPlayerGroup().send(PlayerGroup.SYSTEM_SENDER_ID,
						SM_Member_EnhanceLevel_Change.valueOf(player, equipStorageType));
			}
		}
	}

	public int getEnhanceMinLevel(Player player, EquipmentStorageType equipStorageType) {
		int[] enhanceArr = null;
		if (equipStorageType == EquipmentStorageType.PLAYER) {
			enhanceArr = new int[EquipmentStorage.DEFAULT_SIZE];
		} else if (equipStorageType == EquipmentStorageType.HORSE) {
			enhanceArr = new int[EquipmentStorage.HORSE_SIZE];
		}
		for (Equipment equip : equipStorageType.getEquipmentStorage(player).getEquipments()) {
			if (equip == null || equip.getEnhanceLevel() == 0)
				continue;
			enhanceArr[equip.getEquipmentType().getStorageIndex()] = equip.getEnhanceLevel();
		}
		Arrays.sort(enhanceArr);
		return enhanceArr[MIN_ENHANCE_LEVEL_INDEX.getValue().get(equipStorageType.name())];
	}

	// public void refreshStarSuitStats(Player player, int minEnhanceLevel,
	// boolean recompute) {
	// EquipmentSuitResource starResource =
	// getStarSuitResource(minEnhanceLevel);
	// player.getGameStats().endModifiers(Equipment.STAR_SUIT, recompute);
	// if (starResource != null &&
	// starResource.getActiveCoreCondition().verify(player, false))
	// player.getGameStats().addModifiers(Equipment.STAR_SUIT,
	// starResource.getStats(), recompute);
	// }

	public void refreshStarSuitStats(Player player, EquipmentStorageType equipStorageType, int minEnhanceLevel,
			boolean recompute) {
		EquipmentSuitResource starResource = getStarSuitResource(equipStorageType.getWhere(), minEnhanceLevel);
		player.getGameStats().endModifiers(
				StatEffectId.valueOf(equipStorageType.name() + "_SUIT", StatEffectType.EQUIPMENT), recompute);
		if (starResource != null && starResource.getActiveCoreCondition().verify(player, false))
			player.getGameStats().addModifiers(
					StatEffectId.valueOf(equipStorageType.name() + "_SUIT", StatEffectType.EQUIPMENT),
					starResource.getStats(), recompute);
	}

	/**
	 * 装备影响的装备灵魂属性激活
	 * 
	 * @param player
	 * @param equipType
	 */
	public void soulActivateChange(Player player, EquipmentType equipType, EquipmentStorageType equipStorageType) {
		EquipmentType equipmentType = soulEffectActivate.get(equipType);
		if (equipmentType == null) {
			return;
		}
		Equipment effectEquipment = equipStorageType.getEquipmentStorage(player).getEquip(equipmentType);
		if (effectEquipment == null) {
			return;
		}
		EquipmentStat effect = effectEquipment.getExtraStats().get(EquipmentStatType.SOUL_STAT.getValue());
		if (effect != null) {
			List<Stat> effectStats = calculateStat(player, effectEquipment, effect);
			player.getGameStats().replaceModifiers(
					effectEquipment.getExtraStatEffectId(EquipmentStatType.SOUL_STAT.name()), effectStats, true);
		}
	}

	public Double getEnhanceRatio(int index) {
		if (index >= ENHANCE_RATIO.getValue().length)
			return 1.0;
		return ENHANCE_RATIO.getValue()[index];
	}

	public Double getHorseEnhanceRatio(int index) {
		if (index >= HORSE_ENHANCE_RATIO.getValue().length) {
			return 1.0;
		}
		return HORSE_ENHANCE_RATIO.getValue()[index];

	}

	/**
	 * 装备自身的灵魂属性激活
	 * 
	 * @param player
	 * @param equipType
	 */
	public void soulSelfActivateChange(Player player, EquipmentType equipType, EquipmentStorageType equipStorageType) {
		soulActivateChange(player, soulActivateRelation.get(equipType), equipStorageType);
	}

	@Override
	public Class<?> getResourceClass() {
		return ItemResource.class;
	}

	public List<ItemResource> getSameTypeLifeGridResource(int quality, int lifeGridType) {
		List<ItemResource> allLifeGridResources = itemResources.getIndex("itemType", ItemType.LIFEGRID);
		List<ItemResource> result = new ArrayList<ItemResource>();
		for (ItemResource resource : allLifeGridResources) {
			if (resource.getLifeGridType() == lifeGridType && resource.getQuality() == quality) {
				result.add(resource);
			}
		}

		Collections.sort(result, new Comparator<ItemResource>() {

			@Override
			public int compare(ItemResource o1, ItemResource o2) {
				return o1.getLifeGridLevel() - o2.getLifeGridLevel();
			}
		});
		return result;
	}

	public ItemResource getLastLevelLifeGridResource(LifeGridItem item) {

		List<ItemResource> allLifeGridResources = itemResources.getIndex("itemType", ItemType.LIFEGRID);

		for (ItemResource resource : allLifeGridResources) {
			if (resource.getLifeGridType() == item.getLifeGridType()
					&& resource.getQuality() == item.getResource().getQuality()
					&& resource.getLifeGridLevel() == item.getResource().getLifeGridLevel() - 1) {
				return resource;
			}
		}

		return null;
	}

	public CombiningResource getCombiningResource(String id) {
		return combiningResource.get(id, true);
	}

	public Storage<String, ItemResource> getItemResources() {
		return itemResources;
	}

	public EquipmentCreateSoulResource getCreateSoulResource(String createSoulKey) {
		return equipCreateSoulResources.get(createSoulKey, true);
	}

	public EquipmentCreateSoulResource getCreateSoulResourceByItemKey(String itemKey) {
		return equipCreateSoulResources.getUnique(EquipmentCreateSoulResource.ITEM_INDEX, itemKey);
	}
}
