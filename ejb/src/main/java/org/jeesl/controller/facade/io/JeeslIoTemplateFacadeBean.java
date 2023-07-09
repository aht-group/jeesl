package org.jeesl.controller.facade.io;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.io.JeeslIoTemplateFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoTemplateFactoryBuilder;
import org.jeesl.interfaces.facade.ParentPredicate;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateDefinition;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateToken;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateTokenType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoTemplateFacadeBean<L extends JeeslLang,D extends JeeslDescription,
						CATEGORY extends JeeslStatus<L,D,CATEGORY>,
						CHANNEL extends JeeslTemplateChannel<L,D,CHANNEL,?>,
						TEMPLATE extends JeeslIoTemplate<L,D,CATEGORY,SCOPE,DEFINITION,TOKEN>,
						SCOPE extends JeeslStatus<L,D,SCOPE>,
						DEFINITION extends JeeslIoTemplateDefinition<D,CHANNEL,TEMPLATE>,
						TOKEN extends JeeslIoTemplateToken<L,D,TEMPLATE,TOKENTYPE>,
						TOKENTYPE extends JeeslTemplateTokenType<L,D,TOKENTYPE,?>>
					extends JeeslFacadeBean
					implements JeeslIoTemplateFacade<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE>
{	
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoTemplateFacadeBean.class);

	private final IoTemplateFactoryBuilder<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fbTemplate;
	
	public JeeslIoTemplateFacadeBean(EntityManager em,IoTemplateFactoryBuilder<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fbTemplate)
	{
		super(em);
		this.fbTemplate=fbTemplate;
	}
	
	@Override public TEMPLATE load(TEMPLATE template)
	{
		template = em.find(fbTemplate.getClassTemplate(), template.getId());
		template.getTokens().size();
		template.getDefinitions().size();
		return template;
	}
	
	@Override
	public <E extends Enum<E>> List<TEMPLATE> loadTemplates(E category)
	{
		List<TEMPLATE> result = new ArrayList<TEMPLATE>();
		List<CATEGORY> categories = new ArrayList<CATEGORY>();
		
		try {categories.add(this.fByCode(fbTemplate.getClassCategory(), category));}
		catch (JeeslNotFoundException e) {logger.error(e.getMessage());}
		if(categories.isEmpty()){return result;}
		
		for(TEMPLATE t : fTemplates(categories,false))
		{
			t.getDefinitions().size();
			result.add(t);
		}
		
		return result;
	}
	
	@Override public List<TEMPLATE> fTemplates(List<CATEGORY> categories, boolean showInvisibleEntities)
	{
		List<ParentPredicate<CATEGORY>> ppCategory = ParentPredicate.createFromList(fbTemplate.getClassCategory(),"category",categories);
		return allForOrParents(fbTemplate.getClassTemplate(),ppCategory);
	}
	
	@Override public List<TEMPLATE> fTemplates(CATEGORY category, SCOPE scope)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<TEMPLATE> cQ = cB.createQuery(fbTemplate.getClassTemplate());
		Root<TEMPLATE> template = cQ.from(fbTemplate.getClassTemplate());
		
		Path<CATEGORY> pCategory = template.get(JeeslIoTemplate.Attributes.category.toString());
		Path<SCOPE> pScope = template.get(JeeslIoTemplate.Attributes.scope.toString());
		Path<Boolean> pVisible = template.get(JeeslIoTemplate.Attributes.visible.toString());	

		cQ.where(cB.and(cB.equal(pCategory,category),cB.equal(pScope,scope),cB.equal(pVisible,true)));
		cQ.select(template);
		
		TypedQuery<TEMPLATE> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}

	@Override public DEFINITION fDefinition(CHANNEL type, String code) throws JeeslNotFoundException
	{
		TEMPLATE t = this.fByCode(fbTemplate.getClassTemplate(), code);
		for(DEFINITION d : t.getDefinitions())
		{
			if(d.getType().equals(type)){return d;}
		}
		
		throw new JeeslNotFoundException("No Definition for "+code+" and type="+type.getCode());
	}
}