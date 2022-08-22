package org.jeesl.interfaces.model.module.rmmv;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslRmmvConfig<TE extends JeeslRmmvTreeElement<?,?,TE>,
								MOD extends JeeslRmmvModule<?,?,MOD,?>>
						extends Serializable,EjbSaveable
{	

}