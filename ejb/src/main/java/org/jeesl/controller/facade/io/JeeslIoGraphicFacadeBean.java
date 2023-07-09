package org.jeesl.controller.facade.io;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.io.JeeslIoGraphicFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;

public class JeeslIoGraphicFacadeBean<L extends JeeslLang, D extends JeeslDescription,
									S extends EjbWithId,
									G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<L,D,GT,G>,
									GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<L,D,GS,G>>
					extends JeeslFacadeBean
					implements JeeslIoGraphicFacade<L,D,S,G,GT,GC,GS>
{	
	private static final long serialVersionUID = 1L;
	
	private final Class<S> cStatus;
	private final Class<G> cG;
	
	public JeeslIoGraphicFacadeBean(EntityManager em, final Class<S> cStatus, final Class<G> cG)
	{
		super(em);
		this.cStatus=cStatus;
		this.cG=cG;
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
}