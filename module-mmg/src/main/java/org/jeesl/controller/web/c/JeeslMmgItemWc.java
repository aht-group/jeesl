package org.jeesl.controller.web.c;

import org.jeesl.controller.web.module.mmg.JeeslMmgItemController;
import org.jeesl.factory.builder.io.IoFileRepositoryFactoryBuilder;
import org.jeesl.factory.builder.module.MmgFactoryBuilder;
import org.jeesl.interfaces.controller.web.module.mmg.JeeslMmgItemCallback;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
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
import org.jeesl.model.ejb.module.mmg.MmgClassification;
import org.jeesl.model.ejb.module.mmg.MmgGallery;
import org.jeesl.model.ejb.module.mmg.MmgItem;
import org.jeesl.model.ejb.module.mmg.MmgQuality;
import org.jeesl.model.ejb.system.security.user.SecurityUser;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

public class JeeslMmgItemWc <RREF extends EjbWithId>
					extends JeeslMmgItemController<IoLang,IoDescription,IoLocale,TenantRealm,RREF,MmgGallery,MmgItem,MmgClassification,MmgQuality,IoFileStorage,IoFileContainer,IoFileMeta,SecurityUser>
{
	private static final long serialVersionUID = 1L;

	public JeeslMmgItemWc(final JeeslMmgItemCallback callback)
	{
		super(callback,
				new MmgFactoryBuilder<>(IoLang.class,IoDescription.class,IoLocale.class,MmgClassification.class,MmgGallery.class,MmgItem.class),
				new IoFileRepositoryFactoryBuilder<>(IoLang.class,IoDescription.class,IoFileStorage.class,IoFileStorageType.class,IoFileStorageEngine.class,IoFileContainer.class,IoFileMeta.class,IoFileType.class,IoFileReplication.class,IoFileReplicationType.class,IoFileStatus.class));
	}
}