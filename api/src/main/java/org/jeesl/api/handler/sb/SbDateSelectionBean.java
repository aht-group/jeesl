package org.jeesl.api.handler.sb;

import java.io.Serializable;

public interface SbDateSelectionBean extends Serializable
{
	void callbackDateChanged(SbDateSelection handler);
}