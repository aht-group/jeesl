package org.jeesl.interfaces.util.query;

import java.io.Serializable;

import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;
import org.jeesl.interfaces.util.query.cq.JeeslCqLiteralQuery;
import org.jeesl.interfaces.util.query.cq.JeeslCqLongQuery;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslCoreQuery extends Serializable,
									JeeslCqLiteralQuery,JeeslCqLongQuery
{
	Boolean getDistinct();
	Integer getFirstResult();
	Integer getMaxResults();
	
	Boolean getTupleLoad();
}