package org.jeesl.controller.facade.jx.module;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.dev.JeeslQaFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.controller.facade.jx.predicate.ParentPredicateBuilder;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
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
import net.sf.ahtutils.interfaces.model.qa.UtilsQualityAssurarance;

public class JeeslQaFacadeBean <L extends JeeslLang, D extends JeeslDescription,
								USER extends JeeslUser<?>,
								STAFF extends UtilsQaStaff<?,USER,GROUP,QA,QASH>,
								GROUP extends UtilsQaGroup<STAFF,QA,QASS>,
								QA extends UtilsQualityAssurarance<STAFF,QAC,QASH>,
								QASD extends UtilsQaSchedule<QA,QASS>,
								QASS extends UtilsQaScheduleSlot<GROUP,QASD>,
								QAC extends UtilsQaCategory<QA,QAT>,
								QAT extends UtilsQaTest<GROUP,QAC,QAR,?,?,?>,
								QAR extends UtilsQaResult<STAFF,QAT,?>,
								QASH extends UtilsQaStakeholder<QA>> 
		extends JeeslFacadeBean implements JeeslQaFacade<L,D,STAFF,GROUP,QA,QASD,QASS,QAC,QAT>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslQaFacadeBean.class);
	
	public JeeslQaFacadeBean(EntityManager em){this(em,false);}
	public JeeslQaFacadeBean(EntityManager em, boolean handleTransaction)
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