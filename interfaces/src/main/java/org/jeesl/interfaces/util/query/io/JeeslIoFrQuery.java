package org.jeesl.interfaces.util.query.io;

import java.util.List;

import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;

public interface JeeslIoFrQuery <STORAGE extends JeeslFileStorage<?,?,?,?,?>,
								CONTAINER extends JeeslFileContainer<?,?>>
					extends JeeslCoreQuery
{
//	void x();
	List<STORAGE> getIoFrStorages();
	List<CONTAINER> getIoFrContainers();
}