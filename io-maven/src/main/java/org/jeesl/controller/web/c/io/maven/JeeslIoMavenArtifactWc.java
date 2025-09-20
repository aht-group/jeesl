package org.jeesl.controller.web.c.io.maven;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.controller.converter.fc.io.maven.dependency.IoMavenArtifactConverter;
import org.jeesl.controller.converter.fc.io.maven.dependency.IoMavenVersionConverter;
import org.jeesl.controller.handler.lazy.io.maven.EjbIoMavenArtifactLazyModel;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.controller.processor.io.maven.MavenMetachartGraphProcessor;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.io.maven.EjbMavenDependencyFactory;
import org.jeesl.factory.ejb.io.maven.EjbMavenUsageFactory;
import org.jeesl.factory.ejb.io.maven.EjbMavenVersionFactory;
import org.jeesl.factory.ejb.io.maven.ee.EjbMavenReferralFactory;
import org.jeesl.factory.txt.io.maven.TxtMavenVersionFactory;
import org.jeesl.interfaces.bean.op.OpSingleSelectionBean;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.op.OpSelectionHandler;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenUsage;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.op.OpSingleSelectionHandler;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenDependency;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenScope;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenSuitability;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeEdition;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeReferral;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeStandard;
import org.jeesl.model.ejb.io.maven.module.IoMavenJdk;
import org.jeesl.model.ejb.io.maven.module.IoMavenModule;
import org.jeesl.model.ejb.io.maven.module.IoMavenStructure;
import org.jeesl.model.ejb.io.maven.module.IoMavenType;
import org.jeesl.model.ejb.io.maven.usage.IoMavenUsage;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.jeesl.util.comparator.ejb.PositionComparator;
import org.jeesl.util.comparator.ejb.io.maven.EjbMavenArtifactComparator;
import org.jeesl.util.query.ejb.io.maven.EjbIoMavenQuery;
import org.metachart.factory.json.chart.echart.js.graph.JsonEchartGraphFactory;
import org.metachart.interfaces.data.EchartGraphDataProvider;
import org.metachart.model.json.graph.mc.JsonGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoMavenArtifactWc extends AbstractJeeslLocaleWebController<IoLang,IoDescription,IoLocale>
									implements OpSingleSelectionBean<IoMavenArtifact>,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenArtifactWc.class);
	
	private JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenType,IoMavenUsage,IoMavenEeReferral> fMaven;

	private final OpSingleSelectionHandler<IoMavenArtifact> opArtifact; public OpSingleSelectionHandler<IoMavenArtifact> getOpArtifact() {return opArtifact;}
	
	private EchartGraphDataProvider graph; public EchartGraphDataProvider getGraph() {return graph;}
	
	private final Comparator<IoMavenArtifact> cpArtifact;
	private final Comparator<IoMavenVersion> cpVersion;
	
	private final Nested2Map<IoMavenVersion,IoMavenEeStandard,IoMavenEeReferral> n2m; public Nested2Map<IoMavenVersion,IoMavenEeStandard,IoMavenEeReferral> getN2m() {return n2m;}
	
	private final Map<IoMavenArtifact,List<IoMavenVersion>> mapVersion; public Map<IoMavenArtifact, List<IoMavenVersion>> getMapVersion() {return mapVersion;}
	private final Map<IoMavenVersion,List<IoMavenModule>> mapRoot; public Map<IoMavenVersion,List<IoMavenModule>> getMapRoot() {return mapRoot;}
	private final Map<IoMavenVersion,Map<IoMavenModule,List<IoMavenUsage>>> mapUsage; public Map<IoMavenVersion,Map<IoMavenModule,List<IoMavenUsage>>> getMapUsage() {return mapUsage;}
	private final Map<IoMavenVersion,List<IoMavenVersion>> mapParent; public Map<IoMavenVersion, List<IoMavenVersion>> getMapParent() {return mapParent;}
	
	private final List<IoMavenArtifact> artifacts; public List<IoMavenArtifact> getArtifacts() {return artifacts;}
	private final List<IoMavenArtifact> replacements; public List<IoMavenArtifact> getReplacements() {return replacements;}
	private final List<IoMavenVersion> versions; public List<IoMavenVersion> getVersions() {return versions;}
	private final List<IoMavenSuitability> suitabilities; public List<IoMavenSuitability> getSuitabilities() {return suitabilities;}
	private final List<IoMavenOutdate> outdates; public List<IoMavenOutdate> getOutdates() {return outdates;}
	private final List<IoMavenMaintainer> maintainers; public List<IoMavenMaintainer> getMaintainers() {return maintainers;}
	private final List<IoMavenDependency> dependencies; public List<IoMavenDependency> getDependencies() {return dependencies;}
	private final List<IoMavenEeEdition> eeEditions; public List<IoMavenEeEdition> getEeEditions() {return eeEditions;}
	private final List<IoMavenEeStandard> eeStandards; public List<IoMavenEeStandard> getEeStandards() {return eeStandards;}
	private final List<IoMavenJdk> compilers; public List<IoMavenJdk> getCompilers() {return compilers;}
	
	private IoMavenArtifact artifact; public IoMavenArtifact getArtifact() {return artifact;} public void setArtifact(IoMavenArtifact artifact) {this.artifact = artifact;}
	private IoMavenVersion version; public IoMavenVersion getVersion() {return version;} public void setVersion(IoMavenVersion version) {this.version = version;}
	private IoMavenDependency dependency; public IoMavenDependency getDependency() {return dependency;} public void setDependency(IoMavenDependency dependency) {this.dependency = dependency;}

	private String fvGroup; public String getFvGroup() {return fvGroup;} public void setFvGroup(String fvGroup) {this.fvGroup = fvGroup;}
	private String fvArtifact; public String getFvArtifact() {return fvArtifact;} public void setFvArtifact(String fvArtifact) {this.fvArtifact = fvArtifact;}
	private String clipboard; public String getClipboard() {return clipboard;}

	public JeeslIoMavenArtifactWc()
	{
		super(IoLang.class,IoDescription.class);
		
		opArtifact = OpSingleSelectionHandler.instance(this);
		
		cpArtifact = EjbMavenArtifactComparator.instance(EjbMavenArtifactComparator.Type.code);
		cpVersion = new PositionComparator<>();
		
		n2m = new Nested2Map<>();
		
		mapVersion = new HashMap<>();
		mapRoot = new HashMap<>();
		mapUsage = new HashMap<>();
		mapParent = new HashMap<>();
		
		artifacts = new ArrayList<>();
		replacements = new ArrayList<>();
		versions = new ArrayList<>();
		suitabilities = new ArrayList<>();
		outdates = new ArrayList<>();
		maintainers = new ArrayList<>();
		dependencies = new ArrayList<>();
		
		eeEditions = new ArrayList<>();
		eeStandards = new ArrayList<>();
		compilers = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<IoLocale> lp, JeeslFacesMessageBean bMessage,
							JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenType,IoMavenUsage,IoMavenEeReferral> fMaven)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fMaven=fMaven;
		
		opArtifact.lazyModel(new EjbIoMavenArtifactLazyModel(fMaven));
		
		suitabilities.addAll(fMaven.allOrderedPositionVisible(IoMavenSuitability.class));
		outdates.addAll(fMaven.allOrderedPositionVisible(IoMavenOutdate.class));
		maintainers.addAll(fMaven.allOrderedPositionVisible(IoMavenMaintainer.class));
		compilers.addAll(fMaven.allOrderedPositionVisible(IoMavenJdk.class));
		
		this.reloadArtifacts();
	}
	
	@Override
	public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(debugOnInfo){logger.info(SbMultiHandler.class.getSimpleName()+" toggled, but NYI");}
	}
	
	public void prepareSearch() {logger.info("Prepare Search");}
	@Override public void callbackOpSelection(OpSelectionHandler handler, IoMavenArtifact ejb) throws JeeslLockingException, JeeslConstraintViolationException
	{
		logger.info("Selected "+ejb);
//		referral.setArtifact(fMaven.find(IoMavenVersion.class,ejb));
	}
	
	public void cancelVersion() {this.reset(false,true,true,false);}
	private void reset(boolean rVersions, boolean rVersion, boolean rDependencies, boolean rEe)
	{
		if(rVersions) {versions.clear();}
		if(rVersion) {version=null;}
		if(rDependencies) {dependencies.clear(); mapParent.clear();}
		if(rEe) {eeEditions.clear(); eeStandards.clear();}
	}
	
	private void reloadArtifacts()
	{
		EjbIoMavenQuery q = EjbIoMavenQuery.instance();
		q.setTupleLoad(true);
		
		JsonTuple1Handler<IoMavenVersion> th = new JsonTuple1Handler<>(IoMavenVersion.class);
		th.init(fMaven.tpUsageByVersion(q));
		
		List<IoMavenVersion> list = th.getListA();
		Collections.sort(list,cpVersion);
		mapVersion.clear();
		mapVersion.putAll(EjbMavenVersionFactory.toMapArtifactVersion(list));
		
		artifacts.clear();
		artifacts.addAll(mapVersion.keySet());
		Collections.sort(artifacts,cpArtifact);
		logger.info(AbstractLogMessage.reloaded(IoMavenArtifact.class,artifacts));
	}
	
	public void selectArtifact()
	{
		this.reset(true,true,true,true);
		logger.info(AbstractLogMessage.selectEntity(artifact));
		artifact = fMaven.find(IoMavenArtifact.class,artifact);
		this.reloadVersions();
		this.reloadUsages();
		
		List<IoMavenEeReferral> referrals = fMaven.fIoMavenEeReferrals(EjbIoMavenQuery.instance().add(artifact));
		eeEditions.addAll(EjbMavenReferralFactory.toEeEditions(referrals));
		eeStandards.addAll(EjbMavenReferralFactory.toEeStandards(referrals));
		n2m.replaceAll(EjbMavenReferralFactory.toN2mVersionStanard(referrals));
		
		if(Objects.nonNull(artifact.getReplacedBy()) && !replacements.contains(artifact.getReplacedBy()))
		{
			replacements.add(artifact.getReplacedBy());
		}
	}
	
	public void updateSuitability() {IoMavenArtifactConverter.jsfSelectOne(fMaven, artifact);}
	public void magnetArtifact()
	{
		IoMavenArtifactConverter.jsfSelectOne(fMaven, artifact);
		if(!replacements.contains(artifact))
		{
			replacements.add(artifact);
		}
	}
	
	public void saveArtifact() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		IoMavenArtifactConverter.jsfSelectOne(fMaven, artifact);
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(artifact));}
		artifact = fMaven.save(artifact);
		
		this.reloadArtifacts();
	}
	
	private void reloadVersions()
	{
		this.reset(true,false,false,false);
		versions.addAll(fMaven.allForParent(IoMavenVersion.class,artifact));
	}
	
	public void selectVersion() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(version));}
		this.reset(false,false,true,false);
		this.reloadDependencies();
		
		clipboard = TxtMavenVersionFactory.xmlMaven(version);
	}
	
	public void saveVersion() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(version));}
		IoMavenVersionConverter.jsfSelectOne(fMaven, version);
		version = fMaven.save(version);
		
		this.reloadVersions();
		mapVersion.remove(version.getArtifact());
		mapVersion.putAll(EjbMavenVersionFactory.toMapArtifactVersion(versions));
	}
	
	public void reorderVersions() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fMaven,versions); this.reloadVersions();}
	
	private void reloadUsages()
	{
		mapRoot.clear();
		mapUsage.clear();
		
		List<IoMavenUsage> usages = fMaven.fIoMavenUsages(EjbIoMavenQuery.instance().addVersions(versions).addRootFetch(JeeslIoMavenUsage.Attributes.scopes));
		mapRoot.putAll(EjbMavenUsageFactory.toMapVersionRootModules(usages));
		mapUsage.putAll(EjbMavenUsageFactory.toMapVersionModuleUsage(usages));
	}
	
	private void reloadDependencies()
	{
		this.reset(false,false,true,false);
		dependencies.addAll(fMaven.fIoMavenDependencies(EjbIoMavenQuery.instance().add(version)));

		List<IoMavenVersion> dependenciesDependOn = EjbMavenDependencyFactory.toListDependsOn(dependencies);
		List<IoMavenDependency> parents = fMaven.fIoMavenDependencies(EjbIoMavenQuery.instance().addVersions(dependenciesDependOn));
		
		mapParent.putAll(EjbMavenDependencyFactory.toMapDependsOn(parents));
		
		logger.info(IoMavenDependency.class.getSimpleName()+": "+dependencies.size());
		for(IoMavenVersion v : dependenciesDependOn) {logger.info("\t"+v.getArtifact().getCode());}
		
		logger.info(IoMavenVersion.class.getSimpleName()+": "+dependenciesDependOn.size());
		logger.info("Parents "+parents.size());
		
		MavenMetachartGraphProcessor mmgp = MavenMetachartGraphProcessor.instance(fMaven);
		mmgp.dependenciesOf(version);
		JsonGraph g = mmgp.toMetachartGraph();
		graph = JsonEchartGraphFactory.instance().transform(g);
	}
	
	public void addModulePath()
	{
		this.reset(false,false,true,false);
		dependencies.addAll(fMaven.fIoMavenDependencies(EjbIoMavenQuery.instance().add(version)));

		List<IoMavenVersion> dependenciesDependOn = EjbMavenDependencyFactory.toListDependsOn(dependencies);
		List<IoMavenDependency> parents = fMaven.fIoMavenDependencies(EjbIoMavenQuery.instance().addVersions(dependenciesDependOn));
		
		mapParent.putAll(EjbMavenDependencyFactory.toMapDependsOn(parents));
		
		logger.info(IoMavenDependency.class.getSimpleName()+": "+dependencies.size());
		for(IoMavenVersion v : dependenciesDependOn) {logger.info("\t"+v.getArtifact().getCode());}
		
		logger.info(IoMavenVersion.class.getSimpleName()+": "+dependenciesDependOn.size());
		logger.info("Parents "+parents.size());
		
		MavenMetachartGraphProcessor mmgp = MavenMetachartGraphProcessor.instance(fMaven);
		mmgp.dependenciesOf(version);
		mmgp.addUsagePath(version);
		JsonGraph g = mmgp.toMetachartGraph();
		graph = JsonEchartGraphFactory.instance().transform(g);
	}
}