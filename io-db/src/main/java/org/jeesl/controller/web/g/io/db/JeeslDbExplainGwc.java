package org.jeesl.controller.web.g.io.db;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.factory.builder.io.IoLocaleFactoryBuilder;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.model.json.io.db.pg.explain.JsonPostgresExplain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import net.sf.exlp.util.io.JsonUtil;

public class JeeslDbExplainGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>>
					extends AbstractJeeslWebController<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslDbExplainGwc.class);
	
	private String explainString; public String getExplainString() {return explainString;} public void setExplainString(String explainString) {this.explainString = explainString;}

	private JsonPostgresExplain explain; public JsonPostgresExplain getExplain() {return explain;} public void setExplain(JsonPostgresExplain explain) {this.explain = explain;}
	
	public JeeslDbExplainGwc(IoLocaleFactoryBuilder<L,D,LOC> fbLoc)
	{
		super(fbLoc.getClassL(),fbLoc.getClassD());
	}

	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage)
	{
		super.postConstructWebController(lp,bMessage);
	}
	
	public void process() throws JsonParseException, JsonMappingException, IOException
	{
		explain = null;
		
		String s = explainString.replaceAll("(\\u00A0)","");
		
//		FileUtils.writeStringToFile(Paths.get("/Volumes/ramdisk/test.json").toFile(), s, StandardCharsets.UTF_8);
		
		List<JsonPostgresExplain> list = JsonUtil.jom().readValue(s, new TypeReference<List<JsonPostgresExplain>>(){});
		logger.info(JsonPostgresExplain.class.getSimpleName()+" "+list.size());
		if(ObjectUtils.isNotEmpty(list))
		{
			explain = list.get(0);
		}
		
		explainString = null;
	}
}