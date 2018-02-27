package org.jeesl.factory.xml.domain.finance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.module.currency.UtilsCurrency;
import org.jeesl.interfaces.model.system.with.code.EjbWithCode;
import org.jeesl.model.xml.jeesl.QueryFinance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.finance.UtilsFinance;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;
import net.sf.ahtutils.xml.finance.Figures;
import net.sf.ahtutils.xml.finance.Finance;

public class XmlFinanceFactory <L extends UtilsLang, C extends UtilsCurrency<L>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlFinanceFactory.class);
	
	private final Finance q;
	private XmlCurrencyFactory<L,C> xfCurrency;
	
	public XmlFinanceFactory(QueryFinance query) {this(query.getLocaleCode(),query.getFinance());}
	public XmlFinanceFactory(String localeCode, Finance q)
	{
		this.q=q;
		if(q.getCurrency()!=null) {xfCurrency = new XmlCurrencyFactory<L,C>(localeCode,q.getCurrency());}
	}
	
	public <E extends Enum<E>> Finance build(E code, double value, C currency)
	{
		Finance xml = build();
		xml.setCode(code.toString());
		xml.setValue(value);
		if(q.isSetCurrency()) {xml.setCurrency(xfCurrency.build(currency));}
		return xml;
	}
	
	public static <F extends UtilsFinance> Finance create(F ejb)
	{
		Finance xml = build();
		xml.setValue(ejb.getValue());
		return xml;
	}
	
	public static <C extends EjbWithCode> Finance build(C code, double value){return create(code.getCode(),value);}
	public static <E extends Enum<E>> Finance build(E code, double value){return create(code.toString(),value);}
	public static Finance create(String code, double value)
	{
		return build(code,null,value);
	}
	public static Finance build(String code, String label, double value)
	{
		Finance xml = build();
		xml.setCode(code);
		xml.setLabel(label);
		xml.setValue(value);
		return xml;
	}
	
	public static Finance create(String code, String label)
	{
		Finance xml = build();
		xml.setCode(code);
		xml.setLabel(label);
		return xml;
	}
	
	public static <E extends Enum<E>> void plus(Figures figures, E code, Double value)
	{
		if(value!=null)
		{
			boolean added = false;
			for(Finance f : figures.getFinance())
			{
				if(f.getCode().equals(code.toString()))
				{
					f.setValue(f.getValue()+value);
					added = true;
				}
			}
			if(!added)
			{
				XmlFinanceFactory.add(figures, code, value);
			}
		}
	}
	
	public static <E extends Enum<E>> void add(Figures figures, E code, Integer value)
	{
		if(value!=null){figures.getFinance().add(XmlFinanceFactory.build(code, value));}
	}
	public static <E extends Enum<E>> void add(Figures figures, E code, Double value)
	{
		if(value!=null){figures.getFinance().add(XmlFinanceFactory.build(code, value));}
	}
	public static <E extends Enum<E>> void add(Figures figures, E code, int index, Double[] values)
	{
		if(values!=null && values.length>index && values[index]!=null)
		{
			figures.getFinance().add(build(code,values[index]));
		}
	}
	public static <C extends EjbWithCode> void add(Figures figures, C code, int index, Double[] values)
	{
		if(values!=null && values.length>index && values[index]!=null)
		{
			figures.getFinance().add(build(code,values[index]));
		}
	}
	
	public static void addId(Figures figures, EjbWithId ejb, Double[] values, int index)
	{
		if(values!=null)
		{
			addId(figures,ejb,values[index]);
		}
	}
	public static void addCode(Figures figures, EjbWithCode ejb, Double[] values, int index)
	{
		if(values!=null)
		{
			addCode(figures,ejb.getCode(),values[index]);
		}
	}
	public static void addId(Figures figures, EjbWithId ejb, Double value){addId(figures,ejb.getId(),value);}
	public static void addId(Figures figures, long id, Double value)
	{
		if(value!=null)
		{
			figures.getFinance().add(XmlFinanceFactory.id(id, value));
		}
	}
	
	public static void addCode(Figures figures, String code, Double value)
	{
		if(value!=null)
		{
			figures.getFinance().add(XmlFinanceFactory.create(code, value));
		}
	}
	
	public static Finance id(long id)
	{
		Finance xml = build();
		xml.setId(id);
		return xml;
	}
	
	public static Finance id(long id, double value)
	{
		Finance xml = build();
		xml.setId(id);
		xml.setValue(value);
		return xml;
	}
	
	public static Finance nr(int nr, double value)
	{
		Finance xml = build();
		xml.setNr(nr);
		xml.setValue(value);
		return xml;
	}
	
	public static Finance build(double value)
	{
		Finance xml = build();
		xml.setValue(value);
		return xml;
	}
	
	public static Finance build()
	{
		return new Finance();
	}
	
	public static List<Finance> pivot(Double[] values, List<EjbWithId> last, Map<EjbWithId,Double[]> mapLast)
	{
		List<Finance> finances = new ArrayList<Finance>();
		if(values!=null)
		{
			for(int i=0;i<values.length;i++)
			{
				Finance f = XmlFinanceFactory.nr(i+1,values[i]);
			
				if(last!=null)
				{
					for(EjbWithId entity : last)
					{
						Double[] lastValues = mapLast.get(entity);
						if(lastValues!=null){f.getFinance().add(XmlFinanceFactory.id(entity.getId(), lastValues[i]));}
						else {f.getFinance().add(XmlFinanceFactory.id(entity.getId()));}
					}
				}
				finances.add(f);
			}
		}
		return finances;
	}
}