package org.jeesl.factory.xml.domain.finance;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.model.xml.text.Remark;
import org.jeesl.util.query.xpath.FiguresXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.finance.Counter;
import net.sf.ahtutils.xml.finance.Figures;
import net.sf.ahtutils.xml.finance.Finance;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class XmlFiguresFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlFiguresFactory.class);
	
	private final Integer decimals;
	
	public XmlFiguresFactory(Integer decimals)
	{
		this.decimals=decimals;
	}
	
	public static Figures build(long id){return build(id,null,null);}
	public static <E extends Enum<E>> Figures build(E code){return build(code.toString());}
	public static Figures build(String code){return (build(null,code,null));}
	public static Figures build(){return build(null,null,null);}
	
	public static Figures build(Figures... figures)
	{
		Figures result = build();
		for(Figures f : figures)
		{
			result.getFigures().add(f);
		}
		
		return result;
	}
	
	public static Figures label(String label){return build(null,null,label);}
	
	public static Figures build(Long id, String code, String label)
	{
		Figures xml = new Figures();
		if(id!=null){xml.setId(id);}
		if(code!=null){xml.setCode(code);}
		if(label!=null){xml.setLabel(label);}
		return xml;
	}
	
	public static Figures build(Counter counter)
	{
		Figures xml = new Figures();
		xml.getCounter().add(counter);
		return xml;
	}
	
	public static Map<Long,Map<String,Remark>> toMapRemark(List<Figures> figures)
	{
		Map<Long,Map<String,Remark>> map = new Hashtable<Long,Map<String,Remark>>();
		for(Figures f : figures)
		{
			long id = f.getId();
			if(!map.containsKey(id)){map.put(id,new Hashtable<String,Remark>());}
			Map<String,Remark> m = map.get(id);
			for(Remark r : f.getRemark())
			{
				m.put(r.getKey(), r);
			}
		}
		return map;
	}
	
	public static Map<Long,Map<String,Finance>> toMapFinance(List<Figures> figures)
	{
		Map<Long,Map<String,Finance>> map = new Hashtable<Long,Map<String,Finance>>();
		for(Figures f : figures)
		{
			long id = f.getId();
			if(!map.containsKey(id)){map.put(id,new Hashtable<String,Finance>());}
			Map<String,Finance> m = map.get(id);
			for(Finance r : f.getFinance())
			{
				m.put(r.getCode(), r);
			}
		}
		return map;
	}
	
	public static boolean hasFinanceElements(Figures figures)
	{
		if(figures.isSetFinance()){return true;}
		else
		{
			for(Figures f : figures.getFigures())
			{
				if(hasFinanceElements(f)){return true;}
			}
		}
		return false;
	}
	
	public static Figures add(Figures a, Figures b)
	{
		Set<String> figureCodes = new HashSet<String>();
		Set<String> financeCodes = new HashSet<String>();
		Set<String> counterCodes = new HashSet<String>();
		
		if(a!=null)
		{
			for(Figures f : a.getFigures()) {if(!figureCodes.contains(f.getCode())) {figureCodes.add(f.getCode());}}
			for(Finance f : a.getFinance()) {if(!financeCodes.contains(f.getCode())) {financeCodes.add(f.getCode());}}
			for(Counter c : a.getCounter()) {if(!counterCodes.contains(c.getCode())) {counterCodes.add(c.getCode());}}
		}
		if(b!=null)
		{
			for(Figures f : b.getFigures()) {if(!figureCodes.contains(f.getCode())) {figureCodes.add(f.getCode());}}
			for(Finance f : b.getFinance()) {if(!financeCodes.contains(f.getCode())) {financeCodes.add(f.getCode());}}
			for(Counter c : b.getCounter()) {if(!counterCodes.contains(c.getCode())) {counterCodes.add(c.getCode());}}
		}
		
		logger.info("Adding");
		logger.info("figureCodes: "+StringUtils.join(figureCodes, ", "));
		logger.info("financeCodes: "+StringUtils.join(financeCodes, ", "));
		logger.info("counterCodes: "+StringUtils.join(counterCodes, ", "));

		Figures xml = XmlFiguresFactory.build();
		for(String code : figureCodes)
		{
			logger.info("code "+code);
			Figures f = XmlFiguresFactory.add(FiguresXpath.getChild(a, code),FiguresXpath.getChild(b, code));
			f.setCode(code);
			
			xml.getFigures().add(f);
		}
		for(String code : financeCodes)
		{
			logger.info("FinanceCode "+code);
			Finance f = XmlFinanceFactory.create(code,0d);
			
			boolean codeAvailable = false;
			if(a!=null)
			{
				try {XmlFinanceFactory.add(f, FiguresXpath.getFinance(a, code).getValue());}
				catch (ExlpXpathNotFoundException e) {}
				catch (ExlpXpathNotUniqueException e) {}
				codeAvailable = true;
			}
			if(b!=null)
			{
				try {XmlFinanceFactory.add(f, FiguresXpath.getFinance(b, code).getValue());}
				catch (ExlpXpathNotFoundException e) {}
				catch (ExlpXpathNotUniqueException e) {}
				codeAvailable = true;
			}
			if(codeAvailable) {xml.getFinance().add(f);}
		}
		
		return xml;
	}
	
	public Figures substract(Figures a, Figures b)
	{
		Set<String> figureCodes = new HashSet<String>();
		Set<String> financeCodes = new HashSet<String>();
		Set<String> counterCodes = new HashSet<String>();
		
		if(a!=null)
		{
			for(Figures f : a.getFigures()) {if(!figureCodes.contains(f.getCode())) {figureCodes.add(f.getCode());}}
			for(Finance f : a.getFinance()) {if(!financeCodes.contains(f.getCode())) {financeCodes.add(f.getCode());}}
			for(Counter c : a.getCounter()) {if(!counterCodes.contains(c.getCode())) {counterCodes.add(c.getCode());}}
		}
		if(b!=null)
		{
			for(Figures f : b.getFigures()) {if(!figureCodes.contains(f.getCode())) {figureCodes.add(f.getCode());}}
			for(Finance f : b.getFinance()) {if(!financeCodes.contains(f.getCode())) {financeCodes.add(f.getCode());}}
			for(Counter c : b.getCounter()) {if(!counterCodes.contains(c.getCode())) {counterCodes.add(c.getCode());}}
		}

		Figures xml = XmlFiguresFactory.build();
		for(String code : figureCodes)
		{
			Figures f = this.substract(FiguresXpath.getChild(a, code),FiguresXpath.getChild(b, code));
			f.setCode(code);
			xml.getFigures().add(f);
		}
		for(String code : financeCodes)
		{
			Finance f = XmlFinanceFactory.create(code,0d);
			
			boolean codeAvailable = false;
			if(a!=null)
			{
				try {XmlFinanceFactory.add(f, FiguresXpath.getFinance(a, code).getValue());}
				catch (ExlpXpathNotFoundException e) {}
				catch (ExlpXpathNotUniqueException e) {}
				codeAvailable = true;
			}
			if(b!=null)
			{
				try {XmlFinanceFactory.substract(f, FiguresXpath.getFinance(b, code).getValue(), decimals);}
				catch (ExlpXpathNotFoundException e) {}
				catch (ExlpXpathNotUniqueException e) {}
				codeAvailable = true;
			}
			
			if(codeAvailable) {xml.getFinance().add(f);}
		}
		
		return xml;
	}
}