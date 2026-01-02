package org.jeesl.controller.web.module.mmg;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoGraphicFacade;
import org.jeesl.api.facade.module.JeeslMmgFacade;
import org.jeesl.controller.web.util.AbstractTreeClassificationController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.MmgFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.module.mmg.JeeslMmgClassificationCallback;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgClassification;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgGallery;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgItem;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgQuality;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMmgClassificationController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
												G extends JeeslGraphic<GT,?,?>, GT extends JeeslGraphicType<L,D,GT,G>,
												R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
												MG extends JeeslMmgGallery<L>,
												MI extends JeeslMmgItem<L,MG,FRC,USER>,
												MC extends JeeslMmgClassification<L,R,MC,G>,
												MQ extends JeeslMmgQuality<L,D,MQ,?>,
												FRC extends JeeslFileContainer<?,?>,
												USER extends JeeslSimpleUser>
		extends AbstractTreeClassificationController<L,D,LOC,G,GT,R,RREF,MC> 
		implements SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslMmgClassificationController.class);
	
	@SuppressWarnings("unused")
	private final JeeslMmgClassificationCallback callback;

	private final MmgFactoryBuilder<L,D,LOC,R,MG,MI,MC,MQ,USER> fbMmg;

	public JeeslMmgClassificationController(final JeeslMmgClassificationCallback callback,
												final MmgFactoryBuilder<L,D,LOC,R,MG,MI,MC,MQ,USER> fbMmg,
												final SvgFactoryBuilder<L,D,G,GT,?,?> fbSvg)
	{
		super(fbSvg,fbMmg.getClassLocale(),fbMmg.getClassClassification());
		this.callback=callback;
		this.fbMmg=fbMmg;
	}
	
	public void postConstructTreeElement(JeeslMmgFacade<L,D,R,MG,MI,MC,MQ,USER> fMmg,
									JeeslIoGraphicFacade<?,G,GT,?,?> fGraphic,
									JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									R realm)
	{
		super.postConstructTreeClassification(fMmg,fGraphic,lp,bMessage,realm);
	}
	
	@Override
	public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
	}
	
	@Override protected void savedClassification()
	{
		// TODO Auto-generated method stub
		
	}

	public void addClassification()
	{
		this.reset(true);
		classification = fbMmg.ejbClassification().build(realm,rref);
		classification.setName(efLang.buildEmpty(lp.getLocales()));
	}	
}