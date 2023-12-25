package org.jeesl.factory.xml.domain.finance;

import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.finance.Counter;
import net.sf.ahtutils.xml.finance.Figures;

public class XmlCounterFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlCounterFactory.class);
	
	public static Counter create(String code, int counter)
	{
		Counter xml = new Counter();
		xml.setCode(code);
		xml.setCounter(counter);
		return xml;
	}
	
	public static <E extends Enum<E>> Counter build(E code, double counter)
	{
		return create(code.toString(),Double.valueOf(counter).intValue());
	}
	public static <E extends Enum<E>> Counter build(E code, int counter)
	{
		return create(code.toString(),counter);
	}
	public static <E extends Enum<E>> Counter build(E code, long counter)
	{
		return create(code.toString(),Long.valueOf(counter).intValue());
	}
	
	public static <E extends Enum<E>> void add(Figures figures, E code, Long value)
	{
		if(value!=null){figures.getCounter().add(XmlCounterFactory.build(code,value));}
	}
	public static <E extends Enum<E>> void add(Figures figures, E code, Integer value)
	{
		if(value!=null){figures.getCounter().add(XmlCounterFactory.build(code, value));}
	}
	public static <E extends Enum<E>> void add(Figures figures, String code, Long value) {if(value!=null){figures.getCounter().add(XmlCounterFactory.create(code,value.intValue()));}}
	public static <E extends Enum<E>> void add(Figures figures, String code, Integer value) {if(value!=null){figures.getCounter().add(XmlCounterFactory.create(code,value));}}
	
	public static <E extends EjbWithCode> void add(Figures figures, E code, Integer value)
	{
		if(value!=null){figures.getCounter().add(XmlCounterFactory.create(code.getCode(), value));} 
	}
	public static <E extends EjbWithCode> void add(Figures figures, E code, Long value)
	{
		if(value!=null){figures.getCounter().add(XmlCounterFactory.create(code.getCode(), Long.valueOf(value).intValue()));} 
	}
	
	public static <E extends Enum<E>> void plus(Figures figures, E code, Integer value) {plus(figures,code.toString(),value);}
	public static <E extends Enum<E>> void plus(Figures figures, String code, Long value) {plus(figures,code.toString(),value.intValue());}
	public static <E extends Enum<E>> void plus(Figures figures, String code, Integer value)
	{
		if(value!=null)
		{
			for(Counter c : figures.getCounter())
			{
				if(c.getCode().equals(code.toString()))
				{
					c.setCounter(c.getCounter()+value);
					return;
				}
			}
			add(figures,code,value);
		}
	}
}