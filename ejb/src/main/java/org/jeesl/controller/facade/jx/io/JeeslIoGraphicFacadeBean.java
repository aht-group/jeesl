package org.jeesl.controller.facade.jx.io;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.io.JeeslIoGraphicFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.jeesl.interfaces.util.query.io.JeeslIoGraphicQuery;

public class JeeslIoGraphicFacadeBean<L extends JeeslLang, D extends JeeslDescription,
									S extends EjbWithId,
									G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<L,D,GT,G>,
									GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<L,D,GS,G>>
					extends JeeslFacadeBean
					implements JeeslIoGraphicFacade<S,G,GT,GC,GS>
{	
	private static final long serialVersionUID = 1L;
	
	private final Class<S> cStatus;
	private final Class<G> cG;
	private SvgFactoryBuilder<L,D,G,GT,GC,GS> fbGraphic;
	
	public JeeslIoGraphicFacadeBean(EntityManager em, final Class<S> cStatus, final Class<G> cG)
	{
		super(em);
		this.cStatus=cStatus;
		this.cG=cG;
	}
	
	public JeeslIoGraphicFacadeBean(EntityManager em, final Class<S> cStatus, final SvgFactoryBuilder<L,D,G,GT,GC,GS> fbGraphic)
	{
		super(em);
		this.fbGraphic=fbGraphic;
		this.cStatus=cStatus;
		this.cG=fbGraphic.getClassGraphic();
	}
	

	@Override public G fGraphicForStatus(long ejbId) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<G> cQ = cB.createQuery(cG);
		Root<S> root = cQ.from(cStatus);
		
		Path<G> pGraphic = root.get("graphic");
		Path<Long> pId = root.get("id");
		
		cQ.where(cB.equal(pId,ejbId));
		cQ.select(pGraphic);
		
		try	{return em.createQuery(cQ).getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+cG.getSimpleName()+" found for status.id="+ejbId+" ("+JeeslIoGraphicFacadeBean.class.getSimpleName()+".fGraphicForStatus)");}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Multiple "+cG.getSimpleName()+" found for status.id="+ejbId+" ("+JeeslIoGraphicFacadeBean.class.getSimpleName()+".fGraphicForStatus)");}
	}
	
	@Override public <W extends EjbWithGraphic<G>> G fGraphic(Class<W> c, W w) throws JeeslNotFoundException {return fGraphic(c,w.getId());}
	@Override public <W extends EjbWithGraphic<G>> G fGraphic(Class<W> c, long id) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<G> cQ = cB.createQuery(cG);
		Root<W> root = cQ.from(c);
		
		Path<G> pGraphic = root.get("graphic");
		Path<Long> pId =root.get("id");
		
		cQ.select(pGraphic);
		cQ.where(cB.equal(pId,id));
		
		try	{return em.createQuery(cQ).getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+cG.getSimpleName()+" found for "+c.getSimpleName()+"."+id+" ("+JeeslIoGraphicFacadeBean.class.getSimpleName()+".fGraphicForStatus)");}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Multiple "+cG.getSimpleName()+" found for "+c.getSimpleName()+"."+id+" ("+JeeslIoGraphicFacadeBean.class.getSimpleName()+".fGraphicForStatus)");}
	}

	@Override public <T extends EjbWithGraphic<G>> List<T> allWithGraphicFigures(Class<T> c)
	{
		List<T> list = this.all(c);
		for(T ejb : list)
		{
			ejb.getGraphic().getId();
			ejb.getGraphic().getFigures().size();
		}
		return this.all(c);
	}

	@Override public <T extends EjbWithGraphic<G>> List<GC> fIoGraphicComponents(Class<T> c, JeeslIoGraphicQuery<G,GT,GC,GS> query)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<GC> cQ = cB.createQuery(fbGraphic.getClassFigure());
		
		Root<T> owner = cQ.from(c);
		Root<GC> root = cQ.from(fbGraphic.getClassFigure());
		Join<GC,G> jGraphic = root.join(JeeslGraphicComponent.Attributes.graphic.toString());
		
		predicates.addAll(aComponents(cB,query,root));
		predicates.add(cB.equal(jGraphic, owner.get(EjbWithGraphic.Att.graphic.toString())));
		
		cQ.select(root);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		TypedQuery<GC> tQ = em.createQuery(cQ);
		super.pagination(tQ,query);
		return tQ.getResultList();
	}
	
	@SuppressWarnings("unused")
	private Predicate[] pComponents(CriteriaBuilder cB, JeeslIoGraphicQuery<G,GT,GC,GS> query, Root<GC> root)
	{
		List<Predicate> predicates = aComponents(cB,query,root);
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	private List<Predicate> aComponents(CriteriaBuilder cB, JeeslIoGraphicQuery<G,GT,GC,GS> query, Root<GC> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();

		if(ObjectUtils.isNotEmpty(query.getIoGraphicShapes()))
		{
			Path<GS> pShape = root.get(JeeslGraphicComponent.Attributes.shape.toString());
			predicates.add(pShape.in(query.getIoGraphicShapes()));
		}
		return predicates;
	}
}