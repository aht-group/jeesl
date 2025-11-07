package org.jeesl.factory.txt.system.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

public class TxtStatusFactory <L extends JeeslLang, S extends JeeslStatus<L,?,S>>
{
	private final String localeCode;
	
	public static <L extends JeeslLang, S extends JeeslStatus<L,?,S>, E extends Enum<E>> TxtStatusFactory<L,S> instance(E localeCode)
	{
		return new TxtStatusFactory<L,S>(localeCode.toString());
	}
	public static <L extends JeeslLang,  S extends JeeslStatus<L,?,S>> TxtStatusFactory<L,S> factory(String localeCode)
	{
		return new TxtStatusFactory<L,S>(localeCode);
	}
	
	public TxtStatusFactory(String localeCode)
	{
		this.localeCode=localeCode;
	}
	
	public String debug(S status)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(status.getId()).append("]");
		sb.append(" (").append(status.getCode()).append(")");
		sb.append(" ").append(status.getName().get(localeCode).getLang());
		return sb.toString();
	}
	public String codeLabel(S status)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(status.getCode()).append(")");
		sb.append(" ").append(status.getName().get(localeCode).getLang());
		return sb.toString();
	}
	public String build(S status)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(status.getName().get(localeCode).getLang());
		return sb.toString();
	}
	
	public String labels(List<S> list) {return TxtStatusFactory.labels(localeCode,list);}
	
	public static <L extends JeeslLang, S extends JeeslStatus<L,?,S>, E extends Enum<E>> String labels(E localeCode, List<S> list) {return TxtStatusFactory.labels(localeCode.toString(), list);}
	public static <L extends JeeslLang, S extends JeeslStatus<L,?,S>> String labels(String localeCode, List<S> list)
	{
		if(ObjectUtils.isEmpty(list)) {return null;}
		List<String> result = new ArrayList<String>();
		for(S ejb : list) {result.add(ejb.getName().get(localeCode).getLang());}
		return StringUtils.join(result, ", ");
	}
	
	public static <S extends JeeslStatus<L,?,S>, L extends JeeslLang, E extends Enum<E>> String label(E locale, S ejb) {return TxtStatusFactory.label(locale.toString(), ejb);}
	public static <S extends JeeslStatus<L,?,S>, L extends JeeslLang> String label(String lang, S ejb)
	{
		if(Objects.isNull(ejb)) {return "null";}
		else {return ejb.getName().get(lang).getLang();}
	}
	
	public static <S extends JeeslStatus<?,?,S>> List<String> toCodes(Collection<S> list)
	{
		List<String> result = new ArrayList<String>();
		for(S ejb : list) {result.add(ejb.getCode());}
		return result;
	}
	
	public static <S extends JeeslStatus<L,D,S>,L extends JeeslLang, D extends JeeslDescription>
		List<String> toCodes(List<S> list)
	{
		List<String> result = new ArrayList<String>();
		for(S ejb : list){result.add(ejb.getCode());}
		return result;
	}
}