package org.jeesl.doc.ofx.cms.module.workflow;

import java.io.File;
import java.io.IOException;

import org.jeesl.doc.ofx.cms.generic.AbstractJeeslOfxFactory;
import org.jeesl.factory.mc.graph.GraphWorkflowFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleManager;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElement;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.model.xml.module.workflow.Processes;
import org.jeesl.model.xml.module.workflow.Workflow;
import org.metachart.factory.xml.graph.XmlClusterFactory;
import org.metachart.factory.xml.graph.XmlClustersFactory;
import org.metachart.processor.graph.ColorSchemeManager;
import org.metachart.processor.graph.Graph2DotConverter;
import org.metachart.processor.graph.GraphFileWriter;
import org.metachart.xml.graph.Clusters;
import org.metachart.xml.graph.Graph;
import org.openfuxml.content.media.Image;
import org.openfuxml.content.ofx.Section;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.list.XmlListFactory;
import org.openfuxml.factory.xml.media.XmlImageFactory;
import org.openfuxml.factory.xml.media.XmlMediaFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.interfaces.configuration.OfxTranslationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Context;
import net.sf.ahtutils.xml.status.Contexts;
import net.sf.exlp.util.xml.JaxbUtil;

public class OfxSectionWorkflow <L extends JeeslLang, LOC extends JeeslLocale<L,?,LOC,?>,
								E extends JeeslIoCmsElement<?,?,?,?,?,?>>
							extends AbstractJeeslOfxFactory<L,LOC>
{
	final static Logger logger = LoggerFactory.getLogger(OfxSectionWorkflow.class);
	
	private final OfxTableWorkflowProcess<L,LOC> ofxTableProcess;
	
	private GraphWorkflowFactory gfWorkflow;
	private Graph2DotConverter dgf;
	private GraphFileWriter gfw;
	
	public OfxSectionWorkflow(OfxTranslationProvider tp)
	{
		super(tp);
		ofxTableProcess = new OfxTableWorkflowProcess<>(tp);
		
		gfWorkflow = new GraphWorkflowFactory("en");
		dgf = new Graph2DotConverter(new ColorSchemeManager());
		gfw = new GraphFileWriter("dot");
	}
	
	public Section build(JeeslLocaleManager<LOC> lp, E element) throws OfxAuthoringException
	{
//		try
//		{
//			ResteasyClient client = new ResteasyClientBuilder().build();
////			client.register(new BasicAuthentication(restUser, restPwd));
//			ResteasyWebTarget restTarget = client.target(element.getJson());
//			
//			Workflow workflow = JaxbUtil.loadJAXB("/Volumes/ramdisk/workflow.xml",Workflow.class);
//			return build(lp,workflow);
//		}
//		catch (FileNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		return null;
	}

	public Section build(JeeslLocaleManager<LOC> lp, Workflow workflow)
	{
		
		Section xml = XmlSectionFactory.build();
		xml.setContainer(true);
		xml.getContent().add(contextList(lp,workflow.getContexts()));
		
		for(Context context : workflow.getContexts().getContext())
		{
			xml.getContent().add(build(lp,context,workflow.getProcesses()));
		}
		
		return xml;
	}
	
	public org.openfuxml.content.list.List contextList(JeeslLocaleManager<LOC> lp, Contexts contexts)
	{

		org.openfuxml.content.list.List list = XmlListFactory.description();
		
		logger.info("Building for"+contexts.getContext().size());
		for(Context context : contexts.getContext())
		{
			list.getItem().addAll(ofxMultiLocale.listDescription(lp, context.getLangs(), context.getDescriptions()));
		}
		
		return list;
	}
	
	private Section build(JeeslLocaleManager<LOC> lp, Context context, Processes processes)
	{
		Section xml = XmlSectionFactory.build();
		xml.getContent().addAll(ofxMultiLocale.titles(lp, context.getLangs()));
		xml.getContent().addAll(ofxMultiLocale.paragraphs(lp,context.getDescriptions(),false));
		for(org.jeesl.model.xml.module.workflow.Process process : processes.getProcess())
		{
			if(process.getContext().getCode().equals(context.getCode()))
			{
				xml.getContent().add(build(lp,process));
			}
		}
		return xml;
	}
	
	private Section build(JeeslLocaleManager<LOC> lp, org.jeesl.model.xml.module.workflow.Process process)
	{
		Section sub = XmlSectionFactory.build();
		sub.getContent().addAll(ofxMultiLocale.titles(lp, process.getLangs()));
		sub.getContent().add(ofxTableProcess.build(lp,process));
		sub.getContent().addAll(ofxMultiLocale.paragraphs(lp,process.getDescriptions(),false));
		sub.getContent().add(graph(lp,process));
		return sub;
	}
	
	private Image graph(JeeslLocaleManager<LOC> lp, org.jeesl.model.xml.module.workflow.Process process)
	{
		logger.trace("Building Image ");
		Image image = XmlImageFactory.centerPercent(process.getId(), 80);
		image.setTitle(ofxMultiLocale.title(lp,process.getLangs()));
		image.setMedia(XmlMediaFactory.build(process.getId()+".pdf",process.getId()+".pdf"));
//		JaxbUtil.info(image);System.exit(1);
		
		Graph g = gfWorkflow.build(process);
		
		Clusters clusters = XmlClustersFactory.build();
		clusters.getCluster().add(XmlClusterFactory.build("test", "Simple Test"));
		
		JaxbUtil.info(g);
		
		File fSrc = new File("/Volumes/ramdisk/dot.dot");
		File fDir = new File("/Volumes/ramdisk/dev/srs/png");
		File fDst = new File(fDir,process.getId()+".pdf");
		dgf.build(g);
		dgf.save(fSrc);
		try
		{
			gfw.pdf(fSrc,fDst);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
}