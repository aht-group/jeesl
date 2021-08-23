package org.jeesl.factory.sql.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.interfaces.model.module.rating.JeeslRating;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlWeightedRatingFactory
{
	final static Logger logger = LoggerFactory.getLogger(SqlWeightedRatingFactory.class);


	public static <L extends JeeslLang, D extends JeeslDescription,
					CATEGORY extends EjbWithId,
					DOMAIN extends EjbWithId,
					RATING extends JeeslRating<L,D,CATEGORY,DOMAIN>>
					String build(String table, String category, String domain, Collection<RATING> ratings)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("WITH weights (id, weight) AS (").append(weightValues(ratings)).append(")").append(System.lineSeparator());
		sb.append("SELECT ").append(domain).append("_id").append(System.lineSeparator());
		sb.append("       ,SUM(rating*weight) as rate").append(System.lineSeparator());
		sb.append("FROM ").append(table).append(System.lineSeparator());
		sb.append("INNER JOIN weights ON ").append(category.toString()).append("_id=weights.id").append(System.lineSeparator());
		sb.append("GROUP BY ").append(domain).append("_id").append(System.lineSeparator());
		sb.append("HAVING SUM(rating*weight)>0").append(System.lineSeparator());
		sb.append("ORDER BY SUM(rating*weight) DESC");
		sb.append(";");
		return sb.toString();
	}

	private static <L extends JeeslLang, D extends JeeslDescription,
				CATEGORY extends EjbWithId, DOMAIN extends EjbWithId,
				RATING extends JeeslRating<L,D,CATEGORY,DOMAIN>>
				String weightValues(Collection<RATING> ratings)
	{
		List<String> pairs = new ArrayList<String>();
		for(RATING rating : ratings)
		{
			pairs.add("("+rating.getCategory().getId()+","+rating.getRating()+")");
		}

		StringBuilder sb = new StringBuilder();
		sb.append("VALUES ").append(StringUtils.join(pairs,","));
		return sb.toString();
	}
}