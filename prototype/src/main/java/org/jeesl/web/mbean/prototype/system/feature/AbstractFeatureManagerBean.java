package org.jeesl.web.mbean.prototype.system.feature;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.JeeslFeatureManagerBean;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.JeeslSystemFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractFeatureManagerBean <F extends JeeslSystemFeature>
		implements Serializable,JeeslFeatureManagerBean<F>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractFeatureManagerBean.class);
	
	private JeeslFacade fUtils;
	
	private Class<F> cFeature;
	
	private List<F> features; public List<F> getFeatures() {return features;}
	protected Map<String,Boolean> map; public Map<String, Boolean> getMap() {return map;}
	
	public void initSuper(JeeslFacade fUtils, final Class<F> cFeature)
	{
		this.fUtils=fUtils;
		this.cFeature=cFeature;
		map = new HashMap<String,Boolean>();
		realodFeatures();
	}
	
	public <E extends Enum<E>> boolean has(E code){return has(code.toString());}
	public boolean has(String code){return (map.containsKey(code) && map.get(code));}
	
	@Override
	public void realodFeatures()
	{
		features = fUtils.all(cFeature);
		map.clear();
		for(F feature : features)
		{
			map.put(feature.getCode(),feature.isVisible());
		}
	}
}