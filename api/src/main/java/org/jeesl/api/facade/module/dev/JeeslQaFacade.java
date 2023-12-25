package org.jeesl.api.facade.module.dev;

import java.util.List;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;

import net.sf.ahtutils.interfaces.model.qa.UtilsQaCategory;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaGroup;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaSchedule;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaScheduleSlot;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaStaff;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaTest;
import net.sf.ahtutils.interfaces.model.qa.UtilsQualityAssurarance;

public interface JeeslQaFacade <L extends JeeslLang, D extends JeeslDescription,
								STAFF extends UtilsQaStaff<?,?,GROUP,QA,?>,
								GROUP extends UtilsQaGroup<STAFF,QA,QASS>,
								QA extends UtilsQualityAssurarance<STAFF,QAC,?>,
								QASD extends UtilsQaSchedule<QA,QASS>,
								QASS extends UtilsQaScheduleSlot<GROUP,QASD>,
								QAC extends UtilsQaCategory<QA,QAT>,
								QAT extends UtilsQaTest<GROUP,QAC,?,?,?,?>>

		extends JeeslFacade
{	
		QA load(Class<QA> cQa, QA qa);
		QAC load(Class<QAC> cQac, QAC category);
		QAT load(Class<QAT> cTest, QAT test);
		GROUP load(Class<GROUP> cGroup, GROUP group);
		QASD load(Class<QASD> cSchedule, QASD schedule);
		QASS load(Class<QASS> cSlot, QASS slot);
		STAFF load(Class<STAFF> cStaff, STAFF staff);
		
		List<GROUP> fQaGroups(Class<GROUP> cGroup, QA qa);
		List<QAT> fQaTests(Class<QAT> cTest, Class<QAC> cCategory, Class<QA> cQa, QA qa);
		List<QAT> fQaTests(Class<QAT> lTest, Class<QAC> cCategory, List<QAC> category);
}