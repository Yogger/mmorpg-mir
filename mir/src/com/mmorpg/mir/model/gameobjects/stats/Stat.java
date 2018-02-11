package com.mmorpg.mir.model.gameobjects.stats;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.utility.JsonUtils;

public class Stat {

	public final static int CARDINAL = 10000;

	private StatEnum type;

	private int moduleKey;

	private long valueA;

	private long valueB;

	private long valueC;

	public Stat() {

	}

	@JsonIgnore
	public Stat getNewProperty() {
		Stat property = new Stat();
		property.setModuleKey(moduleKey);
		property.type = type;
		property.merge(this);
		return property;
	}

	public Stat(StatEnum type, long valueA, long valueB, long valueC) {
		this.type = type;
		this.valueA = valueA;
		this.valueB = valueB;
		this.valueC = valueC;
	}

	public Stat(StatEnum type, long valueA, long valueB, long valueC, int moduleKey) {
		this.type = type;
		this.valueA = valueA;
		this.valueB = valueB;
		this.valueC = valueC;
		this.moduleKey = moduleKey;
	}

	/**
	 * 合并两个property
	 * 
	 * @param pro
	 */
	public void merge(Stat pro) {
		this.increaseValueA(pro.getValueA());
		this.increaseValueB(pro.getValueB());
		this.increaseValueC(pro.getValueC());
	}

	public void multipMerge(double mul) {
		this.valueA = (long) Math.ceil((this.valueA * mul));
		this.valueB = (long) Math.ceil((this.valueB * mul));
		this.valueC = (long) Math.ceil((this.valueC * mul));
	}

	@JsonIgnore
	public long getValue() {
		return (long) Math.ceil(((valueA * 1.0 * (CARDINAL + valueC) * 1.0 / CARDINAL) + valueB));
	}

	public static void main(String[] args) {
		System.out.println(JsonUtils.object2String(new Stat(StatEnum.MAXHP, 100, 20, 100, 1)));
	}

	public StatEnum getType() {
		return type;
	}

	public long increaseValueA(long valueA) {
		long old = this.valueA;
		this.valueA += valueA;
		// if (this.valueA < 0) {
		// this.valueA = 0;
		// }
		return this.valueA - old;
	}

	public long increaseValueB(long valueB) {
		long old = this.valueB;
		this.valueB += valueB;
		// if (this.valueB < 0) {
		// this.valueB = 0;
		// }
		return this.valueB - old;
	}

	public long increaseValueC(long valueC) {
		long old = this.valueC;
		this.valueC += valueC;
		// if (this.valueC < (-CARDINAL)) {
		// this.valueC = (-CARDINAL);
		// }
		return this.valueC - old;
	}

	public long getValueA() {
		return valueA;
	}

	public void setValueA(long valueA) {
		this.valueA = valueA;
	}

	public long getValueB() {
		return valueB;
	}

	public void setValueB(long valueB) {
		this.valueB = valueB;
	}

	public long getValueC() {
		return valueC;
	}

	public void setValueC(long valueC) {
		this.valueC = valueC;
	}

	public void setType(StatEnum type) {
		this.type = type;
	}

	public int getModuleKey() {
		return moduleKey;
	}

	public void setModuleKey(int moduleKey) {
		this.moduleKey = moduleKey;
	}
	
	public Stat copyOf() {
		return new Stat(getType(), getValueA(), getValueB(), getValueC(), getModuleKey());
	}
	
	@Override
	public String toString() {
		return String.format("moduleKey : [%s] type : [%s] valueA : [%d] valueB : [%d] valueC : [%d]", moduleKey,
				type.name(), valueA, valueB, valueC);
	}
}
