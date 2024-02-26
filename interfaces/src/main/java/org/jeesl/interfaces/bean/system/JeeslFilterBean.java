package org.jeesl.interfaces.bean.system;

import java.io.Serializable;

import org.jeesl.interfaces.controller.handler.system.JeeslFilterHandler;
import org.jeesl.interfaces.model.system.filter.JeeslFilter;

public interface JeeslFilterBean <FILTER extends JeeslFilter<?,FILTER,?,?,?>> extends Serializable
{
	void applyFilter(JeeslFilterHandler handler, FILTER filter);
	void storeFilter(JeeslFilterHandler handler, FILTER filter);
}