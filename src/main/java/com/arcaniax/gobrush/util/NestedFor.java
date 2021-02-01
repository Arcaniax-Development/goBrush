package com.arcaniax.gobrush.util;

public class NestedFor {

	public interface IAction {
		public void act(int[] indices);
	}

	private final int lo;
	private final int hi;
	private final IAction action;

	public NestedFor(int lo, int hi, IAction action) {
		this.lo = lo;
		this.hi = hi;
		this.action = action;
	}

	public void nFor(int depth) {
		n_for(0, new int[0], depth);
	}

	private void n_for(int level, int[] indices, int maxLevel) {
		if (level == maxLevel) {
			action.act(indices);
		} else {
			int newLevel = level + 1;
			int[] newIndices = new int[newLevel];
			System.arraycopy(indices, 0, newIndices, 0, level);
			newIndices[level] = lo;

			while(newIndices[level] < hi) {
				n_for(newLevel, newIndices, maxLevel);
				++newIndices[level];
			}

		}
	}
}
