package org.jeesl.interfaces.util.query.io;

import java.util.List;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.model.ejb.io.db.JeeslCqLiteral;

public interface JeeslIoLabelQuery<ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>
>
{
	List<JeeslCqLiteral> getLiterals();
	JeeslIoLabelQuery<ENTITY> add(JeeslCqLiteral literal);
}