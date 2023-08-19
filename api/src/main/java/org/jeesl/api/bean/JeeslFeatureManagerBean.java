package org.jeesl.api.bean;

import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.system.property.JeeslSystemFeature;

public interface JeeslFeatureManagerBean <F extends JeeslSystemFeature>
{
	List<F> getFeatures();
	Map<String,Boolean> getMap();
	void realodFeatures();
}