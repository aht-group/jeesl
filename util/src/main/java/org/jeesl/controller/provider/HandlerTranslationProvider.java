package org.jeesl.controller.provider;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.controller.handler.io.label.JeeslTranslationHandler;
import org.jeesl.factory.txt.system.status.TxtStatusFactory;
import org.jeesl.interfaces.controller.handler.JeeslTranslationProvider;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionMissingLabel;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerTranslationProvider <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										RE extends JeeslRevisionEntity<L,D,?,?,RA,?>,
										RA extends JeeslRevisionAttribute<L,D,RE,?,?>,
										RML extends JeeslRevisionMissingLabel>
					implements JeeslTranslationProvider<LOC>
{
	final static Logger logger = LoggerFactory.getLogger(HandlerTranslationProvider.class);

	private final Set<String> setLocaleCodes;
	private final List<String> localeCodes; @Override public List<String> getLocaleCodes() {return localeCodes;}

	private final SimpleDateFormat sdfDate;
	private final SimpleDateFormat sdfTime;
	private final JeeslTranslationHandler<L,D,RE,RA,RML> th;

	private DecimalFormat dfCurrency;

	public HandlerTranslationProvider(JeeslTranslationHandler<L,D,RE,RA,RML> th)
	{
		this.th=th;
		setLocaleCodes = new HashSet<String>();
		localeCodes = new ArrayList<String>();
		sdfDate = new SimpleDateFormat("dd.MM.yyyy");
		sdfTime = new SimpleDateFormat("hh:mm");

		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		otherSymbols.setDecimalSeparator(',');
		otherSymbols.setGroupingSeparator('.');
		dfCurrency = new DecimalFormat("0.00",otherSymbols);
	}

	@Override
	public void setLanguages(List<LOC> locales)
	{
		localeCodes.clear();
		localeCodes.addAll(TxtStatusFactory.toCodes(locales));
		setLocaleCodes.addAll(localeCodes);
	}

	@Override public String tlEntity(String localeCode, Class<?> c) {return tlEntity(localeCode,c.getSimpleName());}
	@Override public String tlEntity(String localeCode, String key)
	{
		return th.getEntities().get(key).get(localeCode).getLang();
	}

	@Override public <E extends Enum<E>> String tlAttribute(String localeCode, Class<?> c, E code)
	{
		return tlAttribute(localeCode,c.getSimpleName(),code.toString());
	}

	@Override public String tlAttribute(String localeCode, String key1, String key2)
	{
		if(key2==null) {return tlEntity(localeCode,key1);}
		return th.getLabels().get(key1).get(key2).get(localeCode).getLang();
	}

	@Override public boolean hasLocale(String localeCode) {return setLocaleCodes.contains(localeCode);}

	@Override public String toDate(String locleCode, LocalDate record)
	{
		if(record==null){return "";}
		return sdfDate.format(record);
	}
	
	@Override public String toDate(String locleCode, Date record)
	{
		if(record==null){return "";}
		return sdfDate.format(record);
	}

	@Override public String toTime(String localeCode, Date record)
	{
		if(record==null){return "";}
		return sdfTime.format(record);
	}

	@Override
	public String toCurrency(String localeCode, Double value)
	{
		if(value==null){return "";}
		return dfCurrency.format(value);
	}

	@Override
	public String toCurrency(String localeCode, boolean grouping, int decimals, Double value)
	{
		if(value==null){return "";}
		return dfCurrency.format(value);
	}

	@Override
	public <E extends Enum<E>> String xpAttribute(String localeCode, Class<?> c, E code)
	{
		logger.warn("NYI xpAttribute");
		return null;
	}
}