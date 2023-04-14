package org.jeesl.interfaces.model.io.ssi.core;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoSsiPort <L extends JeeslLang, D extends JeeslDescription,
									HOST extends JeeslIoSsiHost<L,D,?>>
							extends Serializable,EjbSaveable,
									EjbWithId,EjbWithParentAttributeResolver,
									EjbWithLangDescription<L,D>,
									EjbWithPosition
{
	public enum Attributes{host}
	
	HOST getHost();
	void setHost(HOST host);
	
	int getPort();
	void setPort(int port);
	
	
	HOST getDestinationHost();
	void setDestinationHost(HOST destinationHost);
	
	int getDestinationPort();
	void setDestinationPort(int destinationPort);
}