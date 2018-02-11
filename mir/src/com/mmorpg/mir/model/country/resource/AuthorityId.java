package com.mmorpg.mir.model.country.resource;

public enum AuthorityId {

	A1("A1"), A2("A2");

	private String name;

	private AuthorityId(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
