package org.jeesl.controller.web.c.io.maven;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.exlp.util.jx.JsfUtil;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoGraphicFacade;
import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.io.maven.EjbMavenModuleFactory;
import org.jeesl.factory.ejb.io.maven.ee.EjbMavenReferralFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenModule;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicComponent;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicShape;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicType;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.locale.IoStatus;
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
import org.jeesl.util.query.ejb.io.maven.EjbIoMavenQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslIoMavenReferralWc extends AbstractJeeslLocaleWebController<IoLang,IoDescription,IoLocale>
									implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenReferralWc.class);
	
	private JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenUsage> fMaven;

	private final List<IoMavenEeEdition> eeEditions;  public List<IoMavenEeEdition> getEeEditions() {return eeEditions;}
	private final List<IoMavenEeStandard> eeStandards;  public List<IoMavenEeStandard> getEeStandards() {return eeStandards;}

	private final List<IoMavenEeReferral> referrals; public List<IoMavenEeReferral> getReferrals() {return referrals;}
	
	private IoMavenEeReferral referral; public IoMavenEeReferral getReferral() {return referral;} public void setReferral(IoMavenEeReferral referral) {this.referral = referral;}
	
	public JeeslIoMavenReferralWc()
	{
		super(IoLang.class,IoDescription.class);
		
		eeEditions = new ArrayList<>();
		eeStandards = new ArrayList<>();
		referrals = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<IoLocale> lp, JeeslFacesMessageBean bMessage,
							JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenUsage> fMaven)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fMaven=fMaven;
		
		eeEditions.addAll(fMaven.allOrderedPositionVisible(IoMavenEeEdition.class));
		logger.info(jogger.milestone(IoMavenEeEdition.class, "fMaven.allOrderedPositionVisible()", eeEditions.size()));
		
		eeStandards.addAll(fMaven.allOrderedPositionVisible(IoMavenEeStandard.class));
		logger.info(jogger.milestone(IoMavenEeEdition.class, "fMaven.allOrderedPositionVisible()", eeStandards.size()));

		this.reloadReferrals();
	}

	
	private void reset(boolean rReferral)
	{
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
		this.reset(false);
	}
	
	public void addReferral() throws JeeslNotFoundException
	{
		this.reset(true);
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(IoMavenEeReferral.class));}
		referral = EjbMavenReferralFactory.build();
	}
	
	public void saveReferral() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(referral));}
		EjbMavenReferralFactory.converter(fMaven,referral);
		referral = fMaven.save(referral);
		EjbIdFactory.integrate(referrals,referral);
	}
}