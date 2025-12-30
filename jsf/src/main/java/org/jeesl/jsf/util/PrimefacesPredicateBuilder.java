package org.jeesl.jsf.util;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jeesl.interfaces.util.query.cq.JeeslCqLiteralQuery;
import org.jeesl.util.query.cq.CqLiteral;
import org.primefaces.model.FilterMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrimefacesPredicateBuilder
{
	final static Logger logger = LoggerFactory.getLogger(PrimefacesPredicateBuilder.class);
	
	public static void apply(Map<String,FilterMeta> filters, JeeslCqLiteralQuery query)
	{
		if(Objects.nonNull(filters))
		{
			for(FilterMeta meta : filters.values().stream().filter(m -> Objects.nonNull(m.getFilterValue())).collect(Collectors.toList()))
			{
				logger.info("Filter field:{} value:{}",meta.getFilterField(),meta.getFilterValue());
				query.addCqLiteral(CqLiteral.contains(meta.getFilterValue().toString(),meta.getFilterField()));
			}
		}
	}
}