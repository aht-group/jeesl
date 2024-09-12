package org.jeesl.client.controller.factory;

import org.jeesl.factory.builder.io.IoFileRepositoryFactoryBuilder;
import org.jeesl.model.ejb.io.fr.IoFileContainer;
import org.jeesl.model.ejb.io.fr.IoFileMeta;
import org.jeesl.model.ejb.io.fr.IoFileReplication;
import org.jeesl.model.ejb.io.fr.IoFileReplicationType;
import org.jeesl.model.ejb.io.fr.IoFileStatus;
import org.jeesl.model.ejb.io.fr.IoFileStorage;
import org.jeesl.model.ejb.io.fr.IoFileStorageEngine;
import org.jeesl.model.ejb.io.fr.IoFileStorageType;
import org.jeesl.model.ejb.io.fr.IoFileType;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;

public class JeeslFactoryProvider
{
	public static IoFileRepositoryFactoryBuilder<IoLang,IoDescription,IoLocale,IoSsiSystem,IoFileStorage,IoFileStorageType,IoFileStorageEngine,IoFileContainer,IoFileMeta,IoFileType,IoFileReplication,IoFileReplicationType,IoFileStatus> ioFileRepository() {return new IoFileRepositoryFactoryBuilder<>(IoLang.class,IoDescription.class,IoFileStorage.class,IoFileStorageType.class,IoFileStorageEngine.class,IoFileContainer.class,IoFileMeta.class,IoFileType.class,IoFileReplication.class,IoFileReplicationType.class,IoFileStatus.class);}
}