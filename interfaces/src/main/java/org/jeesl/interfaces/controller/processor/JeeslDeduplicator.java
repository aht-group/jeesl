package org.jeesl.interfaces.controller.processor;

import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslDeduplicator <RE extends JeeslRevisionEntity<?,?,?,?,?,?>, ICON extends JeeslStatus<?,?,ICON>, EJB extends EjbWithId>
{
	EJB getEjbOriginal();
	EJB getEjbDuplicate();
	
	List<RE> getEntities();
	
	Map<RE,Integer> getMapOriginal();
	Map<RE,Integer> getMapDuplicate();
	Map<RE,List<ICON>> getMapAction();
}
