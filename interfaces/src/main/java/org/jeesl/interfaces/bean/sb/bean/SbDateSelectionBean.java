package org.jeesl.interfaces.bean.sb.bean;

import java.io.Serializable;

import org.jeesl.interfaces.bean.sb.handler.SbDateSelection;

public interface SbDateSelectionBean extends Serializable
{
	void callbackDateChanged(SbDateSelection handler);
}