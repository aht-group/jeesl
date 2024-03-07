package org.jeesl.interfaces.util.query;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;
import org.jeesl.model.ejb.io.db.CqDate;
import org.jeesl.model.ejb.io.db.CqFetch;
import org.jeesl.model.ejb.io.db.CqId;
import org.jeesl.model.ejb.io.db.CqLiteral;
import org.jeesl.model.ejb.io.db.CqOrdering;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslCoreQuery extends Serializable
{
	Boolean getTupleLoad();
	Integer getFirstResult();
	Integer getMaxResults();
}