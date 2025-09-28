package org.jeesl.interfaces.util.query;

import java.io.Serializable;

import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;
import org.jeesl.interfaces.util.query.cq.JeeslCqAggregationQuery;
import org.jeesl.interfaces.util.query.cq.JeeslCqBooleanQuery;
import org.jeesl.interfaces.util.query.cq.JeeslCqDateQuery;
import org.jeesl.interfaces.util.query.cq.JeeslCqDoubleQuery;
import org.jeesl.interfaces.util.query.cq.JeeslCqEntityQuery;
import org.jeesl.interfaces.util.query.cq.JeeslCqIntegerQuery;
import org.jeesl.interfaces.util.query.cq.JeeslCqLiteralQuery;
import org.jeesl.interfaces.util.query.cq.JeeslCqLongQuery;
import org.jeesl.interfaces.util.query.cq.JeeslCqRootFetchQuery;
import org.jeesl.interfaces.util.query.cq.JeeslCqTimeQuery;
import org.jeesl.interfaces.util.query.jpa.JeeslOrderingQuery;
import org.jeesl.interfaces.util.query.jpa.JeeslPaginationQuery;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslCoreQuery extends Serializable,
									JeeslPaginationQuery,JeeslOrderingQuery,
									JeeslCqRootFetchQuery,
									JeeslCqEntityQuery,
									JeeslCqLiteralQuery,
									JeeslCqBooleanQuery,JeeslCqIntegerQuery,JeeslCqLongQuery,JeeslCqDoubleQuery,
									JeeslCqDateQuery,JeeslCqTimeQuery,
									JeeslCqAggregationQuery
{
	Boolean getDistinct();
	Boolean getTupleLoad();
}