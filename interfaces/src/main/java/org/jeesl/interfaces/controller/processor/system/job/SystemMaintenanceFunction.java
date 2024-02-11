package org.jeesl.interfaces.controller.processor.system.job;

import java.util.List;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

@FunctionalInterface
public interface SystemMaintenanceFunction <T extends EjbWithId>
{
	List<T> find();
}