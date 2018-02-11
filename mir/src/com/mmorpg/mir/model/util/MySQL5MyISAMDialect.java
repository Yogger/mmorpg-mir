package com.mmorpg.mir.model.util;

import org.hibernate.dialect.MySQLDialect;

public class MySQL5MyISAMDialect extends MySQLDialect{

	public String getTableTypeString() {
		return " ENGINE=MyISAM";
	}

	public boolean dropConstraints() {
		return false;
	}
	
}
