package com.mmorpg.mir.model.welfare.manager;

import java.util.Collection;

import com.mmorpg.mir.model.welfare.resource.SignResource;

public interface ISignManager {
	SignResource getSignResource(int days, boolean throwException);

	Collection<SignResource> getAllSignResources();
}
