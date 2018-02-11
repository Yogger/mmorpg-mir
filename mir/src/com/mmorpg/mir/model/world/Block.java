package com.mmorpg.mir.model.world;

/**
 * 地图上动态的阻挡区
 * 
 * @author Kuang Hao
 * @since v1.0 2014-11-17
 * 
 */
public class Block {
	private int x;
	private int y;

	public static Block valueOf(int x, int y) {
		Block block = new Block();
		block.x = x;
		block.y = y;
		return block;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Block other = (Block) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
