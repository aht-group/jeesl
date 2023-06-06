package org.jeesl.api.facade.io;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.dms.JeeslIoDms;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsDocument;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsSection;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsView;
import org.jeesl.interfaces.model.io.domain.JeeslDomainSet;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

public interface JeeslIoDmsFacade <L extends JeeslLang,D extends JeeslDescription,LOC extends JeeslStatus<L,D,LOC>,
									DMS extends JeeslIoDms<L,D,STORAGE,AS,DS,S>,
									STORAGE extends JeeslFileStorage<L,D,?,?,?>,
									AS extends JeeslAttributeSet<L,D,?,?,?>,
									DS extends JeeslDomainSet<L,D,?>,
									S extends JeeslIoDmsSection<L,D,S>,
									FILE extends JeeslIoDmsDocument<L,S,FC,AC>,
									VIEW extends JeeslIoDmsView<L,D,DMS>,
									FC extends JeeslFileContainer<?,?>,
									AC extends JeeslAttributeContainer<?,?>>
						extends JeeslFacade
{
	S load(S section, boolean recursive);
}