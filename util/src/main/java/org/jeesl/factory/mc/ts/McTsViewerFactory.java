package org.jeesl.factory.mc.ts;

import java.util.List;

import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.metachart.factory.xml.chart.high.core.XmlDataFactory;
import org.metachart.factory.xml.chart.high.core.XmlDataSetFactory;
import org.metachart.xml.chart.Ds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class McTsViewerFactory <TS extends JeeslTimeSeries<?,TS,?,?,?>,
								DATA extends JeeslTsData<TS,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(McTsViewerFactory.class);

	public McTsViewerFactory()
	{

	}
	
	public Ds build2(List<DATA> list)
	{
		Ds dsValue = XmlDataSetFactory.build();
		for(DATA d : list)
		{
			if(d.getValue() != null && d.getRecord() != null)
			{
				dsValue.getData().add(XmlDataFactory.build(d.getValue(), d.getRecord()));
			}

		}
		return XmlDataSetFactory.build(dsValue);
	}
}