package org.jeesl.controller.provider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.controller.handler.system.locales.JeeslTranslationProvider;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextTranslationProvider <LOC extends JeeslLocale<?,?,LOC,?>>
							implements JeeslTranslationProvider<LOC>
{
	final static Logger logger = LoggerFactory.getLogger(ContextTranslationProvider.class);
	
	private final JeeslTranslationProvider<LOC> tp;
	private String localeCode;
	
	public static <LOC extends JeeslLocale<?,?,LOC,?>> ContextTranslationProvider<LOC> instance(JeeslTranslationProvider<LOC> tp)
	{
		return new ContextTranslationProvider<>(tp);
	}
	
	private ContextTranslationProvider(JeeslTranslationProvider<LOC> tp)
	{
		this.tp=tp;
	}

	public ContextTranslationProvider<LOC> locale(String localeCode) {this.localeCode=localeCode;return this;}
//	public ContextTranslationProvider<LOC> cache() {this.localeCode=localeCode;return this;}
	

	@Override public void setLanguages(List<LOC> locales) {throw new UnsupportedOperationException("NYI");}
	@Override public boolean hasLocale(String localeCode) {throw new UnsupportedOperationException("NYI");}
	@Override public List<String> getLocaleCodes() {throw new UnsupportedOperationException("NYI");}
	@Override public void setContext(String localeCode, Class<?> c) {throw new UnsupportedOperationException("NYI");}

	@Override public String tlEntity(String localeCode, String key) {throw new UnsupportedOperationException("NYI");}
	@Override public String tlEntity(String localeCode, Class<?> c) {throw new UnsupportedOperationException("NYI");}
	@Override public String tlEntity(Class<?> c)
	{
		if(Objects.isNull(localeCode)) {throw new UnsupportedOperationException("It's not allowed to use context shortcuts without locale");}
		return tp.tlEntity(localeCode, c);
	}
	
	@Override public <E extends Enum<E>> String tAttribute(Class<?> c, E code)
	{
		if(Objects.isNull(localeCode)) {throw new UnsupportedOperationException("It's not allowed to use context shortcuts without locale");}
		return tp.tAttribute(localeCode,c,code);
	}
	@Override public String tAttribute(String localeCode, String key1, String key2) {throw new UnsupportedOperationException("NYI");}

	@Override public <E extends Enum<E>> String toLabel(E code) {throw new UnsupportedOperationException("NYI");}
	@Override public <E extends Enum<E>> String tAttribute(String localeCode, Class<?> c, E code) {throw new UnsupportedOperationException("NYI");}
	
	@Override public <E extends Enum<E>> String toDescription(String localeCode, Class<?> c, E code) {throw new UnsupportedOperationException("NYI");}



	@Override public String toDate(String localeCode, LocalDate record) {throw new UnsupportedOperationException("NYI");}
	@Override public String toDate(String localeCode, LocalDateTime ldt) {throw new UnsupportedOperationException("NYI");}
	@Override public String toDate(String localeCode, Date record) {throw new UnsupportedOperationException("NYI");}

	@Override public String toTime(String localeCode, Date record) {throw new UnsupportedOperationException("NYI");}
	@Override public String toTime(String localeCode, LocalDateTime ldt) {throw new UnsupportedOperationException("NYI");}
	
	@Override public String toCurrency(String localeCode, Double value) {throw new UnsupportedOperationException("NYI");}

	@Override public String toCurrency(String localeCode, boolean grouping, int decimals, Double value) {throw new UnsupportedOperationException("NYI");}
	@Override public <E extends Enum<E>> String xpAttribute(String localeCode, Class<?> c, E code) {throw new UnsupportedOperationException("NYI");}	

}