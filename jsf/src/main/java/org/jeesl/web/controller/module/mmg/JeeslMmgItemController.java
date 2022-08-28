package org.jeesl.web.controller.module.mmg;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslMmgFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.MmgFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.module.mmg.JeeslMmgItemCallback;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgClassification;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgGallery;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgItem;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgQuality;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.web.AbstractJeeslWebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMmgItemController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
											G extends JeeslMmgGallery<L>,
											I extends JeeslMmgItem<L,D,R>,
											C extends JeeslMmgClassification<L,R,C,?>,
											Q extends JeeslMmgQuality<L,D,Q,?>>
		extends AbstractJeeslWebController<L,D,LOC>
		implements SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslMmgItemController.class);
	
	private final JeeslMmgItemCallback callback;
	private JeeslMmgFacade<L,D,R,G,I,C,Q> fRmmv;
	
	private final MmgFactoryBuilder<L,D,LOC,R,G,I,C,Q> fbMmg;
	
	protected final SbSingleHandler<LOC> sbhLocale; public SbSingleHandler<LOC> getSbhLocale() {return sbhLocale;}
	
	private final List<I> items; public List<I> getItems() {return items;}
	private final List<C> classifications; public List<C> getClassifications() {return classifications;}

	
	protected R realm;
	protected RREF rref;
	
	public JeeslMmgItemController(final JeeslMmgItemCallback callback, final MmgFactoryBuilder<L,D,LOC,R,G,I,C,Q> fbRmmv)
	{
		super(fbRmmv.getClassL(),fbRmmv.getClassD());
		this.callback=callback;
		this.fbMmg=fbRmmv;
		
		sbhLocale = new SbSingleHandler<>(fbRmmv.getClassLocale(),this);
		
		items = new ArrayList<>();
		classifications = new ArrayList<>();
	}
	
	public void postConstructTreeElement(JeeslMmgFacade<L,D,R,G,I,C,Q> fRmmv,
									JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									R realm)
	{
		super.postConstructWebController(lp);
		this.fRmmv=fRmmv;
		this.realm=realm;
		
		sbhLocale.setList(lp.getLocales());
		sbhLocale.setDefault();
	}
	
	public void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		
		classifications.addAll(fRmmv.all(fbMmg.getClassClassification(),realm,rref));
	}
	
	@Override
	public void selectSbSingle(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
	}
	
	private void reset(boolean rElement, boolean rConfigs, boolean rConfig)
	{
//		if(rElement) {element=null;}
//		if(rConfigs) {configs.clear();}
//		if(rConfig) {config=null;}
	}
	
	public void addItem()
	{
		this.reset(true,true,true);
//		element = fbRmmv.ejbElement().build(realm,rref);
//		element.setName(efLang.createEmpty(lp.getLocales()));
	}
	
	public void saveElement() throws JeeslConstraintViolationException, JeeslLockingException
	{
//		boolean isNew = EjbIdFactory.isUnSaved(element);
//		element = fRmmv.save(element);
//		if(isNew)
//		{
//			tree.getChildren().add(new DefaultTreeNode(element));
//		}
//		treePath.clear();TreeHelper.fillPath(treePath,element);
//		this.reloadTree();
	}
}