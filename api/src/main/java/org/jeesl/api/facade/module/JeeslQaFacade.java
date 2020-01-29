package org.jeesl.api.facade.module;

import java.util.List;

import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;

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
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public interface JeeslQaFacade
				<L extends UtilsLang, D extends UtilsDescription,
				L2 extends UtilsLang, D2 extends UtilsDescription,
				C extends JeeslSecurityCategory<L,D>,
				R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
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
				QAT extends UtilsQaTest<GROUP,QAC,QAR,QATD,QATI,QATS>,
				QAU extends UtilsQaUsability,
				QAR extends UtilsQaResult<STAFF,QAT,QARS>,
				QASH extends UtilsQaStakeholder<QA>,
				QATD extends UtilsQaTestDiscussion<STAFF,QAT>,
				QATI extends UtilsQaTestInfo<QATC>,
				QATC extends UtilsStatus<QATC,L2,D2>,
				QATS extends UtilsStatus<QATS,L2,D2>,
				QARS extends UtilsStatus<QARS,L2,D2>,
				QAUS extends UtilsStatus<QAUS,L2,D2>>

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