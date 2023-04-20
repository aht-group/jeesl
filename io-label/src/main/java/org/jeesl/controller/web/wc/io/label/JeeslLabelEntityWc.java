package org.jeesl.controller.web.wc.io.label;

import org.jeesl.controller.web.io.label.JeeslLabelEntityController;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.model.ejb.io.label.entity.IoLabelAttribute;
import org.jeesl.model.ejb.io.label.entity.IoLabelAttributeType;
import org.jeesl.model.ejb.io.label.entity.IoLabelCategory;
import org.jeesl.model.ejb.io.label.entity.IoLabelEntity;
import org.jeesl.model.ejb.io.label.er.IoLabelDiagram;
import org.jeesl.model.ejb.io.label.er.IoLabelEntityRelation;
import org.jeesl.model.ejb.io.label.revision.IoRevisionEntityMapping;
import org.jeesl.model.ejb.io.label.revision.IoRevisionScope;
import org.jeesl.model.ejb.io.label.revision.IoRevisionScopeType;
import org.jeesl.model.ejb.io.label.revision.IoRevisionView;
import org.jeesl.model.ejb.io.label.revision.IoRevisionViewMapping;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;

public class JeeslLabelEntityWc extends JeeslLabelEntityController<IoLang,IoDescription,IoLocale,IoLabelCategory,IoRevisionView,IoRevisionViewMapping,IoRevisionScope,IoRevisionScopeType,IoLabelEntity,IoRevisionEntityMapping,IoLabelAttribute,IoLabelEntityRelation,IoLabelAttributeType,IoLabelDiagram>
{
	private static final long serialVersionUID = 1L;

	public JeeslLabelEntityWc(final IoRevisionFactoryBuilder<IoLang,IoDescription,IoLabelCategory,IoRevisionView,IoRevisionViewMapping,IoRevisionScope,IoRevisionScopeType,IoLabelEntity,IoRevisionEntityMapping,IoLabelAttribute,IoLabelEntityRelation,IoLabelAttributeType,IoLabelDiagram,?> fbRevision)
	{
		super(fbRevision);
	}
}