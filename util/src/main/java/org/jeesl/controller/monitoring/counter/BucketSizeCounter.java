package org.jeesl.controller.monitoring.counter;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jeesl.factory.xml.domain.finance.XmlCounterFactory;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.openfuxml.factory.xml.table.XmlTableFactory;
import org.openfuxml.renderer.text.OfxTextSilentRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.finance.Figures;

public class BucketSizeCounter
{
	final static Logger logger = LoggerFactory.getLogger(BucketSizeCounter.class);
	
	private String category;
	private final Map<String,Long> map;
	
	private long loop;
	
	public static BucketSizeCounter instance() {return new BucketSizeCounter();}
	public BucketSizeCounter(){this("Default Category");}
	public BucketSizeCounter(String category)
	{
		this.category=category;
		map = new HashMap<String,Long>();
		clear();
	}
	
	public void clear()
	{
		map.clear();
		loop=0;
	}
	
	public <C extends EjbWithId> void countClass(C ejb){add(ejb.getClass().getSimpleName(),1);}
	public <C extends EjbWithCode> void add(C ejb){add(ejb.getCode(),1);}
	public <C extends EjbWithCode> void add(C ejb, long size){add(ejb.getCode(),size);}
	public <E extends Enum<E>> void add(E event, long size){add(event.toString(),size);}
	public void add(String event) {this.add(event,1);}
	public void add(String event, long size)
	{
		if(!map.containsKey(event)){map.put(event, 0l);}
		map.put(event, map.get(event)+size);
	}
	
	public <E extends Enum<E>> long events(E event, long size){return events(event.toString());}
	public long events(String event)
	{
		if(map.containsKey(event)){return map.get(event);}
		else{return 0;}
	}
	
	public void debugAsFileSize()
	{
		logger.info("Sizes in category "+category);
		for(String key : map.keySet())
		{
			logger.info("\t"+key+": "+FileUtils.byteCountToDisplaySize(map.get(key)));
		}
	}
	
	public void debugAsSize()
	{
		logger.info("Sizes in category "+category);
		for(String key : map.keySet())
		{
			logger.info("\t"+key+": "+map.get(key));
		}
	}
	
	public void ofx(OutputStream os)
	{
		List<String> header = new ArrayList<>();
		header.add("Event");
		header.add("Size");
		
		List<Object[]> data = new ArrayList<>();
		
		for(String key : map.keySet())
		{
			String numbers[] = {key, Long.valueOf(map.get(key)).toString()};
			data.add(numbers);
		}
		
		OfxTextSilentRenderer.table(XmlTableFactory.build(category,header, data),os);
	}
	
	public void jira(OutputStream os)
	{
		System.out.println("*"+category+"*");
		System.out.println("||Event||Size||");
		
		for(String key : map.keySet())
		{
			System.out.println("|"+key+"|"+Long.valueOf(map.get(key)).toString()+"|");
		}
	}
	
	public List<String> toCodes(){return new ArrayList<>(map.keySet());}
	
	public boolean hasValue(String code) {return map.containsKey(code);}
	public Long value(String code) {return map.get(code);}
	
	public void debugLoop(int modulo){debugLoop(modulo,null);}
	public void debugLoop(int modulo, Integer max)
	{
		loop++;
		if(loop%modulo==0)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(category).append(": ");
			sb.append(loop);
			if(max!=null) {sb.append("/").append(max);}
			logger.debug(sb.toString());
		}		
	}
	
	public void toCounter(Figures figures)
	{
		for(String code : map.keySet())
		{
			XmlCounterFactory.plus(figures, code, map.get(code));
		}
	}
}