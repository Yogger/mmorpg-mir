package com.mmorpg.mir.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

public final class CharCheckUtil {

	private static ThreadLocal<Pattern> NICKNAME_PATTERN = new ThreadLocal<Pattern>() {
		protected Pattern initialValue() {
			return Pattern.compile("^[\u4e00-\u9fa5a-zA-Z0-9]+$");
		}
	};

	public static boolean checkString(String str) {
		if (str == null || str.isEmpty()) {
			return false;
		}

		Pattern pattern = NICKNAME_PATTERN.get();
		return pattern.matcher(str.trim()).find();
	}

	public static String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
		byte[] bytes = text.getBytes("utf-8");
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		int i = 0;
		while (i < bytes.length) {
			short b = bytes[i];
			if (b > 0) {
				buffer.put(bytes[i++]);
				continue;
			}
			b += 256;
			if ((b ^ 0xC0) >> 4 == 0) {
				buffer.put(bytes, i, 2);
				i += 2;
			} else if ((b ^ 0xE0) >> 4 == 0) {
				buffer.put(bytes, i, 3);
				i += 3;
			} else if ((b ^ 0xF0) >> 4 == 0) {
				i += 4;
			} else {
				// 添加处理如b的指为-48等情况出现的死循环
				buffer.put(bytes[i++]);
				continue;
			}
		}

		buffer.flip();
		return new String(buffer.array(), "utf-8");
	}

}
