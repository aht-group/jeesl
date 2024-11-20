package org.jeesl.controller.web.system.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.handler.NullNumberBinder;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbSecurityContextFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSecurityContextController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											CTX extends JeeslSecurityContext<L,D>>
			extends AbstractJeeslLocaleWebController<L,D,LOC>
			implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSecurityContextController.class);
	
	private final SecurityFactoryBuilder<L,D,?,?,?,?,?,?,CTX,?,?,?,?,?,?,?,?,?> fbSecurity;
	private JeeslSecurityFacade<?,?,?,?,?,CTX,?,?> fSecurity;
	
	private final EjbSecurityContextFactory<CTX> efContext;
	
	private final NullNumberBinder nnb; public NullNumberBinder getNnb() {return nnb;}

	private final List<CTX> contexts; public List<CTX> getContexts(){return contexts;}
	
	private CTX context; public CTX getContext() {return context;} public void setContext(CTX context) {this.context = context;}
	
	public JeeslSecurityContextController(SecurityFactoryBuilder<L,D,?,?,?,?,?,?,CTX,?,?,?,?,?,?,?,?,?> fbSecurity)
	{
		super(fbSecurity.getClassL(),fbSecurity.getClassD());
		this.fbSecurity=fbSecurity;
		efContext = fbSecurity.ejbContext();
		contexts = new ArrayList<CTX>();
		nnb = new NullNumberBinder();
	}
	
	public void postConstructContext(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
											JeeslSecurityFacade<?,?,?,?,?,CTX,?,?> fSecurity)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fSecurity = fSecurity;
		this.reloadContexts();
	}
	
	private void reloadContexts()
	{
		contexts.clear();
		contexts.addAll(fSecurity.allOrderedPosition(fbSecurity.getClassContext()));
		if(Objects.nonNull(jogger)) {logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassContext(),contexts));}
	}

	public void addContext()
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassRole()));
		context = efContext.build();
		context.setName(efLang.buildEmpty(lp.getLocales()));
		context.setDescription(efDescription.buildEmpty(lp.getLocales()));
		efContext.ejb2nnb(context,nnb);
	}
	
	public void deleteContext() throws JeeslConstraintViolationException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.deleteEntity(context));
		fSecurity.rm(context);
		context=null;
		reloadContexts();
	}
	
	public void saveContext() throws JeeslLockingException, JeeslNotFoundException, JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.saveEntity(context));
		efContext.nnb2ejb(context,nnb);
		context = fSecurity.save(context);
		selectContext();
		reloadContexts();
	}
	
	public void selectContext()
	{
		logger.trace(AbstractLogMessage.selectEntity(context));
		context = fSecurity.find(fbSecurity.getClassContext(),context);
		context = efLang.persistMissingLangs(fSecurity,lp,context);
		context = efDescription.persistMissingLangs(fSecurity,lp.getLocales(),context);	
		efContext.ejb2nnb(context,nnb);
	}
	
	public void reorderContexts() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSecurity,contexts);}
}