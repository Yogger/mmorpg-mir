package com.mmorpg.mir.model.skill.resource.grid;

/**
 * 地图方格
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-11
 * 
 */
public class Grid {
	private int x;
	private int y;

	public static Grid valueOf(int x, int y) {
		Grid grid = new Grid();
		grid.x = x;
		grid.y = y;
		return grid;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		}
		if (obj instanceof Grid) {
			Grid target = (Grid) obj;
			if (this.x == target.x && this.y == target.y) {
				return true;
			}
		}
		return false;
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
