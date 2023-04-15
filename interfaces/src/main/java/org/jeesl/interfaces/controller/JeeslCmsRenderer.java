package org.jeesl.interfaces.controller;

import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleManager;
import org.jeesl.interfaces.model.io.cms.JeeslIoCms;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsContent;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElement;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElementType;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsVisiblity;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.openfuxml.content.ofx.Section;
import org.openfuxml.exception.OfxAuthoringException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface JeeslCmsRenderer <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								CAT extends JeeslStatus<L,D,CAT>,
								CMS extends JeeslIoCms<L,D,LOC,CAT,S>,
								V extends JeeslIoCmsVisiblity,
								S extends JeeslIoCmsSection<L,S>,
								E extends JeeslIoCmsElement<V,S,EC,ET,C,FC>,
								EC extends JeeslStatus<L,D,EC>,
								ET extends JeeslIoCmsElementType<L,D,ET,?>,
								C extends JeeslIoCmsContent<V,E,MT>,
								MT extends JeeslIoMarkupType<L,D,MT,?>,
								FC extends JeeslFileContainer<?,?>
								>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslCmsRenderer.class);
	
	Section build(JeeslLocaleManager<LOC> lp, String localeCode, S section) throws OfxAuthoringException;
	Section build(JeeslLocaleManager<LOC> lp, String localeCode, E element) throws OfxAuthoringException;
}