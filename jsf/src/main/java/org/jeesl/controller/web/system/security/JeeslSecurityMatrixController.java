package org.jeesl.controller.web.system.security;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSecurityMatrixController  <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										C extends JeeslSecurityCategory<L,D>,
										R extends JeeslSecurityRole<L,D,C,V,U,?>,
										V extends JeeslSecurityView<L,D,C,R,U,?>,
										U extends JeeslSecurityUsecase<L,D,C,R,V,?>
										>
									extends AbstractJeeslLocaleWebController<L,D,LOC>
									implements SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSecurityMatrixController.class);
	
	private final SecurityFactoryBuilder<L,D,C,R,V,U,?,?,?,?,?,?,?,?,?,?,?> fbSecurity;
	private JeeslSecurityFacade<C,R,V,U,?,?,?,?> fSecurity;
	
	private final SbMultiHandler<C> sbhRole; public SbMultiHandler<C> getSbhRole() {return sbhRole;}
	private final SbMultiHandler<C> sbhUsecase; public SbMultiHandler<C> getSbhUsecase() {return sbhUsecase;}
	
	private final Map<R,Map<U,Boolean>> map; public Map<R, Map<U, Boolean>> getMap() {return map;}
	
	protected Comparator<U> comparatorUsecase;

	private final List<R> roles; public List<R> getRoles() {return roles;}
	private final List<U> usecases; public List<U> getUsecases() {return usecases;}
		
	public JeeslSecurityMatrixController(SecurityFactoryBuilder<L,D,C,R,V,U,?,?,?,?,?,?,?,?,?,?,?> fbSecurity)
	{
		super(fbSecurity.getClassL(),fbSecurity.getClassD());
		this.fbSecurity=fbSecurity;
		
		sbhRole = new SbMultiHandler<>(fbSecurity.getClassCategory(),this);
		sbhUsecase = new SbMultiHandler<>(fbSecurity.getClassCategory(),this);
		
		map = new HashMap<>();
		
		roles = new ArrayList<>();
		usecases = new ArrayList<>();
	}
	
	public void postConstructMatrix(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
			JeeslSecurityFacade<C,R,V,U,?,?,?,?> fSecurity)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fSecurity=fSecurity;
	}

	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		this.reloadMatrix();
	}
	
	public void reloadMatrix()
	{
		roles.clear();
		for(C category : sbhRole.getSelected())
		{
			roles.addAll(fSecurity.allForParent(fbSecurity.getClassRole(),category));
		}
		
		usecases.clear();
		for(C category : sbhUsecase.getSelected())
		{
			usecases.addAll(fSecurity.allForParent(fbSecurity.getClassUsecase(),category));
		}
		
		for(R r : roles)
		{
			r = fSecurity.load(r);
			Map<U,Boolean> m = new HashMap<>();
			for(U u : r.getUsecases())
			{
				m.put(u,true);
			}
			map.put(r,m);
		}
		
		
	}
}