package org.jeesl.factory.pivot;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.pojo.DynamicPivotData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PivotDataFactory
{
	final static Logger logger = LoggerFactory.getLogger(PivotDataFactory.class);
	
	public static DynamicPivotData build(int size, double value, EjbWithId... ejb)
	{
		double values[] = new double[1];
		values[0] = value;
		DynamicPivotData dpd = new DynamicPivotData(size);
		dpd.setData(values,ejb);
		return dpd;
	}
	
}