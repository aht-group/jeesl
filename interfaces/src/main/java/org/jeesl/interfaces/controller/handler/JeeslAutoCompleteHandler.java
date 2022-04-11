package org.jeesl.interfaces.controller.handler;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslAutoCompleteHandler<T extends EjbWithId> extends Serializable
{
	List<T> acListByQuery(Class<T> c, String query);
}