package org.jeesl.util.query.xml;

import java.util.Hashtable;
import java.util.Map;

import org.jeesl.factory.xml.domain.finance.XmlCurrencyFactory;
import org.jeesl.factory.xml.domain.finance.XmlFinanceFactory;
import org.jeesl.model.xml.jeesl.QueryFinance;

import net.sf.ahtutils.xml.finance.Currency;
import net.sf.ahtutils.xml.finance.Finance;

public class XmlFinanceQuery
{
	public static enum Key {FinanceCurrency}
	
	private static Map<Key,QueryFinance> mQueries;
	
	public static QueryFinance get(Key key){return get(key,null);}
	public static QueryFinance get(Key key,String localeCode)
	{
		if(mQueries==null){mQueries = new Hashtable<Key,QueryFinance>();}
		if(!mQueries.containsKey(key))
		{
			QueryFinance q = new QueryFinance();
			switch(key)
			{
				case FinanceCurrency: q.setFinance(financeCurrency());break;
			}
			mQueries.put(key, q);
		}
		QueryFinance q = mQueries.get(key);
		q.setLocaleCode(localeCode);
		return q;
	}

	private static Finance financeCurrency()
	{
		Finance xml = XmlFinanceFactory.build();
		xml.setCurrency(currency());
		return xml;
	}
	
	private static Currency currency()
	{
		Currency xml = XmlCurrencyFactory.build();
		xml.setCode("");
		xml.setSymbol("");
		xml.setLabel("");
		return xml;
	}
}