package org.jeesl.api.facade.io;

import java.util.List;

import org.exlp.model.xml.io.File;
import org.exlp.model.xml.io.Files;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslIoJcrFacade <L extends JeeslLang,D extends JeeslDescription> extends JeeslFacade
{
	List<File> jcrFiles(EjbWithId ejb);
	File jcrFile(EjbWithId ejb, String fileName) throws JeeslNotFoundException;
	Files jcrUpload(EjbWithId ejb, File file) throws UtilsProcessingException;
}