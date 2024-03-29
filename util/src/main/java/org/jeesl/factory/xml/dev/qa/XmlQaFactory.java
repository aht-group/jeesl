package org.jeesl.factory.xml.dev.qa;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.model.xml.module.dev.qa.Qa;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class XmlQaFactory<L extends JeeslLang,
D extends JeeslDescription,
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
QAT extends UtilsQaTest<GROUP,QAC,QAR,QATD,QATI,QATS>,
QAU extends UtilsQaUsability,
QAR extends UtilsQaResult<STAFF,QAT,QARS>,
QASH extends UtilsQaStakeholder<QA>,
QATD extends UtilsQaTestDiscussion<STAFF,QAT>,
QATI extends UtilsQaTestInfo<QATC>,
QATC extends JeeslStatus<L,D,QATC>,
QATS extends JeeslStatus<L,D,QATS>,
QARS extends JeeslStatus<L,D,QARS>,
QAUS extends JeeslStatus<L,D,QAUS>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlQaFactory.class);
		
	@SuppressWarnings("unused")
	private Qa q;
	
	public XmlQaFactory(Qa q)
	{
		this.q=q;
	}
	
	public static Qa build()
	{
		Qa xml = new Qa();

		return xml;
	}
}