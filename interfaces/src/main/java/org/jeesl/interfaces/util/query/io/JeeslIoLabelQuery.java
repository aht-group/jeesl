package org.jeesl.interfaces.util.query.io;

import java.util.List;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;

public interface JeeslIoLabelQuery<ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>>
		extends JeeslCoreQuery
{
	List<ENTITY> getIoLabelEntityOwner();
	List<ENTITY> getIoLabelEntityReferenced();
}