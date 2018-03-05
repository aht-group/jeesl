package org.jeesl.factory.xml.dev.qa;

import org.jeesl.factory.xml.system.security.XmlStaffFactory;
import org.jeesl.factory.xml.system.status.XmlStatusFactory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
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
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.xml.qa.Actual;
import net.sf.ahtutils.xml.qa.Comment;
import net.sf.ahtutils.xml.qa.Result;
import net.sf.ahtutils.xml.qa.Test;
import net.sf.exlp.util.DateUtil;

public class XmlResultFactory<L extends UtilsLang, D extends UtilsDescription,
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
							QAT extends UtilsQaTest<L,D,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS>,
							QAU extends UtilsQaUsability<L,D,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS>,
							QAR extends UtilsQaResult<STAFF,QAT,QARS>,
							QASH extends UtilsQaStakeholder<L,D,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS>,
							QATD extends UtilsQaTestDiscussion<L,D,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS>,
							QATI extends UtilsQaTestInfo<L,D,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS>,
							QATC extends UtilsStatus<QATC,L,D>,
							QATS extends UtilsStatus<QATS,L,D>,
							QARS extends UtilsStatus<QARS,L,D>,
							QAUS extends UtilsStatus<QAUS,L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlResultFactory.class);
		
	private Result q;
	private XmlStatusFactory<QARS,L,D> xfResultStatus;
	
	public XmlResultFactory(Result q)
	{
		this.q=q;
		if(q.isSetStatus()) {xfResultStatus = new XmlStatusFactory<QARS,L,D>(null,q.getStatus());}
	}
	
	public static Test build()
	{
		Test xml = new Test();

		return xml;
	}
	
	public Result build(QAR result)
	{
		Result xml = new Result();
	
		if(q.isSetId()){xml.setId(result.getId());}
		if(q.isSetRecord() && result.getRecord()!=null){xml.setRecord(DateUtil.toXmlGc(result.getRecord()));}
		
		if(q.isSetStatus()){xml.setStatus(xfResultStatus.build(result.getStatus()));}
		
		if(q.isSetStaff())
		{
			XmlStaffFactory<L,D,C,R,V,U,A,AT,USER,STAFF,QA,QA> f = new XmlStaffFactory<L,D,C,R,V,U,A,AT,USER,STAFF,QA,QA>(q.getStaff());
			xml.setStaff(f.build(result.getStaff()));
		}
		
		if(q.isSetActual()){xml.setActual(buildActual(result.getActualResult()));}
		if(q.isSetComment()){xml.setComment(buildComment(result.getComment()));}
		
		return xml;
	}
	
	public static Actual buildActual(String actual)
	{
		Actual xml = new Actual();
		if(actual==null){xml.setValue("");}
		else{xml.setValue(actual);}
		return xml;
	}
	
	public static Comment buildComment(String comment)
	{
		Comment xml = new Comment();
		if(comment==null){xml.setValue("");}
		else{xml.setValue(comment);}
		return xml;
	}
}