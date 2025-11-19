package org.jeesl.controller.web.module.aom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoGraphicFacade;
import org.jeesl.api.facade.module.JeeslAomFacade;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.interfaces.cache.module.aom.JeeslAomAssetCache;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslAomViewController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										G extends JeeslGraphic<GT,?,?>, GT extends JeeslGraphicType<L,D,GT,G>,
										REALM extends JeeslTenantRealm<L,D,REALM,?>,
										ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,VIEW,G>,
										VIEW extends JeeslAomView<L,D,REALM,G>>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslAomViewController.class);

	private JeeslAomFacade<L,D,REALM,?,?,?,?,ATYPE,VIEW,?,?> fAom;
	private JeeslIoGraphicFacade<?,G,GT,?,?> fGraphic;

	@SuppressWarnings("unused")
	private JeeslAomAssetCache<REALM,?,ATYPE,VIEW> bCache;

	private final SvgFactoryBuilder<L,D,G,GT,?,?> fbSvg;
	private final AomFactoryBuilder<L,D,REALM,?,?,?,?,ATYPE,VIEW,?,?,?,?,?,?,?,?> fbAsset;

	private final JsonTuple1Handler<VIEW> thTypeByView; public JsonTuple1Handler<VIEW> getThTypeByView() {return thTypeByView;}

	private final List<VIEW> schemes; public List<VIEW> getSchemes() {return schemes;}

    private TenantIdentifier<REALM> identifier;
    private VIEW level; public VIEW getLevel() {return level;} public void setLevel(VIEW level) {this.level = level;}

	public JeeslAomViewController(AomFactoryBuilder<L,D,REALM,?,?,?,?,ATYPE,VIEW,?,?,?,?,?,?,?,?> fbAom,
								SvgFactoryBuilder<L,D,G,GT,?,?> fbSvg)
	{
		super(fbAom.getClassL(),fbAom.getClassD());
		this.fbAsset=fbAom;
		this.fbSvg=fbSvg;
		
		thTypeByView = new JsonTuple1Handler<>(fbAom.getClassAssetLevel());

		schemes = new ArrayList<>();
	}

	public void postConstructView(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									JeeslAomAssetCache<REALM,?,ATYPE,VIEW> bCache,
									JeeslAomFacade<L,D,REALM,?,?,?,?,ATYPE,VIEW,?,?> fAsset,
									JeeslIoGraphicFacade<?,G,GT,?,?> fGraphic,
									REALM realm)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.bCache=bCache;
		this.fAom=fAsset;
		this.fGraphic=fGraphic;
		identifier = TenantIdentifier.instance(realm);
	}

	public <RREF extends EjbWithId> void updateRealmReference(RREF rref)
	{
		identifier.withRref(rref);
		reload();
	}

	private void reload()
	{
		schemes.clear();
		schemes.addAll(fAom.fAomViews(identifier));
		thTypeByView.load(fAom.tpcTypeByView(identifier));
	}

	private void reset(boolean rLevel)
	{
		if(rLevel) {level=null;}
	}

	public void selectLevel()
	{
		level = efLang.persistMissingLangs(fAom, lp.getLocales(),level);
		level = efDescription.persistMissingLangs(fAom, lp.getLocales(),level);
	}

	public void addLevel()
	{
		level = fbAsset.ejbLevel().build(identifier.getRealm(),identifier,schemes);
		level.setName(efLang.buildEmpty(lp.getLocales()));
		level.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}

	public void saveLevel() throws JeeslConstraintViolationException, JeeslLockingException
	{
		level = fAom.save(level);
		reload();
//		bCache.update(realm, rref, type);
	}

	public void deleteLevel() throws JeeslLockingException
	{
		try
		{
			fAom.rm(level);
//			bCache.delete(realm,rref,level);
			reload();
			reset(true);
		}
		catch (JeeslConstraintViolationException e) {bMessage.constraintInUse(null);}
	}

	public void handleFileUpload(FileUploadEvent event) throws JeeslConstraintViolationException, JeeslLockingException
	{
		UploadedFile file = event.getFile();
		logger.info("Received file with a size of " +file.getSize());
		if(level.getGraphic()==null)
		{
			GT gt = fAom.fByEnum(fbSvg.getClassGraphicType(), JeeslGraphicType.Code.svg);
			G g = fbSvg.efGraphic().build(gt);
			g = fAom.save(g);
			level.setGraphic(g);
			level = fAom.save(level);
			level.getGraphic().setData(file.getContent());
			level = fAom.save(level);
		}
		else
		{
			try
			{
				G g = fGraphic.fGraphic(fbAsset.getClassAssetLevel(),level);
				g.setData(file.getContent());
				fAom.save(g);
			}
			catch (JeeslNotFoundException e) {e.printStackTrace();}
		}
	}

	public void reorder() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fAom,schemes);}
}