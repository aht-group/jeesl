package org.jeesl.factory.xml.dev.qa;

import java.util.Objects;

import org.jeesl.factory.xml.system.security.XmlStaffFactory;
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
import org.jeesl.model.xml.module.dev.qa.Actual;
import org.jeesl.model.xml.module.dev.qa.Comment;
import org.jeesl.model.xml.module.dev.qa.Result;
import org.jeesl.model.xml.module.dev.qa.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.qa.UtilsQaGroup;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaResult;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaStaff;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaTest;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaUsability;
import net.sf.ahtutils.interfaces.model.qa.UtilsQualityAssurarance;
import net.sf.exlp.util.DateUtil;

public class XmlResultFactory<L extends JeeslLang, D extends JeeslDescription,
							C extends JeeslSecurityCategory<L,D>,
							R extends JeeslSecurityRole<L,D,C,V,U,A>,
							V extends JeeslSecurityView<L,D,C,R,U,A>,
							U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
							A extends JeeslSecurityAction<L,D,R,V,U,AT>,
							AT extends JeeslSecurityTemplate<L,D,C>,
							USER extends JeeslUser<R>,
							STAFF extends UtilsQaStaff<R,USER,?,QA,?>,
							QA extends UtilsQualityAssurarance<STAFF,?,?>,
							QAR extends UtilsQaResult<STAFF,?,QARS>,
							QARS extends JeeslStatus<L,D,QARS>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlResultFactory.class);
		
	private Result q;
	private XmlStatusFactory<L,D,QARS> xfResultStatus;
	private XmlStaffFactory<L,D,C,R,V,U,A,AT,USER,STAFF,QA,QA> xfStaff;
	
	public XmlResultFactory(Result q)
	{
		this.q=q;
		if(Objects.nonNull(q.getStatus()))  {xfResultStatus = new XmlStatusFactory<>(null,q.getStatus());}
		if(Objects.nonNull(q.getStaff())) {xfStaff = new XmlStaffFactory<>(q.getStaff());}
	}
	
	public static Test build()
	{
		Test xml = new Test();

		return xml;
	}
	
	public Result build(QAR result)
	{
		Result xml = new Result();
	
		if(Objects.nonNull(q.getId())) {xml.setId(result.getId());}
		if(q.isSetRecord() && result.getRecord()!=null){xml.setRecord(DateUtil.toXmlGc(result.getRecord()));}
		
		if(Objects.nonNull(q.getStatus())) {xml.setStatus(xfResultStatus.build(result.getStatus()));}
		if(Objects.nonNull(q.getStaff())) {xml.setStaff(xfStaff.build(result.getStaff()));}
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