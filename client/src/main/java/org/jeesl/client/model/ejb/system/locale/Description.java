package org.jeesl.client.model.ejb.system.locale;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name = "UtilsLang")
@EjbErNode(name="Description",category="status",subset="status",level=3)
public class Description implements JeeslDescription,EjbRemoveable,Serializable
{
	public static final long serialVersionUID=1;

	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private String lkey;
	
	@NotNull
	private String lang;
	
	private Boolean styled;
	@Override public Boolean getStyled() {return styled;}
	@Override public void setStyled(Boolean styled) {this.styled = styled;}
	
	// >>>>>>>>>>>>>>>>>>>>>Getters and Setters<<<<<<<<<<<<<<<<<<<
	
	public long getId() {return id;}
	public void setId(long id) {this.id = id;}
	
	public String getLkey() {return lkey;}
	public void setLkey(String lkey) {this.lkey = lkey;}
	
	public String getLang() {return lang;}
	public void setLang(String name) {this.lang = name;}
	
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
			sb.append(id);
			sb.append(" ["+lkey+"]");
			sb.append(" "+lang);
		return sb.toString();
	}
	
	public synchronized static Map<String,Description> createMap(Description... languages)
	{
		Map<String,Description> langMap = new Hashtable<String, Description>();
		for(Description lang : languages)
		{
			langMap.put(lang.getLkey(), lang);
		}
		return langMap;
	}
	
	public synchronized static Description create(String key, String lang)
	{
		Description pl = new Description();
		pl.setLkey(key);
		pl.setLang(lang);
		return pl;
	}
}