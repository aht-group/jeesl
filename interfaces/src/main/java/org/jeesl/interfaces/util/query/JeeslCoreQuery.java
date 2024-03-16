package org.jeesl.interfaces.util.query;

import java.io.Serializable;

import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslCoreQuery extends Serializable
{
	Boolean getDistinct();
	Boolean getTupleLoad();
	Integer getFirstResult();
	Integer getMaxResults();
}