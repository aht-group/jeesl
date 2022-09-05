package org.jeesl.controller.facade.system;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.system.graphic.JeeslGraphicFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;

public class JeeslGraphicFacadeBean<L extends JeeslLang, D extends JeeslDescription,
									S extends EjbWithId,
									G extends JeeslGraphic<GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
									F extends JeeslGraphicComponent<L,D,G,GT,F,FS>, FS extends JeeslGraphicShape<L,D,FS,G>>
					extends JeeslFacadeBean
					implements JeeslGraphicFacade<L,D,S,G,GT,F,FS>
{	
	private static final long serialVersionUID = 1L;
	
	private final Class<S> cStatus;
	private final Class<G> cG;
	
	public JeeslGraphicFacadeBean(EntityManager em, final Class<S> cStatus, final Class<G> cG)
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
		catch (NoResultException ex){throw new JeeslNotFoundException("No Graphic found for status.id"+ejbId);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Multiple Results for status.id"+ejbId);}
	}
	
	@Override public <W extends EjbWithGraphic<G>> G fGraphic(Class<W> c, W w) throws JeeslNotFoundException {return fGraphic(c,w.getId());}
	@Override public <W extends EjbWithGraphic<G>> G fGraphic(Class<W> c, long id) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<G> cQ = cB.createQuery(cG);
		Root<W> w = cQ.from(c);
		
		Path<G> pGraphic = w.get("graphic");
		Path<Long> pId = w.get("id");
		
		cQ.where(cB.equal(pId,id));
		cQ.select(pGraphic);
		
		try	{return em.createQuery(cQ).getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No Graphic found for status.id"+id);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Multiple Results for status.id"+id);}
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