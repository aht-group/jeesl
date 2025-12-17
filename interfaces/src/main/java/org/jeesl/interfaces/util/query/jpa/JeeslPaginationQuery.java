package org.jeesl.interfaces.util.query.jpa;

import java.io.Serializable;

import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslPaginationQuery extends Serializable
{
	void setFirstResult(Integer first);
	
	Integer getFirstResult();
	Integer getMaxResults();
}