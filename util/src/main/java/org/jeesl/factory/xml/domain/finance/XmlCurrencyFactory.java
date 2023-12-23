package org.jeesl.factory.xml.domain.finance;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.xml.system.lang.XmlLangFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.module.currency.UtilsCurrency;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.module.finance.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlCurrencyFactory <L extends JeeslLang, C extends UtilsCurrency<L>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlCurrencyFactory.class);

	private final String localeCode;
	private final Currency q;

	private XmlLangsFactory<L> xfLangs;
	
	public XmlCurrencyFactory(String localeCode, Currency q)
	{
		this.localeCode=localeCode;
		this.q=q;
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
	}
	
	public Currency build(C currency)
	{
		Currency xml = build();
		
		if(Objects.nonNull(q.getId())) {xml.setId(currency.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(currency.getCode());}
		if(Objects.nonNull(q.getSymbol())) {xml.setSymbol(currency.getSymbol());}
		
		if(ObjectUtils.allNotNull(q.getLabel(),localeCode)){xml.setLabel(XmlLangFactory.label(localeCode,currency));}
		if(Objects.nonNull(q.getLangs())){xml.setLangs(xfLangs.getUtilsLangs(currency.getName()));}
		
		return xml;
	}
	
	public static Currency build()
	{
		return new Currency();
	}
}