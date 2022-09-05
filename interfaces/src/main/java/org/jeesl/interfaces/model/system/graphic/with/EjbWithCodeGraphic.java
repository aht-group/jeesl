package org.jeesl.interfaces.model.system.graphic.with;

import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;

public interface EjbWithCodeGraphic<G extends JeeslGraphic<?,?,?>> extends EjbWithGraphic<G>,EjbWithCode
{
}