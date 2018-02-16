package com.mmorpg.mir.model.complexstate;

import java.util.BitSet;

import com.alibaba.fastjson.annotation.JSONField;
import com.mmorpg.mir.utils.BitSetConvert;

public class ComplexState {
	private byte[] states;

	@JSONField(serialize = false)
	private transient BitSet bitSet;

	public static ComplexState valueOf() {
		ComplexState complexState = new ComplexState();
		complexState.states = new byte[2];
		complexState.setState(ComplexStateType.AUTO_ACCEPT_INVITE, ComplexStateType.AUTO_AGREED_JOIN,
				ComplexStateType.AUTO_UPGRADE_PROTECTURE, ComplexStateType.SHOW_ARTIFACT);
		return complexState;
	}

	public static ComplexState valueOf(int size) {
		ComplexState complexState = new ComplexState();
		complexState.states = new byte[(size / 8) + 1];
		return complexState;
	}

	@JSONField(serialize = false)
	public boolean isState(ComplexStateType... types) {
		if (bitSet == null) {
			bitSet = BitSetConvert.byteArray2BitSet(states);
		}
		for (ComplexStateType type : types) {
			if (!bitSet.get(type.getValue())) {
				return false;
			}
		}
		return true;
	}

	@JSONField(serialize = false)
	public void setState(ComplexStateType... types) {
		this.setState(true, true, types);
	}

	@JSONField(serialize = false)
	public void removeState(ComplexStateType... types) {
		this.setState(true, false, types);
	}

	@JSONField(serialize = false)
	public void setState(boolean persistent, boolean state, ComplexStateType... types) {
		if (bitSet == null) {
			bitSet = BitSetConvert.byteArray2BitSet(states);
		}
		for (ComplexStateType type : types) {
			bitSet.set(type.getValue(), state);
		}
		if (persistent) {
			bitSet2ByteArray();
		}
	}

	@JSONField(serialize = false)
	public void bitSet2ByteArray() {
		if (bitSet == null) {
			bitSet = new BitSet();
		}
		this.states = BitSetConvert.bitSet2ByteArray(bitSet);
	}

	public byte[] getStates() {
		return states;
	}

	public void setStates(byte[] states) {
		this.states = states;
	}

	@Override
	public String toString() {
		if (bitSet == null) {
			bitSet = BitSetConvert.byteArray2BitSet(states);
		}
		StringBuilder builder = new StringBuilder();
		for (ComplexStateType type : ComplexStateType.values()) {
			builder.append("[" + type.name() + ":" + (this.bitSet.get(type.getValue()) ? 1 : 0) + "]");
		}
		return builder.toString();
	}

	public static void main(String[] args) {
		// System.out.println(Byte.MIN_VALUE);
		// ComplexState cs = ComplexState.valueOf();
		// System.out.println(JsonUtils.object2String(cs));
		// cs.setState(ComplexStateType.GROUP);
		// System.out.println(cs.toString());
		// // System.out.println((System.currentTimeMillis() - now));
		// String json = JsonUtils.object2String(cs);
		// System.out.println(json);
		// ComplexState newCs = JsonUtils.string2Object(json,
		// ComplexState.class);
		// System.out.println(newCs.toString());
		//
		// SimpleScheduler simpleScheduler = new SimpleScheduler();
		// ScheduledFuture future = simpleScheduler.schedule(new ScheduledTask()
		// {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public String getName() {
		// // TODO Auto-generated method stub
		// return null;
		// }
		// }, new Date(System.currentTimeMillis()));
		// future.cancel(false);

		// try {
		// // Method method = ComplexState.class.getMethod("test");
		// ComplexState cs = ComplexState.valueOf();
		// long now = System.currentTimeMillis();
		// int count = 1000000;
		// Method method = ComplexState.class.getMethod("test");
		// for (int i = 0; i < count; i++) {
		// method.invoke(cs);
		// }
		// System.out.println((System.currentTimeMillis() - now));
		//
		// now = System.currentTimeMillis();
		//
		// for (int i = 0; i < count; i++) {
		// cs.test();
		// }
		// System.out.println((System.currentTimeMillis() - now));
		// } catch (SecurityException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

}
