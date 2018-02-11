package com.mmorpg.mir.model.skill.effecttemplate;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class EffectTemplateManager {

	@Static
	private Storage<Integer, EffectTemplateResource> effectTemplateResource;

	private static EffectTemplateManager self;

	public static EffectTemplateManager getInstance() {
		return self;
	}

	@PostConstruct
	public void init() {
		self = this;
	}

	public EffectTemplate createEffectTemplate(int effectTemplateId) {
		EffectTemplateResource er = getEffectTemplateResource().get(effectTemplateId, true);
		EffectTemplate template = er.getEffectTemplateType().create();
		template.init(er);
		return template;
	}

	public Storage<Integer, EffectTemplateResource> getEffectTemplateResource() {
		return effectTemplateResource;
	}

	public void setEffectTemplateResource(Storage<Integer, EffectTemplateResource> effectTemplateResource) {
		this.effectTemplateResource = effectTemplateResource;
	}

}
