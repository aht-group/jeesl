package org.jeesl.interfaces.model.system.graphic.core;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.option.JeeslOptionRestDownload;

import net.sf.ahtutils.interfaces.model.crud.EjbPersistable;

public interface JeeslGraphicStyle <S extends JeeslStatus<S,L,D>, L extends JeeslLang, D extends JeeslDescription,G extends JeeslGraphic<L,D,?,?,?>>
							extends Serializable,EjbPersistable,JeeslOptionRestDownload,EjbWithCodeGraphic<G>,
									JeeslStatus<S,L,D>
{
	public static enum Code{circle,square,triangle}
	public static enum Group{outer,inner}
	public static enum Color{outer,inner}
	public static enum Size{outer}
}