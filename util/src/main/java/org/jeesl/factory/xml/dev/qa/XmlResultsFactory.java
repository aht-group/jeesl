package org.jeesl.factory.xml.dev.qa;

import org.apache.commons.lang3.ObjectUtils;
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
import org.jeesl.model.xml.module.dev.qa.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.qa.UtilsQaResult;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaSchedule;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaScheduleSlot;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaStaff;
import net.sf.ahtutils.interfaces.model.qa.UtilsQaTest;
import net.sf.ahtutils.interfaces.model.qa.UtilsQualityAssurarance;

public class XmlResultsFactory<L extends JeeslLang, D extends JeeslDescription,
								C extends JeeslSecurityCategory<L,D>,
								R extends JeeslSecurityRole<L,D,C,V,U,A>,
								V extends JeeslSecurityView<L,D,C,R,U,A>,
								U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
								A extends JeeslSecurityAction<L,D,R,V,U,AT>,
								AT extends JeeslSecurityTemplate<L,D,C>,
								USER extends JeeslUser<R>,
								STAFF extends UtilsQaStaff<R,USER,?,QA,?>,
								
								QA extends UtilsQualityAssurarance<STAFF,?,?>,
								QASD extends UtilsQaSchedule<QA,QASS>,
								QASS extends UtilsQaScheduleSlot<?,QASD>,
								QAT extends UtilsQaTest<?,?,QAR,?,?,?>,
								QAR extends UtilsQaResult<STAFF,QAT,QARS>,
								QARS extends JeeslStatus<L,D,QARS>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlResultsFactory.class);
		
	private Results q;
	
	private XmlResultFactory<L,D,C,R,V,U,A,AT,USER,STAFF,QA,QAR,QARS> xfResult;
	
	public XmlResultsFactory(Results q)
	{
		this.q=q;
		if(ObjectUtils.isNotEmpty(q.getResult())) {xfResult = new XmlResultFactory<L,D,C,R,V,U,A,AT,USER,STAFF,QA,QAR,QARS>(q.getResult().get(0));}
	}
	
	public Results build(QAT test)
	{
		Results xml = new Results();
	
		if(ObjectUtils.isNotEmpty(q.getResult()))
		{
			for(QAR result : test.getResults())
			{
				xml.getResult().add(xfResult.build(result));
			}
		}
		
		return xml;
	}
}