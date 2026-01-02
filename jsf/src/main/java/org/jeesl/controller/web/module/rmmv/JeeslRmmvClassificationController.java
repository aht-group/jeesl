package org.jeesl.controller.web.module.rmmv;

import java.util.Objects;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoGraphicFacade;
import org.jeesl.api.facade.module.JeeslRmmvFacade;
import org.jeesl.controller.web.util.AbstractTreeClassificationController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.RmmvFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.module.rmmv.JeeslRmmvClassificationCallback;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvClassification;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvElement;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslRmmvClassificationController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											G extends JeeslGraphic<GT,?,?>, GT extends JeeslGraphicType<L,D,GT,G>,
											R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
											E extends JeeslRmmvElement<L,R,E,EC>,
											EC extends JeeslRmmvClassification<L,R,EC,G>>
		extends AbstractTreeClassificationController<L,D,LOC,G,GT,R,RREF,EC>
		implements SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslRmmvClassificationController.class);
	
	private final JeeslRmmvClassificationCallback callback;

	private final RmmvFactoryBuilder<L,D,LOC,R,E,EC,?,?,?,?,?> fbRmmv;

	public JeeslRmmvClassificationController(final JeeslRmmvClassificationCallback callback,
												final RmmvFactoryBuilder<L,D,LOC,R,E,EC,?,?,?,?,?> fbRmmv,
												final SvgFactoryBuilder<L,D,G,GT,?,?> fbSvg)
	{
		super(fbSvg,fbRmmv.getClassLocale(),fbRmmv.getClassClasification());
		this.callback=callback;
		this.fbRmmv=fbRmmv;
	}
	
	public void postConstructTreeElement(JeeslRmmvFacade<L,D,R,E,EC,?,?,?,?,?> fRmmv,
									JeeslIoGraphicFacade<?,G,GT,?,?> fGraphic,
									JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									R realm)
	{
		super.postConstructTreeClassification(fRmmv,fGraphic,lp,bMessage,realm);
		logger.trace("Callback: "+callback.toString());
	}
	
	@Override
	public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		
	}	

	public void addClassification()
	{
		this.reset(true);
		classification = fbRmmv.ejbClassification().build(realm,rref);
		classification.setName(efLang.buildEmpty(lp.getLocales()));
	}

	@Override protected void savedClassification()
	{
		if(Objects.nonNull(callback)) {callback.classificationSaved();}
	}
}