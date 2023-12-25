package org.jeesl.api.facade.module;

import java.util.List;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;

import net.sf.ahtutils.interfaces.model.qa.UtilsQaCategory;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaGroup;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaResult;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaSchedule;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaScheduleSlot;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaStaff;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaStakeholder;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaTest;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaTestDiscussion;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaTestInfo;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaUsability;
import net.sf.ahtutils.interfaces.model.qa.UtilsQualityAssurarance;

public interface JeeslQaFacade
				<L extends JeeslLang, D extends JeeslDescription,
				
				C extends JeeslSecurityCategory<L,D>,
				R extends JeeslSecurityRole<L,D,C,V,U,A>,
				V extends JeeslSecurityView<L,D,C,R,U,A>,
				U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
				A extends JeeslSecurityAction<L,D,R,V,U,AT>,
				AT extends JeeslSecurityTemplate<L,D,C>,
				USER extends JeeslUser<R>,
				STAFF extends UtilsQaStaff<R,USER,GROUP,QA,QASH>,
				GROUP extends UtilsQaGroup<STAFF,QA,QASS>,
				QA extends UtilsQualityAssurarance<STAFF,QAC,QASH>,
				QASD extends UtilsQaSchedule<QA,QASS>,
				QASS extends UtilsQaScheduleSlot<GROUP,QASD>,
				QAC extends UtilsQaCategory<QA,QAT>,
				QAT extends UtilsQaTest<GROUP,QAC,QAR,?,?,?>,
				QAU extends UtilsQaUsability,
				QAR extends UtilsQaResult<STAFF,QAT,?>,
				QASH extends UtilsQaStakeholder<QA>>

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