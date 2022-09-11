package net.sf.ahtutils.doc.word;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import net.sf.ahtutils.xml.project.Responsibilities;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.jeesl.model.xml.system.security.Role;
import org.jeesl.model.xml.system.security.Roles;
import org.jeesl.model.xml.system.security.Staff;
import org.jeesl.model.xml.system.security.Staffs;
import org.jeesl.model.xml.system.security.User;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTextDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordResponsibilityMatrixFactory
{
	private final static Logger logger = LoggerFactory.getLogger(WordResponsibilityMatrixFactory.class);

	private String staff = "staff";
	private String colorcCode = "008000";
	private String comma = ", ";
	private String primaryCode = "\u2611";
	private String secondaryCode = "\u2713";
	private String white = "FFFFFF";
	private String arialFont = "Arial";
	private String wingdingsFont = "Wingdings";
	private String primary = "primary";
	private String secondary = "secondary";
	

	public int tableWidth     = 10000;
	public int firstColWidth  = 6500;
	public int otherColWidth  = 800;
	public int rowheight      = 5000;

	public static enum Status {primary, secondary}

	public WordResponsibilityMatrixFactory()
	{

	}

	public void buildWord(File fDst, Responsibilities responsibilites) {

		XWPFDocument document = new XWPFDocument();

		Roles roles = responsibilites.getRoles();

		Map<String, Integer> roleIndex = new HashMap<String, Integer>();
		int index = 1;

		if (roles != null) {

			XWPFTable tab = document.createTable();
			CTTblWidth width = tab.getCTTbl().addNewTblPr().addNewTblW();
			width.setType(STTblWidth.DXA);
			width.setW(BigInteger.valueOf(tableWidth));

			XWPFTableRow row = tab.getRow(0);
			row.setHeight(rowheight);
			
			XWPFParagraph paragraph = row.getCell(0).getParagraphs().get(0);
			paragraph.setAlignment(ParagraphAlignment.CENTER);
			
			XWPFRun run = paragraph.createRun();
			run.setBold(true);
			run.setItalic(false);
			run.setFontFamily(arialFont);
			run.setFontSize(16);
			run.setColor(white);
			run.setText(staff);

			row.getCell(0).setVerticalAlignment(XWPFVertAlign.CENTER);
			row.getCell(0).setColor(colorcCode);

			XWPFTableCell cell0 = row.getCell(0);
			cell0.setVerticalAlignment(XWPFVertAlign.CENTER);


			CTTblWidth wid = cell0.getCTTc().addNewTcPr().addNewTcW();
			wid.setType(STTblWidth.DXA);
			wid.setW(BigInteger.valueOf(firstColWidth));
			
			for (Role role : roles.getRole()) {

				roleIndex.put(role.getLabel(), index++);
				XWPFTableCell cell = row.addNewTableCell();

				CTTextDirection textDirec = cell.getCTTc().addNewTcPr()
						.addNewTextDirection();
				textDirec.setVal(STTextDirection.BT_LR);
				
				cell.setVerticalAlignment(XWPFVertAlign.CENTER);
				
				wid = cell.getCTTc().addNewTcPr().addNewTcW();
				wid.setType(STTblWidth.DXA);
				wid.setW(BigInteger.valueOf(otherColWidth));
				
				paragraph = cell.getParagraphs().get(0);
				paragraph.setAlignment(ParagraphAlignment.CENTER);
				paragraph.setIndentationHanging(500);
				
				run = paragraph.createRun();
				run.setBold(true);
				run.setItalic(false);
				run.setFontFamily(arialFont);
				run.setFontSize(16);
				run.setColor(white);
				run.setText(role.getLabel());
				cell.setColor(colorcCode);
				
				cell.setVerticalAlignment(XWPFVertAlign.CENTER); 
			}

			for (User user : responsibilites.getUser()) {

				row = tab.createRow();
				
				paragraph = row.getCell(0).getParagraphs().get(0);
				row.getCell(0).setVerticalAlignment(XWPFVertAlign.CENTER);
				
				run = paragraph.createRun();
				run.setBold(false);
				run.setItalic(false);
				run.setFontFamily(arialFont);
				run.setFontSize(12);
				run.setText(user.getLastName() + comma + user.getFirstName());
				
				Staffs staffs = user.getStaffs();

				Map<Integer, String> createRows = new TreeMap<Integer, String>();

				for (Staff staff : staffs.getStaff()) {

					Role role = staff.getRole();
					net.sf.ahtutils.xml.status.Status status = staff
							.getStatus();
					createRows.put(roleIndex.get(role.getLabel()),
							status.getCode());
				}

				for (Map.Entry entry : createRows.entrySet()) {

					if (primary.equalsIgnoreCase(entry.getValue().toString())) {

						row.getCell((Integer) entry.getKey()).setVerticalAlignment(XWPFVertAlign.CENTER);
						
						paragraph = row
								.getCell((Integer) entry.getKey())
								.getParagraphs().get(0);

						paragraph.setAlignment(ParagraphAlignment.CENTER);
						run = paragraph.createRun();
						run.setBold(false);
						run.setItalic(false);
						run.setFontSize(14);
						run.setFontFamily(wingdingsFont);
						run.setText(primaryCode);

					} else if (secondary.equalsIgnoreCase(entry.getValue()
							.toString())) {

						row.getCell((Integer) entry.getKey()).setVerticalAlignment(XWPFVertAlign.CENTER);
						
						paragraph = row
								.getCell((Integer) entry.getKey())
								.getParagraphs().get(0);
						paragraph.setAlignment(ParagraphAlignment.CENTER);
						
						run = paragraph.createRun();
						run.setBold(false);
						run.setItalic(false);
						run.setFontSize(14);
						run.setFontFamily(wingdingsFont);
						run.setText(secondaryCode);
					}
				}

			}

			FileOutputStream outStream = null;
			try {
				outStream = new FileOutputStream(new File(
						fDst.getAbsolutePath()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			try {
				document.write(outStream);
				outStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		logger.info("Building for " + responsibilites.getUser().size()
				+ " users file: " + fDst.getAbsolutePath());
	}
}