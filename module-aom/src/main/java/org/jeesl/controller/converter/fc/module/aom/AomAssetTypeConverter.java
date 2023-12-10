package org.jeesl.controller.converter.fc.module.aom;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.aom.asset.AomAssetType;

@RequestScoped
@FacesConverter(forClass=AomAssetType.class)
public class AomAssetTypeConverter extends AbstractEjbIdConverter<AomAssetType>
{      
	public AomAssetTypeConverter()
	{
		super(AomAssetType.class);
	}
}  