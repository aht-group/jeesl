package org.jeesl.factory.txt.module.aom;

import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtAomAssetFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtAomAssetFactory.class);
	    
	public static <A extends JeeslAomAsset <?,A,?,?,?>> String positions(A asset)
	{
		StringBuilder sb = new StringBuilder();
		
		positions(sb,asset);
		
		return sb.reverse().toString();
	}

	private static <A extends JeeslAomAsset <?,A,?,?,?>> void positions(StringBuilder sb, A asset)
	{
		sb.append(asset.getPosition());
		if(asset.getParent()!=null)
		{
			sb.append(".");
			TxtAomAssetFactory.positions(sb,asset.getParent());
		}
	}
}