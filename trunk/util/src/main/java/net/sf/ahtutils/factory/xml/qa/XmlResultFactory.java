package net.sf.ahtutils.factory.xml.qa;

import net.sf.ahtutils.factory.xml.security.XmlStaffFactory;
import net.sf.ahtutils.factory.xml.status.XmlStatusFactory;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaCategory;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaResult;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaStaff;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaStakeholder;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaTest;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaTestDiscussion;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaTestInfo;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaUsability;
import net.sf.ahtutils.interfaces.model.qa.UtilsQualityAssurarance;
import net.sf.ahtutils.model.interfaces.idm.UtilsUser;
import net.sf.ahtutils.model.interfaces.security.UtilsSecurityAction;
import net.sf.ahtutils.model.interfaces.security.UtilsSecurityCategory;
import net.sf.ahtutils.model.interfaces.security.UtilsSecurityRole;
import net.sf.ahtutils.model.interfaces.security.UtilsSecurityUsecase;
import net.sf.ahtutils.model.interfaces.security.UtilsSecurityView;
import net.sf.ahtutils.model.interfaces.status.UtilsDescription;
import net.sf.ahtutils.model.interfaces.status.UtilsLang;
import net.sf.ahtutils.model.interfaces.status.UtilsStatus;
import net.sf.ahtutils.xml.qa.Actual;
import net.sf.ahtutils.xml.qa.Comment;
import net.sf.ahtutils.xml.qa.Result;
import net.sf.ahtutils.xml.qa.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlResultFactory<L extends UtilsLang,
D extends UtilsDescription,
C extends UtilsSecurityCategory<L,D,C,R,V,U,A,USER>,
R extends UtilsSecurityRole<L,D,C,R,V,U,A,USER>,
V extends UtilsSecurityView<L,D,C,R,V,U,A,USER>,
U extends UtilsSecurityUsecase<L,D,C,R,V,U,A,USER>,
A extends UtilsSecurityAction<L,D,C,R,V,U,A,USER>,
USER extends UtilsUser<L,D,C,R,V,U,A,USER>,
STAFF extends UtilsQaStaff<L,D,C,R,V,U,A,USER,STAFF,QA,QAC,QAT,QAU,QAR,QAS,QATD,QATI,QATC,QATS,QARS,QAUS>,
QA extends UtilsQualityAssurarance<L,D,C,R,V,U,A,USER,STAFF,QA,QAC,QAT,QAU,QAR,QAS,QATD,QATI,QATC,QATS,QARS,QAUS>,
QAC extends UtilsQaCategory<L,D,C,R,V,U,A,USER,STAFF,QA,QAC,QAT,QAU,QAR,QAS,QATD,QATI,QATC,QATS,QARS,QAUS>,
QAT extends UtilsQaTest<L,D,C,R,V,U,A,USER,STAFF,QA,QAC,QAT,QAU,QAR,QAS,QATD,QATI,QATC,QATS,QARS,QAUS>,
QAU extends UtilsQaUsability<L,D,C,R,V,U,A,USER,STAFF,QA,QAC,QAT,QAU,QAR,QAS,QATD,QATI,QATC,QATS,QARS,QAUS>,
QAR extends UtilsQaResult<L,D,C,R,V,U,A,USER,STAFF,QA,QAC,QAT,QAU,QAR,QAS,QATD,QATI,QATC,QATS,QARS,QAUS>,
QAS extends UtilsQaStakeholder<L,D,C,R,V,U,A,USER,STAFF,QA,QAC,QAT,QAU,QAR,QAS,QATD,QATI,QATC,QATS,QARS,QAUS>,
QATD extends UtilsQaTestDiscussion<L,D,C,R,V,U,A,USER,STAFF,QA,QAC,QAT,QAU,QAR,QAS,QATD,QATI,QATC,QATS,QARS,QAUS>,
QATI extends UtilsQaTestInfo<L,D,C,R,V,U,A,USER,STAFF,QA,QAC,QAT,QAU,QAR,QAS,QATD,QATI,QATC,QATS,QARS,QAUS>,
QATC extends UtilsStatus<QATC,L,D>,
QATS extends UtilsStatus<QATS,L,D>,
QARS extends UtilsStatus<QARS,L,D>,
QAUS extends UtilsStatus<QAUS,L,D>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlResultFactory.class);
		
	private Result q;
	
	public XmlResultFactory(Result q)
	{
		this.q=q;
	}
	
	public static Test build()
	{
		Test xml = new Test();

		return xml;
	}
	
	public Result build(QAR result)
	{
		Result xml = new Result();
	
		if(q.isSetStatus())
		{
			XmlStatusFactory f = new XmlStatusFactory(null,q.getStatus());
			xml.setStatus(f.build(result.getStatus()));
		}
		
		if(q.isSetStaff())
		{
			XmlStaffFactory<L,D,C,R,V,U,A,USER,STAFF,QA> f = new XmlStaffFactory<L,D,C,R,V,U,A,USER,STAFF,QA>(q.getStaff());
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