package org.jeesl.controller.facade.jx.io;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.jeesl.api.facade.io.JeeslIoLabelFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.controller.facade.jx.predicate.LiteralPredicateBuilder;
import org.jeesl.controller.facade.jx.predicate.ParentPredicateBuilder;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.json.system.io.label.JsonRevisionFactory;
import org.jeesl.interfaces.facade.ParentPredicate;
import org.jeesl.interfaces.model.io.label.entity.EjbWithRevisionAttributes;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionMissingLabel;
import org.jeesl.interfaces.model.io.label.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScope;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScopeType;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenArtifact;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.io.JeeslIoLabelQuery;
import org.jeesl.model.ejb.io.db.JeeslCqLiteral;
import org.jeesl.model.json.system.io.revision.JsonRevision;
import org.jeesl.util.query.cq.CqLiteral;
import org.jeesl.util.query.sql.SqlNativeQueryHelper;
import org.jeesl.util.query.sql.SqlRevisionQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoLabelFacadeBean<L extends JeeslLang,D extends JeeslDescription,
									RC extends JeeslRevisionCategory<L,D,RC,?>,
									RV extends JeeslRevisionView<L,D,RVM>,
									RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
									RS extends JeeslRevisionScope<L,D,RC,RA>,
									RST extends JeeslRevisionScopeType<L,D,RST,?>,
									RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,
									REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
									RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>,
									RER extends JeeslStatus<L,D,RER>,
									RAT extends JeeslStatus<L,D,RAT>,
									ERD extends JeeslRevisionDiagram<L,D,RC>,
									RML extends JeeslRevisionMissingLabel>
					extends JeeslFacadeBean
					implements JeeslIoLabelFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,ERD,RML>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoLabelFacadeBean.class);
	private final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,RML> fbRevision;

	private String revisionPrefix;
	private String revisionTable;

	public JeeslIoLabelFacadeBean(EntityManager em, final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,RML> fbRevision)
	{
		this("_at_","auditinfo",em,fbRevision);
	}

	public JeeslIoLabelFacadeBean(String revisionPrefix, String revisionTable, EntityManager em,
								final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,RML> fbRevision)
	{
		super(em);
		this.fbRevision=fbRevision;
		this.revisionPrefix=revisionPrefix;
		this.revisionTable=revisionTable;
	}

	@Override public RV load(Class<RV> cView, RV view)
	{
		view = em.find(cView, view.getId());
		view.getMaps().size();
		return view;
	}

	@Override public RS load(Class<RS> cScope, RS scope)
	{
		scope = em.find(cScope, scope.getId());
		scope.getAttributes().size();
		return scope;
	}

	@Override public RE load(Class<RE> cEntity, RE entity)
	{
		entity = em.find(cEntity, entity.getId());
		entity.getAttributes().size();
		entity.getMaps().size();
		return entity;
	}

	@Override public List<RS> findRevisionScopes(List<RC> categories, boolean showInvisibleScopes)
	{
		List<ParentPredicate<RC>> ppCategory = ParentPredicateBuilder.createFromList(fbRevision.getClassCategory(),"category",categories);
		return allForOrParents(fbRevision.getClassScope(),ppCategory);
	}

	@Override public List<RE> findLabelEntities(JeeslIoLabelQuery<RE> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaQuery<RE> cQ = cB.createQuery(fbRevision.getClassEntity());
		Root<RE> root = cQ.from(fbRevision.getClassEntity());
		
		for(JeeslCqLiteral cq : ListUtils.emptyIfNull(query.getCqLiterals()))
		{
			if(cq.getPath().equals(CqLiteral.path(JeeslRevisionEntity.Attributes.jscn)))
			{
				Expression<String> literal = cB.upper(cB.literal("%"+cq.getLiteral()+"%"));
				Expression<String> eJscn = root.get(JeeslRevisionEntity.Attributes.jscn.toString());
				
				switch(cq.getType())
				{
					case CONTAINS: predicates.add(cB.like(cB.upper(eJscn),literal)); break;
					default: logger.error("NYI "+cq.toString());
				}
			}
			else {logger.warn("NYI: "+cq.toString());}
		}
		
		cQ.select(root);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public List<RA> fLabelAttributes(JeeslIoLabelQuery<RE> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<RA> cQ = cB.createQuery(fbRevision.getClassAttribute());
		Root<RA> root = cQ.from(fbRevision.getClassAttribute());
		super.rootFetch(root, query);
		
		cQ.select(root);
		cQ.where(cB.and(this.pAttribute(cB,query,root)));
		
		TypedQuery<RA> tQ = em.createQuery(cQ);
		super.pagination(tQ, query);
		return tQ.getResultList();
	}
	
	@Override public List<RE> findLabelEntities(RC category, ERD diagram)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaQuery<RE> cQ = cB.createQuery(fbRevision.getClassEntity());
		Root<RE> root = cQ.from(fbRevision.getClassEntity());
		
		Path<RC> pCategory = root.get(JeeslRevisionEntity.Attributes.category.toString());
		Path<ERD> pDiagram = root.get(JeeslRevisionEntity.Attributes.diagram.toString());

		predicates.add(cB.equal(pCategory,category));
		
		if(diagram==null) {predicates.add(cB.isNull(pDiagram));}
		else {predicates.add(cB.equal(pDiagram,diagram));}
		
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(root);
		
		return em.createQuery(cQ).getResultList();
	}

	@Override public void rm(Class<RVM> cMappingView, RVM mapping) throws JeeslConstraintViolationException
	{
		mapping = em.find(cMappingView, mapping.getId());
		mapping.getView().getMaps().remove(mapping);
		this.rmProtected(mapping);
	}

	@Override public <W extends EjbWithRevisionAttributes<RA>>
			RA save(Class<W> cW, W entity, RA attribute) throws JeeslLockingException, JeeslConstraintViolationException
	{
		entity = this.find(cW, entity);
		attribute = this.saveProtected(attribute);
		if(!entity.getAttributes().contains(attribute))
		{
			entity.getAttributes().add(attribute);
			this.saveProtected(entity);
		}
		return attribute;
	}

	@Override public <W extends EjbWithRevisionAttributes<RA>> void rm(Class<W> cW, W entity, RA attribute) throws JeeslConstraintViolationException, JeeslLockingException
	{
		entity = this.find(cW, entity);
		if(entity.getAttributes().contains(attribute))
		{
			entity.getAttributes().remove(attribute);
			this.saveProtected(entity);
		}
		this.rmProtected(attribute);
	}

	@Override public <T extends EjbWithId> T jpaTree(Class<T> c, String jpa, long id) throws JeeslNotFoundException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT c ");
		sb.append(" FROM "+c.getName()+" c");
		sb.append(" WHERE c.").append(jpa);
		sb.append("=:refId");

		TypedQuery<T> q = em.createQuery(sb.toString(), c);
		q.setParameter("refId", id);

		try	{return q.getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("Nothing found "+c.getSimpleName()+" for jpa="+jpa);}
	}

	@Override
	public <T extends EjbWithId> List<T> revisions(Class<T> c, List<Long> ids)
	{
		AuditQuery query = AuditReaderFactory.get(em).createQuery().forRevisionsOfEntity(c, false, true);
		query.add(AuditEntity.revisionNumber().in(ids));
		query.addOrder(AuditEntity.revisionNumber().desc());
//		List<SimpleRevisionEntity<T>> list = SimpleRevisionEntity.build(query.getResultList());
//		for(SimpleRevisionEntity<T> item : list){lazyLoad(item.getEntity());}
//		return list;
		return null;
	}

	@Override
	public <T extends EjbWithId> List<Long> ids(Class<T> c, JeeslIoLabelFacade.Scope scope)
	{
		List<Long> result = new ArrayList<Long>();

		Table t = c.getAnnotation(Table.class);
		if(t!=null)
		{
			String query=null;
			switch(scope)
			{
				case live: query = SqlRevisionQueries.idsLive(t.name());break;
				case revision: query = SqlRevisionQueries.idsRevision(revisionPrefix+t.name());break;
			}

			for(Object o : em.createNativeQuery(query).getResultList())
			{
				long id = ((BigInteger)o).longValue();
				result.add(id);
			}
		}
		return result;
	}

	@Override
	public <T extends EjbWithId> List<JsonRevision> findCreated(Class<T> c, Date from, Date to)
	{
		List<JsonRevision> revisions = new ArrayList<JsonRevision>();
		Table t = c.getAnnotation(Table.class);
		if(t!=null)
		{
			for(Object o : em.createNativeQuery(SqlRevisionQueries.revisionsIn(revisionPrefix+t.name(), revisionTable, from, to, SqlRevisionQueries.typesCreateRemove())).getResultList())
			{
				Object[] array = (Object[])o;
				SqlNativeQueryHelper.debugDataTypes(false, "findCreated", array);
				revisions.add(JsonRevisionFactory.build(array));
			}

		}
		return revisions;
	}

	@Override
	public void cleanMissingLabels(Class<RML> cRml)
	{
		 String query = new StringBuilder("DELETE FROM ")
                 .append(cRml.getSimpleName())
                 .append(" e")
                 .toString();
		 em.createQuery(query).executeUpdate();
	}

	@Override
	public void addMissingLabel(RML rMl)
	{
		try
		{
			if(!this.hasMissingLabel(rMl)) {this.save(rMl);}
		}
		catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
	}

	private boolean hasMissingLabel(RML rMl)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT c ");
		sb.append(" FROM "+fbRevision.getClassMissingRevision().getName()+" c");
		sb.append(" WHERE c.missingEntity");
		sb.append("=:refMissingEntity");
		sb.append(" and ");
		sb.append("c.missingCode");
		sb.append("=:refMissingCode");
		sb.append(" and ");
		sb.append("c.missingLocal");
		sb.append("=:refMissingLocal");

		TypedQuery<RML> q = em.createQuery(sb.toString(), fbRevision.getClassMissingRevision());
		q.setParameter("refMissingEntity", rMl.getMissingEntity());
		q.setParameter("refMissingCode", rMl.getMissingCode());
		q.setParameter("refMissingLocal", rMl.getMissingLocal());

		try
		{
			if(q.getResultList().size() > 0) {return true;}
			return false;
		}
		catch (NoResultException ex){return false;}
	}

	@Override public RE fRevisionEntity(String jscn) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
        CriteriaQuery<RE> cQ = cB.createQuery(fbRevision.getClassEntity());
        Root<RE> root = cQ.from(fbRevision.getClassEntity());
        
        Expression<String> eCode = root.get(JeeslRevisionEntity.Attributes.jscn.toString());
        cQ = cQ.where(cB.equal(eCode,jscn));

		TypedQuery<RE> q = em.createQuery(cQ);
//		logger.info(fbRevision.getClassEntity().getName()+": "+q.unwrap(org.hibernate.Query.class).getQueryString());
		
		try	{return q.getSingleResult();}
		catch (NoResultException ex)
		{
			//Update jscn from fully qualified class name from code field
//			return updateJscnFromCode(jscn);
			throw new JeeslNotFoundException("No "+fbRevision.getClassEntity().getSimpleName()+" for "+JeeslRevisionEntity.Attributes.jscn+"="+jscn);
		}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Results for "+fbRevision.getClassEntity().getSimpleName()+" and "+JeeslRevisionEntity.Attributes.jscn+"="+jscn+" not unique");}
	}

	@SuppressWarnings("unused")
	private RE updateJscnFromCode(String jscn) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		logger.info(fbRevision.getClassEntity().getName());
        CriteriaQuery<RE> cQ = cB.createQuery(fbRevision.getClassEntity());
        Root<RE> root = cQ.from(fbRevision.getClassEntity());
        Expression<String> literal = cB.literal("%."+jscn);
	    Expression<String> eCode = root.get("code");

	    CriteriaQuery<RE> select = cQ.select(root);
	    select.where(cB.like(eCode,literal));

		TypedQuery<RE> q = em.createQuery(select);
		logger.info(q.unwrap(org.hibernate.Query.class).getQueryString());

		try
		{
			RE revEntity =  q.getSingleResult();
			revEntity.setJscn(jscn);
			revEntity = this.save(revEntity);
			return revEntity;
		}
		catch (NoResultException ex) {throw new JeeslNotFoundException("Nothing found "+fbRevision.getClassEntity().getSimpleName()+" for jscn="+jscn);}
		catch (JeeslConstraintViolationException| JeeslLockingException | NonUniqueResultException ex) {throw new JeeslNotFoundException("Results for "+fbRevision.getClassEntity().getSimpleName()+" and jscn="+jscn+" not unique");}
	}
	
	public Predicate[] pAttribute(CriteriaBuilder cB, JeeslIoLabelQuery<RE> query, Root<RA> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(ObjectUtils.isNotEmpty(query.getIoLabelEntityOwner()))
		{
			Join<RA,RE> jEntity = root.join(JeeslRevisionAttribute.Attributes.ownerEntity.toString());
			predicates.add(jEntity.in(query.getIoLabelEntityOwner()));
		}
		
		if(ObjectUtils.isNotEmpty(query.getIoLabelEntityReferenced()))
		{
			Join<RA,RE> jEntity = root.join(JeeslRevisionAttribute.Attributes.entity.toString());
			predicates.add(jEntity.in(query.getIoLabelEntityReferenced()));
		}
		
		for(JeeslCqLiteral cq : ListUtils.emptyIfNull(query.getCqLiterals()))
		{
			if(cq.getPath().equals(CqLiteral.path(JeeslRevisionAttribute.Attributes.code)))
			{					
				LiteralPredicateBuilder.add(cB,predicates,cq,root.<String>get(JeeslRevisionAttribute.Attributes.code.toString()));
			}
			else {logger.warn(cq.nyi(fbRevision.getClassAttribute()));}
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
}