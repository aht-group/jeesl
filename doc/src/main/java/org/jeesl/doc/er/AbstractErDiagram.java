package org.jeesl.doc.er;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.collections4.ListUtils;
import org.exlp.controller.handler.io.log.LoggedExit;
import org.exlp.interfaces.system.property.ConfigKey;
import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.api.rest.rs.jx.io.label.JeeslIoLabelRest;
import org.jeesl.api.rest.rs.jx.io.label.JeeslRevisionRestImport;
import org.jeesl.model.xml.io.label.Diagram;
import org.jeesl.model.xml.io.label.Diagrams;
import org.jeesl.model.xml.io.label.Entities;
import org.jeesl.util.query.xpath.StatusXpath;
import org.metachart.factory.xml.graph.XmlDotFactory;
import org.metachart.factory.xml.graph.XmlGraphFactory;
import org.metachart.model.xml.graph.Graph;
import org.metachart.processor.graph.ColorSchemeManager;
import org.metachart.processor.graph.Graph2DotConverter;
import org.metachart.processor.graph.GraphFileWriter;
import org.openfuxml.media.transcode.Svg2PdfTranscoder;
import org.openfuxml.renderer.latex.OfxMultiLangLatexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class AbstractErDiagram
{
	final static Logger logger = LoggerFactory.getLogger(AbstractErDiagram.class);

	protected final Path pCurrent;
	protected Path pXml;
	
	protected File fTmp;
	protected File fXmlEntities;
	protected File fSrc,fDot,fSvg;
	protected File dPdf,dAtt;

	private final List<Path> packageLocations;

	protected String colorScheme;

	protected String localeCode;
	private String dotGraph; public String getDotGraph() {return dotGraph;}
	private Entities entities;
	private boolean showErDiagramLabel;

	private JeeslRevisionRestImport restUpload; public void setRest(JeeslRevisionRestImport restUpload) {this.restUpload = restUpload;}
	private JeeslIoLabelRest restDownload; public void setRest(JeeslIoLabelRest restDownload) {this.restDownload = restDownload;}

	public AbstractErDiagram(org.exlp.interfaces.system.property.Configuration config)
	{
		localeCode = "en";
		fTmp = new File(config.getString(ConfigKey.dirTmp));
		showErDiagramLabel = true;
		logger.info("Using Tmp: "+fTmp);
		
		pCurrent = Paths.get("").toAbsolutePath();
		packageLocations = new ArrayList<>();
	}
	public AbstractErDiagram(Configuration config, OfxMultiLangLatexWriter ofxWriter)
	{
		localeCode = "en";
		fTmp = new File(config.getString(ConfigKey.dirTmp));
		showErDiagramLabel = true;
		logger.info("Using Tmp: "+fTmp);
		
		pCurrent = Paths.get("").toAbsolutePath();
		packageLocations = new ArrayList<>();
	}
	
	protected void addPackageLocation(Path location)
	{
		logger.info("Added {}",location);
		packageLocations.add(location);
	}

	protected void loadEntites(String path)
	{
		try
		{
			logger.info("Loading entities.xml "+path);
			entities = JaxbUtil.loadJAXB(path, Entities.class);
		}
		catch (FileNotFoundException e) {e.printStackTrace();}
	}
	protected void loadEntites(Path pXml)
	{
		if(!Files.exists(pXml))
		{
			logger.error("Path {} does not exist",pXml.toString());
			throw new RuntimeException("entities.xml missing");
		}
		try
		{
			logger.info("Loading entities.xml via {}",pXml);
			entities = JaxbUtil.loadJAXB(pXml.toFile(), Entities.class);
		}
		catch (FileNotFoundException e) {e.printStackTrace();}
	}

	public void create(String diagramCode, boolean upload) throws ClassNotFoundException, IOException, TranscoderException
	{
		create(diagramCode,diagramCode,upload);
	}
	private void create(String diagramCode, String diagramLabel, boolean upload) throws ClassNotFoundException, IOException, TranscoderException
	{
		List<String> subset = new ArrayList<String>();
		subset.add(diagramCode);
		
		File fPdf = null; if(dPdf!=null){fPdf = new File(dPdf,diagramCode+".pdf");}

		if(!showErDiagramLabel) {diagramLabel = null;}

		this.buildSvg("dot",diagramLabel,subset,new File(fSvg,diagramCode+".svg"),fPdf);

		if(upload && restUpload!=null)
		{
			Graph g = XmlGraphFactory.build(diagramCode);
			g.setDot(XmlDotFactory.build(dotGraph));
			JaxbUtil.info(g);
			if(Objects.nonNull(restUpload)) {restUpload.importSystemRevisionDiagram(g);}
		}
	}

	protected void buildSvg(String type, String label, List<String> subsets, File fDst, File fPdf) throws ClassNotFoundException, IOException, TranscoderException
	{
		Graph xColorScheme = JaxbUtil.loadJAXB(colorScheme, Graph.class);
		ColorSchemeManager csm = new ColorSchemeManager(xColorScheme,subsets);

		ErGraphProcessor egp = new ErGraphProcessor(fSrc, csm);
		if(entities!=null) {egp.activateEntities(localeCode,entities);}
		
		for(Path p : ListUtils.emptyIfNull(packageLocations)) {egp.addPackages(p, subsets);}
		egp.buildGraph();

		Graph g = egp.create();
		JaxbUtil.info(g); LoggedExit.exit(false);
		//JaxbUtil.trace(xml);

		Graph2DotConverter gdc = new Graph2DotConverter(csm);
		gdc.build(g,label);
		dotGraph = gdc.getDot();
		gdc.save(fDot);

		GraphFileWriter w = new GraphFileWriter(type);
		w.svg(fDot,fDst);

		if(Objects.nonNull(fPdf))
		{
			logger.info("SVG to PDF (via "+Svg2PdfTranscoder.class.getSimpleName()+")");
			Svg2PdfTranscoder.transcode(fDst,fPdf);
		}
	}

	public void buildDocumentation(String localeCode, boolean upload) throws ClassNotFoundException, IOException, TranscoderException
	{
		if(restDownload!=null)
		{
			Diagrams diagrams = restDownload.exportSystemRevisionDiagrams();
			JaxbUtil.info(diagrams);
			for(Diagram d : diagrams.getDiagram())
			{
				StringBuilder sb = new StringBuilder();
				try
				{
					sb.append(StatusXpath.getLang(d.getLangs(),localeCode).getTranslation());
				}
				catch (ExlpXpathNotFoundException | ExlpXpathNotUniqueException e) {e.printStackTrace();}
				create(d.getCode(),sb.toString(),upload);
			}
		}
	}

	public void showErDiagramLabel(boolean show) {showErDiagramLabel = show;}
}