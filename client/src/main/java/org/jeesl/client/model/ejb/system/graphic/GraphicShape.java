package org.jeesl.client.model.ejb.system.graphic;

import java.util.Map;

import org.jeesl.client.model.ejb.system.locale.Description;
import org.jeesl.client.model.ejb.system.locale.Lang;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@EjbErNode(name="Style",category="symbol",subset="symbol",level=3)
public class GraphicShape implements JeeslGraphicShape<Lang,Description,GraphicShape,Graphic>
{
	public static enum Code {welcome}
	public static final long serialVersionUID=1;
	
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	protected String symbol;
	public String getSymbol(){return symbol;}
	public void setSymbol(String symbol){this.symbol = symbol;}
	
	protected String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}
	
	protected boolean visible;
	@Override public boolean isVisible() {return visible;}
	@Override public void setVisible(boolean visible) {this.visible = visible;}
	
	protected String image;
	@Override public String getImage() {return image;}
	@Override public void setImage(String image) {this.image = image;}
	
	protected String imageAlt;
	@Override public String getImageAlt() {return imageAlt;}
	@Override public void setImageAlt(String imageAlt) {this.imageAlt=imageAlt;}
	
	protected String style;
	public String getStyle() {return style;}
	public void setStyle(String style) {this.style = style;}
	
	protected int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}
	
	protected Map<String,Lang> name;
	@Override public Map<String,Lang> getName() {return name;}
	@Override public void setName(Map<String,Lang> name) {this.name = name;}
	
	protected Map<String,Description> description;
	@Override public Map<String,Description> getDescription() {return description;}
	@Override public void setDescription(Map<String,Description> description) {this.description = description;}
	
	@Override public <P extends EjbWithCode> P getParent() {return null;}
	@Override public <P extends EjbWithCode> void setParent(P parent) {}
	
	private Graphic graphic;
	@Override public Graphic getGraphic() {return graphic;}
	@Override public void setGraphic(Graphic graphic) {this.graphic=graphic;}
	
	public boolean equals(Object object)
	{
        return (object instanceof GraphicShape) ? id == ((GraphicShape) object).getId() : (object == this);
    }
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		sb.append(" ").append(code);
		return sb.toString();
	}
	
}