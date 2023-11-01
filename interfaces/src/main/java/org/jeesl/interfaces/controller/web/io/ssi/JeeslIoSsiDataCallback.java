package org.jeesl.interfaces.controller.web.io.ssi;

import java.io.Serializable;

public interface JeeslIoSsiDataCallback extends Serializable
{
	void ssiDataLoaded();
	void ssiDataFilterChanged();
}