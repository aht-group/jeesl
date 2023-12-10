package org.jeesl.controller.converter.fc.module.aom;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.aom.asset.AomAssetStatus;

@RequestScoped
@FacesConverter(forClass=AomAssetStatus.class)
public class AomAssetStatusConverter extends AbstractEjbIdConverter<AomAssetStatus>
{      
	public AomAssetStatusConverter()
	{
		super(AomAssetStatus.class);
	}
}  