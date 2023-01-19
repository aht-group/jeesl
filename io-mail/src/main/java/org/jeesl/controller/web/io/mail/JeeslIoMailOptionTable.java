package org.jeesl.controller.web.io.mail;

import java.util.List;

import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.model.ejb.io.locale.IoStatus;
import org.jeesl.model.ejb.io.mail.core.IoMailCategory;
import org.jeesl.model.ejb.io.mail.core.IoMailRetention;
import org.jeesl.model.ejb.io.mail.core.IoMailStatus;

public class JeeslIoMailOptionTable
{
	public static final long serialVersionUID=1;	
	
	public static void addOptions(List<EjbWithPosition> categories)
	{
		JeeslIoMailOptionTable.build(categories,IoMailCategory.class,true);
		JeeslIoMailOptionTable.build(categories,IoMailRetention.class,true);
		JeeslIoMailOptionTable.build(categories,IoMailStatus.class,true);

//			categories.add(build(IoTemplateCategory.class,true));
//			categories.add(build(IoTemplateScope.class,true));
//			categories.add(build(IoTemplateChannel.class,true));
//			categories.add(build(IoTemplateTokenType.class,true));
	}
	
	protected static void build(List<EjbWithPosition> categories, Class<?> c, boolean allowAdditional){build(categories,c,null,allowAdditional);}
	protected static void build(List<EjbWithPosition> categories, Class<?> c, Class<?> parent, boolean allowAdditional)
	{
		IoStatus s = new IoStatus();
		s.setId(categories.size()+1);
		s.setCode(c.getSimpleName());
		s.setImage(c.getName());
		if(parent!=null){s.setImageAlt(parent.getName());}
		categories.add(s);
	}
}