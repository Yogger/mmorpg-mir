package com.mmorpg.mir.model.world;

public final class RoadGrid implements Comparable<RoadGrid> {
	private int weight;

	private int father = -1;

	private Grid grid;

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getFather() {
		return father;
	}

	public void setFather(int father) {
		this.father = father;
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((grid == null) ? 0 : grid.hashCode());
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
		RoadGrid other = (RoadGrid) obj;
		if (grid == null) {
			if (other.grid != null)
				return false;
		} else if (!grid.equals(other.grid))
			return false;
		return true;
	}

	@Override
	public int compareTo(RoadGrid o) {
		int result = weight - o.weight;
		if (result == 0) {
			result = hashCode() - o.hashCode();
		}
		return result;
	}

	@Override
	public String toString() {
		return grid.getX() + " " + grid.getY() + " " + weight;
	}
}
