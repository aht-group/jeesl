package org.jeesl.model.json.io.db;

import java.io.Serializable;

public class JsonDbCacheStatistic implements Serializable
{
	public static final long serialVersionUID=1;
	
	private Long hits;
	public Long getHits() {return hits;}
	public void setHits(Long hits) {this.hits = hits;}

	private Long misses;
	public Long getMisses() {return misses;}
	public void setMisses(Long misses) {this.misses = misses;}
}