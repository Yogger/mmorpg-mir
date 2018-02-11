package com.mmorpg.mir.model.common;


public class Pair<F, S>{
	
	private F first;
	
	private S second;
	
	@Override
	public int hashCode() {
		return first.hashCode() + second.hashCode();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (!(obj instanceof Pair)) {
			return false;
		}
		
		Pair<F, S> parm = (Pair<F, S>)obj;
		return first.equals(parm.getFirst()) && second.equals(parm.getSecond());
	}
	
	public Pair(F f, S s) {
		this.first = f;
		this.second = s;
	}

	public F getFirst() {
		return first;
	}

	public void setFirst(F first) {
		this.first = first;
	}

	public S getSecond() {
		return second;
	}

	public void setSecond(S second) {
		this.second = second;
	}
	
}
