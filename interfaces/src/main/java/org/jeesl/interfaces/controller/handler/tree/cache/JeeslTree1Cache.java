package org.jeesl.interfaces.controller.handler.tree.cache;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslTree1Cache<L1 extends EjbWithId> extends Serializable
{
	List<L1> getCachedL1();
}