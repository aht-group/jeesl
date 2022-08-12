package org.jeesl.interfaces.model.io.mail.template;

import java.io.Serializable;
import java.util.Map;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
public interface JeeslIoTemplateDefinition<D extends JeeslDescription,
								CHANNEL extends JeeslTemplateChannel<?,D,CHANNEL,?>,
								TEMPLATE extends JeeslIoTemplate<?,D,?,?,?,?>
								>
		extends Serializable,EjbPersistable,
				EjbWithId,EjbSaveable,EjbRemoveable,EjbWithParentAttributeResolver,
				EjbWithDescription<D>
{	
	TEMPLATE getTemplate();
	void setTemplate(TEMPLATE template);
	
	CHANNEL getType();
	void setType(CHANNEL type);
	
	public Map<String,D> getHeader();
	public void setHeader(Map<String,D> header);
}