package org.jeesl.factory.json.module.ts;

import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsDataPoint;
import org.jeesl.model.json.module.ts.JsonTsPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTsPointFactory<MP extends JeeslTsMultiPoint<?,?,?,?>,
								POINT extends JeeslTsDataPoint<?,MP>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonTsPointFactory.class);
	
	private final JsonTsPoint q;
	
	private JsonTsMultiPointFactory<MP> jfMp;
	
	public JsonTsPointFactory(JsonTsPoint q)
	{
		this.q=q;
		if(q.isSetMp()) {jfMp = new JsonTsMultiPointFactory<>(q.getMp());}
	}
		
	public JsonTsPoint build(POINT ejb)
	{
		JsonTsPoint json = new JsonTsPoint();
	
		if(q.isSetId()){json.setId(ejb.getId());}
		if(q.isSetMp()) {json.setMp(jfMp.build(ejb.getMultiPoint()));}
		if(q.isSetValue()){json.setValue(ejb.getValue());}
		
		return json;
	}
	
	public static JsonTsPoint build(double value)
	{
		JsonTsPoint json = new JsonTsPoint();
	
		json.setValue(value);
		
		return json;
	}
}