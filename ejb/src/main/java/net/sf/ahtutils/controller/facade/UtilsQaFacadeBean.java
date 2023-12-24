
package net.sf.ahtutils.controller.facade;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.JeeslQaFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.controller.facade.jx.ParentPredicateBuilder;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
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

public class UtilsQaFacadeBean <L extends JeeslLang, D extends JeeslDescription,
								
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
		extends JeeslFacadeBean implements JeeslQaFacade<L,D,C,R,V,U,A,AT,USER,STAFF,GROUP,QA,QASD,QASS,QAC,QAT,QAU,QAR,QASH,QATD,QATI>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(UtilsQaFacadeBean.class);
	
	public UtilsQaFacadeBean(EntityManager em){this(em,false);}
	public UtilsQaFacadeBean(EntityManager em, boolean handleTransaction)
	{
		super(em,handleTransaction);
	}
	
	public QA load(Class<QA> clQa, QA qa)
	{		
		qa = em.find(clQa, qa.getId());
		qa.getCategories().size();
		qa.getStaff().size();
		qa.getStakeholders().size();
		return qa;
	}
	
	public QAC load(Class<QAC> cQac, QAC category)
	{		
		category = em.find(cQac, category.getId());
		category.getTests().size();
		return category;
	}
	
	@Override public QAT load(Class<QAT> cQat, QAT test)
	{		
		test = em.find(cQat, test.getId());
		test.getDiscussions().size();
		test.getResults().size();
		test.getGroups().size();
		return test;
	}
	
	@Override public QASD load(Class<QASD> cSchedule, QASD schedule)
	{		
		schedule = em.find(cSchedule, schedule.getId());
		schedule.getSlots().size();
		return schedule;
	}
	
	@Override public QASS load(Class<QASS> cSlot, QASS slot)
	{		
		slot = em.find(cSlot, slot.getId());
		slot.getGroups().size();
		return slot;
	}
	
	@Override public GROUP load(Class<GROUP> cGroup, GROUP group)
	{		
		group = em.find(cGroup, group.getId());
		group.getStaffs().size();
		group.getSlots().size();
		return group;
	}
	
	@Override public STAFF load(Class<STAFF> cStaff, STAFF staff)
	{
		staff = em.find(cStaff, staff.getId());
		staff.getGroups().size();
		return staff;
	}
	
	public List<QAT> fQaTests(Class<QAT> clTest, Class<QAC> clCategory, Class<QA> clQa, QA qa)
	{		
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<QAT> criteriaQuery = cB.createQuery(clTest);
		 
		Root<QAT> fromTest = criteriaQuery.from(clTest);
		Root<QAC> fromCategory = criteriaQuery.from(clCategory);
		Root<QA>  fromQa = criteriaQuery.from(clQa);
		
		Path<Object> pathToCategory = fromTest.get("category");
	    Path<Object> pathCategoryId = fromCategory.get("id");
	    
	    Path<Object> pathToQa = fromCategory.get("qa");
	    Path<Object> pathToQaId = fromQa.get("id");

		CriteriaQuery<QAT> select = criteriaQuery.select(fromTest);
		select.where(cB.equal(pathToCategory, pathCategoryId),cB.equal(pathToQa, pathToQaId),cB.equal(pathToQaId, qa.getId()));    

		select.orderBy(cB.asc(fromCategory.get("nr")),cB.asc(fromTest.get("nr")));
		
		TypedQuery<QAT> q = em.createQuery(select);
		return q.getResultList();
	}
	 
	public List<QAT> fQaTests(Class<QAT> clTest, Class<QAC> clCategory, List<QAC> categories)
	{
		 return this.allForOrParents(clTest, ParentPredicateBuilder.createFromList(clCategory, "category", categories));
	}
	
	@Override public List<GROUP> fQaGroups(Class<GROUP> cGroup, QA qa)
	{
		return this.allOrderedParent(cGroup, "position", true, "qa", qa);
	}
}