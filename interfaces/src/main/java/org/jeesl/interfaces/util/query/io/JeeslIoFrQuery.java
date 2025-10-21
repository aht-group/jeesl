package org.jeesl.interfaces.util.query.io;

import java.util.List;

import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorageType;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;

public interface JeeslIoFrQuery <STORAGE extends JeeslFileStorage<?,?,?,STYPE,?>,
								STYPE extends JeeslFileStorageType<?,?,STYPE,?>,
								CONTAINER extends JeeslFileContainer<?,?>>
					extends JeeslCoreQuery
{
//	void x();
	List<STORAGE> getIoFrStorages();
	List<STYPE> getIoFrStorageTypes();
	List<CONTAINER> getIoFrContainers();
}