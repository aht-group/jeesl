package net.sf.ahtutils.web.mbean.util;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import net.sf.ahtutils.jsf.filter.UtilsStatusFilter;
import net.sf.ahtutils.model.interfaces.status.UtilsDescription;
import net.sf.ahtutils.model.interfaces.status.UtilsLang;
import net.sf.ahtutils.model.interfaces.status.UtilsStatus;
import net.sf.ahtutils.model.interfaces.with.EjbWithImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractIconBean <L extends UtilsLang, D extends UtilsDescription, S extends UtilsStatus<S,L,D>>	 implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(AbstractIconBean.class);
	private static final long serialVersionUID = 1L;
	
	private String imagePath;
	
	private Map<String,Map<Long,String>> mapImages,mapResource;
	private Map<String,Map<Long,String>> mapImagesAlt,mapResourceAlternative;
	protected Map<String,String> mapStatic;
	
	private Map<Integer,Map<String,String>> icon;
	
	//******* Methods *******************************

	public void initPath(String imagePath)
    {
		this.imagePath=imagePath;
		mapImages = new Hashtable<String,Map<Long,String>>();
		mapImagesAlt = new Hashtable<String,Map<Long,String>>();
		mapResource = new Hashtable<String,Map<Long,String>>();
		mapResourceAlternative = new Hashtable<String,Map<Long,String>>();
		mapStatic = new Hashtable<String,String>();
		
		icon = new Hashtable<Integer,Map<String,String>>();
    }

    @Deprecated
    public String urlFilter(Integer size, UtilsStatusFilter<L,D,S> filter)
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
    public String filter(Integer size, UtilsStatusFilter<L,D,S> filter)
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
	
	public Map<Integer, Map<String, String>> getIcon() {return icon;}
	public Map<String, String> getIcon12() {return icon.get(12);}
}
