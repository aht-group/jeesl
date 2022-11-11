package org.jeesl.web.mbean.prototype.module.lf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslLogframeFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.LfFactoryBuilder;
import org.jeesl.interfaces.model.module.lf.JeeslLfConfiguration;
import org.jeesl.interfaces.model.module.lf.JeeslLfLogframe;
import org.jeesl.interfaces.model.module.lf.indicator.JeeslLfIndicator;
import org.jeesl.interfaces.model.module.lf.indicator.JeeslLfIndicatorLevel;
import org.jeesl.interfaces.model.module.lf.indicator.JeeslLfIndicatorType;
import org.jeesl.interfaces.model.module.lf.indicator.JeeslLfUnit;
import org.jeesl.interfaces.model.module.lf.indicator.JeeslLfVerificationSource;
import org.jeesl.interfaces.model.module.lf.time.JeeslLfTimeElement;
import org.jeesl.interfaces.model.module.lf.time.JeeslLfTimeGroup;
import org.jeesl.interfaces.model.module.lf.time.JeeslLfTimeInterval;
import org.jeesl.interfaces.model.module.lf.value.JeeslLfValue;
import org.jeesl.interfaces.model.module.lf.value.JeeslLfValueType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractLfConfigurationBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
												R extends JeeslTenantRealm<L,D,R,?>,
												LF extends JeeslLfLogframe<L,D,R,LFI,IL,IT>,
												LFI extends JeeslLfIndicator<LF,IL,IT,IU,IV,TG,LFM>,
												IL extends JeeslLfIndicatorLevel<L, D,R, IL, ?>,
												IT extends JeeslLfIndicatorType<L,D,R,IT, ?>,
												IU extends JeeslLfUnit<L,D,R,IU,?>,
												IV extends JeeslLfVerificationSource<L,D,R,IV,?>,
												TG extends JeeslLfTimeGroup<L,TI>,
												TI extends JeeslLfTimeInterval<L,D,TI,?>,
												TE extends JeeslLfTimeElement<L,TG>,
												LFM extends JeeslLfValue<LFI,VT,TG,TE>,
												VT extends JeeslLfValueType<L,D,VT,?>,
												LFC extends JeeslLfConfiguration<LF,IT>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractLfConfigurationBean.class);

	protected final LfFactoryBuilder<L,D,R,LF,LFI,IL,IT,IU,IV,TG,TI,TE,LFM,VT,LFC> fbLf;
	protected JeeslLogframeFacade<L,D,R,LF,LFI,IL,IT,IU,IV,TG,TI,TE,LFM,VT,LFC> fLf;

	protected List<IT> indicatorTypes; public List<IT> getIndicatorTypes() {return indicatorTypes;}

	private LF logframe; public LF getLogframe() {return logframe;} public void setLogframe(LF logframe) {this.logframe = logframe;}
	private List<LFC> configurations; public List<LFC> getConfigurations() {return configurations;}
	private LFC configuration; public LFC getConfiguration() {return configuration;} public void setConfiguration(LFC configuration) {this.configuration = configuration;}

	public AbstractLfConfigurationBean(LfFactoryBuilder<L,D,R,LF,LFI,IL,IT,IU,IV,TG,TI,TE,LFM,VT,LFC> fbLf)
	{
		super(fbLf.getClassL(),fbLf.getClassD());
		this.fbLf=fbLf;
		this.indicatorTypes = new ArrayList<IT>();
	}

	protected void postConstructHd(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslLogframeFacade<L,D,R,LF,LFI,IL,IT,IU,IV,TG,TI,TE,LFM,VT,LFC> fLf)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fLf = fLf;
		this.indicatorTypes = fLf.all(fbLf.getClassIT());
		reloadConfigurations();
	}

	protected void reloadConfigurations()
	{
		configurations = new ArrayList<>();
		if(logframe.getId() != 0){configurations = fLf.allForParent(fbLf.getClassLFC(), logframe);}

		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbLf.getClassLFC(),configurations));}
	}

	public void addConfiguration()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbLf.getClassLFC()));}
		configuration =  fbLf.ejbLfConfiguration().build();
		configuration.setLogframe(logframe);
	}

	public void saveConfiguration() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(configuration));}
		IT type = fLf.find(fbLf.getClassIT(), configuration.getType().getId());
		configuration.setType(type);
		configuration = fLf.save(configuration);
		reloadConfigurations();
	}

	public void selectConfiguration(LFC selectdConfiguration) throws JeeslConstraintViolationException
	{
		configuration = fLf.find(fbLf.getClassLFC(),selectdConfiguration);
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(selectdConfiguration));}
	}

	public void deleteConfiguration() throws JeeslConstraintViolationException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.rmEntity(configuration));}
		fLf.rm(configuration);
		reloadConfigurations();
		reset();
		resetConfiguration();
	}

	public void resetConfiguration() {reset();}

	private void reset()
	{
		configuration=null; configurations=new ArrayList<>();
	}


}