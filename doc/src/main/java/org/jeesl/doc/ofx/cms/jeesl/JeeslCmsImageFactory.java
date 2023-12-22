package org.jeesl.doc.ofx.cms.jeesl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.jeesl.doc.ofx.cms.generic.JeeslMarkupFactory;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryHandler;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsContent;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElement;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.io.fr.JeeslFileType;
import org.openfuxml.factory.xml.media.XmlImageFactory;
import org.openfuxml.factory.xml.media.XmlMediaFactory;
import org.openfuxml.model.xml.core.media.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslCmsImageFactory<E extends JeeslIoCmsElement<?,?,?,?,C,FC>,
								C extends JeeslIoCmsContent<?,E,?>,
								M extends JeeslIoMarkup<MT>,
								MT extends JeeslIoMarkupType<?,?,MT,?>,
								FS extends JeeslFileStorage<?,?,?,?,?>,
								FC extends JeeslFileContainer<FS,FM>,
								FM extends JeeslFileMeta<?,FC,FT,?>,
								FT extends JeeslFileType<?,?,FT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslCmsImageFactory.class);
	
	private final JeeslFileRepositoryHandler<?,FS,FC,FM> frh;
	private final JeeslMarkupFactory<M,MT> ofxMarkup;
	
	public JeeslCmsImageFactory(JeeslFileRepositoryHandler<?,FS,FC,FM> frh)
	{
		this.frh = frh;
		ofxMarkup = new JeeslMarkupFactory<>();
	}
	
	public Image build(String localeCode, E element)
	{
		logger.trace("Building Image ");

		Image xml = XmlImageFactory.centerPercent(element.getId(), 80);
		xml.setMedia(XmlMediaFactory.build());
		
		if(ObjectUtils.isNotEmpty(element.getFrContainer().getMetas()))
		{
			FM meta = element.getFrContainer().getMetas().get(0);
			xml.getMedia().setSrc(meta.getCode());
			xml.getMedia().setDst(meta.getType().getCode());
		}
		
//		if(element.getContent().containsKey(localeCode))
//		{
//			try
//			{
//				C content = element.getContent().get(localeCode);
//				Section section = ofxMarkup.build(content.getMarkup().getCode(),content.getLang());
//				Paragraph p = XmlSectionQuery.getFirstParagraph(section);
//				String s = p.getContent().get(0).toString();
//				logger.trace(s);
//				xml.setTitle(XmlTitleFactory.build(s));
//			}
//			catch (ExlpXpathNotFoundException e)
//			{
//				logger.error(e.getMessage());
//				logger.error(element.getSection().toString()+ " "+element.getPosition());
//			}
//		}
		if(Objects.nonNull(frh))
		{
			try
			{
				frh.init(element);
				List<FM> metas = frh.getMetas();
				for(FM m : metas)
				{
					xml.setMedia(XmlMediaFactory.build(element.getId()+".png",element.getId()+".png"));
					try
					{
						logger.info(m.toString()+" "+m.getType().getCode());
						File fDir = new File("/Volumes/ramdisk/dev/srs/png");
						File f = new File(fDir,element.getId()+".png");
						IOUtil.copyCompletely(frh.download(m), new FileOutputStream(f));
					}
					catch (FileNotFoundException e) {e.printStackTrace();}
					catch (IOException e) {e.printStackTrace();}
					catch (JeeslNotFoundException e) {e.printStackTrace();}
				}
			}
			catch (JeeslConstraintViolationException e) {e.printStackTrace();}
			catch (JeeslLockingException e) {e.printStackTrace();}
		}
		
		
//		JaxbUtil.trace(xml);
		return xml;
	}
}