package org.jeesl.factory.txt.io.dms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.text.diff.StringsComparator;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsSection;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtDmsSectionFactory <L extends JeeslLang,
									SEC  extends JeeslIoDmsSection<L,?,SEC>>
{
	final static Logger logger = LoggerFactory.getLogger(TxtDmsSectionFactory.class);
	
	private final String localeCode;
	
	
	public static <L extends JeeslLang, SEC  extends JeeslIoDmsSection<L,?,SEC>, E extends Enum<E>> TxtDmsSectionFactory<L,SEC> instance(E localeCode) {return new TxtDmsSectionFactory<>(localeCode.toString());}
	private TxtDmsSectionFactory(String localeCode)
	{
		this.localeCode=localeCode;
	}
	
	public String debug(SEC section)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(section.getId()).append("]");
		sb.append(" ").append(section.getName().get(localeCode).getLang());
		return sb.toString();
	}
	
	public String hierarchy(SEC section)
	{
		List<String> levels = new ArrayList<>();
		this.hierarchy(levels, section);
		Collections.reverse(levels);
		return String.join(" - ", levels);
	}
	
	public void hierarchy(List<String> levels, SEC section)
	{
		if(Objects.nonNull(section.getName()) && section.getName().containsKey(localeCode))
		{
			levels.add(section.getName().get(localeCode).getLang());
		}
		
		if(Objects.nonNull(section.getSection())) {this.hierarchy(levels,section.getSection());}
	}
}