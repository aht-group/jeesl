package org.jeesl.factory.sql.module;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.factory.sql.SqlFactory;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlTimeSeriesFactory <TS extends JeeslTimeSeries<?,TS,?,?,?>,
									DATA extends JeeslTsData<TS,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(SqlTimeSeriesFactory.class);

	private final Class<DATA> cData;
	
	public SqlTimeSeriesFactory(Class<DATA> cData)
	{
		this.cData=cData;
	}
	
	public String lastData(List<TS> list)
	{
		boolean newLine = false;
		String alias = "d";
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT");
		SqlFactory.distinct(sb,alias,newLine,JeeslTsData.Attributes.timeSeries);
		sb.append(" d.id ");
		SqlFactory.newLine(newLine,sb);
		
		SqlFactory.from(sb,cData,alias, newLine);
		
		sb.append(" WHERE d.timeSeries_id IN (");
		List<Long> ids = new ArrayList<>();
		for(TS ts : list) {ids.add(ts.getId());}
		sb.append(StringUtils.join(ids,",")).append(")");
		
		SqlFactory.newLine(newLine,sb);
		sb.append(" ORDER BY d.timeSeries_id ASC, d.record DESC;");
		SqlFactory.newLine(newLine,sb);
		return sb.toString();
	}
}