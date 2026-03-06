package org.jeesl.controller.web.c.io.fr;

import org.jeesl.controller.web.g.io.fr.JeeslFrReplicationGwc;
import org.jeesl.factory.builder.io.IoFileRepositoryFactoryBuilder;
import org.jeesl.interfaces.controller.web.io.fr.JeeslIoFrReplicationCallback;
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

public class JeeslFrReplicationWc extends JeeslFrReplicationGwc<IoLang,IoDescription,IoLocale,IoSsiSystem,IoFileStorage,IoFileReplication,IoFileReplicationType,IoFileStatus>
{
	private static final long serialVersionUID = 1L;
	
	public JeeslFrReplicationWc(JeeslIoFrReplicationCallback callback,
			 IoFileRepositoryFactoryBuilder<IoLang,IoDescription,IoLocale,IoSsiSystem,IoFileStorage,IoFileStorageType,IoFileStorageEngine,IoFileContainer,IoFileMeta,IoFileType,IoFileReplication,IoFileReplicationType,IoFileStatus> fb
			)
	{
		super(callback,fb);	
	}
}