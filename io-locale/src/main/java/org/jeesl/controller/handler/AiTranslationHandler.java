package org.jeesl.controller.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AiTranslationHandler
{
	final static Logger logger = LoggerFactory.getLogger(AiTranslationHandler.class);
	
	private JeeslLocaleProvider<IoLocale> tp; public AiTranslationHandler tp(JeeslLocaleProvider<IoLocale> tp) {this.tp=tp; changeSourceLanguageCode(this.tp.getLocales().get(0).getCode()); return this;}
	
	private Map<IoLocale,String> translations; public Map<IoLocale,String> getTranslations() {return translations;}
	private Map<IoLocale,Boolean> utilizations; public Map<IoLocale,Boolean> getUtilizations() {return utilizations;}
	
	private IoLocale source; public IoLocale getSource() { return source; }
	private List<IoLocale> targets; public List<IoLocale> getTargets() { return translations.keySet().stream().filter((IoLocale loc) -> !loc.getCode().equals(source.getCode())).collect(Collectors.toList()); }
	
	public AiTranslationHandler()
	{
		translations = new HashMap<>();
		utilizations = new HashMap<>();
	}
	
	public void changeSourceLanguageCode(String localeCode)
	{
		logger.info("changeSourceLanguageCode: "+localeCode);
		source=null;
		for(IoLocale l : tp.getLocales()) {if(l.getCode().equals(localeCode)){source = l;}};
	}
	
	public void openDialog()
	{
		logger.info("openDialog");
		
		for(IoLocale loc : tp.getLocales())
		{
			translations.put(loc,this.translate("x", null, null));
		}
		
	}
	
	public String translate(String text, String sourceLang, String targetLang) {
		// TODO Auto-generated method stub
		return RandomStringUtils.random(10, true, false);
	}
}  