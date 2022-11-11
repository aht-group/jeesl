package org.jeesl.web.mbean.prototype.io.dashboard;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoDashboardFactoryBuilder;
import org.jeesl.factory.ejb.io.dashboard.EjbDashComponentFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.dash.JeeslIoDashComponent;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.util.comparator.ejb.system.io.dashboard.DashComponentComparator;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractIoDashComponentBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											DBC extends JeeslIoDashComponent<L,D,DBC>>
		extends AbstractAdminBean<L,D,LOC>
		implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractIoDashComponentBean.class);

	private JeeslFacade fUtils;
	private final IoDashboardFactoryBuilder<L,D,?,DBC,?,?> fbDashComponent;

	private EjbDashComponentFactory<L,D,DBC> efDashComponent;
	private final Comparator<DBC> comparatorDashComponent;

	protected List<DBC> dashComponents; public List<DBC> getDashComponents() {return dashComponents;}

	protected DBC dashComponent;

	public DBC getDashComponent() {
		return dashComponent;
	}

	public void setDashComponent(DBC dashComponent) {
		this.dashComponent = dashComponent;
	}

	public AbstractIoDashComponentBean(final IoDashboardFactoryBuilder<L,D,?,DBC,?,?> fbDashComponent)
	{
		super(fbDashComponent.getClassL(),fbDashComponent.getClassD());
		this.fbDashComponent = fbDashComponent;
		this.efDashComponent = this.fbDashComponent.dashComponent();
		comparatorDashComponent = (new DashComponentComparator<L,D,DBC>()).factory(DashComponentComparator.Type.code);
	}


	public void initSuper(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslFacade fUtils)
	{
		super.initJeeslAdmin(bTranslation, bMessage);
		this.fUtils=fUtils;
		refreshList();
	}


	private void refreshList()
	{
		dashComponents = fUtils.all(fbDashComponent.getClassDashComponent());
		Collections.sort(dashComponents,comparatorDashComponent);
	}

	@Override
	public void toggled(Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(debugOnInfo){logger.info(SbMultiHandler.class.getSimpleName()+" toggled, but NYI");}
	}

	public void selectDashComponent() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(dashComponent));}
		dashComponent = fUtils.find(fbDashComponent.getClassDashComponent(), dashComponent);
	}

	public void saveDashComponent() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(dashComponent));
		dashComponent = fUtils.save(dashComponent);
		logger.info("-----" + dashComponent.toString() +"-------");
		refreshList();
	}

	public void addDashComponent() {
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbDashComponent.getClassDashComponent()));}
		dashComponent = efDashComponent.build(null);
		dashComponent.setName(efLang.createEmpty(localeCodes));
		dashComponent.setDescription(efDescription.createEmpty(localeCodes));
	}

	public void deleteDashComponent() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.rmEntity(dashComponent));}
		fUtils.rm(dashComponent);
		dashComponent=null;
		//bMessage.growlSuccessRemoved();
		refreshList();
	}

	public void cancelDashComponent()
	{
		dashComponent = null;
	}
}