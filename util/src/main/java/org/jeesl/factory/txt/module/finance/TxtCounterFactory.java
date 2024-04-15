package org.jeesl.factory.txt.module.finance;

import java.text.DecimalFormat;
import java.util.Objects;

import org.jeesl.model.xml.module.finance.Counter;
import org.jeesl.model.xml.module.finance.Figures;
import org.jeesl.util.query.xpath.FiguresXpath;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class TxtCounterFactory
{
	@SuppressWarnings("unused")
	private DecimalFormat df; public TxtCounterFactory df(DecimalFormat value) {this.df=value; return this;}
	
	private String fallback;
	
	public TxtCounterFactory(DecimalFormat df)
	{
		this.df=df;
		this.fallback = "";
	}
	
	public <E extends Enum<E>, C extends Enum<C>> String build(Figures figures, E figureCode, C code)
	{
		Counter f = null;
		try {f = FiguresXpath.getFiguresCounter(figures,figureCode,code);}
		catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {}
		return this.build(f);
	}
	
	public String build(Counter c)
	{
		if(Objects.isNull(c) || Objects.isNull(c.getCounter())) {return fallback;}
		StringBuilder sb = new StringBuilder();
		sb.append(c.getCounter());
		return sb.toString();
	}
}
