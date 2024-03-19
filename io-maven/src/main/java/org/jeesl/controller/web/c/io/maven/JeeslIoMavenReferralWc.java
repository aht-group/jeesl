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
import org.jeesl.controller.lazy.io.maven.EjbIoMavenVersionLazyModel;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.io.maven.ee.EjbMavenReferralFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.bean.op.OpSingleSelectionBean;
import org.jeesl.interfaces.controller.handler.op.OpSelectionHandler;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.maven.ee.JeeslMavenEeReferral;
import org.jeesl.jsf.handler.op.OpSingleSelectionHandler;
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
import org.jeesl.model.ejb.io.maven.module.IoMavenUsage;
import org.jeesl.util.comparator.ejb.PositionComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslIoMavenReferralWc extends AbstractJeeslLocaleWebController<IoLang,IoDescription,IoLocale>
									implements OpSingleSelectionBean<IoMavenVersion>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenReferralWc.class);
	
	private JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenUsage,IoMavenEeReferral> fMaven;

	private final JeeslTableCellSelectHandler<IoMavenEeStandard,IoMavenEeEdition> tcsh;
	private final OpSingleSelectionHandler<IoMavenVersion> opVersion; public OpSingleSelectionHandler<IoMavenVersion> getOpVersion() {return opVersion;}
	
	private final Comparator<IoMavenEeReferral> cpReferral;
	
	private final List<IoMavenEeEdition> eeEditions;  public List<IoMavenEeEdition> getEeEditions() {return eeEditions;}
	private final List<IoMavenEeStandard> eeStandards;  public List<IoMavenEeStandard> getEeStandards() {return eeStandards;}

	private final List<IoMavenEeReferral> referrals; public List<IoMavenEeReferral> getReferrals() {return referrals;}
	
	private IoMavenEeReferral referral; public IoMavenEeReferral getReferral() {return referral;} public void setReferral(IoMavenEeReferral referral) {this.referral = referral;}
	
	public JeeslIoMavenReferralWc()
	{
		super(IoLang.class,IoDescription.class);
		
		tcsh = JeeslTableCellSelectHandler.instance(IoMavenEeStandard.class,IoMavenEeEdition.class);
		opVersion = OpSingleSelectionHandler.instance(this);
		
		cpReferral = new PositionComparator<>();
		
		eeEditions = new ArrayList<>();
		eeStandards = new ArrayList<>();
		referrals = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<IoLocale> lp, JeeslFacesMessageBean bMessage,
							JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenUsage,IoMavenEeReferral> fMaven)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fMaven=fMaven;
		
		tcsh.facade(fMaven);
		opVersion.lazyModel(new EjbIoMavenVersionLazyModel(fMaven));
		
		eeEditions.add(fMaven.fByEnum(IoMavenEeEdition.class,IoMavenEeEdition.Code.ee8));
		eeEditions.add(fMaven.fByEnum(IoMavenEeEdition.class,IoMavenEeEdition.Code.e10));
		eeEditions.add(fMaven.fByEnum(IoMavenEeEdition.class,IoMavenEeEdition.Code.ee10));
		logger.info(jogger.milestone(IoMavenEeEdition.class, "fMaven.allOrderedPositionVisible()", eeEditions.size()));
		
		eeStandards.addAll(fMaven.allOrderedPositionVisible(IoMavenEeStandard.class));
		logger.info(jogger.milestone(IoMavenEeEdition.class, "fMaven.allOrderedPositionVisible()", eeStandards.size()));

		this.reloadReferrals();
	}

	
	private void reset(boolean rReferrals, boolean rReferral)
	{
		if(rReferrals) {referrals.clear();}
		if(rReferral) {referral = null;}
	}
	
	private void reloadReferrals()
	{
		referrals.clear();
		referrals.addAll(fMaven.all(IoMavenEeReferral.class));
//		Collections.sort(referral,new PositionComparator<IoMavenModule>());
	}
	
	public void selectReferral() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(referral));}
		this.reset(false,false);
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
		
		this.reset(false,true);
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(IoMavenEeReferral.class));}
		referral = EjbMavenReferralFactory.build(previousEdition,previousStandard,referrals);
	}
	
	public void selectCell() throws JeeslNotFoundException 
	{
		tcsh.selectionTriggered();
		
		IoMavenEeStandard row = tcsh.getRow();
		IoMavenEeEdition column = tcsh.getColumn();
		logger.info(row.toString()+" "+column.toString());
		
		this.reset(true,true);
		referrals.addAll(fMaven.allForParent(IoMavenEeReferral.class,
											JeeslMavenEeReferral.Attributes.standard,row,
											JeeslMavenEeReferral.Attributes.edition,column));
		Collections.sort(referrals,cpReferral);
		if(ObjectUtils.isNotEmpty(referrals)) {referral = referrals.get(0);}
		else
		{
			this.addReferral();
			referral.setEdition(column);
			referral.setStandard(row);
		}
	}
	
	public void saveReferral() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(referral));
		EjbMavenReferralFactory.converter(fMaven,referral);
		referral = fMaven.save(referral);
		EjbIdFactory.integrate(referrals,referral);
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
			EjbMavenReferralFactory.recommendation(r);
		}
		fMaven.update(referrals);
		if(Objects.nonNull(referral)) {referral = fMaven.find(IoMavenEeReferral.class,referral);}
	}
}