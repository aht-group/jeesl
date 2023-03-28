package org.jeesl.controller.handler.rest.io.maven;

import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.api.rest.i.io.maven.JeeslIoMavenRestInterface;
import org.jeesl.controller.monitoring.counter.DataUpdateTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.json.io.maven.JsonMavenArtifact;
import org.jeesl.model.json.io.maven.JsonMavenGraph;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JeeslIoMavenRestHandler implements JeeslIoMavenRestInterface
{
	public static final long serialVersionUID=1;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenRestHandler.class);
	
	private final JeeslIoMavenFacade<IoLang,IoDescription,IoMavenGroup,IoMavenArtifact> fMaven;
	
	public JeeslIoMavenRestHandler(JeeslIoMavenFacade<IoLang,IoDescription,IoMavenGroup,IoMavenArtifact> fMaven)
	{
		this.fMaven=fMaven;
	}
	
	@Override public JsonSsiUpdate uploadDependencyGraph(JsonMavenGraph graph)
	{
		DataUpdateTracker dut = DataUpdateTracker.instance();
		
		for(JsonMavenArtifact json : graph.getArtifacts())
		{
			logger.info(json.getGroupId()+":"+json.getArtifactId());
			try
			{
				IoMavenGroup group = null;
				try {group = fMaven.fByCode(IoMavenGroup.class, json.getGroupId());}
				catch (JeeslNotFoundException e)
				{
					group = new IoMavenGroup();
					group.setCode(json.getGroupId());
					group = fMaven.save(group);
				}
				
				IoMavenArtifact artifact = null;
				try {artifact = fMaven.fIoMavenArtifact(group, json.getArtifactId());}
				catch (JeeslNotFoundException e)
				{
					artifact = new IoMavenArtifact();
					artifact.setGroup(group);
					artifact.setCode(json.getArtifactId());
					artifact = fMaven.save(artifact);
				}
			}
			catch (JeeslConstraintViolationException | JeeslLockingException e1)
			{
				dut.createFail(JeeslIoMavenRestHandler.class, e1);
			}
			
		}
		
		return dut.toJson();
	}
}