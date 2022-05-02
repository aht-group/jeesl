package org.jeesl.controller.handler.sb.decade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jeesl.api.handler.sb.SbDateIntervalSelection;
import org.jeesl.controller.handler.sb.SbDateIntervalHandler;
import org.jeesl.controller.handler.sb.tree.SbTree2Handler;
import org.jeesl.controller.handler.tree.TreeUpdateParameter;
import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.controller.handler.OutputXpathPattern;
import org.jeesl.interfaces.controller.handler.tree.JeeslTreeSelected;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.hydro.JeeslHydroDecade;
import org.jeesl.interfaces.model.module.hydro.JeeslHydroYear;
import org.jeesl.util.comparator.ejb.component.sb.HydroCodeComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.StringUtil;

@SuppressWarnings("rawtypes")
public class SbDecadeTreeHandler<HD extends JeeslHydroDecade, HY extends JeeslHydroYear> extends SbTree2Handler<HD, HY> implements Serializable,SbDateIntervalSelection
{
	final static Logger logger = LoggerFactory.getLogger(SbDecadeTreeHandler.class);
	private static final long serialVersionUID = 1L;

	private final JeeslFacade fUtils;
	private final Class<HD> cDecade; public Class<HD> getClassDecade(){return cDecade;}
	private final Class<HY> cYear; public Class<HY> getClassYear(){return cYear;}

	public HD getDecade() {return l1;}
	public HY getYear() {return l2;}

	private SbDateIntervalHandler sbDateHandler;
	private HydroCodeComparator<HD> cpHydroDecade;
	private HydroCodeComparator<HY> cpHydroYear;
	public SbDateIntervalHandler getSbDateHandler() {return sbDateHandler;}

	public SbDecadeTreeHandler(JeeslTreeSelected callback, JeeslFacade fUtils, final Class<HD> cDecade, final Class<HY> cYear)
	{
		super(callback,new SbDecadeTreeCache<HD,HY>(fUtils,cDecade,cYear),new SbDecadeTreeStore<HD,HY>());
		this.fUtils=fUtils;
		this.cDecade = cDecade;
		this.cYear = cYear;
		activateGlobalIgnores();

		xpath1 = OutputXpathPattern.multiLang;
		xpath2 = OutputXpathPattern.multiLang;

		sbDateHandler = new SbDateIntervalHandler(this);
		cpHydroDecade = new  HydroCodeComparator<>();
		cpHydroYear = new  HydroCodeComparator<>();
	}

	public void update() throws JeeslNotFoundException
	{
		ProcessingTimeTracker ptt = new ProcessingTimeTracker(true);
		reset2();
		viewIsGlobal = true;//identity.hasSystemView(view.getCode());

		if(viewIsGlobal)
		{
			if(debugOnInfo) {logger.info("Global View, populating Decades");}
			List<HD> decades = fUtils.all(getClassDecade());
			Collections.sort(decades,cpHydroDecade);
			list1.addAll(decades);
			selectGlobal();
		}
		else
		{
			if(debugOnInfo) {logger.info("Security view, Applying Domain Roles ... populating years" );}
			List<HY> list = new ArrayList<HY>();
			list = fUtils.all(getClassYear());
			Collections.sort(list,cpHydroYear);
			addAllowedL2(list);

			selectSecurity2();
		}

		ptt.stop();
		logger.info("Update "+ptt.toTotalPeriod());
	}

	private void selectGlobal()
	{
		if(debugOnInfo) {logger.info("Selecting Global");}

		if(debugOnInfo) {logger.info("\tSelecting "+getClassDecade().getSimpleName()+" from ");}
		super.cascade1(this.getDecade()!=null ? this.getDecade() : list1.get(0),TreeUpdateParameter.build(false,true,true,true,true));

	}

	protected void activateGlobalIgnores()
	{
		//It is possible to add Elements to the ignoreX sets
		//ignore2.add(object)
	}

	// The methods to get the parent element need to be specified for each hierarchy level
	@SuppressWarnings("unchecked")
	@Override protected HD getParentForL2(HY type) { return (HD) type.getDecade(); }

	@Override
	public void debug(boolean debug)
	{
		if(debug)
		{
			logger.info(StringUtil.stars());
			super.debug(debug);
		}
	}
	@Override
	public void callbackDateChanged() {
		logger.info("sbDateHandler callback  method");

	}
	public void updateDateRange() {
		if(this.getYear() != null) {
			sbDateHandler.setDate1(this.getYear().getValidFrom());
			sbDateHandler.setDate2(this.getYear().getValidUntil());
		}

	}
}