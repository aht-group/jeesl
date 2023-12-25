package org.jeesl.web.rest.dev;

import org.jeesl.api.facade.module.JeeslQaFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.xml.dev.qa.XmlCategoryFactory;
import org.jeesl.factory.xml.dev.qa.XmlGroupFactory;
import org.jeesl.factory.xml.dev.qa.XmlGroupsFactory;
import org.jeesl.factory.xml.system.security.XmlStaffFactory;
import org.jeesl.factory.xml.system.status.XmlStatusFactory;
import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.model.xml.system.security.Staff;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.util.query.xml.module.QaQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.factory.xml.status.XmlResponsibleFactory;
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
import net.sf.ahtutils.xml.qa.Group;
import net.sf.ahtutils.xml.qa.Qa;
import net.sf.ahtutils.xml.qa.Test;

public class QaRestService <L extends JeeslLang, D extends JeeslDescription,
							L2 extends JeeslLang, D2 extends JeeslDescription,
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
					//implements UtilsQualityAssuranceRest
{
	final static Logger logger = LoggerFactory.getLogger(QaRestService.class);
	
	private JeeslQaFacade<L,D,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI> fQa;
		
	private final Class<GROUP> cGroup;
	private final Class<QA> cQa;
	
	private XmlStaffFactory<L,D,C,R,V,U,A,AT,USER,STAFF,QA,QA> xfStaff;
	private XmlGroupFactory<GROUP> xfGroup;
	private XmlCategoryFactory<L,D,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS> xfCategory;
	private XmlCategoryFactory<L,D,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS> xfFrDuration;
	
	private QaRestService(JeeslQaFacade<L,D,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI> fQa, final Class<L> cL, final Class<D> cD,final Class<GROUP> cGroup,final Class<QA> cQa,final Class<QAC> cQAC,final Class<QAT> cQAT)
	{
		this.fQa=fQa;
		this.cGroup=cGroup;
		this.cQa=cQa;
		
		xfStaff = new XmlStaffFactory<L,D,C,R,V,U,A,AT,USER,STAFF,QA,QA>(QaQuery.staff());
		xfGroup = new XmlGroupFactory<GROUP>(QaQuery.group());
		xfCategory = new XmlCategoryFactory<L,D,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS>(QaQuery.category());
		xfFrDuration = new XmlCategoryFactory<L,D,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS>(QaQuery.frDuration());
		
		xfFrDuration.lazyLoader(fQa,cQAC,cQAT);
	}
	
	public static <L extends JeeslLang, D extends JeeslDescription,
					L2 extends JeeslLang, D2 extends JeeslDescription,
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
		QaRestService<L,D,L2,D2,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS>
			factory(JeeslQaFacade<L,D,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI> fQa,final Class<L> cL,final Class<D> cD,final Class<GROUP> cGroup,final Class<QA> cQa,final Class<QAC> cQAC,final Class<QAT> cQAT)
	{
		return new QaRestService<L,D,L2,D2,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI,QATC,QATS,QARS,QAUS>(fQa,cL,cD,cGroup,cQa,cQAC,cQAT);
	}

	public Qa qaGroups(long qaId)
	{
		logger.info("Building QA Groups for QA="+qaId);
		Qa qa = new Qa();
		qa.setGroups(XmlGroupsFactory.build());
		try
		{
			QA eQa = fQa.find(cQa, qaId);
			
			for(GROUP group : fQa.fQaGroups(cGroup,eQa))
			{
				group = fQa.load(cGroup, group);
				Group xGroup = xfGroup.build(group);
				for(STAFF staff : group.getStaffs())
				{
					Staff xStaff = xfStaff.build(staff);
					
					if(staff.getDepartment()!=null){xStaff.setType(XmlTypeFactory.buildLabel(null,staff.getDepartment()));}
					if(staff.getStakeholder()!=null){xStaff.setStatus(XmlStatusFactory.buildLabel(null,staff.getStakeholder().getCode()));}
					if(staff.getResponsibilities()!=null){xStaff.setResponsible(XmlResponsibleFactory.buildLabel(null,staff.getResponsibilities()));}
					xGroup.getStaff().add(xStaff);
				}
						
				qa.getGroups().getGroup().add(xGroup);
			}
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}	
		return qa;
	}
	
	public Qa qaSchedule(long qaId)
	{
		logger.info("Building QA Groups for QA="+qaId);
		Qa qa = new Qa();
		qa.setGroups(XmlGroupsFactory.build());
		try
		{
			QA eQa = fQa.find(cQa, qaId);
			
			for(GROUP group : fQa.fQaGroups(cGroup,eQa))
			{
				group = fQa.load(cGroup, group);
				Group xGroup = xfGroup.build(group);
				for(STAFF staff : group.getStaffs())
				{
					Staff xStaff = xfStaff.build(staff);
					
					if(staff.getDepartment()!=null){xStaff.setType(XmlTypeFactory.buildLabel(null,staff.getDepartment()));}
					if(staff.getStakeholder()!=null){xStaff.setStatus(XmlStatusFactory.buildLabel(null,staff.getStakeholder().getCode()));}
					if(staff.getResponsibilities()!=null){xStaff.setResponsible(XmlResponsibleFactory.buildLabel(null,staff.getResponsibilities()));}
					xGroup.getStaff().add(xStaff);
				}
						
				qa.getGroups().getGroup().add(xGroup);
			}
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}	
		return qa;
	}
	
	public Qa qaCategories(long qaId)
	{
		Qa qa = new Qa();
		try
		{
			QA eQa = fQa.find(cQa, qaId);
			eQa = fQa.load(cQa,eQa);
			
			for(QAC category : eQa.getCategories())
			{
				qa.getCategory().add(xfCategory.build(category));
			}
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}	
		return qa;
	}
	
	public Qa qaFrDurations(long qaId)
	{
		Qa qa = new Qa();
		try
		{
			QA eQa = fQa.find(cQa, qaId);
			eQa = fQa.load(cQa,eQa);
			
			for(QAC category : eQa.getCategories())
			{
				Category c = xfFrDuration.build(category);
				if(logger.isTraceEnabled())
				{
//					logger.info(StringUtil.stars());
					for(Test t : c.getTest())
					{
						logger.info("\tVisible?"+t.isSetVisible());
					}
				}
				
				qa.getCategory().add(c);
			}
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}	
		return qa;
	}
	
}