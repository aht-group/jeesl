package org.jeesl.factory.ejb.finance;

import net.sf.exlp.util.xml.JaxbUtil;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.interfaces.model.module.currency.UtilsCurrency;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.module.finance.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbCurrencyFactory<C extends UtilsCurrency<L>, L extends JeeslLang>
{
	final static Logger logger = LoggerFactory.getLogger(EjbCurrencyFactory.class);
	
	final Class<C> clCurrency;
    final Class<L> langClass;
    
    private EjbLangFactory<L> ejbLangFactory;
	
    private EjbCurrencyFactory(final Class<C> clCurrency, final Class<L> langClass)
    {
        this.clCurrency = clCurrency;
        this.langClass = langClass;
        
        ejbLangFactory = EjbLangFactory.instance(langClass);
    } 
    
    public static <C extends UtilsCurrency<L>, L extends JeeslLang> EjbCurrencyFactory<C, L>
    		factory(final Class<C> clCurrency, final Class<L> langClass)
    {
        return new EjbCurrencyFactory<C, L>(clCurrency, langClass);
    }
	
	public C create(String code, String symbol) throws InstantiationException, IllegalAccessException
	{
        C c = clCurrency.newInstance();
        c.setCode(code);
        c.setSymbol(symbol);
        return c;
    }
	
	public C create(Currency xml) throws JeeslConstraintViolationException
	{
		if(!xml.isSetLangs()){throw new JeeslConstraintViolationException("No <langs> available for "+JaxbUtil.toString(xml));}
		if(!xml.isSetCode()){throw new JeeslConstraintViolationException("No @code available for "+JaxbUtil.toString(xml));}
		if(!xml.isSetSymbol()){throw new JeeslConstraintViolationException("No @symbol available for "+JaxbUtil.toString(xml));}
		
		try
		{
			C c= create(xml.getCode(),xml.getSymbol());
			c.setName(ejbLangFactory.getLangMap(xml.getLangs()));
			return c;
		}
		catch (InstantiationException e) {throw new JeeslConstraintViolationException(e.getMessage());}
		catch (IllegalAccessException e) {throw new JeeslConstraintViolationException(e.getMessage());}
    }
}