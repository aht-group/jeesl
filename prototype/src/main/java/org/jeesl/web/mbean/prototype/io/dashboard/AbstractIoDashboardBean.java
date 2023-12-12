package org.jeesl.web.mbean.prototype.io.dashboard;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.controller.util.comparator.ejb.io.dashboard.DashboardComparator;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoDashboardFactoryBuilder;
import org.jeesl.factory.ejb.io.dashboard.EjbDashboardFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.dash.JeeslIoDashComponent;
import org.jeesl.interfaces.model.io.dash.JeeslIoDashComponentPosition;
import org.jeesl.interfaces.model.io.dash.JeeslIoDashboard;
import org.jeesl.interfaces.model.io.dash.JeeslIoDashboardResolution;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.jsf.handler.op.OpEntitySelectionHandler;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractIoDashboardBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											DBR extends JeeslIoDashboardResolution<L,D,DBR,?>,
											DBC extends JeeslIoDashComponent<L,D,DBC>,
											DBCP extends JeeslIoDashComponentPosition<L,D,DBR,DBC,DB,DBCP>,
											DB extends JeeslIoDashboard<L,D,DBR,DBCP,DB>
											>
		extends AbstractAdminBean<L,D,LOC>
		implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractIoDashboardBean.class);

	private JeeslFacade fUtils;
	private final IoDashboardFactoryBuilder<L,D,DBR,DBC,DBCP,DB> fbDashboard;

	private EjbDashboardFactory<L,D,DB> efDashboard;

	private final SbMultiHandler<DBR> sbhResolution; public SbMultiHandler<DBR> getSbhResolution() {return sbhResolution;}
	private final Comparator<DB> comparatorDashboard;

	private OpEntitySelectionHandler<DBC> opDashComponents;
	public OpEntitySelectionHandler<DBC> getOpDashComponents() {return opDashComponents;} public void setOpDashComponents(OpEntitySelectionHandler<DBC> opDashComponents) {this.opDashComponents = opDashComponents;}
	protected List<DB> dashboards; public List<DB> getDashboards() {return dashboards;}
	protected List<DBR> resolutions; public List<DBR> getResolutions() {return resolutions;}

	protected List<DBC> components; public List<DBC> getComponents() {return components;}

	protected DB dashboard;
	protected DBCP dashComponentPosition;
	public DBCP getDashComponentPosition() {return dashComponentPosition;} public void setDashComponentPosition(DBCP dashComponentPosition) {this.dashComponentPosition = dashComponentPosition;}
	public DB getDashboard() {
		return dashboard;
	}

	public void setDashboard(DB dashboard) {
		this.dashboard = dashboard;
	}

	public AbstractIoDashboardBean(final IoDashboardFactoryBuilder<L,D,DBR,DBC,DBCP,DB> fbDashboard)
	{
		super(fbDashboard.getClassL(),fbDashboard.getClassD());
		this.fbDashboard = fbDashboard;
		this.efDashboard = this.fbDashboard.dashboard();
		sbhResolution = new SbMultiHandler<DBR>(fbDashboard.getClassResolution(),this);
		comparatorDashboard = (new DashboardComparator<L,D,DB>()).factory(DashboardComparator.Type.code);
	}


	public void initSuper(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslFacade fUtils)
	{
		super.initJeeslAdmin(bTranslation, bMessage);
		this.fUtils=fUtils;

		sbhResolution.setList(fUtils.allOrderedPositionVisible(fbDashboard.getClassResolution()));
		sbhResolution.selectAll();
		if(debugOnInfo){logger.info(SbMultiHandler.class.getSimpleName()+": "+fbDashboard.getClassResolution().getSimpleName()+" "+sbhResolution.getSelected().size()+"/"+sbhResolution.getList().size());}
		refreshList();
	}


	private void refreshList()
	{
		dashboards = fUtils.all(fbDashboard.getClassDashboard());
		resolutions = fUtils.all(fbDashboard.getClassResolution());
		components = fUtils.all(fbDashboard.getClassDashComponent());
		Collections.sort(dashboards,comparatorDashboard);
	}

	@Override
	public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(debugOnInfo){logger.info(SbMultiHandler.class.getSimpleName()+" toggled, but NYI");}
	}

	public void selectDashboard() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(dashboard));}
		dashboard = fUtils.find(fbDashboard.getClassDashboard(), dashboard);
	}

	public void saveDashboard() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(dashboard));
		if(dashboard.getResolution()!=null){
			DBR resolution = fUtils.find(fbDashboard.getClassResolution(),dashboard.getResolution());
			dashboard.setResolution(resolution);
			}
		dashboard = fUtils.save(dashboard);
		logger.info("-----" + dashboard.toString() +"-------");
		refreshList();
	}

	public void addDashboard() {
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbDashboard.getClassDashboard()));}
		dashboard = efDashboard.build(null);
		dashboard.setName(efLang.createEmpty(localeCodes));
		dashboard.setDescription(efDescription.createEmpty(localeCodes));
		Set<DBCP> componentPositions = new HashSet<DBCP>();
		dashboard.setComponentPositions(componentPositions);
	}

	public void deleteDashboard() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.deleteEntity(dashboard));}
		fUtils.rm(dashboard);
		bMessage.growlDeleted(dashboard);
		dashboard=null;
		refreshList();
	}

	public void cancelDashboard()
	{
		dashboard = null;
	}

	public void addDashboardComponent()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbDashboard.getClassDashComponentPosition()));}
		Set<DBCP> componentPositions = new HashSet<DBCP>();
		if(dashboard.getComponentPositions() == null) {
			dashboard.setComponentPositions(componentPositions);
		}
		dashComponentPosition = fbDashboard.dashComponentPosition().build();
		if(debugOnInfo){logger.info("dashComponentPosition is set");}
	}

	public void saveDashboardComponent() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info("saving dashComponentPosition");}
		Set<DBCP> componentPositions = dashboard.getComponentPositions();
		dashComponentPosition.setComponent(fUtils.find(fbDashboard.getClassDashComponent(), dashComponentPosition.getComponent()));
		dashComponentPosition.setDashboard(dashboard);

		componentPositions.add(dashComponentPosition);
		dashboard = fUtils.save(dashboard);
		if(debugOnInfo){logger.info("saved dashComponentPosition");}
	}

	@SuppressWarnings("unchecked")
	public void selectDashboardComponent(SelectEvent event)
	{
		if(debugOnInfo){logger.info("select dashComponentPosition");}
		dashComponentPosition =(DBCP) event.getObject();
	}

	public void cancelDashboardComponent()
	{
		dashComponentPosition = null;
	}

	public void deleteDashboardComponent() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.deleteEntity(dashboard));}
		fUtils.rm(dashComponentPosition);
		bMessage.growlDeleted(dashComponentPosition);
		dashComponentPosition = null;
		dashboard = fUtils.find(fbDashboard.getClassDashboard(), dashboard);
	}
}