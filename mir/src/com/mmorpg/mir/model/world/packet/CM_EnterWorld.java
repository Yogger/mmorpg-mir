package com.mmorpg.mir.model.world.packet;

import java.lang.reflect.Method;

public class CM_EnterWorld {
	public static void main(String[] args) {
		Object object = new CM_EnterWorld();
		System.out.println(isEmptyClass(object));
	}

	private static boolean isEmptyClass(Object object) {
		for (Method m : object.getClass().getMethods()) {
			System.out.println(m.getName());
			if (m.getName().startsWith("get") && !m.getName().startsWith("getClass")) {
				return false;
			}
		}
		return true;
	}
}
