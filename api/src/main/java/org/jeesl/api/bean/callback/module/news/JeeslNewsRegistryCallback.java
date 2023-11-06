package org.jeesl.api.bean.callback.module.news;

import java.io.Serializable;

public interface JeeslNewsRegistryCallback extends Serializable
{	
	void callbackPostAddItem();
}