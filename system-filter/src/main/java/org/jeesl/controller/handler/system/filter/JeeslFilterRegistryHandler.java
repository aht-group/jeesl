package org.jeesl.controller.handler.system.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.ejb.c.system.filter.EjbFilterFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.bean.system.JeeslFilterBean;
import org.jeesl.interfaces.controller.handler.system.JeeslFilterHandler;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.system.filter.SystemFilter;
import org.jeesl.model.ejb.system.filter.SystemFilterScope;
import org.jeesl.model.ejb.system.filter.SystemFilterType;
import org.jeesl.model.ejb.system.security.user.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslFilterRegistryHandler implements JeeslFilterHandler,SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslFilterRegistryHandler.class);
	
	private JeeslLocaleProvider<IoLocale> lp;
	private JeeslFacade fFilter;
	
	private JeeslFilterBean<SystemFilter> callback;
	
	private final SbSingleHandler<SystemFilter> sbh; public SbSingleHandler<SystemFilter> getSbh() {return sbh;}

	protected final EjbLangFactory<IoLang> efLang;
	
	private final List<SystemFilterScope> scopes; public List<SystemFilterScope> getScopes() {return scopes;}
	
	private SecurityUser user;
	private SystemFilterType type;
	private SystemFilter filter; public SystemFilter getFilter() {return filter;} public void setFilter(SystemFilter filter) {this.filter = filter;}
	private String singleLang; public String getSingleLang() {return singleLang;} public void setSingleLang(String singleLang) {this.singleLang = singleLang;}
	
	public JeeslFilterRegistryHandler(JeeslFilterBean<SystemFilter> callback)
	{
		this.callback=callback;
		
		sbh = new SbSingleHandler<>(SystemFilter.class,this);
		
		efLang = EjbLangFactory.instance(IoLang.class);
		
		scopes = new ArrayList<>();
	}

	public JeeslFilterRegistryHandler postConstruct(JeeslLocaleProvider<IoLocale> lp, JeeslFacesMessageBean bMessage, JeeslFacade fFilter)
	{
		this.lp=lp;
		this.fFilter=fFilter;
		
		this.reloadFilters();
		
		return this;
	}
	
	public <E extends Enum<E>> JeeslFilterRegistryHandler scope(E code) {scopes.add(fFilter.fByEnum(SystemFilterScope.class,code)); return this;}
	public <E extends Enum<E>> JeeslFilterRegistryHandler type(E code) {type = fFilter.fByEnum(SystemFilterType.class,code); return this;}
	public JeeslFilterRegistryHandler user(SecurityUser user) {this.user=user; return this;}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.selectEntity(item));
		filter = (SystemFilter)item;
		if(Objects.nonNull(callback)) {callback.applyFilter(this,filter);}
	}
	
	private void reloadFilters()
	{
		sbh.getList().clear();
		sbh.getList().addAll(fFilter.all(SystemFilter.class));
	}
	
	public void addFilter()
	{
		filter = EjbFilterFactory.instance().build(null,type,user,sbh.getList());
		if(ObjectUtils.isNotEmpty(scopes)) {filter.setScope(scopes.get(0));}
		
		filter.setName(efLang.buildEmpty(lp.getLocales()));
	}
	
	public void selectFilter()
	{
		logger.info(AbstractLogMessage.selectEntity(filter));
	}
	
	public void saveFilter() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(filter));
		efLang.persistMissingLangs(fFilter,lp,filter);
		
		if(ObjectUtils.isNotEmpty(singleLang))
		{
			for(IoLocale l : lp.getLocales()) {filter.getName().get(l.getCode()).setLang(singleLang);}
		}
		filter = fFilter.save(filter);
		this.reloadFilters();
		if(Objects.nonNull(callback)) {callback.storeFilter(this, filter);}
	}
	
	public void applyDefault()
	{
		sbh.setDefault();
		if(sbh.isSelected() && Objects.nonNull(callback)) {callback.applyFilter(this,sbh.getSelection());}
	}
}