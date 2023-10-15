package org.jeesl.controller.web.c.io.maven;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenScope;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.font.IoMavenFont;
import org.jeesl.model.ejb.io.maven.font.IoMavenFontUsage;
import org.jeesl.model.ejb.io.maven.module.IoMavenModule;
import org.jeesl.model.ejb.io.maven.module.IoMavenStructure;
import org.jeesl.model.ejb.io.maven.module.IoMavenUsage;
import org.jeesl.model.ejb.io.ssi.core.IoSsiHost;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoMavenFontWc extends AbstractJeeslWebController<IoLang,IoDescription,IoLocale>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenFontWc.class);
	
	private JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenUsage> fMaven;

	private final Nested2Map<IoMavenFont,IoSsiHost,IoMavenFontUsage> n2m; public Nested2Map<IoMavenFont, IoSsiHost, IoMavenFontUsage> getN2m() {return n2m;}
	
	private final List<IoMavenFont> fonts; public List<IoMavenFont> getFonts() {return fonts;}
	private final List<IoSsiHost> hosts; public List<IoSsiHost> getHosts() {return hosts;}
	
	public JeeslIoMavenFontWc()
	{
		super(IoLang.class,IoDescription.class);
		
		n2m = new Nested2Map<>();
		
		fonts = new ArrayList<>();
		hosts = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<IoLocale> lp, JeeslFacesMessageBean bMessage,
							JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenUsage> fMaven)
	{
		super.postConstructWebController(lp,bMessage);
		this.fMaven=fMaven;
		
		List<IoMavenFontUsage> usages = fMaven.all(IoMavenFontUsage.class);
		logger.info(IoMavenFontUsage.class.getSimpleName()+": "+usages.size());
		
		for(IoMavenFontUsage u : usages)
		{
			n2m.put(u.getFont(),u.getHost(), u);
		}

		fonts.addAll(n2m.toL1());
		hosts.addAll(n2m.toL2());
	}
	

}