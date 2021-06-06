package org.jeesl.factory.xml.dev.qa;

import org.jeesl.api.facade.module.JeeslQaFacade;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
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
import net.sf.ahtutils.xml.qa.Category;

public class XmlCategoryFactory<L extends JeeslLang, D extends JeeslDescription,
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
	final static Logger logger = LoggerFactory.getLogger(XmlCategoryFactory.class);
		
	private Category q;
	
	public XmlCategoryFactory(Category q)
	{
		this.q=q;
	}
	
	private Class<QAC> cQAC;
	private Class<QAT> cQAT;
	private JeeslQaFacade<L,D,L2,D2,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS> fQa;
	public void lazyLoader(JeeslQaFacade<L,D,L2,D2,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS> fQa,Class<QAC> cQAC, Class<QAT> cQAT)
	{
		this.fQa=fQa;
		this.cQAC=cQAC;
		this.cQAT=cQAT;
	}
	
	public Category build(QAC category)
	{
		if(fQa!=null){category = fQa.load(cQAC, category);}
		
		Category xml = new Category();
		
		if(q.isSetId()){xml.setId(category.getId());}
		if(q.isSetCode()){xml.setCode(category.getCode());}
		if(q.isSetName()){xml.setName(category.getName());}
		
		if(q.isSetTest())
		{
			XmlTestFactory<L,D,L2,D2,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS> f = new XmlTestFactory<L,D,L2,D2,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS>(q.getTest().get(0));
			f.lazyLoader(fQa, cQAT);
			for(QAT test : category.getTests())
			{
				xml.getTest().add(f.build(test));
			}
		}
		
		return xml;
	}
	
	public static Category build()
	{
		Category xml = new Category();

		return xml;
	}
	
	public static Category build(String code)
	{
		Category xml = build();
		xml.setCode(code);
		return xml;
	}
}