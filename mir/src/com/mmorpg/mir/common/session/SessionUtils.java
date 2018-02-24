package com.mmorpg.mir.common.session;

import com.windforce.core.Wsession;

public class SessionUtils {

	private static String LOGIN_AUTH = "LOGIN_AUTH";

	public static boolean isLoginAuth(Wsession session) {
		if (session.getAttributes().containsKey(LOGIN_AUTH)) {
			return true;
		}
		return false;
	}

	public static void setLoginAuth(Wsession session) {
		session.getAttributes().put(LOGIN_AUTH, new Object());
	}
}
