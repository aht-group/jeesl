package org.jeesl.controller.web.io.ssi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.io.ssi.JeeslIoSsiDataCallback;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiLink;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSsiDataController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									
									DATA extends JeeslIoSsiData<?,STATUS,?>,
									STATUS extends JeeslIoSsiLink<L,D,STATUS,?>>
									extends AbstractJeeslWebController<L,D,LOC>
									implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSsiDataController.class);
	
	private final JeeslIoSsiDataCallback callback;
	protected final SbMultiHandler<STATUS> sbhStatus; public SbMultiHandler<STATUS> getSbhStatus() {return sbhStatus;}
	
	final Map<DATA,Object> mapJson; public Map<DATA, Object> getMapJson() {return mapJson;}

	private final List<DATA> datas; public List<DATA> getDatas() {return datas;}
	private List<DATA> selection; public List<DATA> getSelection() {return selection;} public void setSelection(List<DATA> selection) {this.selection = selection;}
	
	private String json;
	
//	<L extends JeeslLang,D extends JeeslDescription,
//	SYSTEM extends JeeslIoSsiSystem<L,D>,
//	MAPPING extends JeeslIoSsiMapping<SYSTEM,ENTITY>,
//	ATTRIBUTE extends JeeslIoSsiAttribute<MAPPING,ENTITY>,
//	DATA extends JeeslIoSsiData<MAPPING,LINK,?>,
//	LINK extends JeeslIoSsiLink<L,D,LINK,?>,
//	ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
//	CLEANING extends JeeslIoSsiCleaning<L,D,CLEANING,?>,
//	JOB extends JeeslJobStatus<L,D,JOB,?>>
	
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public JeeslSsiDataController(final JeeslIoSsiDataCallback callback, IoSsiDataFactoryBuilder<L,D,?,?,?,DATA,STATUS,?,?,?> fbSsiData)
	{
		super(fbSsiData.getClassL(),fbSsiData.getClassD());
		this.callback=callback;
		
		sbhStatus = new SbMultiHandler<>(fbSsiData.getClassLink(),this);
		
		mapJson = new HashMap<>();
		datas = new ArrayList<>();
	}

	public void postConstructSsiData(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
										JeeslIoSsiFacade<L,D,?,?,?,?,?,?,?,?,?,?> fSsi)
	{
		super.postConstructWebController(lp,bMessage);
	}
	
	public void addDatas(List<DATA> list)
	{
		datas.clear();
		datas.addAll(list);
	}

	@Override
	public void toggled(Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException {
		// TODO Auto-generated method stub
		
	}
}