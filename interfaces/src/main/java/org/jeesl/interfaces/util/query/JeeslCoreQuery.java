package org.jeesl.interfaces.util.query;

import java.io.Serializable;

import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;
import org.jeesl.interfaces.util.query.cq.JeeslCqLiteralQuery;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslCoreQuery extends Serializable,JeeslCqLiteralQuery
{
	Boolean getDistinct();
	Integer getFirstResult();
	Integer getMaxResults();
	
	Boolean getTupleLoad();
}