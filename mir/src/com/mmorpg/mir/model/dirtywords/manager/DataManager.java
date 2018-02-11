package com.mmorpg.mir.model.dirtywords.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;

/**
 * 敏感词原始数据管理器
 * 
 * @author YUTAO
 */
@Component
public class DataManager implements IDataManager {

	/**
	 * 把敏感词库加载到内存并初始化过滤模型
	 * 
	 * @return
	 */
	public Collection<String> loadDirtyWords(String path) {
		Set<String> words = new HashSet<String>();
		InputStream in = DataManager.class.getClassLoader().getResourceAsStream(path);
		InputStreamReader inReader = new InputStreamReader(in, Charset.forName("UTF-8"));
		BufferedReader bf = new BufferedReader(inReader);
		String tempString = null;
		try {
			while ((tempString = bf.readLine()) != null) {
				String dirtyWord = parse(tempString);
				if (dirtyWord != null && dirtyWord.length() > 0)
					words.add(dirtyWord);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bf.close();
				inReader.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return words;
	}

	/**
	 * 当我们使用 unicode编码时--由于我们的过滤词库有可能由于用记事本打开会在文本开始的时候 添加一个标识(0宽度的空格符,65279
	 * 该标识标识文本使用的是unicode编码)导致第一个词不能匹配所以这里我们要把那个字符去掉
	 * 
	 * @param input
	 * @return
	 */
	public String parse(String input) {
		if (input == null) {
			return null;
		}
		char chars[] = input.trim().toCharArray();
		if (!ArrayUtils.isEmpty(chars)) {
			if (chars[0] == 65279) {
				if (chars.length > 1) {
					char c[] = new char[chars.length - 1];
					System.arraycopy(chars, 1, c, 0, c.length);
					return new String(c);
				} else
					return null;
			}
		}
		return input;
	}
}
