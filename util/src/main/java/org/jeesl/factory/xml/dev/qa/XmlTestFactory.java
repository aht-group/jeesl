package org.jeesl.factory.xml.dev.qa;

import org.jeesl.api.facade.module.JeeslQaFacade;
import org.jeesl.factory.xml.system.status.XmlStatusFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.factory.xml.status.XmlStatementFactory;
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
import net.sf.ahtutils.xml.qa.Test;

public class XmlTestFactory<L extends JeeslLang, D extends JeeslDescription,
							L2 extends JeeslLang, D2 extends JeeslDescription,
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
							QATC extends JeeslStatus<L2,D2,QATC>,
							QATS extends JeeslStatus<L2,D2,QATS>,
							QARS extends JeeslStatus<L2,D2,QARS>,
							QAUS extends JeeslStatus<L2,D2,QAUS>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlTestFactory.class);
		
	private Test q;
	
	private Class<QAT> cQAT;
	
	private XmlStatusFactory<QATS,L2,D2> xfDeveloperStatus;
	private XmlGroupsFactory<GROUP,QAT> xfGroups;
	private XmlInfoFactory<L2,D2,QATI,QATC> xfInfo;
	private XmlStatementFactory<QATS,L2,D2> xfStatement;
	
	public XmlTestFactory(Test q)
	{
		this.q=q;
		if(q.isSetStatus()){xfDeveloperStatus = new XmlStatusFactory<QATS,L2,D2>(null,q.getStatus());}
		if(q.isSetGroups()) {xfGroups = new XmlGroupsFactory<GROUP,QAT>(q.getGroups());}
		if(q.isSetInfo()) {xfInfo = new XmlInfoFactory<L2,D2,QATI,QATC>(q.getInfo());}
		if(q.isSetStatement()) {xfStatement = new XmlStatementFactory<QATS,L2,D2>(null,q.getStatement());}
	}
	
	private JeeslQaFacade<L,D,L2,D2,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS> fQa;
	public void lazyLoader(JeeslQaFacade<L,D,L2,D2,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS> fQa,Class<QAT> cQAT)
	{
		this.fQa=fQa;
		this.cQAT=cQAT;
	}
	
	
	
	public static Test build()
	{
		Test xml = new Test();

		return xml;
	}
	
	public Test build(QAT test)
	{
		if(fQa!=null){test = fQa.load(cQAT, test);}
		
		Test xml = new Test();
		
		if(q.isSetId()){xml.setId(test.getId());}
		if(q.isSetCode()){xml.setCode(test.getCode());}
		if(q.isSetName()){xml.setName(test.getName());}
		if(q.isSetVisible()){xml.setVisible(test.getVisible());}
		if(q.isSetDuration())
		{
			if(test.getDuration()!=null){xml.setDuration(test.getDuration());}
			else{xml.setDuration(0);}
		}

		if(q.isSetReference() && test.getReference()!=null){xml.setReference(XmlReferenceFactory.build(test.getReference()));}
		if(q.isSetDescription() && test.getDescription()!=null){xml.setDescription(XmlDescriptionFactory.build(test.getDescription()));}
		if(q.isSetPreCondition() && test.getPreCondition()!=null){xml.setPreCondition(XmlPreConditionFactory.build(test.getPreCondition()));}
		if(q.isSetSteps() && test.getSteps()!=null){xml.setSteps(XmlStepsFactory.build(test.getSteps()));}
		if(q.isSetExpected() && test.getExpectedResult()!=null){xml.setExpected(XmlExpectedFactory.build(test.getExpectedResult()));}
		
		if(q.isSetStatement() && test.getClientStatus()!=null){xml.setStatement(xfStatement.build(test.getClientStatus()));}
		if(q.isSetStatus() && test.getDeveloperStatus()!=null){xml.setStatus(xfDeveloperStatus.build(test.getDeveloperStatus()));}
		
		if(q.isSetResults())
		{
			XmlResultsFactory<L,D,L2,D2,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS> f = new XmlResultsFactory<L,D,L2,D2,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS>(q.getResults());
			xml.setResults(f.build(test));
		}
		
		if(q.isSetInfo() && test.getInfo()!=null) {xml.setInfo(xfInfo.build(test.getInfo()));}
		
		if(q.isSetGroups()) {xml.setGroups(xfGroups.build(test));}
		
		return xml;
	}
}