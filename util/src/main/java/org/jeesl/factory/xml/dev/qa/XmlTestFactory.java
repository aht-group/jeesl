package org.jeesl.factory.xml.dev.qa;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.module.dev.JeeslQaFacade;
import org.jeesl.factory.xml.system.status.XmlStatusFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.model.xml.module.dev.qa.Test;
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
import net.sf.ahtutils.interfaces.model.qa.UtilsQualityAssurarance;

public class XmlTestFactory<L extends JeeslLang, D extends JeeslDescription,
							C extends JeeslSecurityCategory<L,D>,
							R extends JeeslSecurityRole<L,D,C,V,U,A>,
							V extends JeeslSecurityView<L,D,C,R,U,A>,
							U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
							A extends JeeslSecurityAction<L,D,R,V,U,AT>,
							AT extends JeeslSecurityTemplate<L,D,C>,
							USER extends JeeslUser<R>,
							STAFF extends UtilsQaStaff<R,USER,GROUP,QA,QASH>,
							GROUP extends UtilsQaGroup<STAFF,QA,QASS>,
							QA extends UtilsQualityAssurarance<STAFF,?,QASH>,
							QASD extends UtilsQaSchedule<QA,QASS>,
							QASS extends UtilsQaScheduleSlot<GROUP,QASD>,
							QAT extends UtilsQaTest<GROUP,?,QAR,QATD,QATI,QATS>,
							QAR extends UtilsQaResult<STAFF,QAT,QARS>,
							QASH extends UtilsQaStakeholder<QA>,
							QATD extends UtilsQaTestDiscussion<STAFF,QAT>,
							QATI extends UtilsQaTestInfo<QATC>,
							QATC extends JeeslStatus<L,D,QATC>,
							QATS extends JeeslStatus<L,D,QATS>,
							QARS extends JeeslStatus<L,D,QARS>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlTestFactory.class);
		
	private Test q;
	
	private Class<QAT> cQAT;
	
	private XmlStatusFactory<L,D,QATS> xfDeveloperStatus;
	private XmlGroupsFactory<GROUP,QAT> xfGroups;
	private XmlInfoFactory<L,D,QATI,QATC> xfInfo;
	private XmlStatementFactory<L,D,QATS> xfStatement;
	private XmlResultsFactory<L,D,C,R,V,U,A,AT,USER,STAFF,QA,QASD,QASS,QAT,QAR,QARS> xfResults;
	
	public XmlTestFactory(Test q)
	{
		this.q=q;
		if(Objects.nonNull(q.getStatus())) {xfDeveloperStatus = new XmlStatusFactory<>(null,q.getStatus());}
		if(q.isSetGroups()) {xfGroups = new XmlGroupsFactory<GROUP,QAT>(q.getGroups());}
		if(q.isSetInfo()) {xfInfo = new XmlInfoFactory<L,D,QATI,QATC>(q.getInfo());}
		if(q.isSetStatement()) {xfStatement = new XmlStatementFactory<L,D,QATS>(null,q.getStatement());}
		
		if(Objects.nonNull(q.getResults())) {xfResults = new XmlResultsFactory<L,D,C,R,V,U,A,AT,USER,STAFF,QA,QASD,QASS,QAT,QAR,QARS>(q.getResults());}
	}
	
	private JeeslQaFacade<L,D,STAFF,GROUP,QA,QASD,QASS,?,QAT> fQa;
	public void lazyLoader(JeeslQaFacade<L,D,STAFF,GROUP,QA,QASD,QASS,?,QAT> fQa,Class<QAT> cQAT)
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
		
		if(Objects.nonNull(q.getId())) {xml.setId(test.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(test.getCode());}
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
		if(ObjectUtils.allNotNull(q.getStatus(),test.getDeveloperStatus())) {xml.setStatus(xfDeveloperStatus.build(test.getDeveloperStatus()));}
		
		if(q.isSetResults()) {xml.setResults(xfResults.build(test));}
		
		if(q.isSetInfo() && test.getInfo()!=null) {xml.setInfo(xfInfo.build(test.getInfo()));}
		
		if(q.isSetGroups()) {xml.setGroups(xfGroups.build(test));}
		
		return xml;
	}
}