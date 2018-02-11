package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.serverstate.ServerState;

public class MergeDayJudgeCondition extends AbstractCoreCondition {
	private Operator op;
	private int low;
	private int high;
	
	@Override
	public boolean verify(Object object) {	
		int mergedDays = ServerState.getInstance().hasMergedDays();
		if(mergedDays == -1){
			return false;
		}
		if(op != null){
			if (op == Operator.EQUAL) {
				return mergedDays == this.value;
			} else if (op == Operator.GREATER) {
				return mergedDays > this.value;
			} else if (op == Operator.GREATER_EQUAL) {
				return mergedDays >= this.value;
			} else if (op == Operator.LESS) {
				return mergedDays < this.value;
			} else if (op == Operator.LESS_EQUAL) {
				return mergedDays <= this.value;
			}
		}else{
			if(mergedDays >= low && mergedDays<=high){
				return true;
			}
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		if(resource.getOperator() != null){
			this.op = resource.getOperatorEnum();
		}
		this.low = resource.getLow();
		this.high = resource.getHigh();
	}
}
