package org.jeesl.web.mbean.system;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.jsf.filter.UtilsStatusFilter;

public class AbstractIconBean implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(AbstractIconBean.class);
	private static final long serialVersionUID = 1L;

	private String imagePath;

	private Map<String,Map<Long,String>> mapImages,mapResource;
	private Map<String,Map<Long,String>> mapImagesAlt,mapResourceAlternative;
	protected Map<String,String> mapStatic;

	protected Map<String,String> svg; public Map<String, String> getSvg() {return svg;}

	private Map<Integer,Map<String,String>> icon; public Map<Integer,Map<String,String>> getIcon() {return icon;}
	public Map<String,String> getIcon12() {return icon.get(12);}
	public Map<String,String> getIcon16() {return icon.get(16);}

	public void initPath(String imagePath)
    {
		this.imagePath=imagePath;
		mapImages = new Hashtable<String,Map<Long,String>>();
		mapImagesAlt = new Hashtable<String,Map<Long,String>>();
		mapResource = new Hashtable<String,Map<Long,String>>();
		mapResourceAlternative = new Hashtable<String,Map<Long,String>>();

		mapStatic = new HashMap<String,String>();
		svg = new HashMap<String,String>();

		icon = new Hashtable<Integer,Map<String,String>>();
    }

    @Deprecated
    public <L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>>
    String urlFilter(Integer size, UtilsStatusFilter<L,D,S> filter)
    {
//    	logger.info("URL for "+filter.getValue().getCode()+" active="+filter.isActive());
    	if(filter.isActive()){return url(size,filter.getValue());}
    	else
    	{
    		String key = filter.getValue().getClass().getSimpleName();
        	if(!mapImagesAlt.containsKey(key)){generate(mapImagesAlt,size, filter.getValue().getId(), filter.getValue().getImageAlt(), key);}
        	else
        	{
        		if(!mapImagesAlt.get(key).containsKey(filter.getValue().getId())){generate(mapImagesAlt,size, filter.getValue().getId(), filter.getValue().getImageAlt(), key);}
        	}

    		return mapImagesAlt.get(key).get(filter.getValue().getId());
    	}
    }

    @Deprecated
    public <L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>>
    String filter(Integer size, UtilsStatusFilter<L,D,S> filter)
    {
//    	logger.info("Filter for "+filter.getValue().getCode()+" active="+filter.isActive());
    	if(filter.isActive()){return resource(size,filter.getValue());}
    	else
    	{
    		String key = filter.getValue().getClass().getSimpleName();
        	if(!mapResourceAlternative.containsKey(key))
        	{
        		generateResource(mapResourceAlternative,size, filter.getValue().getId(), filter.getValue().getImageAlt(), key);
        	}
        	else if(!mapResourceAlternative.get(key).containsKey(filter.getValue().getId()))
    		{
    			generateResource(mapResourceAlternative,size, filter.getValue().getId(), filter.getValue().getImageAlt(), key);
    		}
    		return mapResourceAlternative.get(key).get(filter.getValue().getId());
    	}
    }

    @Deprecated
    public String resource(Integer size, EjbWithImage image)
	{
    	String key = image.getClass().getSimpleName();
    	if(!mapResource.containsKey(key))
    	{
    		generateResource(mapResource, size, image.getId(), image.getImage(), key);
    	}
    	else if(!mapResource.get(key).containsKey(image.getId()))
    	{
    		generateResource(mapResource, size, image.getId(), image.getImage(), key);
    	}

		return mapResource.get(key).get(image.getId());
	}

    @Deprecated
	public String url(Integer size, EjbWithImage image)
	{
    	String key = image.getClass().getSimpleName();
    	if(!mapImages.containsKey(key)){generate(mapImages, size, image.getId(), image.getImage(), key);}
    	else
    	{
    		if(!mapImages.get(key).containsKey(image.getId())){generate(mapImages, size, image.getId(), image.getImage(), key);}
    	}

		return mapImages.get(key).get(image.getId());
	}

	@Deprecated
    private void generate(Map<String,Map<Long,String>> map, int size, long id, String image, String key)
    {
    	if(!map.containsKey(key))
    	{
    		map.put(key, new Hashtable<Long,String>());
    	}
    	StringBuffer sb = new StringBuffer();
    	sb.append("/").append(imagePath);
    	sb.append("/").append(size);
    	sb.append("/");
    	if(image!=null){sb.append(image);}
    	else{sb.append("noImage.png");}
    	map.get(key).put(id, sb.toString());
    }

    private void generateResource(Map<String,Map<Long,String>> map, int size, long id, String image, String key)
    {
    	if(!map.containsKey(key)){map.put(key, new Hashtable<Long,String>());}
    	StringBuffer sb = new StringBuffer();
//    	sb.append("/").append(imagePath);
//    	sb.append("/");
    	sb.append(size);
    	sb.append("/");
    	if(image!=null){sb.append(image);}
    	else{sb.append("noImage.png");}
    	map.get(key).put(id, sb.toString());
    }

    @Deprecated
	public String icon(int size,  String key)
	{
		StringBuffer sb = new StringBuffer();
    	sb.append("/").append(imagePath);
    	sb.append("/").append(size);
    	sb.append("/").append(mapStatic.get(key));
		return sb.toString();
	}

    protected void generateIconMap(Integer... sizes)
    {
    	for(int size : sizes)
    	{
    		Map<String,String> mapSize = new Hashtable<String,String>();
    		for(String key : mapStatic.keySet())
    		{
    			mapSize.put(key, size+"/"+mapStatic.get(key));
    		}
    		icon.put(size, mapSize);
    	}
    }

	protected void add(String key, String resource, String lib, boolean sizeFolder)
	{

	}

	protected void jeesl()
	{
		mapStatic.put("jeeslAdd", "ui/jeesl/control/add.png");							svg.put("jeeslAdd","ui/control/add.svg");
		mapStatic.put("jeeslCancel", "ui/jeesl/control/cancel.png");					svg.put("jeeslCancel","ui/control/cancel.svg");
		mapStatic.put("jeeslClone", "ui/jeesl/control/clone.png");						svg.put("jeeslClone","ui/control/clone.svg");
		mapStatic.put("jeeslDelete", "ui/jeesl/control/delete.png");					svg.put("jeeslDelete","ui/control/delete.svg");
		mapStatic.put("jeeslEdit", "ui/jeesl/control/edit.png");
		mapStatic.put("jeeslEditGrey", "ui/jeesl/control/editGrey.png");
		mapStatic.put("jeeslFilter", "ui/jeesl/control/filter.png");
		mapStatic.put("jeeslMove", "ui/jeesl/control/move.png");
		mapStatic.put("jeeslRemove", "ui/jeesl/control/remove.png");
		mapStatic.put("jeeslRefresh", "ui/jeesl/control/refresh.png");					svg.put("jeeslRefresh","ui/control/refresh.svg");
		mapStatic.put("jeeslClean", "ui/jeesl/control/clean.png");
		mapStatic.put("jeeslSave", "ui/jeesl/control/save.png");						svg.put("jeeslSave","ui/control/save.svg");
		mapStatic.put("jeeslSearch", "ui/jeesl/control/search.png");					svg.put("jeeslSearch","ui/control/search.svg");
		mapStatic.put("jeeslDownload", "ui/jeesl/control/download.png");				svg.put("jeeslDownload","ui/control/download.svg");
		mapStatic.put("jeeslUpload", "ui/jeesl/control/upload.png");					svg.put("jeeslUpload","ui/control/upload.svg");
		mapStatic.put("jeeslGenerate", "ui/jeesl/control/gear.png");					svg.put("jeeslUpload","ui/control/gear.svg");
																						svg.put("jeeslWizard","ui/control/wizard/wand.svg");
		mapStatic.put("jeeslArrowUp", "ui/jeesl/control/arrow/blue/up.png");			svg.put("jeeslArrowUp","ui/control/arrow/blue/up.svg");
		mapStatic.put("jeeslArrowDown", "ui/jeesl/control/arrow/blue/down.png");		svg.put("jeeslArrowDown","ui/control/arrow/blue/down.svg");
		mapStatic.put("jeeslArrowLeft", "ui/jeesl/control/arrow/blue/left.png");		svg.put("jeeslArrowLeft","ui/control/arrow/blue/left.svg");
		mapStatic.put("jeeslArrowRight", "ui/jeesl/control/arrow/blue/right.png");		svg.put("jeeslArrowRight","ui/control/arrow/blue/right.svg");

		mapStatic.put("jeeslInvisible", "ui/jeesl/generic/ghost.png");
		mapStatic.put("jeeslUnsaved", "ui/jeesl/generic/unsaved.png");

		svg.put("jeeslHome", "svg/jeesl/icon/ui/system/home.svg");
		
		svg.put("jeeslSbLogging", "svg/jeesl/icon/ui/ui/io/logging/sbLogging.svg");

		svg.put("jeeslEmail","ui/control/email/email.svg");
		svg.put("jeeslEmailSend","ui/control/email/send.svg");

		svg.put("jeeslTreeExpandTwo", "ui/control/tree/expand-2.svg");
		svg.put("jeeslTreeExpandThree", "ui/control/tree/expand-3.svg");
		svg.put("jeeslTreeExpandAll", "ui/control/tree/expand-all.svg");
		svg.put("jeeslTreeExpandRelevant", "ui/control/tree/expand-relevant.svg");
		svg.put("jeeslTreeCollapseAll", "ui/control/tree/collapse-all.svg");

		svg.put("jeeslExclamationMark", "ui/info/mark/exclamation.svg");
		svg.put("jeeslCheckMark", "ui/info/mark/check.svg");
		svg.put("jeeslCrossMark", "ui/info/mark/cross.svg");
		svg.put("jeeslQuestionMark", "ui/info/mark/question.svg");
		
		svg.put("jeeslCheckbox", "ui/control/checkbox/bright-unchecked.svg");
		svg.put("jeeslCheckboxChecked", "ui/control/checkbox/bright-checked.svg");

		svg.put("jeeslOnlineHelp", "ui/info/help.svg");
	}

	protected void jeeslRevision()
	{
		svg.put("jeeslRevision","ui/io/revision/envers/revision.svg");
		svg.put("jeeslRevisionAdd","ui/io/revision/envers/add.svg");
		svg.put("jeeslRevisionMod","ui/io/revision/envers/mod.svg");
		svg.put("jeeslRevisionDel","ui/io/revision/envers/del.svg");
	}
	
	protected void jeeslDm()
	{
		mapStatic.put("dmUnlockDeactivated", "ui/jeesl/control/dm/circleMinusGrey.png");
		mapStatic.put("dmUnlocked", "ui/jeesl/control/dm/circleMinusRed.png");
		mapStatic.put("dmNotSelected", "ui/jeesl/control/dm/circleGrey.png");
		mapStatic.put("dmSelected", "ui/jeesl/control/dm/circleGreen.png");
	}

	protected void geojsf()
	{
		mapStatic.put("geoJsfAdd", "ui/jeesl/control/add.png");
		mapStatic.put("geoJsfCancel", "ui/jeesl/control/cancel.png");
		mapStatic.put("geoJsfDelete", "ui/jeesl/control/delete.png");
		mapStatic.put("geoJsfRemove", "ui/jeesl/control/remove.png");
		mapStatic.put("geoJsfSave", "ui/jeesl/control/save.png");
	}

	protected void jeeslSecurity()
	{
		mapStatic.put("jeeslSecurityDocumentation", "ui/jeesl/security/documentation.png");
		mapStatic.put("jeeslSecurityVisible", "ui/jeesl/security/check-mark.png");
		mapStatic.put("jeeslSecurityInvisible", "ui/jeesl/security/x-mark.png");

		mapStatic.put("jeeslSecuritySelected", "ui/jeesl/security/check-mark.png");
		mapStatic.put("jeeslSecurityNotSelected", "ui/jeesl/security/x-mark.png");
	}

	protected void jeeslIo()
	{
		mapStatic.put("jeeslIoAttributeEmpty", "ui/jeesl/io/attribute/empty.png");
		mapStatic.put("jeeslIoAttributeCriteriaWithDescription", "ui/jeesl/io/attribute/criteriaWithDescription.png");
		mapStatic.put("jeeslIoAttributeCriteriaWithoutDescription", "ui/jeesl/io/attribute/criteriaWithoutDescription.png");
	}

	protected void jeeslFile()
	{
		mapStatic.put("reportPdf", "ui/jeesl/system/io/file/pdf.png");	svg.put("reportPdf","ui/io/fr/type/pdf.svg");
		mapStatic.put("reportXls", "ui/jeesl/system/io/file/xls.png");	svg.put("reportXls","ui/io/fr/type/xls.svg");
		mapStatic.put("reportDoc", "ui/jeesl/system/io/file/doc.png");	svg.put("reportDoc","ui/io/fr/type/doc.svg");
	}
}
