package net.sf.ahtutils.doc.ofx.status;

import org.jeesl.model.xml.io.locale.status.Status;
import org.openfuxml.factory.xml.layout.XmlHeightFactory;
import org.openfuxml.model.xml.core.media.Image;
import org.openfuxml.model.xml.core.media.Media;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfxStatusImageFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxStatusImageFactory.class);
	
	public static Image build(String imagePathPrefix, Status status)
	{
		int index = status.getImage().lastIndexOf(".");
		String name = status.getImage().substring(0,index);
		
		StringBuffer sb = new StringBuffer();
		sb.append(imagePathPrefix).append("/");
		sb.append(name);
		sb.append(".svg");
		logger.trace(sb.toString());
		
		Media media = new Media();
		media.setSrc(sb.toString());
		media.setDst(name);
		
		Image image = new Image();
		image.setMedia(media);
		image.setHeight(XmlHeightFactory.em(1));
		return image;
	}
}