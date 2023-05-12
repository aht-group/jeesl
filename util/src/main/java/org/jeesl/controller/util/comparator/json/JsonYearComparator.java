package org.jeesl.controller.util.comparator.json;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.model.json.util.time.JsonYear;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonYearComparator implements Comparator<JsonYear>
{
	final static Logger logger = LoggerFactory.getLogger(JsonYearComparator.class);

	public JsonYearComparator()
	{
		
	}
	
	public int compare(JsonYear a, JsonYear b)
    {
		  CompareToBuilder ctb = new CompareToBuilder();
		  ctb.append(a.getYear(), b.getYear());
		  return ctb.toComparison();
    }
}