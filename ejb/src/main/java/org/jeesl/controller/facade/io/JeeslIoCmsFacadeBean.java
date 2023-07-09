package org.jeesl.controller.facade.io;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.io.JeeslIoCmsFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoCmsFactoryBuilder;
import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsCategory;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsContent;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElement;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElementType;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsVisiblity;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.cms.markup.w.JeeslWithMarkupSingle;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoCmsFacadeBean<L extends JeeslLang, D extends JeeslDescription,
									CAT extends JeeslIoCmsCategory<L,D,CAT,?>,
									CMS extends JeeslIoCms<L,D,LOC,CAT,S>,
									V extends JeeslIoCmsVisiblity,
									S extends JeeslIoCmsSection<L,S>,
									E extends JeeslIoCmsElement<V,S,EC,ET,C,FC>,
									EC extends JeeslStatus<L,D,EC>,
									ET extends JeeslIoCmsElementType<L,D,ET,?>,
									C extends JeeslIoCmsContent<V,E,MT>,
									M extends JeeslIoMarkup<MT>,
									MT extends JeeslIoMarkupType<L,D,MT,?>,
									FC extends JeeslFileContainer<?,FM>,
									FM extends JeeslFileMeta<D,FC,?,?>,
									LOC extends JeeslLocale<L,D,LOC,?>>
					extends JeeslFacadeBean
					implements JeeslIoCmsFacade<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,M,MT,FC,FM>
{	
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoCmsFacadeBean.class);
	
	private final IoCmsFactoryBuilder<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,M,MT,FC,FM> fbCms;
	
	public JeeslIoCmsFacadeBean(EntityManager em,
			IoCmsFactoryBuilder<L,D,LOC,CAT,CMS,V,S,E,EC,ET,C,M,MT,FC,FM> fbCms)
	{
		super(em);
		this.fbCms=fbCms;
	}
	
	@Override public S load(S section, boolean recursive)
	{
		section = em.find(fbCms.getClassSection(), section.getId());
		if(recursive)
		{
			for(S s : section.getSections())
			{
				s = this.load(s,recursive);
			}
		}
		return section;
	}

	@Override public List<E> fCmsElements(S section) {return this.allForParent(fbCms.getClassElement(),section);}

	@Override public void deleteCmsElement(E element) throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(element.getFrContainer()!=null)
		{
			List<FM> files = this.allForParent(fbCms.getClassFileMeta(),element.getFrContainer());
			if(ObjectUtils.isNotEmpty(files)) {throw new JeeslConstraintViolationException("There are still files");}
			else
			{
				FC container = element.getFrContainer();
				element.setFrContainer(null);
				element = this.save(element);
				this.rm(container);
			}
		}
		
		if(ObjectUtils.isNotEmpty(element.getContent()))
		{
			List<C> list = this.allForParent(fbCms.getClassContent(),element);
			for(C c : list)
			{
				c.getElement().getContent().remove(c.getLkey());
				this.rm(c);
			}
		}
		element.setContent(null);
		element = this.save(element);
		
		this.rmProtected(element);
	}

	@Override public <W extends JeeslWithMarkupSingle<M>> M fIoMarkup(Class<W> c, W with) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<M> cQ = cB.createQuery(fbCms.getClassMarkup());
		Root<W> root = cQ.from(c);
		
		Path<M> pGraphic = root.get(JeeslWithMarkupSingle.Attributes.markup.toString());
		Path<Long> pId = root.get(JeeslWithMarkupSingle.Attributes.id.toString());
		
		cQ.select(pGraphic);
		cQ.where(cB.equal(pId,with.getId()));
		
		try	{return em.createQuery(cQ).getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+fbCms.getClassMarkup().getSimpleName()+" found for "+c.getSimpleName()+"."+with.getId()+" ("+JeeslIoCmsFacadeBean.class.getSimpleName()+".fIoMarkup)");}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Multiple "+fbCms.getClassMarkup().getSimpleName()+" found for "+c.getSimpleName()+"."+with.getId()+" ("+JeeslIoCmsFacadeBean.class.getSimpleName()+".fIoMarkup)");}
	}
}