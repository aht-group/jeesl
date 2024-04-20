package org.jeesl.interfaces.util.query.jpa;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;
import org.jeesl.model.ejb.io.db.JeeslCqRootFetch;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslRootFetchQuery extends Serializable
{
	List<JeeslCqRootFetch> getRootFetches();
}