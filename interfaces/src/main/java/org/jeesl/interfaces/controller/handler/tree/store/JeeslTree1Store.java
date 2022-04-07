package org.jeesl.interfaces.controller.handler.tree.store;

import java.io.Serializable;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslTree1Store<L1 extends EjbWithId> extends Serializable
{
	void storeTreeLevel1(L1 l1);
}