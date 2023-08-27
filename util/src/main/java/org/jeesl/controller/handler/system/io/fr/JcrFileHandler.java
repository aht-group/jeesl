package org.jeesl.controller.handler.system.io.fr;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.exlp.model.xml.io.File;
import org.jeesl.api.facade.io.JeeslIoJcrFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.interfaces.bean.JcrBean;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JcrFileHandler<L extends JeeslLang,D extends JeeslDescription, T extends JeeslStatus<L,D,T>> implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JcrFileHandler.class);
	
	private List<File> files; public List<File> getFiles() {return files;}
	private final List<T> types; public List<T> getTypes() {return types;}
	
	private File file; public File getFile() {return file;} public void setFile(File file) {this.file = file;}
	private EjbWithId ejb; public EjbWithId getEjb() {return ejb;} public void setEjb(EjbWithId ejb) {this.ejb = ejb;}
	private T type; public T getType() {return type;} public void setType(T type) {this.type = type;}
	
	private Class<T> cType;
	private boolean withTypes; public boolean isWithTypes() {return withTypes;}
	
	private final JeeslIoJcrFacade<L,D> fJcr;
	private final JcrBean bean; public JcrBean getBean() {return bean;}
	
	public JcrFileHandler(JcrBean bean, JeeslIoJcrFacade<L,D> fJcr){this(bean,fJcr,null,null);}
	public JcrFileHandler(JcrBean bean, JeeslIoJcrFacade<L,D> fJcr, List<T> types, Class<T> cType)
	{
		this.bean=bean;
		this.fJcr=fJcr;
		this.types=types;
		this.cType=cType;
		withTypes = types!=null && !types.isEmpty();
		
		files = new ArrayList<File>();
	}
	
	public void reset()
	{
		files.clear();
		file=null;
		ejb=null;
	}
	
	public void reload(EjbWithId ejb)
	{
		this.ejb=ejb;
		files = fJcr.jcrFiles(ejb);
		file = null;
	}
	
	public void loadFile() throws JeeslNotFoundException
	{
		file = fJcr.jcrFile(ejb,file.getName());
	}
	
	public InputStream toInputStream()
	{
		return new ByteArrayInputStream(file.getData().getValue());
	}
	
	public void saveFile() throws UtilsProcessingException
	{
		boolean alreadyAvailable = false;
		for(File f : files)
		{
			if(f.getName().equals(file.getName())){alreadyAvailable=true;}
		}
		
		logger.info("Save (existing:"+alreadyAvailable+")");
		if(!alreadyAvailable)
		{
			if(type!=null)
			{
				type = fJcr.find(cType,type);
				file.setCategory(type.getCode());
				logger.info("Using type:" +type.toString());
			}
			
			fJcr.jcrUpload(ejb,file);
			reload(ejb);
		}
		file = null;
    }
	
	public void addFile()
	{
		file = null;
	}
	
	public void selectFile() throws JeeslNotFoundException
	{
		logger.info("selectFile");
		loadFile();
		bean.jcrFileSelected(ejb);
	}
}