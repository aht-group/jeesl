package org.jeesl.controller.web.g.io.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.util.io.JsonUtil;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.db.IoDbMetaFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumn;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumnType;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraintType;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaDifference;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaUnique;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.model.json.io.db.pg.explain.JsonPostgresExplain;
import org.jeesl.model.json.io.db.pg.explain.JsonPostgresTrigger;
import org.jeesl.util.query.ejb.io.EjbIoDbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JeeslDbExplainGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								SYSTEM extends JeeslIoSsiSystem<L,D>,
								SNAP extends JeeslDbMetaSnapshot<SYSTEM,?,TAB,COL,CON>,
								TAB extends JeeslDbMetaTable<SYSTEM,SNAP,?>,
								COL extends JeeslDbMetaColumn<SNAP,TAB,COLT>,
								COLT extends JeeslDbMetaColumnType<L,D,COLT,?>,
								CON extends JeeslDbMetaConstraint<SNAP,TAB,COL,CONT,CUN>,
								CONT extends JeeslDbMetaConstraintType<L,D,CONT,?>,
								CUN extends JeeslDbMetaUnique<COL,CON>,
								DIFF extends JeeslDbMetaDifference<L,D,DIFF,?>>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslDbExplainGwc.class);
	
	private final IoDbMetaFactoryBuilder<L,D,SYSTEM,SNAP,?,TAB,COL,COLT,CON,CONT,CUN,DIFF,?> fbDb;
	
	private JeeslIoDbFacade<SYSTEM,?,?,?,SNAP,?,TAB,COL,CON,CUN,?> fDb;
	
	
	private final SbSingleHandler<SYSTEM> sbhSystem; public SbSingleHandler<SYSTEM> getSbhSystem() {return sbhSystem;}
	
	private final Map<String,CON> mapConstraint;
	
	public Map<String, CON> getMapConstraint() {
		return mapConstraint;
	}

	private String explainString; public String getExplainString() {return explainString;} public void setExplainString(String explainString) {this.explainString = explainString;}

	private JsonPostgresExplain explain; public JsonPostgresExplain getExplain() {return explain;} public void setExplain(JsonPostgresExplain explain) {this.explain = explain;}
	
	public JeeslDbExplainGwc(IoDbMetaFactoryBuilder<L,D,SYSTEM,SNAP,?,TAB,COL,COLT,CON,CONT,CUN,DIFF,?> fbDb)
	{
		super(fbDb.getClassL(),fbDb.getClassD());
		this.fbDb=fbDb;
		
		sbhSystem = new SbSingleHandler<>(fbDb.getClassSsiSystem(),this);
		
		mapConstraint = new HashMap<>();
	}

	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage, JeeslIoDbFacade<SYSTEM,?,?,?,SNAP,?,TAB,COL,CON,CUN,?> fDb)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fDb=fDb;
	}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		sbhSystem.setList(fDb.all(fbDb.getClassSsiSystem()));
	}
	
	public void process() throws JsonParseException, JsonMappingException, IOException
	{
		mapConstraint.clear();
		explain = null;
		
		String s = explainString.replaceAll("(\\u00A0)","");
		
//		FileUtils.writeStringToFile(Paths.get("/Volumes/ramdisk/test.json").toFile(), s, StandardCharsets.UTF_8);
		
		List<JsonPostgresExplain> list = JsonUtil.jom().readValue(s, new TypeReference<List<JsonPostgresExplain>>(){});
		logger.info(JsonPostgresExplain.class.getSimpleName()+" "+list.size());
		if(ObjectUtils.isNotEmpty(list))
		{
			explain = list.get(0);
		}
		
		
		
		List<String> codes = new ArrayList<>();
		for(JsonPostgresTrigger t : explain.getTriggers())
		{
			codes.add(t.getConstraintName());
			logger.info(t.getConstraintName());
		}
		
		EjbIoDbQuery<SYSTEM,SNAP> query = new EjbIoDbQuery<>();
		query.codeList(codes);
		
		List<CON> constraints = fDb.fIoDbMetaConstraints(query);
		logger.info(fbDb.getClassConstraint().getSimpleName()+": "+constraints.size());
		for(CON c : constraints)
		{
			mapConstraint.put(c.getCode(), c);
		}
		
		explainString = null;
	}
}