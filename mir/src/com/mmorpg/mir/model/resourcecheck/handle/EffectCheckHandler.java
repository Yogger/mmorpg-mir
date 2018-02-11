package com.mmorpg.mir.model.resourcecheck.handle;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.skill.effecttemplate.EffectTemplateType;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class EffectCheckHandler extends ResourceCheckHandle {

	@Static
	private Storage<Integer, EffectTemplateResource> effectStorage;

	@Override
	public Class<?> getResourceClass() {
		return EffectTemplateResource.class;
	}

	@Override
	public void check() {
		ArrayList<Integer> error = new ArrayList<Integer>();
		for (EffectTemplateResource r : effectStorage.getAll()) {
			if (r.getEffectTemplateType() == EffectTemplateType.DAMAGEEFFECT
					&& (r.getPercents() == null || r.getPercents().length == 0)) {
				error.add(r.getEffectTemplateId());
			}
		}

		for (Integer e : error) {
			System.out.println(e);
		}
	}

}
