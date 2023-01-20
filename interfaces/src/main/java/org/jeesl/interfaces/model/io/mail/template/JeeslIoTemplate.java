package org.jeesl.interfaces.model.io.mail.template;

import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.model.with.system.status.JeeslWithCategory;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoTemplate<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslStatus<L,D,CATEGORY>,
								SCOPE extends JeeslStatus<L,D,SCOPE>,
								DEFINITION extends JeeslIoTemplateDefinition<D,?,?>,
								TOKEN extends JeeslIoTemplateToken<L,D,?,?>
								>
		extends EjbWithId,EjbSaveable,EjbRemoveable,
				EjbWithCode,EjbWithPositionVisible,EjbWithPositionParent,
				EjbWithLang<L>,EjbWithDescription<D>,
				JeeslWithCategory<CATEGORY>
{	
	public enum Attributes{category,scope,visible}
	
	SCOPE getScope();
	void setScope(SCOPE scope);
	
	List<TOKEN> getTokens();
	void setTokens(List<TOKEN> tokens);
	
	List<DEFINITION> getDefinitions();
	void setDefinitions(List<DEFINITION> definitions);
}