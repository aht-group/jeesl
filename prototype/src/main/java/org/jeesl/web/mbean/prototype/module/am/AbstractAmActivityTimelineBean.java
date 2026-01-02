package org.jeesl.web.mbean.prototype.module.am;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslAmFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.AmFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.model.module.am.JeesAmProject;
import org.jeesl.interfaces.model.module.am.JeeslAmActivity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAmActivityTimelineBean <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										REALM extends JeeslTenantRealm<L,D,REALM,?>,
										ACTIVITY extends JeeslAmActivity<L,D,REALM,ACTIVITY,PROJ>,
										PROJ extends JeesAmProject<L,D,REALM,ACTIVITY,PROJ>
										>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbToggleBean,SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAmActivityTimelineBean.class);

	private final AmFactoryBuilder<L,D,LOC,REALM,ACTIVITY,PROJ> fbAm;

	protected JeeslAmFacade<L,D,LOC,REALM,ACTIVITY,PROJ> fAm;


	protected String[] amLocales; public String[] getAmLocales() {return amLocales;}
	protected final SbSingleHandler<PROJ> sbhProject; public SbSingleHandler<PROJ> getSbhProject() {return sbhProject;}
	private final SbSingleHandler<LOC> sbhLocale; public SbSingleHandler<LOC> getSbhLocale() {return sbhLocale;}


	protected PROJ project; public PROJ getProject() {return project;} public void setProject(PROJ project) {this.project = project;}
	private ACTIVITY timelineGroup; public ACTIVITY getTimelineGroup() {return timelineGroup;} public void setTimelineGroup(ACTIVITY timelineGroup) {this.timelineGroup = timelineGroup;}
	private String currentLocaleCode; public String getCurrentLocaleCode() {return currentLocaleCode;} public void setCurrentLocaleCode(String currentLocaleCode) {this.currentLocaleCode = currentLocaleCode;}


	public AbstractAmActivityTimelineBean(AmFactoryBuilder<L,D,LOC,REALM,ACTIVITY,PROJ> fbAm)
	{
		super(fbAm.getClassL(),fbAm.getClassD());
		this.fbAm=fbAm;

		sbhProject = new SbSingleHandler<>(fbAm.getClassProject(),this);
		sbhLocale = new SbSingleHandler<>(fbAm.getClassLocale(),this);

	}

	protected void postConstructAm(JeeslTranslationBean<L,D,LOC> bTranslation, String currentLocaleCode,
									List<LOC> locales, JeeslFacesMessageBean bMessage,
									JeeslAmFacade<L,D,LOC,REALM,ACTIVITY,PROJ> fAm)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.setCurrentLocaleCode(currentLocaleCode);
		this.fAm=fAm;
		sbhProject.setList(fAm.allOrderedPosition(fbAm.getClassProject()));
		sbhProject.setDefault();
		project = sbhProject.getSelection();
		reloadProject();
	}


	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.toggled(c));
	}

	@SuppressWarnings("unchecked")
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId ejb)
	{
		if(ejb==null) {project=null;}
		else if(JeesAmProject.class.isAssignableFrom(ejb.getClass()))
		{
			project = (PROJ)ejb;
			if(EjbIdFactory.isSaved(project))
			{
				reloadProject();
			}
			logger.info("Twice:"+sbhProject.getTwiceSelected()+" for "+project.toString());
		}
		else
		{
			logger.info("NOT Assignable");
		}
		reset(true);
	}

	private void reset(boolean rTimelineGroup)
	{
		if(rTimelineGroup) {timelineGroup=null;}
	}



	abstract protected void reloadProject();

}