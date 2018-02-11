package com.mmorpg.mir.model.resourcecheck.handle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.mmorpg.mir.model.dirtywords.model.WordsType;
import com.mmorpg.mir.model.dirtywords.service.DirtyWordsManager;
import com.windforce.common.utility.JsonUtils;

public class CreateRoleCheckHandler {

	
	public static CreateRoleCheckHandler Instance = new CreateRoleCheckHandler();
	
	void init() {
		Instance = this;
	}
	

	public static CreateRoleCheckHandler getInstance() {
		return Instance;
	}

	public void check() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("c1.txt")));
			BufferedReader reader2 = new BufferedReader(new FileReader(new File("c2.txt")));
			BufferedReader reader3 = new BufferedReader(new FileReader(new File("c3.txt")));
			String line = reader.readLine();
			String line2 = reader2.readLine();
			String line3 = reader3.readLine();
			String[] c1s = JsonUtils.string2Array(line, String.class);
			String[] c2s = JsonUtils.string2Array(line2, String.class);
			String[] c3s = JsonUtils.string2Array(line3, String.class);
			ArrayList<String> infos = new ArrayList<String>(1000);
			
			for (String s: c1s) {
				if (DirtyWordsManager.getInstance().containsWords(s, WordsType.ROLEWORDS)) {
					infos.add("c1 " + s);
				}
			}
			
			for (String s: c2s) {
				if (DirtyWordsManager.getInstance().containsWords(s, WordsType.ROLEWORDS)) {
					infos.add("c2 " + s);
				}
			}
			
			for (String s: c3s) {
				if (DirtyWordsManager.getInstance().containsWords(s, WordsType.ROLEWORDS)) {
					infos.add("c3 " + s);
				}
			}
			
			for (int i = 0; i < c1s.length; i++) {
				for (int j = 0; j < c2s.length; j++) {
					String s = c1s[i] + c2s[j];
					if (DirtyWordsManager.getInstance().containsWords(s, WordsType.ROLEWORDS)) {
						infos.add("c1 + c2 " + c1s[i] + " " + c2s[j]);
					}
				}
			}
			
			for (int i = 0; i < c1s.length; i++) {
				for (int j = 0; j < c3s.length; j++) {
					String s = c1s[i] + c3s[j];
					if (DirtyWordsManager.getInstance().containsWords(s, WordsType.ROLEWORDS)) {
						infos.add("c1 + c3 " + c1s[i] + " " + c3s[j]);
					}
				}
			}
			
			FileWriter writer = new FileWriter(new File("output.txt"));
			for (String s: infos) {
				writer.write(s + "\n");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
