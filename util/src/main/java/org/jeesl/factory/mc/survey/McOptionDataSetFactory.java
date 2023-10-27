package org.jeesl.factory.mc.survey;

import java.util.List;
import java.util.Map;

import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.factory.txt.JeeslReportAggregationLevelFactory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.JsonFlatFigure;
import org.jeesl.model.json.JsonFlatFigures;
import org.metachart.factory.xml.chart.high.core.XmlDataFactory;
import org.metachart.xml.chart.Ds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class McOptionDataSetFactory <OPTION extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(McOptionDataSetFactory.class);
	
	private final JeeslReportAggregationLevelFactory tfName;
	
	public McOptionDataSetFactory(JeeslReportAggregationLevelFactory tfName)
	{
		this.tfName=tfName;
	}
	
	public Ds build(JsonFlatFigures figures, List<OPTION> list)
	{
		Ds dsOption = new Ds();
		Ds dsValue = new Ds();
		Map<Long,OPTION> map = EjbIdFactory.toIdMap(list);
		
        for(JsonFlatFigure f : figures.getFigures())
        {
        	dsOption.getData().add(XmlDataFactory.build(tfName.build(map.get(f.getL2()))));
            dsValue.getData().add(XmlDataFactory.build(f.getL3()));
        }
        
        Ds ds = new Ds();
        ds.getDs().add(dsOption);
        ds.getDs().add(dsValue);

		return ds;
	}
}