package org.jeesl.controller.handler.tuple;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTupleHandler implements Serializable
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JsonTupleHandler.class);
	
	protected boolean withSum; public boolean isWithSum() {return withSum;} public void setWithSum(boolean withSum) {this.withSum = withSum;}
	protected int sumDivider; public void setSumDivider(int sumDivider) {this.sumDivider = sumDivider;}
	protected int dimension; protected int getDimension() {return dimension;}
	protected int size; public int getSize() {return size;} public int size() {return size;}
	
	public JsonTupleHandler()
	{		
		withSum = true;
		sumDivider = 1;
		dimension = 0;
		size=0;
	}
}