package org.jeesl.doc.word;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.jeesl.model.xml.io.label.Attribute;
import org.jeesl.model.xml.io.label.Diagrams;
import org.jeesl.model.xml.io.label.Entities;
import org.jeesl.model.xml.io.label.Entity;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.xsd.Container;
import org.jeesl.util.query.xpath.RevisionXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspose.words.BreakType;
import com.aspose.words.Cell;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FindReplaceDirection;
import com.aspose.words.FindReplaceOptions;
import com.aspose.words.ImportFormatMode;
import com.aspose.words.RowCollection;
import com.aspose.words.Table;
import com.aspose.words.Underline;

public final class EntityWordRenderer extends AbstractEntityWordRenderer
{
	final static Logger logger = LoggerFactory.getLogger(EntityWordRenderer.class);
	
	private final Document entityDoc,template,templateTable;
	private final Container categories;
	private final Container relationTypes;
	private final Entities entities;
    private HashMap<String,Container> statusContainer;
    private Diagrams diagrams;

    public EntityWordRenderer(final Document templateDoc,final Document templateTableDoc, final Entities entities, final Container categories, final Container relationTypes) throws Exception {this.entityDoc=templateDoc;this.template=templateDoc;this.templateTable=templateTableDoc;this.categories=categories;this.relationTypes=relationTypes;this.entities=entities;this.statusContainer=new HashMap<String,Container>();this.diagrams=null;}
    
	public EntityWordRenderer(final Document templateDoc,final Document templateTableDoc, final Entities entities, final Container categories, final Container relationTypes, final Diagrams diagrams) throws Exception
	{
		this.entityDoc=templateDoc;	
		this.template=templateDoc;
		this.templateTable=templateTableDoc;
		this.categories=categories;
		this.relationTypes=relationTypes;
		this.entities=entities;
		this.statusContainer=new HashMap<String,Container>();
		this.diagrams=diagrams;
	}

    public Document render(Entity entity, String savingDirectory, boolean renderStatus) throws Exception 
	{
		DocumentBuilder docBuilder = new DocumentBuilder(entityDoc);						
		Map<String, String> replacementTags = new HashMap<String, String>();	
		List<String> keys= new ArrayList<>();
		
//		StyleCollection tableStyles = entityDoc.getStyles().getByStyleIdentifier(StyleType.TABLE).getStyles();
//		for (Style s : tableStyles) {logger.info(s.getName());}
		
		keys.add("_ENTITY_");replacementTags.put("_ENTITY_", entity.getLangs().getLang().get(0).getTranslation());
		keys.add("_CATEGORY_");replacementTags.put("_CATEGORY_", categoryForCode(categories, entity.getCategory().getCode()));
		keys.add("_CLASS1_");replacementTags.put("_CLASS1_",	packageShrinker(entity.getCode().replace(FilenameUtils.getExtension(entity.getCode()), ""), 5));
		keys.add("_CLASS2_");replacementTags.put("_CLASS2_", FilenameUtils.getExtension(entity.getCode()));
		keys.add("_DESCRIPTION_");replacementTags.put("_DESCRIPTION_", entity.getDescriptions().getDescription().get(0).getValue().toString().trim());
			
		for (String s : keys)
		{
		    docBuilder.moveToDocumentStart();
		    if (s.equals("_CLASS2_")) {docBuilder.getFont().setBold(true);}
		    try{entityDoc.getRange().replace(s, replacementTags.get(s).toString(),new FindReplaceOptions(FindReplaceDirection.FORWARD));}
		    catch (Exception e1) {e1.printStackTrace();}
		    if (s.equals("_CLASS2_")) {docBuilder.getFont().setBold(false);};
		}
		
		List<Attribute> attrbs = new ArrayList<Attribute>();
		for (Attribute a:entity.getAttribute()){if(a.isBean()){attrbs.add(a);}}	
		
		boolean makerStatusTable = false;
		List<Attribute> attrbsStatusTable = new ArrayList<Attribute>();
		
		
		Table templateTable = template.getSections().get(0).getBody().getTables().get(0);
	    RowCollection templateRows = templateTable.getRows();
		
		Table table = entityDoc.getSections().get(0).getBody().getTables().get(0);
		RowCollection rows = table.getRows();
		 
		int rowHelper = 0; 
		for (Attribute a: attrbs)
		{
		    int cellHelperRow5=0;
			for (Cell c : rows.get(5+rowHelper).getCells())
			{
				c.getLastParagraph().getRuns().clear();
				docBuilder.moveTo(c.getFirstParagraph());				
				if (cellHelperRow5==0){c.getParagraphs().get(0).getRuns().clear();docBuilder.write(a.getCode().trim());}
				if (cellHelperRow5==1){renderRelationOrType(docBuilder, a);}	
				if (cellHelperRow5 == 2)
				{
				    c.getParagraphs().get(0).getRuns().clear();
				    logger.info("paragraphs count: " + c.getParagraphs().getCount());
				    if (a.getRelation()!=null && Objects.nonNull(a.getRelation().getEntity()))
				    {       
				        
				        Entity e = RevisionXpath.getEntity(entities, a.getRelation().getEntity().getCode());
				        docBuilder.getFont().setColor(Color.black);docBuilder.getFont().setItalic(false);
				        
				        if (a.getDescriptions().getDescription().get(0).getValue().trim()!="")
				        {
				            docBuilder.writeln(a.getDescriptions().getDescription().get(0).getValue().trim());
				        }
				        else if(a.getDescriptions().getDescription().get(0).getValue().trim()=="") 
				        {
				            docBuilder.writeln(e.getDescriptions().getDescription().get(0).getValue().trim());
				        }

				        String parentDiagram = ":";
				        if (Objects.nonNull(e.getDiagram()))
				        {
				            try{parentDiagram=diagramForCode(diagrams,e.getDiagram().getCode())+ ":";}catch(Exception e1){e1.printStackTrace();}
				        }
				        docBuilder.getFont().setColor(Color.gray);docBuilder.getFont().setItalic(true);
				        docBuilder.write("("+relationTypeForCode(relationTypes, a.getRelation().getType().getCode())+" to "+categoryForCode(categories, e.getCategory().getCode()) + ":" + parentDiagram +e.getLangs().getLang().get(0).getTranslation()+")");
				        docBuilder.getFont().setColor(Color.black);docBuilder.getFont().setItalic(false);
				    }	
				    else if (a.getRelation()==null)
				    {
				        docBuilder.write(a.getDescriptions().getDescription().get(0).getValue().trim());
				    }

				    if (renderStatus) 
				    {
				        if (a.getRelation()!=null && Objects.nonNull(a.getRelation().isDocOptionsTable())) {makerStatusTable=true;attrbsStatusTable.add(a);}

				        if (a.getRelation() !=null && Objects.nonNull(a.getRelation().isDocOptionsInline()))  
				        {
				            for (String s : statusContainer.keySet()){if(s.equals(a.getCode())){renderStatusInline(docBuilder,s);}}
				        }
				    }
				}
				cellHelperRow5++;
			}

			int cellHelperRow6=0;
			for (Cell c : rows.get(6+rowHelper).getCells())
			{
			    c.getParagraphs().get(0).getRuns().clear();;docBuilder.moveTo(c.getFirstParagraph());
			    if (cellHelperRow6==0)
			    {
			        docBuilder.write(a.getLangs().getLang().get(0).getTranslation().toString());
			    }
			    cellHelperRow6++;
			}
			table.appendChild(templateRows.get(5).deepClone(true));table.appendChild(templateRows.get(6).deepClone(true));	
			rowHelper=rowHelper+2;
		}		
		table.getLastRow().remove();table.getLastRow().remove();

		if (renderStatus&&makerStatusTable){renderStatusTable(entity, docBuilder, attrbsStatusTable);}
		return entityDoc;
	}

    private void renderStatusInline(DocumentBuilder docBuilder, String s)
    {
        docBuilder.writeln();
        docBuilder.getFont().setUnderline(Underline.SINGLE);
        docBuilder.write("Status-Table:");
        docBuilder.getFont().setUnderline(Underline.NONE);
        for (Status status : statusContainer.get(s).getStatus())
        { 
            logger.info("info about status parent: " + status.getParent().getCode());
            docBuilder.writeln();
            if(Objects.nonNull(status.getGraphic()) && status.getGraphic().getType().getCode().equals("svg")&&status.getGraphic().getFile().getData().getValue()!=null)
            {
                renderStatusSvg(docBuilder,(byte[])status.getGraphic().getFile().getData().getValue());
                docBuilder.write("");
            }				                
            else {docBuilder.write(" "); }
            docBuilder.writeln(status.getCode()+" = "+status.getLangs().getLang().get(0).getTranslation());
            if (Objects.nonNull(status.getDescriptions()) && status.getDescriptions().getDescription().get(0).getValue()!="")
            {
                docBuilder.getFont().setColor(Color.gray);docBuilder.getFont().setItalic(true);docBuilder.getFont().setSize(7);
                docBuilder.write("  (" + status.getDescriptions().getDescription().get(0).getValue() +")");
                docBuilder.getFont().setColor(Color.black);docBuilder.getFont().setItalic(false);docBuilder.getFont().setSize(9);
            }
//				                        docBuilder.writeln();
        }
    }

    private void renderRelationOrType(DocumentBuilder docBuilder, Attribute a)
    {
        if (a.getRelation()!=null && Objects.nonNull(a.getRelation().getEntity())) {docBuilder.write(relationTypeForCode(relationTypes, a.getRelation().getType().getCode()));}
        else if (a.getRelation()==null || Objects.isNull(a.getRelation().getEntity()))
        {
            if (a.getType().getCode().equals("bool")) {docBuilder.write("boolean");}
            if (a.getType().getCode().equals("text")) {docBuilder.write("string");}
            if (a.getType().getCode().equals("numberLong")) {docBuilder.write("long");}
            if (a.getType().getCode().equals("date")) {docBuilder.write("date");}
            if (a.getType().getCode().equals("numberInteger")) {docBuilder.write("integer");}
            if (a.getType().getCode().equals("long")) {docBuilder.write("long");}
            if (a.getType().getCode().equals("numberDouble")) {docBuilder.write("double");}
            if (a.getType().getCode().equals("numberDoubleAmount")) {docBuilder.write("double");}
        }
    }

    private void renderStatusTable(Entity entity, DocumentBuilder docBuilder, List<Attribute> attrbsStatusTable) throws Exception
    {
        docBuilder.moveToDocumentEnd();    
        docBuilder.insertBreak(BreakType.PAGE_BREAK);
        StatusWordRenderer statusRenderer = new StatusWordRenderer(this.templateTable, this.statusContainer, attrbsStatusTable, entity);
        docBuilder.insertDocument(statusRenderer.render(),ImportFormatMode.KEEP_SOURCE_FORMATTING).deepClone(true);
    }

    public Document render(final Entity entity, final String absolutePath, final HashMap<String, Container> statusContainer, final boolean renderStatus) throws Exception {this.statusContainer = statusContainer;Document doc = render(entity, absolutePath, renderStatus);return doc;}
    
}