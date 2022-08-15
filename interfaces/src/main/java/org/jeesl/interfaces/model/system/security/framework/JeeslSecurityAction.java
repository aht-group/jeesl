package org.jeesl.interfaces.model.system.security.framework;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslSecurityAction<L extends JeeslLang, D extends JeeslDescription,
								   R extends JeeslSecurityRole<L,D,?,V,U,?,?>,
								   V extends JeeslSecurityView<L,D,?,R,U,?>,
								   U extends JeeslSecurityUsecase<L,D,?,R,V,?>,
								   AT extends JeeslSecurityTemplate<L,D,?>>
			extends Serializable,EjbWithCode,EjbPersistable,EjbSaveable,EjbRemoveable,
					EjbWithPositionVisible,EjbWithParentAttributeResolver,
					EjbWithLang<L>,EjbWithDescription<D>
{	
	public enum Attributes{view}
	
//	public String toCode2();
	
	public Map<String,L> toName();
	
	public V getView();
	public void setView(V view);
	
	public List<R> getRoles();
	public void setRoles(List<R> roles);
	
	public List<U> getUsecases();
	public void setUsecases(List<U> usecases);
	
	public Boolean getDocumentation();
	public void setDocumentation(Boolean documentation);
	
	public AT getTemplate();
	public void setTemplate(AT template);
	
	
    public static String toCode(JeeslSecurityAction<?,?,?,?,?,?> action)
    {
	    	StringBuilder sb = new StringBuilder();
	    	if(action.getTemplate()==null) {sb.append(action.getCode());}
	    	else
	    	{
	    		sb.append(action.getView().getCode());
//	    		sb.append(".");
	    		sb.append(action.getTemplate().getCode().substring(action.getTemplate().getCode().lastIndexOf("."), action.getTemplate().getCode().length()));
	    	}
	    	
	    	return sb.toString();
    }
}