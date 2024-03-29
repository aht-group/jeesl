package org.jeesl.interfaces.model.module.rmmv;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslRmmvModuleConfig<TE extends JeeslRmmvElement<?,?,TE,?>,
								MOD extends JeeslRmmvModule<?,?,MOD,?>>
						extends Serializable,EjbSaveable,
								EjbWithParentAttributeResolver,EjbWithPositionVisible
{
	public enum Attributes{element,module}
	
	TE getElement();
	void setElement(TE element);
	
	MOD getModule();
	void setModule(MOD module);
	
	
	String getColor();
	void setColor(String color);
}