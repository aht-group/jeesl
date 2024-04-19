package org.jeesl.controller.converter.fc.module.aom;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.aom.asset.AomAsset;

@RequestScoped
@FacesConverter(forClass=AomAsset.class)
public class AomAssetConverter extends AbstractEjbIdConverter<AomAsset>
{      
	public AomAssetConverter()
	{
		super(AomAsset.class);
	}
}  