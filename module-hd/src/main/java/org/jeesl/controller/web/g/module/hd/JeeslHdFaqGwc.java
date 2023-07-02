package org.jeesl.controller.web.g.module.hd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslHdFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.HdFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.module.hd.JeeslHdFaqCallback;
import org.jeesl.interfaces.model.module.hd.JeeslHdCategory;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdFaq;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdFga;
import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdScope;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslHdFaqGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
								CAT extends JeeslHdCategory<L,D,R,CAT,?>,
								SCOPE extends JeeslHdScope<L,D,SCOPE,?>,
								FAQ extends JeeslHdFaq<L,D,R,CAT,SCOPE>,
								FGA extends JeeslHdFga<FAQ,?,?>
								>
					extends AbstractJeeslWebController<L,D,LOC>
					implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslHdFaqGwc.class);
	
	protected final HdFactoryBuilder<L,D,LOC,R,?,CAT,?,?,?,?,?,?,?,?,FAQ,SCOPE,FGA,?,?,?,?> fbHd;
//	private final IoCmsFactoryBuilder<L,D,LOC,?,DOC,?,SEC,?,?,?,?,?,?,?,?> fbCms;
	
	private JeeslHdFacade<L,D,R,CAT,?,?,?,?,?,?,?,?,?,FAQ,SCOPE,FGA,?,?,?> fHd;
	
	private final JeeslHdFaqCallback<SCOPE> callback;
	
	protected final SbMultiHandler<CAT> sbhCategory; public SbMultiHandler<CAT> getSbhCategory() {return sbhCategory;}
	protected final SbMultiHandler<SCOPE> sbhScope; public SbMultiHandler<SCOPE> getSbhScope() {return sbhScope;}

	private final List<FAQ> faqs; public List<FAQ> getFaqs() {return faqs;}
	private final List<FGA> answers; public List<FGA> getAnswers() {return answers;}

	private R realm;
	private FAQ faq; public FAQ getFaq() {return faq;} public void setFaq(FAQ faq) {this.faq = faq;}
	private FGA fga; public FGA getFga() {return fga;} public void setFga(FGA fga) {this.fga = fga;}
	
	public JeeslHdFaqGwc(JeeslHdFaqCallback<SCOPE> callback,
							HdFactoryBuilder<L,D,LOC,R,?,CAT,?,?,?,?,?,?,?,?,FAQ,SCOPE,FGA,?,?,?,?> fbHd)
	{
		super(fbHd.getClassL(),fbHd.getClassD());
		this.callback=callback;
		this.fbHd=fbHd;
//		this.fbCms=fbCms;
		
		sbhCategory = new SbMultiHandler<>(fbHd.getClassCategory(),this);
		sbhScope = new SbMultiHandler<>(fbHd.getClassScope(),this);
		
		faqs = new ArrayList<>();
		answers = new ArrayList<>();
	}

	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									JeeslHdFacade<L,D,R,CAT,?,?,?,?,?,?,?,?,?,FAQ,SCOPE,FGA,?,?,?> fHd,
									R realm)
	{
		super.postConstructWebController(lp,bMessage);
		this.fHd=fHd;
		this.realm=realm;
	}
		
	public void updateRealmReference(RREF rref)
	{
		sbhCategory.setList(fHd.all(fbHd.getClassCategory(),realm,rref));
		sbhCategory.selectAll();
		
		sbhScope.setList(fHd.all(fbHd.getClassScope()));
		sbhScope.selectAll();
		
		List<SCOPE> scopes = callback.getAllowedScopes();
		logger.info(fbHd.getClassScope().getSimpleName()+": "+scopes.size());
		
		faqs.clear();
		for(FAQ f : fHd.all(fbHd.getClassFaq(),realm,rref))
		{
			if(scopes.contains(f.getScope()))
			{
				faqs.add(f);
			}
		}
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{

	}
	
	private void reset(boolean rFaq, boolean rAnswer)
	{
		if(rFaq) {faq=null;}
		if(rAnswer) {fga=null;}
	}
	
	public void selectFaq()
	{
		reset(false,true);
		logger.info(AbstractLogMessage.selectEntity(faq));
		
		answers.clear();
		answers.addAll(fHd.allForParent(fbHd.getClassFga(), faq));
	}
}