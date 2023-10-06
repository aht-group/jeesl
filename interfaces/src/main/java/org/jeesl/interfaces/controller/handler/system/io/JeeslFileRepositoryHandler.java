package org.jeesl.interfaces.controller.handler.system.io;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.io.fr.JeeslWithFileRepositoryContainer;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;

public interface JeeslFileRepositoryHandler <LOC extends JeeslLocale<?,?,LOC,?>,
											STORAGE extends JeeslFileStorage<?,?,?,?,?>,
											CONTAINER extends JeeslFileContainer<STORAGE,META>,
											META extends JeeslFileMeta<?,CONTAINER,?,?>>
		extends Serializable
{
	void setDebugOnInfo(boolean debugOnInfo);
	void setLocales(List<LOC> locales);
	void setLocale(LOC locale);
	
	void reset();
	
	STORAGE getStorage();
	void setStorage(STORAGE storage);
	
	CONTAINER getContainer();
	List<META> getMetas();
	
	<W extends JeeslWithFileRepositoryContainer<CONTAINER>> void initSilent(W with);
	<W extends JeeslWithFileRepositoryContainer<CONTAINER>> void init(W with) throws JeeslConstraintViolationException, JeeslLockingException;
	<W extends JeeslWithFileRepositoryContainer<CONTAINER>> void init(STORAGE storage, W with) throws JeeslConstraintViolationException, JeeslLockingException;
	
	void addFile(String name, byte[] bytes, String category);
	
	InputStream download(META meta) throws JeeslNotFoundException;
	
	META saveFile()  throws JeeslConstraintViolationException, JeeslLockingException;
	void saveThreadsafe(CONTAINER container, String name, byte[] bytes, String category) throws JeeslConstraintViolationException, JeeslLockingException;
	<W extends JeeslWithFileRepositoryContainer<CONTAINER>> void saveDeferred(W with) throws JeeslConstraintViolationException, JeeslLockingException;
	
	void copyTo(JeeslFileRepositoryHandler<LOC,STORAGE,CONTAINER,META> target) throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException;
	void deleteFile() throws JeeslConstraintViolationException, JeeslLockingException;
	
//	StreamedContent fileStream() throws Exception;
}