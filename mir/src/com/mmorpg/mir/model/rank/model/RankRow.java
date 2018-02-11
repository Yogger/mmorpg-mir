package com.mmorpg.mir.model.rank.model;

import com.windforce.common.event.event.IEvent;

public abstract class RankRow implements Comparable<RankRow> {

	/** 某一行内容拥有者的唯一ID */
	protected long objId;
	/** 某一行内容拥有者的名称 */
	protected String name;

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public abstract int compareEvent(IEvent event);
	
	public abstract void changeByEvent(IEvent event);
	
	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + (int) (objId ^ (objId >>> 32));
	    return result;
    }

	@Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    RankRow other = (RankRow) obj;
	    if (objId != other.objId)
		    return false;
	    return true;
    }
}
