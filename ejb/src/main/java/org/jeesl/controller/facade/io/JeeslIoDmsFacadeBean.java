package org.jeesl.controller.facade.io;

import javax.persistence.EntityManager;

import org.jeesl.api.facade.io.JeeslIoDmsFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.factory.builder.io.IoDmsFactoryBuilder;
import org.jeesl.interfaces.model.io.dms.JeeslIoDms;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsDocument;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsLayer;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsSection;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsView;
import org.jeesl.interfaces.model.io.domain.JeeslDomainSet;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoDmsFacadeBean<L extends JeeslLang,D extends JeeslDescription,LOC extends JeeslStatus<L,D,LOC>,
								DMS extends JeeslIoDms<L,D,STORAGE,AS,DS,S>,
								STORAGE extends JeeslFileStorage<L,D,?,?,?>,
								AS extends JeeslAttributeSet<L,D,?,?,?>,
								DS extends JeeslDomainSet<L,D,?>,
								S extends JeeslIoDmsSection<L,D,S>,
								F extends JeeslIoDmsDocument<L,S,FC,AC>,
								VIEW extends JeeslIoDmsView<L,D,DMS>,
								LAYER extends JeeslIoDmsLayer<VIEW,AI>,
								FC extends JeeslFileContainer<?,?>,
								AI extends JeeslAttributeItem<?,AS>,
								AC extends JeeslAttributeContainer<?,?>>
					extends JeeslFacadeBean
					implements JeeslIoDmsFacade<L,D,LOC,DMS,STORAGE,AS,DS,S,F,VIEW,FC,AC>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoDmsFacadeBean.class);
	
	private final IoDmsFactoryBuilder<L,D,LOC,DMS,STORAGE,S,F,VIEW,LAYER> fbDms;
	
	public JeeslIoDmsFacadeBean(EntityManager em, final IoDmsFactoryBuilder<L,D,LOC,DMS,STORAGE,S,F,VIEW,LAYER> fbDms)
	{
		super(em);
		this.fbDms=fbDms;
	}
	
	@Override public S load(S section, boolean recursive)
	{
		section = em.find(fbDms.getClassSection(), section.getId());
		if(recursive)
		{
			for(S s : section.getSections())
			{
				s = this.load(s,recursive);
			}
		}
		return section;
	}
}