package org.jeesl.factory.txt.io.locale;

import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtNameFactory <L extends JeeslLang>
{
	final static Logger logger = LoggerFactory.getLogger(TxtNameFactory.class);
	
	private final String localeCode;
	
	public static <L extends JeeslLang, E extends Enum<E>> TxtNameFactory<L> instance(E locale) {return new TxtNameFactory<>(locale.toString());}
	private TxtNameFactory(String localeCode)
	{
		this.localeCode = localeCode;
	}
	
	public <T extends EjbWithLang<L>> String build(T ejb)
	{
		return ejb.getName().get(localeCode).getLang();
	}
}