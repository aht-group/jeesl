package org.jeesl.controller.web.io.maven;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoLocaleFactoryBuilder;
import org.jeesl.factory.ejb.io.maven.EjbMavenModuleFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicType;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.usage.IoMavenModule;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslIoMavenModuleWc extends AbstractJeeslWebController<IoLang,IoDescription,IoLocale>
		implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenModuleWc.class);
	
	private JeeslIoMavenFacade<IoLang,IoDescription,IoMavenGroup,IoMavenArtifact,IoMavenVersion> fMaven;

	private final List<IoMavenModule> developments; public List<IoMavenModule> getDevelopments() {return developments;}

	private IoMavenModule development; public IoMavenModule getDevelopment() {return development;} public void setDevelopment(IoMavenModule development) {this.development = development;}
	
	public JeeslIoMavenModuleWc()
	{
		super(IoLang.class,IoDescription.class);
		
		developments = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<IoLocale> lp, JeeslFacesMessageBean bMessage,
							JeeslIoMavenFacade<IoLang,IoDescription,IoMavenGroup,IoMavenArtifact,IoMavenVersion> fMaven)
	{
		super.postConstructWebController(lp,bMessage);
		this.fMaven=fMaven;

		this.reloadDevelopments();
	}
	
	private void reloadDevelopments()
	{
		developments.clear();
		developments.addAll(fMaven.all(IoMavenModule.class));
	}
	
	@Override
	public void toggled(Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(debugOnInfo){logger.info(SbMultiHandler.class.getSimpleName()+" toggled, but NYI");}
	}
	
	public void selectDevelopment() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(development));}
	}
	
	public void addDevelopment() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(IoMavenModule.class));}
		development = EjbMavenModuleFactory.build();
	}
	
	public void saveDevelopment() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(development));}
		development = fMaven.save(development);
		
		this.reloadDevelopments();
	}
	
	public void handleFileUpload(FileUploadEvent event) throws JeeslNotFoundException
	{
		UploadedFile file = event.getFile();
		logger.info("Received file with a size of " +file.getSize());
		if(development.getGraphic()==null)
		{
			IoGraphicType type = fMaven.fByCode(IoGraphicType.class,JeeslGraphicType.Code.svg);
			development.setGraphic(IoLocaleFactoryBuilder.svg().efGraphic().build(type));
		}
		development.getGraphic().setData(file.getContent());
	}
}