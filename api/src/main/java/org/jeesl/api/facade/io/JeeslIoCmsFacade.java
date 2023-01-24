package org.jeesl.api.facade.io;

import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsCategory;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsContent;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElement;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElementType;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsVisiblity;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

public interface JeeslIoCmsFacade <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									CAT extends JeeslIoCmsCategory<L,D,CAT,?>,
									CMS extends JeeslIoCms<L,D,LOC,CAT,S>,
									V extends JeeslIoCmsVisiblity,
									S extends JeeslIoCmsSection<L,S>,
									E extends JeeslIoCmsElement<V,S,EC,ET,C,FC>,
									EC extends JeeslStatus<L,D,EC>,
									ET extends JeeslIoCmsElementType<L,D,ET,?>,
									C extends JeeslIoCmsContent<V,E,MT>,
									MT extends JeeslIoCmsMarkupType<L,D,MT,?>,
									FC extends JeeslFileContainer<?,?>,
									FM extends JeeslFileMeta<D,FC,?,?>
									>
						extends JeeslFacade
{
	S load(S section, boolean recursive);
	List<E> fCmsElements(S section);
	
	void deleteCmsElement(E element) throws JeeslConstraintViolationException, JeeslLockingException;
}