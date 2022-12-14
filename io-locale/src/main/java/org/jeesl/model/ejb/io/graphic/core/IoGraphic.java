package org.jeesl.model.ejb.io.graphic.core;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="IoGraphic")
@EjbErNode(name="Graphic",category="io",subset="ioGraphic",level=3)
public class IoGraphic implements JeeslGraphic<IoGraphicType,IoGraphicComponent,IoGraphicShape>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@Version
	private Long versionLock;
	@Override public Long getVersionLock() {return versionLock;}

	@NotNull @ManyToOne
	private IoGraphicType type;
	public IoGraphicType getType(){return type;}
	public void setType(IoGraphicType type){this.type = type;}
	
	@ManyToOne
	private IoGraphicShape style;
	public IoGraphicShape getStyle() {return style;}
	public void setStyle(IoGraphicShape style) {this.style = style;}
	
	@Basic
	private byte[] data;
    @Override public byte[] getData() {return data;}
    @Override public void setData(byte[] data) {this.data = data;}
	
    private Integer size;
	public Integer getSize() {return size;}
	public void setSize(Integer size) {this.size = size;}
	
    private Integer sizeBorder;
	public Integer getSizeBorder() {return sizeBorder;}
	public void setSizeBorder(Integer sizeBorder) {this.sizeBorder = sizeBorder;}
	
	private String color;
	public String getColor() {return color;}
	public void setColor(String color) {this.color = color;}
	
	private String colorBorder;
	public String getColorBorder() {return colorBorder;}
	public void setColorBorder(String colorBorder) {this.colorBorder = colorBorder;}
    
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="graphic")
	@OrderBy("position ASC")
	private List<IoGraphicComponent> figures;
	@Override public List<IoGraphicComponent> getFigures() {return figures;}
	@Override public void setFigures(List<IoGraphicComponent> figures) {this.figures = figures;}


	@Override public boolean equals(Object object) {return (object instanceof IoGraphic) ? id == ((IoGraphic) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(13,37).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		sb.append(" t:").append(type.getCode());
		if(style!=null){sb.append(" s:").append(style.getCode());}
		sb.append(" size:").append(size);
		if(versionLock!=null){sb.append(" v:"+versionLock);}
		return sb.toString();
	}
}