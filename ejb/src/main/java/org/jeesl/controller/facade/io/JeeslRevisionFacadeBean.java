package org.jeesl.controller.facade.io;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.json.system.revision.JsonRevisionFactory;
import org.jeesl.interfaces.model.io.revision.core.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.io.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.io.revision.data.EjbWithRevisionAttributes;
import org.jeesl.interfaces.model.io.revision.data.JeeslRevisionScope;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionMissingLabel;
import org.jeesl.interfaces.model.io.revision.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.system.io.revision.JsonRevision;
import org.jeesl.util.query.sql.SqlNativeQueryHelper;
import org.jeesl.util.query.sql.SqlRevisionQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.controller.util.ParentPredicate;

public class JeeslRevisionFacadeBean<L extends JeeslLang,D extends JeeslDescription,
									RC extends JeeslRevisionCategory<L,D,RC,?>,
									RV extends JeeslRevisionView<L,D,RVM>,
									RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
									RS extends JeeslRevisionScope<L,D,RC,RA>,
									RST extends JeeslStatus<RST,L,D>,
									RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,
									REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
									RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>,
									RER extends JeeslStatus<RER,L,D>,
									RAT extends JeeslStatus<RAT,L,D>,
									ERD extends JeeslRevisionDiagram<L,D,RC>,
									RML extends JeeslRevisionMissingLabel>
					extends JeeslFacadeBean
					implements JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,RML>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslRevisionFacadeBean.class);
	private final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,RML> fbRevision;

	private String revisionPrefix;
	private String revisionTable;

	public JeeslRevisionFacadeBean(EntityManager em, final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,RML> fbRevision)
	{
		this("_at_","auditinfo",em,fbRevision);
	}

	public JeeslRevisionFacadeBean(String revisionPrefix, String revisionTable, EntityManager em,
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
		List<ParentPredicate<RC>> ppCategory = ParentPredicate.createFromList(fbRevision.getClassCategory(),"category",categories);
		return allForOrParents(fbRevision.getClassScope(),ppCategory);
	}

	@Override public List<RE> findRevisionEntities(List<RC> categories, boolean showInvisibleEntities)
	{
		List<ParentPredicate<RC>> ppCategory = ParentPredicate.createFromList(fbRevision.getClassCategory(),"category",categories);
		return allForOrParents(fbRevision.getClassEntity(),ppCategory);
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

	@Override public <W extends EjbWithRevisionAttributes<RA>>
			void rm(Class<W> cW, W entity, RA attribute) throws JeeslConstraintViolationException, JeeslLockingException
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
	public <T extends EjbWithId> List<Long> ids(Class<T> c, JeeslIoRevisionFacade.Scope scope)
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
	public <MR extends EjbWithId> List<MR> allMissingLabels(Class<MR> cLMr) {
		return this.all(cLMr);
	}

	@Override
	public void cleanMissingLabels(Class<RML> cRml) {
		 String query = new StringBuilder("DELETE FROM ")
                 .append(cRml.getSimpleName())
                 .append(" e")
                 .toString();
		 em.createQuery(query).executeUpdate();
	}

	@Override
	public void addMissingLabel(RML rMl) {
		try {
			if(!this.hasMissingLabel(rMl)) {
				this.save(rMl);
			}
		} catch (JeeslConstraintViolationException | JeeslLockingException e) {
			e.printStackTrace();
		}	}

	private boolean hasMissingLabel(RML rMl) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT c ");
		sb.append(" FROM "+fbRevision.getClassMissingRevision().getName()+" c");
		sb.append(" WHERE c.missingEntity");
		sb.append("=:refMissingEntity");
		sb.append(" and ");
		sb.append("c.missingCode");
		sb.append("=:refMissingCode");
		sb.append(" and ");
		//sb.append(" (c.missingCode IS EMPTY OR c.missingCode");
		//sb.append("=:refMissingCode");
		//sb.append( " ) and " );
		sb.append("c.missingLocal");
		sb.append("=:refMissingLocal");

		TypedQuery<RML> q = em.createQuery(sb.toString(), fbRevision.getClassMissingRevision());
		q.setParameter("refMissingEntity", rMl.getMissingEntity());
		q.setParameter("refMissingCode", rMl.getMissingCode());
		q.setParameter("refMissingLocal", rMl.getMissingLocal());

		try	{
			if(q.getResultList().size() > 0) {return true;}
			return false;
			}
		catch (NoResultException ex){return false;}
	}
}