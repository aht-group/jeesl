package org.jeesl.factory.json.system.io.report;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.JsonFlatFigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.finance.Figures;
import net.sf.ahtutils.xml.finance.Finance;

public class JsonFlatFigureFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonFlatFigureFactory.class);
	
	public static JsonFlatFigure build(){return new JsonFlatFigure();}
	
	public static JsonFlatFigure build(EjbWithId ejb, Double...doubles)
	{
		JsonFlatFigure json = build(doubles);
		json.setEjb1(ejb);
		return json;
	}
	
	public static JsonFlatFigure build(EjbWithId ejb1, EjbWithId ejb2, Double...doubles)
	{
		JsonFlatFigure json = build(doubles);
		json.setEjb1(ejb1);
		json.setEjb2(ejb2);
		return json;
	}
	
	public static JsonFlatFigure build(Double...doubles)
	{
		JsonFlatFigure json = build();
		for(int i=1;i<=doubles.length;i++)
		{
			double value = doubles[i-1];
			if(i==1){json.setD1(value);}
			else if(i==2){json.setD2(value);}
			else if(i==3){json.setD3(value);}
			else if(i==4){json.setD4(value);}
			else {logger.warn("No Handling for double.index="+(i-1));}
		}
		return json;
	}
	
	public static JsonFlatFigure build(Figures figures)
	{
		JsonFlatFigure json = build();
		for(Finance f : figures.getFinance())
		{
			if(f.getNr()==1){json.setD1(f.getValue());}
			else if(f.getNr()==2){json.setD2(f.getValue());}
			else if(f.getNr()==3){json.setD3(f.getValue());}
			else if(f.getNr()==4){json.setD4(f.getValue());}
			else {logger.warn("No Handling for double.index="+f.getNr());}
		}
		return json;
	}
	
	public static JsonFlatFigure build(String text)
	{
		JsonFlatFigure json = build();
		json.setS1(text);
		return json;
	}
}