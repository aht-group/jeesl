package org.jeesl.factory.ejb.io.fr;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.io.fr.JeeslWithFileRepositoryContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoFrContainerFactory<STORAGE extends JeeslFileStorage<?,?,?,?,?>,
									CONTAINER extends JeeslFileContainer<STORAGE,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoFrContainerFactory.class);

	private final Class<CONTAINER> cContainer;

//	public static <STORAGE extends JeeslFileStorage<?,?,?,?,?>,CONTAINER extends JeeslFileContainer<STORAGE,?>>
//			EjbIoFrContainerFactory<STORAGE,CONTAINER> instance(final Class<CONTAINER> cContainer)
//	{
//		return new EjbIoFrContainerFactory<>(cContainer);
//	}
	public EjbIoFrContainerFactory(final Class<CONTAINER> cContainer)
	{
        this.cContainer = cContainer;
	}

	public CONTAINER build(long id)
	{
		CONTAINER ejb = null;
		try
		{
			 ejb = cContainer.newInstance();
			 ejb.setId(id);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}

		return ejb;
	}

	public CONTAINER build(STORAGE storage)
	{
		CONTAINER ejb = null;
		try
		{
			 ejb = cContainer.newInstance();
			 ejb.setStorage(storage);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}

		return ejb;
	}

	public <W extends JeeslWithFileRepositoryContainer<CONTAINER>> List<CONTAINER> toListContainer(List<W> owners)
	{
		List<CONTAINER> list = new ArrayList<>();
		for(W owner : owners) {if(Objects.nonNull(owner.getFrContainer())) {list.add(owner.getFrContainer());}}
		return list;
	}

	public <W extends JeeslWithFileRepositoryContainer<CONTAINER>> BidiMap<W,CONTAINER> toBidiMapContainer(List<W> owners)
	{
		BidiMap<W,CONTAINER> map = new DualHashBidiMap<>();
		for(W owner : owners) {if(Objects.nonNull(owner.getFrContainer())) {map.put(owner,owner.getFrContainer());}}
		return map;
	}
}