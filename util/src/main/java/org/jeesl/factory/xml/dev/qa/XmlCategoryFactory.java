package org.jeesl.factory.xml.dev.qa;

import java.util.Objects;

import org.jeesl.api.facade.module.JeeslQaFacade;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.model.xml.module.dev.qa.Category;
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

public class XmlCategoryFactory<L extends JeeslLang, D extends JeeslDescription,
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
								QAT extends UtilsQaTest<GROUP,QAC,QAR,QATD,QATI,?>,
								QAU extends UtilsQaUsability,
								QAR extends UtilsQaResult<STAFF,QAT,?>,
								QASH extends UtilsQaStakeholder<QA>,
								QATD extends UtilsQaTestDiscussion<STAFF,QAT>,
								QATI extends UtilsQaTestInfo<?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlCategoryFactory.class);
		
	private Category q;
	
	public XmlCategoryFactory(Category q)
	{
		this.q=q;
	}
	
	private Class<QAC> cQAC;
	private Class<QAT> cQAT;
	private JeeslQaFacade<L,D,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI> fQa;
	public void lazyLoader(JeeslQaFacade<L,D,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI> fQa,Class<QAC> cQAC, Class<QAT> cQAT)
	{
		this.fQa=fQa;
		this.cQAC=cQAC;
		this.cQAT=cQAT;
	}
	
	public Category build(QAC category)
	{
		if(fQa!=null){category = fQa.load(cQAC, category);}
		
		Category xml = new Category();
		
		if(Objects.nonNull(q.getId())) {xml.setId(category.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(category.getCode());}
		if(Objects.nonNull(q.getName())) {xml.setName(category.getName());}
		
		if(q.isSetTest())
		{
			logger.warn("Deactivated -- XMAS Issue");
//			XmlTestFactory<L,D,L2,D2,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS> f = new XmlTestFactory<>(q.getTest().get(0));
//			f.lazyLoader(fQa, cQAT);
//			for(QAT test : category.getTests())
//			{
//				xml.getTest().add(f.build(test));
//			}
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