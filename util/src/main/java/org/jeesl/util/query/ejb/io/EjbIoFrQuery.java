package org.jeesl.util.query.ejb.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorageType;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.io.JeeslIoFrQuery;
import org.jeesl.util.query.ejb.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoFrQuery<STORAGE extends JeeslFileStorage<?,?,?,STYPE,?>,
							STYPE extends JeeslFileStorageType<?,?,STYPE,?>,
							CONTAINER extends JeeslFileContainer<?,?>>

			extends AbstractEjbQuery
			implements JeeslIoFrQuery<STORAGE,STYPE,CONTAINER>
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbIoFrQuery.class);
	
	public EjbIoFrQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{
		super.reset();
		ioFrContainers=null;
		ioFrStorages=null;
	}
	
	//Fetches
	public EjbIoFrQuery<STORAGE,STYPE,CONTAINER> distinct(boolean value) {super.setDistinct(value); return this;}
	public <E extends Enum<E>> EjbIoFrQuery<STORAGE,STYPE,CONTAINER> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}

	
	//Lists
	@Override public EjbIoFrQuery<STORAGE,STYPE,CONTAINER> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbIoFrQuery<STORAGE,STYPE,CONTAINER> ids(List<T> ids) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(EjbIdFactory.toIds(ids)); return this;}
	@Override public EjbIoFrQuery<STORAGE,STYPE,CONTAINER> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbIoFrQuery<STORAGE,STYPE,CONTAINER> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}
	
	// IO - FR
	private List<STORAGE> ioFrStorages;
	@Override public List<STORAGE> getIoFrStorages() {return ioFrStorages;}
	public EjbIoFrQuery<STORAGE,STYPE,CONTAINER> add(STORAGE storage) {if(Objects.isNull(ioFrStorages)) {ioFrStorages = new ArrayList<>();} ioFrStorages.add(storage); return this;}
	public EjbIoFrQuery<STORAGE,STYPE,CONTAINER> addIoFrStorages(List<STORAGE> list) {if(Objects.isNull(ioFrStorages)) {ioFrStorages = new ArrayList<>();} ioFrStorages.addAll(list); return this;}

	private List<STYPE> ioFrStorageType;
	@Override public List<STYPE> getIoFrStorageTypes() {return ioFrStorageType;}
//	@Override public EjbQuery add(IoFileStorageType storage) {if(Objects.isNull(ioFrStorageType)) {ioFrStorageType = new ArrayList<>();} ioFrStorageType.add(storage); return this;}

	
	private List<CONTAINER> ioFrContainers;
	@Override public List<CONTAINER> getIoFrContainers() {return ioFrContainers;}
	public EjbIoFrQuery<STORAGE,STYPE,CONTAINER> add(CONTAINER container) {if(Objects.isNull(ioFrContainers)) {ioFrContainers = new ArrayList<>();} ioFrContainers.add(container); return this;}
	public EjbIoFrQuery<STORAGE,STYPE,CONTAINER> addIoFrContainer(List<CONTAINER> list) {if(Objects.isNull(ioFrContainers)) {ioFrContainers = new ArrayList<>();} ioFrContainers.addAll(list); return this;}
		
}