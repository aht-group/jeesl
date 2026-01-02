package org.jeesl.web.mbean.prototype.io.dms;

import java.io.Serializable;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoDmsFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoDmsFactoryBuilder;
import org.jeesl.factory.ejb.io.dms.EjbIoDmsDocumentFactory;
import org.jeesl.factory.ejb.io.dms.EjbIoDmsSectionFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
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
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDmsBean <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										DMS extends JeeslIoDms<L,D,STORAGE,AS,DS,SECTION>,
										STORAGE extends JeeslFileStorage<L,D,?,?,?>,
										AS extends JeeslAttributeSet<L,D,?,?,?>,
										DS extends JeeslDomainSet<L,D,?>,
										SECTION extends JeeslIoDmsSection<L,D,SECTION>,
										F extends JeeslIoDmsDocument<L,SECTION,FC,AC>,
										VIEW extends JeeslIoDmsView<L,D,DMS>,
										LAYER extends JeeslIoDmsLayer<VIEW,AI>,
										FC extends JeeslFileContainer<?,?>,
										AI extends JeeslAttributeItem<?,AS>,
										AC extends JeeslAttributeContainer<?,?>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractDmsBean.class);
	
	protected JeeslIoDmsFacade<L,D,LOC,DMS,STORAGE,AS,DS,SECTION,F,VIEW,FC,AC> fDms;
	protected final IoDmsFactoryBuilder<L,D,LOC,DMS,STORAGE,SECTION,F,VIEW,LAYER> fbDms;
	
	protected final EjbIoDmsSectionFactory<SECTION> efSection;
	protected final EjbIoDmsDocumentFactory<SECTION,F> efFile;
	
	protected DMS dm; public DMS getDm() {return dm;} public void setDm(DMS dm) {this.dm = dm;}
	protected final SbSingleHandler<LOC> sbhLocale; public SbSingleHandler<LOC> getSbhLocale() {return sbhLocale;}

	public AbstractDmsBean(IoDmsFactoryBuilder<L,D,LOC,DMS,STORAGE,SECTION,F,VIEW,LAYER> fbDms)
	{
		super(fbDms.getClassL(),fbDms.getClassD());
		this.fbDms=fbDms;
		
		sbhLocale = new SbSingleHandler<LOC>(fbDms.getClassLocale(),this);
		efSection = fbDms.ejbSection();
		efFile = fbDms.ejbFile();
	}
	
	protected void initDms(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,JeeslIoDmsFacade<L,D,LOC,DMS,STORAGE,AS,DS,SECTION,F,VIEW,FC,AC> fDms)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fDms=fDms;
	}
	
	protected void initLocales()
	{
		localeCodes = new String[sbhLocale.getList().size()];
		for(int i=0;i<sbhLocale.getList().size();i++) {localeCodes[i]=sbhLocale.getList().get(i).getCode();}
	}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId ejb) throws JeeslLockingException, JeeslConstraintViolationException
	{
		
	}
}