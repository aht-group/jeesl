package org.jeesl.api.bean;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;

public interface JeeslLabelBean<RE extends JeeslRevisionEntity<?,?,?,?,?,?>> extends Serializable
{	
	void reload(RE re);
	List<RE> allEntities();
}