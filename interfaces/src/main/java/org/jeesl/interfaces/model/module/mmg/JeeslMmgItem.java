package org.jeesl.interfaces.model.module.mmg;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslWithFileRepositoryContainer;
import org.jeesl.interfaces.model.io.label.revision.data.JeeslLastModified;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisible;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslMmgItem<L extends JeeslLang,
								MG extends JeeslMmgGallery<L>,
								FRC extends JeeslFileContainer<?,?>,
								USER extends JeeslSimpleUser>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,EjbWithVisible,
				EjbWithParentAttributeResolver,
				EjbWithLang<L>,JeeslLastModified<USER>,JeeslWithFileRepositoryContainer<FRC>
{
	public static enum Attributes{id,gallery}
	
	MG getGallery();
	void setGallery(MG gallery);
	
	LocalDateTime getLdtImage();
	void setLdtImage(LocalDateTime ldtImage);
	
	LocalDateTime getLdtUpload();
	void setLdtUpload(LocalDateTime ldtUpload);
}