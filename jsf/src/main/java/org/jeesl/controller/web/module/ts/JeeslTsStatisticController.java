package org.jeesl.controller.web.module.ts;

import java.util.ArrayList;
import java.util.List;

import org.exlp.util.io.JsonUtil;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.TsFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.module.ts.JeeslTsStatisticCallback;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.model.json.module.ts.JsonTsStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JeeslTsStatisticController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											CAT extends JeeslTsCategory<L,D,CAT,?>,
											SCOPE extends JeeslTsScope<L,D,CAT,?,?,EC,INT>,
											TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INT,STAT>,
											BRIDGE extends JeeslTsBridge<EC>,
											EC extends JeeslTsEntityClass<L,D,CAT,ENTITY>,
											ENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>,
											INT extends JeeslTsInterval<L,D,INT,?>,
											STAT extends JeeslTsStatistic<L,D,STAT,?>
											>
		extends AbstractJeeslLocaleWebController<L,D,LOC>
		implements SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslTsStatisticController.class);
	
	private JeeslTsFacade<CAT,SCOPE,?,?,?,TS,?,?,BRIDGE,EC,ENTITY,INT,STAT,?,?,?,?,?,?> fTs;
	
	private final TsFactoryBuilder<L,D,LOC,CAT,SCOPE,?,?,?,TS,?,?,BRIDGE,EC,ENTITY,INT,STAT,?,?,?,?,?,?,?> fbTs;
	
	private final SbSingleHandler<LOC> sbhLocale; public SbSingleHandler<LOC> getSbhLocale() {return sbhLocale;}
	
	private final JsonTuple1Handler<TS> th; public JsonTuple1Handler<TS> getTh() {return th;}
	private final JeeslTsStatisticCallback callback;
	
	protected String jsonStream; public String getJsonStream() {return jsonStream;}

	public JeeslTsStatisticController(JeeslTsStatisticCallback callback, final TsFactoryBuilder<L,D,LOC,CAT,SCOPE,?,?,?,TS,?,?,BRIDGE,EC,ENTITY,INT,STAT,?,?,?,?,?,?,?> fbTs)
	{
		super(fbTs.getClassL(),fbTs.getClassD());
		this.fbTs=fbTs;
		this.callback=callback;
		sbhLocale = new SbSingleHandler<>(fbTs.getClassLocale(),this);
		th = new JsonTuple1Handler<TS>(fbTs.getClassTs());
	}
	
	public void postConstructStatistic(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
			JeeslTsFacade<CAT,SCOPE,?,?,?,TS,?,?,BRIDGE,EC,ENTITY,INT,STAT,?,?,?,?,?,?> fTs)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fTs=fTs;
		
		sbhLocale.setList(lp.getLocales());
		sbhLocale.setDefault();
		
		reloadBridges();
		logger.trace("Callback: "+callback.toString());
	}
	
	@Override
	public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
	}
	
	private void reloadBridges()
	{
		List<TS> series = fTs.all(fbTs.getClassTs());
		th.load(fTs.tpcTsDataByTs(series));
		
		List<JsonTsStatistic> list = new ArrayList<JsonTsStatistic>();
		for(TS ts : series)
		{
			JsonTsStatistic json = new JsonTsStatistic();
			if(th.contains(ts)){json.setCount(Long.valueOf(th.getMap1().get(ts).getCount()).intValue());}
			else {json.setCount(0);}
			json.setCategory(ts.getScope().getCategory().getName().get(sbhLocale.getSelection().getCode()).getLang());
			json.setScope(ts.getScope().getName().get(sbhLocale.getSelection().getCode()).getLang());
			json.setEntity(ts.getBridge().getEntityClass().getName().get(sbhLocale.getSelection().getCode()).getLang());
			json.setInterval(ts.getInterval().getName().get(sbhLocale.getSelection().getCode()).getLang());

			list.add(json);
		}
		jsonStream = "";

		try {jsonStream = JsonUtil.toString(list);}
		catch (JsonProcessingException e) {e.printStackTrace();}
	}
}