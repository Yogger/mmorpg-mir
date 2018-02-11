package com.mmorpg.mir.model.resourcecheck.handle;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class FormulaCheck extends ResourceCheckHandle {
	@Static
	private Storage<String, Formula> formulas;

	@Override
	public Class<?> getResourceClass() {
		return Formula.class;
	}

	@Override
	public void check() {
		for (Formula cf : formulas.getAll()) {
			cf.getId();
			// cf.calculate(null);
		}
	}

}
