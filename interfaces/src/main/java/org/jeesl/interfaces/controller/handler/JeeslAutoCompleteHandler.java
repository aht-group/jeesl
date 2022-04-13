package org.jeesl.interfaces.controller.handler;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

//jeesl.highlight:showcase
public interface JeeslAutoCompleteHandler<T extends EjbWithId> extends Serializable
{
	List<T> autoCompleteListByQuery(Class<T> c, String query);
}
//jeesl.highlight:showcase