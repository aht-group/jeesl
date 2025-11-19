package org.jeesl.controller.web.c.io.maven;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.controller.handler.lazy.io.maven.EjbIoMavenVersionLazyModel;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.io.maven.ee.EjbMavenReferralFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.txt.io.maven.TxtMavenVersionFactory;
import org.jeesl.interfaces.bean.op.OpSingleSelectionBean;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.op.OpSelectionHandler;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.io.maven.JeeslIoMavenReferralCallback;
import org.jeesl.interfaces.model.io.maven.ee.JeeslIoMavenEeReferral;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenUsage;
import org.jeesl.jsf.handler.op.OpSingleSelectionHandler;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.jsf.handler.th.JeeslTableCellSelectHandler;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenDependency;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenScope;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeEdition;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeReferral;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeStandard;
import org.jeesl.model.ejb.io.maven.module.IoMavenModule;
import org.jeesl.model.ejb.io.maven.module.IoMavenStructure;
import org.jeesl.model.ejb.io.maven.module.IoMavenType;
import org.jeesl.model.ejb.io.maven.usage.IoMavenUsage;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.jeesl.util.comparator.ejb.PositionComparator;
import org.jeesl.util.comparator.ejb.io.maven.EjbMavenUsageComparator;
import org.jeesl.util.query.cq.CqBool;
import org.jeesl.util.query.ejb.io.maven.EjbIoMavenQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoMavenReferralWc extends AbstractJeeslLocaleWebController<IoLang,IoDescription,IoLocale>
									implements OpSingleSelectionBean<IoMavenVersion>,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenReferralWc.class);
	
	private JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenType,IoMavenUsage,IoMavenEeReferral> fMaven;

	private final JeeslIoMavenReferralCallback<IoMavenType,IoMavenEeEdition> callback;
	
	protected final SbMultiHandler<IoMavenEeEdition> sbhEdition; public SbMultiHandler<IoMavenEeEdition> getSbhEdition() {return sbhEdition;}
	private final JeeslTableCellSelectHandler<IoMavenEeStandard,IoMavenEeEdition> tcsh;
	private final OpSingleSelectionHandler<IoMavenVersion> opVersion; public OpSingleSelectionHandler<IoMavenVersion> getOpVersion() {return opVersion;}
	private final JsonTuple1Handler<IoMavenVersion> thUsage; public JsonTuple1Handler<IoMavenVersion> getThUsage() {return thUsage;}
	
	private final Comparator<IoMavenEeReferral> cpReferral;
	private final Comparator<IoMavenUsage> cpUsage;
	
	private final Nested2Map<IoMavenEeStandard,IoMavenEeEdition,IoMavenEeReferral> n2m; public Nested2Map<IoMavenEeStandard,IoMavenEeEdition,IoMavenEeReferral> getN2m() {return n2m;}

	private final List<IoMavenEeStandard> eeStandards;  public List<IoMavenEeStandard> getEeStandards() {return eeStandards;}
	private final List<IoMavenEeReferral> referrals; public List<IoMavenEeReferral> getReferrals() {return referrals;}
	private final List<IoMavenUsage> usages; public List<IoMavenUsage> getUsages() {return usages;}

	private IoMavenEeReferral referral; public IoMavenEeReferral getReferral() {return referral;} public void setReferral(IoMavenEeReferral referral) {this.referral = referral;}
	private String clipboard; public String getClipboard() {return clipboard;}
	
	public JeeslIoMavenReferralWc(JeeslIoMavenReferralCallback<IoMavenType,IoMavenEeEdition> callback)
	{
		super(IoLang.class,IoDescription.class);
		this.callback=callback;
		
		sbhEdition = new SbMultiHandler<>(IoMavenEeEdition.class,this);
		tcsh = JeeslTableCellSelectHandler.instance(IoMavenEeStandard.class,IoMavenEeEdition.class);
		opVersion = OpSingleSelectionHandler.instance(this);
		thUsage = JsonTuple1Handler.instance(IoMavenVersion.class);
		
		cpReferral = new PositionComparator<>();
		cpUsage = EjbMavenUsageComparator.instance(EjbMavenUsageComparator.Type.version);
		
		n2m = new Nested2Map<>();
		
		eeStandards = new ArrayList<>();
		referrals = new ArrayList<>();
		usages = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<IoLocale> lp, JeeslFacesMessageBean bMessage,
							JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenType,IoMavenUsage,IoMavenEeReferral> fMaven)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fMaven=fMaven;
		
		tcsh.facade(fMaven);
		opVersion.lazyModel(new EjbIoMavenVersionLazyModel(fMaven));
		
		sbhEdition.setList(fMaven.allOrderedPositionVisible(IoMavenEeEdition.class));
		sbhEdition.preSelect(callback.getPreselectionEeEditions());
		
		logger.info(jogger.milestone(IoMavenEeEdition.class, "fMaven.allOrderedPositionVisible()", sbhEdition.getList().size()));
		
		eeStandards.addAll(fMaven.allOrderedPositionVisible(IoMavenEeStandard.class));
		logger.info(jogger.milestone(IoMavenEeEdition.class, "fMaven.allOrderedPositionVisible()", eeStandards.size()));
		
		this.reloadN2m();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		callback.setPreselectionEeEditions(sbhEdition.getSelected());
	}

	private void reset(boolean rReferrals, boolean rReferral, boolean rUsages)
	{
		if(rReferrals) {referrals.clear();}
		if(rReferral) {referral = null;}
		if(rUsages) {usages.clear();}
	}
	
	private void reloadN2m()
	{
		EjbIoMavenQuery q = EjbIoMavenQuery.instance();
		q.add(CqBool.isValue(true,CqBool.path(JeeslIoMavenEeReferral.Attributes.recommendation)));
		
		List<IoMavenEeReferral> list = fMaven.fIoMavenEeReferrals(q);
		n2m.replaceAll(EjbMavenReferralFactory.toN2mStanardEdition(list));
	}
	
	public void selectCell() throws JeeslNotFoundException 
	{
		tcsh.selectionTriggered();
		
		IoMavenEeStandard row = tcsh.getRow();
		IoMavenEeEdition column = tcsh.getColumn();
		logger.info(row.toString()+" "+column.toString());
		
		this.reset(true,true,true);
		referrals.addAll(fMaven.allForParent(IoMavenEeReferral.class,
											JeeslIoMavenEeReferral.Attributes.standard,row,
											JeeslIoMavenEeReferral.Attributes.edition,column));
		Collections.sort(referrals,cpReferral);
		if(ObjectUtils.isNotEmpty(referrals))
		{
			referral = referrals.get(0);
			this.selectReferral();
		}
		else
		{
			this.addReferral();
			referral.setEdition(column);
			referral.setStandard(row);
		}
		
		thUsage.clear();
		thUsage.load(fMaven.tpUsageByVersion(EjbIoMavenQuery.instance().addVersions(EjbMavenReferralFactory.toVersions(referrals))));
	}
	
	public void selectReferral() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(referral));}
		this.reset(false,false,true);
		this.reloadUsages();
		
		clipboard = TxtMavenVersionFactory.xmlMaven(referral.getArtifact());
	}
	
	public void addReferral() throws JeeslNotFoundException
	{
		IoMavenEeStandard previousStandard = null;
		IoMavenEeEdition previousEdition = null;
		if(Objects.nonNull(referral))
		{
			previousStandard = referral.getStandard();
			previousEdition = referral.getEdition();
		}
		
		this.reset(false,true,true);
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(IoMavenEeReferral.class));}
		referral = EjbMavenReferralFactory.build(previousEdition,previousStandard,referrals);
	}
	
	public void saveReferral() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(referral));
		EjbMavenReferralFactory.converter(fMaven,referral);
		referral = fMaven.save(referral);
		EjbIdFactory.integrate(referrals,referral);
	}
	
	public void deleteReferral() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.deleteEntity(referral));
		fMaven.rm(referral);
	}
	
	public void prepareSearch() {logger.info("Prepare Search");}
	@Override public void callbackOpSelection(OpSelectionHandler handler, IoMavenVersion ejb) throws JeeslLockingException, JeeslConstraintViolationException
	{
		referral.setArtifact(fMaven.find(IoMavenVersion.class,ejb));
	}
	
	public void reorderReferrals() throws JeeslConstraintViolationException, JeeslLockingException
	{
		AtomicInteger index = new AtomicInteger(1);
		for(IoMavenEeReferral r : referrals)
		{
			r.setPosition(index.getAndIncrement());
			r.setRecommendation(null);
		}
		fMaven.save(referrals);
		if(ObjectUtils.isNotEmpty(referrals))
		{
			IoMavenEeReferral r = referrals.get(0);
			EjbMavenReferralFactory.recommendation(r);
			fMaven.save(r);
		}
		if(Objects.nonNull(referral)) {referral = fMaven.find(IoMavenEeReferral.class,referral);}
		this.reloadN2m();
	}
	
	private void reloadUsages()
	{
		this.reset(false, false, true);
		EjbIoMavenQuery q = EjbIoMavenQuery.instance();
		q.addRootFetch(JeeslIoMavenUsage.Attributes.scopes);
		q.add(referral.getArtifact());
		q.addIoMavenTypes(callback.getTypes());
		
		usages.addAll(fMaven.fIoMavenUsages(q));
		Collections.sort(usages,cpUsage);
	}
}