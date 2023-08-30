package org.jeesl.controller.facade.jx.module;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.JeeslWorkflowFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.WorkflowFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.json.io.db.tuple.JsonTupleFactory;
import org.jeesl.factory.json.system.io.db.tuple.t1.Json1TuplesFactory;
import org.jeesl.factory.json.system.io.db.tuple.t2.Json2TuplesFactory;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowAction;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowBot;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWithWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowActivity;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowDelegate;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowLink;
import org.jeesl.interfaces.model.module.workflow.msg.JeeslWorkflowActionNotification;
import org.jeesl.interfaces.model.module.workflow.msg.JeeslWorkflowStageNotification;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowContext;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowDocument;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowProcess;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowModificationLevel;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowPermissionType;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStage;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStagePermission;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStageType;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransition;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransitionType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.DateUtil;

public class JeeslWorkflowFacadeBean<L extends JeeslLang, D extends JeeslDescription,
									AX extends JeeslWorkflowContext<L,D,AX,?>,
									WP extends JeeslWorkflowProcess<L,D,AX,WS>,
									WPD extends JeeslWorkflowDocument<L,D,WP>,
									WS extends JeeslWorkflowStage<L,D,WP,WST,WSP,WT,?>,
									WST extends JeeslWorkflowStageType<L,D,WST,?>,
									WSP extends JeeslWorkflowStagePermission<WS,WPT,WML,SR>,
									WPT extends JeeslWorkflowPermissionType<L,D,WPT,?>,
									WML extends JeeslWorkflowModificationLevel<L,D,WML,?>,
									WSN extends JeeslWorkflowStageNotification<WS,MT,MC,SR,RE>,
									WT extends JeeslWorkflowTransition<L,D,WPD,WS,WTT,SR,?>,
									WTT extends JeeslWorkflowTransitionType<L,D,WTT,?>,
									AC extends JeeslWorkflowActionNotification<WT,MT,MC,SR,RE>,
									AA extends JeeslWorkflowAction<WT,AB,AO,RE,RA>,
									AB extends JeeslWorkflowBot<AB,L,D,?>,
									AO extends EjbWithId,
									MT extends JeeslIoTemplate<L,D,?,?,?,?>,
									MC extends JeeslTemplateChannel<L,D,MC,?>,
									SR extends JeeslSecurityRole<L,D,?,?,?,?>,
									RE extends JeeslRevisionEntity<L,D,?,?,RA,?>,
									RA extends JeeslRevisionAttribute<L,D,RE,?,?>,
									WL extends JeeslWorkflowLink<WF,RE>,
									WF extends JeeslWorkflow<WP,WS,WY,USER>,
									WY extends JeeslWorkflowActivity<WT,WF,WD,FRC,USER>,
									WD extends JeeslWorkflowDelegate<WY,USER>,
									FRC extends JeeslFileContainer<?,?>,
									USER extends JeeslUser<SR>>
					extends JeeslFacadeBean
					implements JeeslWorkflowFacade<AX,WP,WPD,WS,WST,WSP,WPT,WML,WSN,WT,WTT,AC,AA,AB,AO,MT,MC,SR,RE,RA,WL,WF,WY,WD,USER>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslWorkflowFacadeBean.class);
	
	private final WorkflowFactoryBuilder<L,D,AX,WP,WPD,WS,WST,WSP,WPT,WML,WSN,WT,WTT,AC,AA,AB,AO,MT,MC,SR,RE,RA,WL,WF,WY,WD,FRC,USER> fbWorkflow;
	
	public JeeslWorkflowFacadeBean(EntityManager em, final WorkflowFactoryBuilder<L,D,AX,WP,WPD,WS,WST,WSP,WPT,WML,WSN,WT,WTT,AC,AA,AB,AO,MT,MC,SR,RE,RA,WL,WF,WY,WD,FRC,USER> fbApproval)
	{
		super(em);
		this.fbWorkflow=fbApproval;
	}

	@Override
	public WT fTransitionBegin(WP process)
	{
		logger.warn("Optimisation required here!!");
		for(WS stage : this.allForParent(fbWorkflow.getClassStage(), process))
		{
			if(stage.getType().getCode().equals(JeeslWorkflowStageType.Code.start.toString()))
			{
				List<WT> transitions = this.allForParent(fbWorkflow.getClassTransition(), stage);
				for(WT t : transitions)
				{
					if(!t.getType().getCode().equals(JeeslWorkflowTransitionType.Code.auto.toString()))
					{
						logger.info("Returning: "+t.getType().getCode());
						return t;
					}
				}
			}
		}
		return null;
	}
	
	@Override public WL fWorkflowLink(WF workflow) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<WL> cQ = cB.createQuery(fbWorkflow.getClassLink());
		Root<WL> link = cQ.from(fbWorkflow.getClassLink());
		
		Join<WL,WF> jWorkflow = link.join(JeeslWorkflowLink.Attributes.workflow.toString());
		
		cQ.where(cB.and(cB.equal(jWorkflow,workflow)));
		cQ.select(link);
		
		List<WL> links = em.createQuery(cQ).getResultList();
		
		if(!links.isEmpty())
		{
			if(links.size()==1) {return links.get(0);}
			else
			{
				logger.warn("NYI Multiple links");
				return links.get(0);
			}
		}
		else
		{
			throw new JeeslNotFoundException("No "+fbWorkflow.getClassLink()+" found for "+workflow.toString());
		}
	}
	
	@Override public List<WL> fWorkflowLinks(List<WF> workflows)
	{
		return new ArrayList<WL>();
	}

	@Override public <W extends JeeslWithWorkflow<WF>> WL fWorkflowLink(WP process, W owner) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<WL> cQ = cB.createQuery(fbWorkflow.getClassLink());
		Root<WL> link = cQ.from(fbWorkflow.getClassLink());
		
		Join<WL,WF> jWorkflow = link.join(JeeslWorkflowLink.Attributes.workflow.toString());
		Path<WP> pProcess = jWorkflow.get(JeeslWorkflow.Attributes.process.toString());
		Path<Long> pRefId = link.get(JeeslWorkflowLink.Attributes.refId.toString());
		
		cQ.where(cB.and(cB.equal(pRefId,owner.getId()),cB.equal(pProcess,process)));
		cQ.select(link);
		
		List<WL> links = em.createQuery(cQ).getResultList();
		
		if(!links.isEmpty())
		{
			if(links.size()==1) {return links.get(0);}
			else
			{
				logger.warn("NYI Multiple links");
				return links.get(0);
			}
		}
		else
		{
			throw new JeeslNotFoundException("No "+fbWorkflow.getClassLink()+" found for "+owner);
		}
	}
	
	@Override public WF loadWorkflow(WF workflow)
	{
		workflow = em.find(fbWorkflow.getClassWorkflow(), workflow.getId());
		workflow.getResponsibles().size();
		return workflow;
	}
	
	@Override public void deleteWorkflow(WL link) throws JeeslConstraintViolationException
	{
		WF workflow = link.getWorkflow();
		
		logger.info("Removing Link: "+link.toString());
		this.rm(link);
	
		List<WL> links = this.allForParent(fbWorkflow.getClassLink(),workflow);
		logger.info("Remaining Links: "+links.size());
		if(links.isEmpty())
		{
			this.rm(workflow);
		}
		
	}
	
	@Override public WT loadTransition(WT transition)
	{
		transition = em.find(fbWorkflow.getClassTransition(), transition.getId());
		transition.getDocuments().size();
		return transition;
	}

	@Override public List<WF> fWorkflows(WP process, List<WS> stages) 
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<WF> cQ = cB.createQuery(fbWorkflow.getClassWorkflow());
		Root<WF> workflow = cQ.from(fbWorkflow.getClassWorkflow());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Path<WP> pProcess = workflow.get(JeeslWorkflow.Attributes.process.toString());
		predicates.add(cB.equal(pProcess,process));
		
		if(stages!=null)
		{
			if(stages.isEmpty()) {return new ArrayList<>();}
			else
			{
				Join<WF,WS> jStage = workflow.join(JeeslWorkflow.Attributes.currentStage.toString());
				predicates.add(jStage.in(stages));
			}
		}
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(workflow);
		
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<WF> fWorkflows(List<WP> processes, List<WST> types)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<WF> cQ = cB.createQuery(fbWorkflow.getClassWorkflow());
		Root<WF> workflow = cQ.from(fbWorkflow.getClassWorkflow());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(processes!=null && !processes.isEmpty())
		{
			Join<WF,WP> jProcess = workflow.join(JeeslWorkflow.Attributes.process.toString());
			predicates.add(jProcess.in(processes));
		}
		
		Join<WF,WS> jStage = workflow.join(JeeslWorkflow.Attributes.currentStage.toString());
		
		if(types!=null && !types.isEmpty())
		{
			Join<WS,WST> jType = jStage.join(JeeslWorkflowStage.Attributes.type.toString());
			predicates.add(jType.in(types));
		}
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(workflow);
		
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public <W extends JeeslWithWorkflow<WF>> List<WL> fWorkflowLinks(WP process, List<W> owners)
	{
		if(owners==null || owners.isEmpty()) {return new ArrayList<WL>();}
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<WL> cQ = cB.createQuery(fbWorkflow.getClassLink());
		Root<WL> link = cQ.from(fbWorkflow.getClassLink());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Join<WL,WF> jWorkflow = link.join(JeeslWorkflowLink.Attributes.workflow.toString());
		Path<WP> pProcess = jWorkflow.get(JeeslWorkflow.Attributes.process.toString());
		predicates.add(cB.equal(pProcess,process));
		
		Path<Long> pRefId = link.get(JeeslWorkflowLink.Attributes.refId.toString());
		predicates.add(pRefId.in(EjbIdFactory.toIds(owners)));
	
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(link);
		
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<WL> fWorkflowRepsonsibleLinks(USER user)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<WL> cQ = cB.createQuery(fbWorkflow.getClassLink());
		Root<WL> link = cQ.from(fbWorkflow.getClassLink());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Join<WL,WF> jWorkflow = link.join(JeeslWorkflowLink.Attributes.workflow.toString());
		ListJoin<WF,USER> jUser = jWorkflow.joinList(JeeslWorkflow.Attributes.responsibles.toString());
		predicates.add(jUser.in(user));
		
		Join<WF,WP> jProcess = jWorkflow.join(JeeslWorkflow.Attributes.process.toString());
		Path<Boolean> pBoolean = jProcess.get(JeeslWorkflowProcess.Attributes.includeInDashboard.toString());
		predicates.add(cB.isTrue(pBoolean));
		
		Join<WF,WS> jStage = jWorkflow.join(JeeslWorkflow.Attributes.currentStage.toString());
		Path<WST> pType = jStage.get(JeeslWorkflowStage.Attributes.type.toString());
		predicates.add(cB.equal(pType,this.fByEnum(fbWorkflow.getClassStageType(),JeeslWorkflowStageType.Code.intermediate)));
	
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(link);
		
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<WL> fWorkflowDelegationLinks(USER user)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<WL> cQ = cB.createQuery(fbWorkflow.getClassLink());
		Root<WL> link = cQ.from(fbWorkflow.getClassLink());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Join<WL,WF> jWorkflow = link.join(JeeslWorkflowLink.Attributes.workflow.toString());
		Join<WF,WY> jActivity = jWorkflow.join(JeeslWorkflow.Attributes.lastActivity.toString());
		Join<WY,WD> jDelegate = jActivity.join(JeeslWorkflowActivity.Attributes.delegate.toString());
		
		Path<Boolean> pResult = jDelegate.get(JeeslWorkflowDelegate.Attributes.result.toString());
		predicates.add(cB.isTrue(pResult));
		
		Join<WD,USER> jUser = jDelegate.join(JeeslWorkflowDelegate.Attributes.userRequest.toString());
		predicates.add(jUser.in(user));
		
		Join<WF,WP> jProcess = jWorkflow.join(JeeslWorkflow.Attributes.process.toString());
		Path<Boolean> pBoolean = jProcess.get(JeeslWorkflowProcess.Attributes.includeInDashboard.toString());
		predicates.add(cB.isTrue(pBoolean));
	
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(link);
		
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<WL> fWorkflowEscalations(WP process)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<WL> cQ = cB.createQuery(fbWorkflow.getClassLink());
		Root<WL> link = cQ.from(fbWorkflow.getClassLink());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Join<WL,WF> jWorkflow = link.join(JeeslWorkflowLink.Attributes.workflow.toString());
		
		Path<WP> pProcess = jWorkflow.get(JeeslWorkflow.Attributes.process.toString());
		predicates.add(cB.equal(pProcess,process));
		
		Join<WF,WS> jStage = jWorkflow.join(JeeslWorkflow.Attributes.currentStage.toString());
		Join<WS,WST> jStageType = jStage.join(JeeslWorkflowStage.Attributes.type.toString());
		predicates.add(cB.equal(jStageType,this.fByEnum(fbWorkflow.getClassStageType(),JeeslWorkflowStageType.Code.intermediate)));
		
		
		ListJoin<WS,WT> jTransition = jStage.joinList(JeeslWorkflowStage.Attributes.transitions.toString());
		Join<WT,WTT> jTransitionType = jTransition.join(JeeslWorkflowTransition.Attributes.type.toString());
		predicates.add(cB.equal(jTransitionType,this.fByEnum(fbWorkflow.getClassTransitionType(),JeeslWorkflowTransitionType.Code.escalation)));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(link);
		
		return em.createQuery(cQ).getResultList();
	}
	
	@Override
	public List<WL> fWorkflowsForNotification(WSN notifciation)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<WL> cQ = cB.createQuery(fbWorkflow.getClassLink());
		Root<WL> link = cQ.from(fbWorkflow.getClassLink());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Join<WL,WF> jWorkflow = link.join(JeeslWorkflowLink.Attributes.workflow.toString());
		Join<WF,WS> jStage = jWorkflow.join(JeeslWorkflow.Attributes.currentStage.toString());
		predicates.add(cB.equal(jStage,notifciation.getStage()));
		
		LocalDateTime ldt = LocalDateTime.now().minusHours(notifciation.getOverdueHours());
		Join<WF,WY> jActivity = jWorkflow.join(JeeslWorkflow.Attributes.lastActivity.toString());
		Expression<Date> eRecord = jActivity.get(JeeslWorkflowActivity.Attributes.record.toString());
		predicates.add(cB.lessThanOrEqualTo(eRecord, DateUtil.toDate(ldt)));
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(link);
		
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<WL> fWorkflowDelegationReuquests(Boolean result)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<WL> cQ = cB.createQuery(fbWorkflow.getClassLink());
		Root<WL> link = cQ.from(fbWorkflow.getClassLink());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Join<WL,WF> jWorkflow = link.join(JeeslWorkflowLink.Attributes.workflow.toString());
		Join<WF,WY> jActivity = jWorkflow.join(JeeslWorkflow.Attributes.lastActivity.toString());
		Join<WY,WD> jDelegate = jActivity.join(JeeslWorkflowActivity.Attributes.delegate.toString());
		
		Path<Boolean> pResult = jDelegate.get(JeeslWorkflowDelegate.Attributes.result.toString());
		if(result==null) {predicates.add(cB.isNull(pResult));}
		else {predicates.add(cB.equal(pResult,result));}
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(link);
		
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<WY> fWorkflowActivities(Date from, Date to, List<USER> users, List<WP> processes)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<WY> cQ = cB.createQuery(fbWorkflow.getClassActivity());
		Root<WY> activity = cQ.from(fbWorkflow.getClassActivity());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(users!=null && !users.isEmpty())
		{
			Join<WY,USER> jUser = activity.join(JeeslWorkflowActivity.Attributes.user.toString());
			predicates.add(jUser.in(users));
		}
		
		if(processes!=null && !processes.isEmpty())
		{
			Join<WY,WF> jWf = activity.join(JeeslWorkflowActivity.Attributes.workflow.toString());
			Join<WF,WP> jProcess = jWf.join(JeeslWorkflow.Attributes.process.toString());
			predicates.add(jProcess.in(processes));
		}
		
		Expression<Date> eRecord = activity.get(JeeslWorkflowActivity.Attributes.record.toString());
		if(from!=null) {predicates.add(cB.greaterThanOrEqualTo(eRecord,from));}
		if(to!=null) {predicates.add(cB.lessThan(eRecord,to));}

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(activity);
		return em.createQuery(cQ).getResultList();
	}

	@Override public JsonTuples1<WP> tpcActivitiesByProcess()
	{
		Json1TuplesFactory<WP> jtf = new Json1TuplesFactory<>(fbWorkflow.getClassProcess());
		jtf.setfUtils(this);
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<WF> item = cQ.from(fbWorkflow.getClassWorkflow());
		
		Expression<Long> eCount = cB.count(item.<Long>get("id"));
		Path<WP> pProcess = item.get(JeeslWorkflow.Attributes.process.toString());
		
		cQ.groupBy(pProcess.get("id"));
		cQ.multiselect(pProcess.get("id"),eCount);
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
		return jtf.buildV2(tQ.getResultList(),JsonTupleFactory.Type.count);
	}
	
	@Override public JsonTuples2<WP,WST> tpcActivitiesByProcessType()
	{
		Json2TuplesFactory<WP,WST> jtf = new Json2TuplesFactory<>(fbWorkflow.getClassProcess(),fbWorkflow.getClassStageType());
		jtf.setfUtils(this);
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cQ = cB.createTupleQuery();
		Root<WF> workflow = cQ.from(fbWorkflow.getClassWorkflow());
		
		Expression<Long> eCount = cB.count(workflow.<Long>get("id"));
		Path<WP> pProcess = workflow.get(JeeslWorkflow.Attributes.process.toString());
		
		Join<WF,WS> jStage = workflow.join(JeeslWorkflow.Attributes.currentStage.toString());
		Join<WS,WST> jType = jStage.join(JeeslWorkflowStage.Attributes.type.toString());
		
		cQ.groupBy(pProcess.get("id"),jType.get("id"));
		cQ.multiselect(pProcess.get("id"),jType.get("id"),eCount);
	       
		TypedQuery<Tuple> tQ = em.createQuery(cQ);
        return jtf.build(tQ.getResultList(),JsonTupleFactory.Type.count);
	}

	
}