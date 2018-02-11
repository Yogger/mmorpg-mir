package com.mmorpg.mir.model.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.mmorpg.mir.model.soul.model.Soul;
import com.mmorpg.mir.model.soul.model.SoulVO;

public class ValueOfUtil {

	private ValueOfUtil() {
	}

	public interface ValueOf {
		<T> void set(T result, Object... values);
	}

	/**
	 * 实例化一个对象
	 * 
	 * @param resultClazz
	 *            返回对象的class类型
	 * @param valueOf
	 *            需要作出的操作函数所在的接口
	 * @param values
	 *            初始化需要的数值参数
	 * @return resultClazz.newInstance();
	 */
	public static <T> T valueOf(Class<T> resultClazz, ValueOf valueOf, Object... values) {
		T result = null;
		try {
			result = resultClazz.newInstance();
			if (valueOf != null) {
				valueOf.set(result, values);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 根据一个目标对象,创建自己, 例如:根据po对象创建vo实例,反之也行
	 * 忽略final和static修饰的字段,如果需要初始化final域或者改变static字段的值 请额外处理
	 * 
	 * @param resultClazz
	 *            将要返回的对象 class类型
	 * @param obj
	 *            传递进来的对象
	 * @return resultClazz.newInstance()
	 */
	public static <T> T valueOf(Class<T> resultClazz, Object obj) {
		T result = null;
		Class<?> ObjClazz = obj.getClass();
		try {
			result = resultClazz.newInstance();
			Field[] tFields = resultClazz.getDeclaredFields();
			for (int i = 0; i < tFields.length; i++) {
				Field tfField = tFields[i];
				int modifiers = tfField.getModifiers();
				if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
					continue;
				}
				tfField.setAccessible(true);
				String tName = tfField.getName();
				Field target = ObjClazz.getDeclaredField(tName);
				target.setAccessible(true);
				Object targetValue = target.get(obj);
				tfField.set(result, targetValue);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 根据一个目标map{name,value},创建实例 map的key为字段名字,value为值
	 * 例如在把po转vo时候,不需要使用po对象的全部字段或者vo有比po更多的字段时 用map来储存将要传递给vo的字段和值
	 * 忽略final和static修饰的字段,如果需要初始化final域或者改变static字段的值 请额外处理
	 * 
	 * @param resultClazz
	 *            将要返回的对象 class类型
	 * @param valueMap
	 *            传递进来的map对象
	 * @return resultClazz.newInstance()
	 */
	public static <T> T valueOf(Class<T> resultClazz, Map<String, Object> valueMap) {
		T result = null;
		try {
			result = resultClazz.newInstance();
			Field[] tFields = resultClazz.getDeclaredFields();
			for (int i = 0; i < tFields.length; i++) {
				Field tfField = tFields[i];
				int modifiers = tfField.getModifiers();
				if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
					continue;
				}
				tfField.setAccessible(true);
				String tName = tfField.getName();
				Object targetValue = valueMap.get(tName);
				tfField.set(result, targetValue);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static String toString(Object obj) {
		StringBuilder builder = new StringBuilder();
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			int i = 0;
			for (Field field : fields) {
				field.setAccessible(true);
				String name = field.getName();
				Object value = field.get(obj);
				builder.append("{");
				builder.append(name).append("=").append(value);
				builder.append("}");
				if (i++ < fields.length - 1) {
					builder.append(",");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return builder.toString();
	}

	public static Map<String, Object> toMap(Object obj) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String name = field.getName();
				Object value = field.get(obj);
				resultMap.put(name, value);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return resultMap;
	}

	// TEST--------------------------------------------------------------------------------------------------------------------------
	public static void main(String[] args) {
		SoulVO soulVO = new SoulVO();
		soulVO.setClearTime(System.currentTimeMillis());
		soulVO.setLevel(1000);
		soulVO.setOpen(true);
		Soul soul = ValueOfUtil.valueOf(Soul.class, soulVO);
		System.out.println(toString(soul));

		// -------------------
		Map<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put("clearTime", System.currentTimeMillis());
		valueMap.put("level", 321456);
		valueMap.put("nowBlessValue", 123456);
		valueMap.put("open", true);
		soul = valueOf(Soul.class, valueMap);
		System.out.println(toString(soul));

		// ---------------------
		Object[] values = new Object[] { System.currentTimeMillis() };
		soul = valueOf(Soul.class, new ValueOf() {

			@Override
			public <T> void set(T result, Object... values) {
				Soul s = (Soul) result;
				s.setClearTime(((Number) values[0]).longValue());
			}
		}, values);

		System.out.println(toString(soul));
		System.out.println(toMap(soul));
	}

}
