package org.jeesl.web.mbean.prototype.module.aom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.module.aom.JeeslAssetCacheBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslAssetFacade;
import org.jeesl.api.facade.system.graphic.JeeslGraphicFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetStatus;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventStatus;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventUpload;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAomViewBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										S extends EjbWithId, G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<L,D,GT,G>,
										GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<L,D,GS,G>,
										REALM extends JeeslTenantRealm<L,D,REALM,?>, RREF extends EjbWithId,
										COMPANY extends JeeslAomCompany<REALM,SCOPE>,
										SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
										ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,ASTATUS,ATYPE>,
										ASTATUS extends JeeslAomAssetStatus<L,D,ASTATUS,?>,
										ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,ALEVEL,G>,
										ALEVEL extends JeeslAomView<L,D,REALM,G>,
										EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,M,USER,FRC>,
										ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
										ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
										M extends JeeslMarkup<MT>,
										MT extends JeeslIoCmsMarkupType<L,D,MT,?>,
										USER extends JeeslSimpleUser,
										FRC extends JeeslFileContainer<?,?>,
										UP extends JeeslAomEventUpload<L,D,UP,?>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAomViewBean.class);

	private JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fAsset;

	@SuppressWarnings("unused")
	private JeeslGraphicFacade<L,D,S,G,GT,GC,GS> fGraphic;

	@SuppressWarnings("unused")
	private JeeslAssetCacheBean<L,D,REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,ETYPE,UP> bCache;

	private final SvgFactoryBuilder<L,D,G,GT,GC,GS> fbSvg;
	private final AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset;

    private REALM realm;
    private RREF rref;

    private final List<ALEVEL> levels; public List<ALEVEL> getLevels() {return levels;}

    private ALEVEL level; public ALEVEL getLevel() {return level;} public void setLevel(ALEVEL level) {this.level = level;}

	public AbstractAomViewBean(AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset, SvgFactoryBuilder<L,D,G,GT,GC,GS> fbSvg)
	{
		super(fbAsset.getClassL(),fbAsset.getClassD());
		this.fbAsset=fbAsset;
		this.fbSvg=fbSvg;

		levels = new ArrayList<>();
	}

	protected void postConstructAomView(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslAssetCacheBean<L,D,REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,ETYPE,UP> bCache,
									JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fAsset,
									JeeslGraphicFacade<L,D,S,G,GT,GC,GS> fGraphic,
									REALM realm)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.bCache=bCache;
		this.fAsset=fAsset;
		this.fGraphic=fGraphic;
		this.realm=realm;
	}

	protected void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		reload();
	}

	private void reload()
	{
		levels.clear();
		levels.addAll(fAsset.fAomViews(realm,rref));
	}

	private void reset(boolean rLevel)
	{
		if(rLevel) {level=null;}
	}

	public void selectLevel()
	{
		level = efLang.persistMissingLangs(fAsset, bTranslation.getLocales(),level);
		level = efDescription.persistMissingLangs(fAsset, bTranslation.getLocales(),level);
	}

	public void addLevel()
	{
		level = fbAsset.ejbLevel().build(realm,rref,levels);
		level.setName(efLang.createEmpty(bTranslation.getLocales()));
		level.setDescription(efDescription.createEmpty(bTranslation.getLocales()));
	}

	public void saveLevel() throws JeeslConstraintViolationException, JeeslLockingException
	{
		level = fAsset.save(level);
		reload();
//		bCache.update(realm, rref, type);
	}

	public void deleteLevel() throws JeeslLockingException
	{
		try
		{
			fAsset.rm(level);
//			bCache.delete(realm,rref,level);
			reload();
			reset(true);
		}
		catch (JeeslConstraintViolationException e) {bMessage.errorConstraintViolationInUse();}
	}

	public void handleFileUpload(FileUploadEvent event) throws JeeslConstraintViolationException, JeeslLockingException
	{
		UploadedFile file = event.getFile();
		logger.info("Received file with a size of " +file.getSize());
		if(level.getGraphic()==null)
		{
			GT gt = fAsset.fByEnum(fbSvg.getClassGraphicType(), JeeslGraphicType.Code.svg);
			G g = fbSvg.efGraphic().build(gt);
			g = fAsset.save(g);
			level.setGraphic(g);
			level = fAsset.save(level);
			level.getGraphic().setData(file.getContent());
			level = fAsset.save(level);
		}
		else
		{
//			try
//			{
//				G g = fGraphic.fGraphic(fbAsset.getClassAssetLevel(),level);
//				g.setData(file.getContents());
//				fAsset.save(g);
//			}
//			catch (JeeslNotFoundException e) {e.printStackTrace();}
		}
	}

	public void reorder() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fAsset,levels);}
}