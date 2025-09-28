package org.jeesl.model.ejb.io.db;

import java.io.Serializable;

public interface JeeslCq extends Serializable
{
	public enum Agg{count,sum,min,avg,max}
}