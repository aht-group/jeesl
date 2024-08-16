package org.jeesl.factory.xlsx.module.survey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeesl.api.facade.module.survey.JeeslSurveyCoreFacade;
import org.jeesl.api.facade.module.survey.JeeslSurveyTemplateFacade;
import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.factory.ejb.module.survey.EjbSurveyAnswerFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyMatrixFactory;
import org.jeesl.factory.ejb.module.survey.EjbSurveyOptionFactory;
import org.jeesl.factory.xlsx.io.report.XlsCellFactory;
import org.jeesl.factory.xlsx.io.report.XlsRowFactory;
import org.jeesl.factory.xlsx.io.report.XlsSheetFactory;
import org.jeesl.interfaces.controller.builder.SurveyCorrelationInfoBuilder;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurvey;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplate;
import org.jeesl.interfaces.model.module.survey.core.JeeslSurveyTemplateVersion;
import org.jeesl.interfaces.model.module.survey.correlation.JeeslSurveyCorrelation;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyAnswer;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyData;
import org.jeesl.interfaces.model.module.survey.data.JeeslSurveyMatrix;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOption;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyOptionSet;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveyQuestion;
import org.jeesl.interfaces.model.module.survey.question.JeeslSurveySection;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XlsSurveyDataFactory <L extends JeeslLang, D extends JeeslDescription,
							SURVEY extends JeeslSurvey<L,D,?,TEMPLATE,DATA>,
							
							
							TEMPLATE extends JeeslSurveyTemplate<L,D,?,TEMPLATE,VERSION,?,?,SECTION,OPTIONS,?>,
							VERSION extends JeeslSurveyTemplateVersion<L,D,TEMPLATE>,

							SECTION extends JeeslSurveySection<L,D,TEMPLATE,SECTION,QUESTION>,
							QUESTION extends JeeslSurveyQuestion<L,D,SECTION,?,?,?,?,?,OPTIONS,OPTION,?>,
							
							ANSWER extends JeeslSurveyAnswer<L,D,QUESTION,MATRIX,DATA,OPTION>,
							MATRIX extends JeeslSurveyMatrix<L,D,ANSWER,OPTION>,
							DATA extends JeeslSurveyData<L,D,SURVEY,ANSWER,CORRELATION>,
							OPTIONS extends JeeslSurveyOptionSet<L,D,TEMPLATE,OPTION>,
							OPTION extends JeeslSurveyOption<L,D>,
							CORRELATION extends JeeslSurveyCorrelation<DATA>>
{
	final static Logger logger = LoggerFactory.getLogger(XlsSurveyDataFactory.class);
	
	private final String localeCode;
	
	public static enum PttBucket {surveyDataLoad,surveyCellLoad}
	
	private ProcessingTimeTracker ptt;
	public ProcessingTimeTracker getPtt() {return ptt;}
	public void setPtt(ProcessingTimeTracker ptt) {this.ptt = ptt;}

	private final JeeslSurveyTemplateFacade<?,TEMPLATE,VERSION,?,?,SECTION,QUESTION,?,OPTIONS,OPTION> fTemplate;
	private final JeeslSurveyCoreFacade<L,D,SURVEY,?,?,SECTION,QUESTION,ANSWER,MATRIX,DATA,CORRELATION> fSurvey;

	private final EjbSurveyMatrixFactory<ANSWER,MATRIX,OPTION> efMatrix;
	private final EjbSurveyOptionFactory<QUESTION,OPTION> efOption;

	private final XlsSurveyQuestionFactory<QUESTION,OPTIONS,OPTION> xfQuestion;

	private XlsSurveyAnswerFactory<L,QUESTION,ANSWER,MATRIX,OPTION> xfAnswer;

	private SurveyCorrelationInfoBuilder<CORRELATION> correlationBuilder;
	
	//Simple
	private final Map<SECTION,Integer> mapSizeSection;
	private final Map<QUESTION,Integer> mapSizeQuestion;
	private final Map<QUESTION,List<OPTION>> mapOptions;
	
	//Full
	private Map<Long, HeaderData> sectionHeaders;
	private Map<Long, HeaderData> questionHeaders;
	private CellStyle style;
	String answerTypes[] = {"Yes/No","Number","Natural Number","Score","Option","Text","Remark"};
	
	
	public XlsSurveyDataFactory(String localeCode,
			JeeslSurveyTemplateFacade<?,TEMPLATE,VERSION,?,?,SECTION,QUESTION,?,OPTIONS,OPTION> fTemplate,
			JeeslSurveyCoreFacade<L,D,SURVEY,?,?,SECTION,QUESTION,ANSWER,MATRIX,DATA,CORRELATION> fSurvey,
			EjbSurveyMatrixFactory<ANSWER,MATRIX,OPTION> efMatrix,
			EjbSurveyOptionFactory<QUESTION,OPTION> efOption,
			SurveyCorrelationInfoBuilder<CORRELATION> builder)
	{
		this.localeCode = localeCode;
		this.fTemplate=fTemplate;
		this.fSurvey = fSurvey;
//		this.efTemplate = efTemplate;
		this.efMatrix = efMatrix;
		this.efOption = efOption;
		this.correlationBuilder = builder;

		xfQuestion = new XlsSurveyQuestionFactory<>(efOption);
		
		mapSizeSection = new HashMap<SECTION,Integer>();
		mapSizeQuestion = new HashMap<QUESTION,Integer>();
		mapOptions = new HashMap<QUESTION,List<OPTION>>();
	}
	
	public void buildSimple(Sheet sheet, TEMPLATE t, List<DATA> list)
	{		
		style = sheet.getWorkbook().createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		xfAnswer = new XlsSurveyAnswerFactory<>(localeCode,style);

		simpleCalculateSizes(t);
		
		MutableInt rowNr = new MutableInt(0);
		simpleHeader(sheet,t,rowNr);
		simpleData(sheet,t,rowNr,list);
	}
	
	private void simpleCalculateSizes(TEMPLATE template)
	{
		for(SECTION section : template.getSections())
		{
			int sizeSection = 0;
			for(QUESTION question : section.getQuestions())
			{
				if(BooleanComparator.active(question.getShowMatrix()))
				{
					mapOptions.put(question,question.getOptions());
				}
				int sizeQuestion = xfQuestion.toSize(question);
				mapSizeQuestion.put(question,sizeQuestion);
				sizeSection = sizeSection + sizeQuestion;
			}			
			mapSizeSection.put(section,sizeSection);
		}
	}
	
	private void simpleHeader(Sheet sheet, TEMPLATE template, MutableInt rowNr)
	{
		MutableInt colNrSection = new MutableInt(0);
		MutableInt colNrQuestion = new MutableInt(0);
		MutableInt colNrMatrix = new MutableInt(0);
		
		Row rowSection = XlsRowFactory.build(sheet,rowNr);
		Row rowQuestion = XlsRowFactory.build(sheet,rowNr);
		Row rowMatrix = XlsRowFactory.build(sheet,rowNr);
		
		colNrSection.add(correlationBuilder.getDataFields());
		colNrQuestion.add(correlationBuilder.getDataFields());
		colNrMatrix.add(correlationBuilder.getDataFields());
		
		for(SECTION section : template.getSections())	
		{
			logger.info("Size: "+mapSizeSection.get(section));
			XlsCellFactory.build(rowSection, colNrSection, style, section.getCode(), mapSizeSection.get(section));
			
			for(QUESTION question : section.getQuestions())
			{
				XlsCellFactory.build(rowQuestion, colNrQuestion, style, question.getCode(), mapSizeQuestion.get(question));
				if(BooleanComparator.active(question.getShowMatrix()))
				{
					List<OPTION> oRows = efOption.toRows(mapOptions.get(question));
					List<OPTION> oCols = efOption.toColumns(mapOptions.get(question));
					for(OPTION oRow : oRows)
					{
						for(OPTION oCol : oCols)
						{
							XlsCellFactory.build(rowMatrix, colNrMatrix, style, oRow.getCode()+"-"+oCol.getCode(), 1);
						}
					}
				}
				else{colNrMatrix.add(mapSizeQuestion.get(question));}
			}
		}
	}
	
	private void simpleData(Sheet sheet, TEMPLATE template, MutableInt rowNr, List<DATA> list)
	{
		MutableInt colNr = new MutableInt(0);
		for(int index=0;index<list.size();index++)
		{
			logger.info(index+" / "+list.size());
			DATA data = list.get(index);
			Row row = XlsRowFactory.build(sheet,rowNr);
			colNr.setValue(0);
			correlationBuilder.init(data.getCorrelation());
			for(int i=0;i<correlationBuilder.getDataFields();i++)
			{
				XlsCellFactory.build(row, colNr, style, correlationBuilder.get(i), 1);
			}
			
			ptt.pB(PttBucket.surveyDataLoad);
			data = fSurvey.load(data);
			ptt.rB(PttBucket.surveyDataLoad);
			
			ptt.pB(PttBucket.surveyCellLoad);
			List<MATRIX> cells = fSurvey.fCells(data.getAnswers());
			ptt.rB(PttBucket.surveyCellLoad);
			
			Nested2Map<OPTION,OPTION,MATRIX> matrix = efMatrix.build(cells);
 			Map<QUESTION,ANSWER> map = EjbSurveyAnswerFactory.toQuestionMap(data.getAnswers());
			
			for(SECTION section : template.getSections())	
			{
				for(QUESTION question : section.getQuestions())
				{
					if(!map.containsKey(question)){mapSizeQuestion.get(question);}
					else
					{
						ANSWER answer = map.get(question);
						if(BooleanComparator.active(question.getShowMatrix()))
						{
							List<OPTION> oRows = efOption.toRows(mapOptions.get(question));
							List<OPTION> oCols = efOption.toColumns(mapOptions.get(question));
							for(OPTION oRow : oRows)
							{
								for(OPTION oCol : oCols)
								{
									if(matrix.containsKey(oRow,oCol)){xfAnswer.build(row, colNr, answer.getQuestion(), matrix.get(oRow,oCol));}
									else{XlsCellFactory.build(row, colNr, style, "", 1);}
								}
							}
						}
						else
						{
							xfAnswer.build(row, colNr, answer);
						}
					}
				}
			}
		}
	}
	
	public Workbook build(SURVEY survey, List<DATA> list)
	{
		// Reset the section and question overview
		sectionHeaders  = new HashMap<Long, HeaderData>();
		questionHeaders = new HashMap<Long, HeaderData>();
		
		// Create an Excel workbook to be filled with given data
		Workbook wb = new XSSFWorkbook();
		
		// Create a sheet in the new workbook to write data into
		Sheet sheet = XlsSheetFactory.getSheet(wb,localeCode);
		
		// Create a style for the cells
		style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
				 
		//Get data for lazy loading
		TEMPLATE template = survey.getTemplate();
		if (fTemplate!=null) {template = fTemplate.load(template,false,false);}
		List<SECTION> sections = template.getSections();
		for (SECTION section : sections)
		{
			if (fSurvey!=null) {section = fSurvey.load(section);}
		}
		
		// The survey object holds information about the structure of the survey
		logger.info("Using survey: "+survey.getName().get(localeCode).getLang());
		logger.info("Processing Data");
		
		// Define the current row and column of the processing
		Row row = sheet.createRow(0);
		MutableInt columnNr = new MutableInt(1);
		
		// Iterate all sections available in the template
		for (SECTION section : sections)
		{
			if (fSurvey!=null) {section = fSurvey.load(section);}
			// Keep in digital mind the width of the section as a sum of all question lengths
			int sectionWidth = 0;
			
			// Handle the individual questions in the section
			for (QUESTION question : section.getQuestions())
			{
				if (fSurvey!=null) {question = fSurvey.load(question);}
				// First, render the headers of the question based on the width
				// Then add that width to the width of the section
				int questionWidth = calculateWidth(question);
				sectionWidth = sectionWidth + questionWidth;
				
				// Render the standard header based on the options named in the answerTypes String Array
				columnNr = buildCells(answerTypes, row, columnNr);
				
				// The buildCells returns to the columnNr where it started after rendering the String Array
				// Next, build the valid options rendered with X in the next row to indicate if answer type is set for the question
				row = sheet.createRow(row.getRowNum()+1);
				columnNr = buildValidOptionsCells(answerTypes, row, columnNr, question);
				
				// Get back to the previous row
				row = sheet.createRow(row.getRowNum()-1);
				// The buildValidOptionsCells also returns to the columnNr where it started after rendering the String Array
				
			}
			
			
			
			//TODO: Render the section header
		}
		
		// Now shift the row to the first data row by skipping the valid option row in first iteration
		row = sheet.createRow(row.getRowNum()+1);
		
		// Reset the column number to be at the very beginning to start with correlation data
		columnNr = new MutableInt(0);
		
		//for (DATA data : survey.getSurveyData())
		//{
			row = sheet.createRow(row.getRowNum()+1);
			// Do the lazy loading of the data object
			DATA data = survey.getSurveyData().get(0);
			if (fSurvey!=null) {data = fSurvey.load(data);}

			// Initialize the correlation builder with the given data objects correlation object
			correlationBuilder.init(data.getCorrelation());

			// Prepare a list of all questions and related answers
			Map<QUESTION, ANSWER> answers = EjbSurveyAnswerFactory.toQuestionMap(data.getAnswers());

			// Render the correlation objects info
			for (int c = 0; c<correlationBuilder.getDataFields(); c++)
			{
				XlsCellFactory.build(row, columnNr, style, correlationBuilder.get(c+1), 1);
			}
			
			// Render the Answers
			for (SECTION section : sections)
			{
				if (fSurvey!=null) {section = fSurvey.load(section);}
				
				// Handle the individual questions in the section
				for (QUESTION question : section.getQuestions())
				{
					if (fSurvey!=null) {question = fSurvey.load(question);}
					ANSWER answer = answers.get(question);
					if (fSurvey!=null) {answer = fSurvey.load(answer);}
					String titleCell = correlationBuilder.get(1);
					logger.info("Rendering answer of " +titleCell +" at " +row.getRowNum() +"," +columnNr);
					columnNr = buildAnswerCells(answerTypes, row, columnNr, answer);
				}
			}
			// Reset the columnNr 
			// Shift the row
		//}
		return wb;
	}
	
	public void renderData(Row startRow, List<DATA> surveyData)
	{
		int rowIterator = startRow.getRowNum();
		for (int i=0; i<6; i++)
		{
			MutableInt columnNr = new MutableInt(0);
			Row currentRow    = startRow.getSheet().createRow(rowIterator);
			
			logger.info("Iteration " +i);
			DATA data = surveyData.get(i);
			correlationBuilder.init(data.getCorrelation());
			if (fSurvey!=null) {data = fSurvey.load(data);}
			logger.info("Using " +data.getAnswers().size() +" answers. IN ROW "+rowIterator);
			for (int c = 0; c<correlationBuilder.getDataFields(); c++)
			{
				XlsCellFactory.build(currentRow, columnNr, style, correlationBuilder.get(c+1), 1);
			}
			Map<QUESTION, ANSWER> infoInventory = EjbSurveyAnswerFactory.toQuestionMap(data.getAnswers());
			logger.info("No of questions in inventory Map: " +infoInventory.size());
			for (QUESTION question : infoInventory.keySet())
			{
				ANSWER answer = infoInventory.get(question);
				String titleCell = correlationBuilder.get(1);
				logger.info("Rendering answer of " +titleCell +" at " +rowIterator +"," +columnNr);
				
				columnNr = buildAnswerCells(answerTypes, currentRow, columnNr, answer);
			}
			rowIterator++;
		}
	}
	
	public int renderHeaderData(Row row, Map<Long, HeaderData> headerData, boolean renderQuestionHeader)
	{
		MutableInt columnNr = new MutableInt(correlationBuilder.getDataFields());
		Row subRow    = null;
		Row subsubRow = null;
		Row answerRow = null;
		Row matrixRow = null;
		
		if (renderQuestionHeader) 
		{
			subRow    = row.getSheet().createRow(row.getRowNum()+1);
			subsubRow = row.getSheet().createRow(row.getRowNum()+2);
			answerRow = row.getSheet().createRow(row.getRowNum()+3);
			matrixRow = row.getSheet().createRow(row.getRowNum()+4);
		}
		for (Long id : headerData.keySet())
		{
			HeaderData header = headerData.get(id);
			XlsCellFactory.build(row, columnNr, style, header.name, header.width);
			
			if (renderQuestionHeader)
			{
				// Render the Option header
				columnNr.subtract(header.width);
				columnNr = buildCells(answerTypes, subRow, columnNr);
				
				// Render the valid options
				ANSWER answer = header.answer;
				if (fSurvey!=null) {answer = fSurvey.load(answer);}
				QUESTION question = answer.getQuestion();
				if (fSurvey!=null) {question = fSurvey.load(question);}
				columnNr = buildValidOptionsCells(answerTypes, subsubRow, columnNr, question);

				// Render the Answers
				//columnNr = buildAnswerCells(answerTypes, answerRow, columnNr, answer);
				
				// Render the Matrix
				//columnNr = buildMatrixCells(answerTypes, answerRow, columnNr, answer);
				
				// Get to the begin of the next one
				//columnNr.add(header.width);
				
				// Get to the begin of the next one
				columnNr.add(header.width);
			}
			
			
		}
		
		// Finally autosize columns
		if (renderQuestionHeader)
		{
			int lastColumn = columnNr.intValue();
			for (int i=0; i<lastColumn; i++)
			{
				row.getSheet().autoSizeColumn(i);
			}
		}
		if (renderQuestionHeader) {return answerRow.getRowNum();} else {return row.getRowNum();} 
		
	}
	
	private MutableInt buildCells(String[] answerTypes, Row row, MutableInt columnNr)
	{
		for (String s : answerTypes)
		{
			XlsCellFactory.build(row, columnNr, style, s, 1);
		}
		columnNr.subtract(answerTypes.length);
		return columnNr;
	}
	
	private MutableInt buildValidOptionsCells(String[] answerTypes, Row row, MutableInt columnNr, QUESTION question)
	{
		for (String s : answerTypes)
		{
			if (s.equals("Yes/No")){						
				if (question.getShowBoolean()!= null && question.getShowBoolean()) {XlsCellFactory.build(row, columnNr, style, "X", 1);} else {columnNr.add(1);}}
			if (s.equals("Number"))
				{if(question.getShowDouble()!= null && question.getShowDouble()) {XlsCellFactory.build(row, columnNr, style, "X", 1);} else {columnNr.add(1);}}
			if (s.equals("Natural Number")){
				if (question.getShowInteger()!= null && question.getShowInteger()) {XlsCellFactory.build(row, columnNr, style, "X", 1);} else {columnNr.add(1);}}
			if (s.equals("Score")){
				if (question.getShowScore()!= null && question.getShowScore()) {XlsCellFactory.build(row, columnNr, style, "X", 1);} else {columnNr.add(1);}}
			if (s.equals("Multi Option")){
				if (question.getShowSelectMulti()!= null && question.getShowSelectMulti()) {XlsCellFactory.build(row, columnNr, style, "X", 1);} else {columnNr.add(1);}}
			if (s.equals("Option")){
				if (question.getShowSelectOne()!= null && question.getShowSelectOne()) {XlsCellFactory.build(row, columnNr, style, "X", 1);} else {columnNr.add(1);}}
			if (s.equals("Text")){
				if (question.getShowText()!= null && question.getShowText()) {XlsCellFactory.build(row, columnNr, style, "X", 1);} else {columnNr.add(1);}}
			if (s.equals("Remark")){
				if (question.getShowRemark()!= null && question.getShowRemark()) {XlsCellFactory.build(row, columnNr, style, "X", 1);} else {columnNr.add(1);}}
			if (s.equals("Matrix")){
				if (question.getShowMatrix()!= null && question.getShowMatrix()) {XlsCellFactory.build(row, columnNr, style, "X", 1);} else {columnNr.add(1);}}
	
		}
		//columnNr.subtract(answerTypes.length);
		return columnNr;
	}
	
	private MutableInt buildAnswerCells(String[] answerTypes, Row row, MutableInt columnNr, ANSWER answer)
	{
		QUESTION question = answer.getQuestion();
		for (String s : answerTypes)
		{
			if (s.equals("Yes/No")){
				if (question.getShowBoolean()!= null && question.getShowBoolean() && answer.getValueBoolean()!=null) {XlsCellFactory.build(row, columnNr, style, answer.getValueBoolean(), 1);} else {columnNr.add(1);}}
			if (s.equals("Number")){
				if (question.getShowDouble()!= null && question.getShowDouble() && answer.getValueDouble()!=null) {XlsCellFactory.build(row, columnNr, style, answer.getValueDouble(), 1);} else {columnNr.add(1);}}
			if (s.equals("Natural Number")){
				if (question.getShowInteger()!= null && question.getShowInteger() && answer.getValueNumber()!=null) {XlsCellFactory.build(row, columnNr, style, answer.getValueNumber(), 1);} else {columnNr.add(1);}}
			if (s.equals("Score")){
				if (question.getShowScore()!= null && question.getShowScore() && answer.getScore()!=null) {XlsCellFactory.build(row, columnNr, style, answer.getScore(), 1);} else {columnNr.add(1);}}
			if (s.equals("Multi Option")){
				if (question.getShowSelectMulti()!= null && question.getShowSelectMulti() && answer.getOptions()!=null) {XlsCellFactory.build(row, columnNr, style, renderOptionsSingle(answer.getOptions()), 1);} else {columnNr.add(1);}}
			if (s.equals("Option")){
				if (question.getShowSelectOne()!= null && question.getShowSelectOne() && answer.getOption()!=null) {XlsCellFactory.build(row, columnNr, style, renderOptionsSingle(answer.getOption()), 1);} else {columnNr.add(1);}}
			if (s.equals("Text")){
				if (question.getShowText()!= null && question.getShowText() && answer.getValueText()!=null) {XlsCellFactory.build(row, columnNr, style, answer.getValueText(), 1);} else {columnNr.add(1);}}
			if (s.equals("Remark")){
				if (question.getShowRemark()!= null && question.getShowRemark() && answer.getRemark()!=null) {XlsCellFactory.build(row, columnNr, style, answer.getRemark(), 1);} else {columnNr.add(1);}}
			if (s.equals("Matrix")){
				if (question.getShowMatrix()!= null && question.getShowMatrix() && answer.getMatrix()!=null) {XlsCellFactory.build(row, columnNr, style, answer.getMatrix(), 1);} else {columnNr.add(1);}}
		}
		//columnNr.subtract(answerTypes.length);
		return columnNr;
	}
	
	
	private MutableInt buildMatrixCells(String[] answerTypes, Row firstRow, MutableInt columnNr, ANSWER answer)
	{
		//logger.info("Starting MATRIX --------- ");
		final int offset = columnNr.intValue();
		QUESTION question = answer.getQuestion();
		if (fSurvey!=null) {answer = fSurvey.load(answer);}
		if (fSurvey!=null) {question = fSurvey.load(question);}
		if (question.getShowMatrix()!=null && question.getShowMatrix() && answer.getMatrix() != null && !answer.getMatrix().isEmpty())
		{
			EjbSurveyMatrixFactory eSMF = new EjbSurveyMatrixFactory(answer.getMatrix().get(0).getClass());
			Nested2Map<OPTION,OPTION,MATRIX> map = eSMF.build(answer.getMatrix());
			// Build a Map to get cell data based on the Row and Column
			
			// Get a sorted collection of rows and columns (OPTION objects to request MATRIX CELL)
			TreeMap<Integer, OPTION> rowData    = new TreeMap<Integer, OPTION>();
			TreeMap<Integer, OPTION> columnData = new TreeMap<Integer, OPTION>();
			for (OPTION row : efOption.toRows(question.getOptions()))
				{
					rowData.put(row.getPosition(), row);
				}
			for (OPTION column : efOption.toColumns(question.getOptions()))
				{
					columnData.put(column.getPosition(), column);
				}
			
			// Iterate the row and column keys (maybe with gaps like 1, 8, 9, 10) and fill cells
			Row row = firstRow;
			final int startRow = firstRow.getRowNum();
			boolean firstIteration = true;
			for (Integer rowKey : rowData.keySet())
			{
				if (!firstIteration ) {row = row.getSheet().createRow(startRow +rowKey);}
				for (Integer columnKey : columnData.keySet())
				{
					columnNr = new MutableInt(offset + columnKey);
					MATRIX m = map.get(rowData.get(rowKey), columnData.get(columnKey));
					if (m!= null && m.getAnswer()!=null)
					{
						Object cellContent = buildAnswer(m);
						logger.info("Row " +rowKey +" : Column " +columnKey +" will be set to " +cellContent.toString());
						XlsCellFactory.build(row, columnNr, style, cellContent, 1);
					}
				}
				firstIteration = false;
			}
		}
		return new MutableInt(offset);
	}
	
	public Object buildAnswer(MATRIX m)
	{
		Object value = null;
		ANSWER answer = m.getAnswer();
		if (fSurvey!=null) {answer = fSurvey.load(answer);}
		QUESTION question = answer.getQuestion();
		if (fSurvey!=null) {question = fSurvey.load(question);}
		for (String s : answerTypes)
		{
			if (s.equals("Yes/No") && question.getShowBoolean()!= null && question.getShowBoolean() && m.getValueBoolean()!=null) 
			{
				if (value==null)
				{
					value = m.getValueBoolean();
				} else
				{
					value = value.toString();
					value = value + ", " +m.getValueBoolean().toString();
				}
			}
			if (s.equals("Number") && question.getShowDouble()!= null && question.getShowDouble() && m.getValueDouble()!=null)
				if (value==null)
				{
					value = m.getValueDouble();
				} else
				{
					value = value.toString();
					value = value + ", " +m.getValueDouble().toString();
				}
			if (s.equals("Natural Number") && question.getShowInteger()!= null && question.getShowInteger() && m.getValueNumber()!=null)
				if (value==null)
				{
					value = m.getValueNumber();
				} else
				{
					value = value.toString();
					value = value + ", " +m.getValueNumber().toString();
				}
			if (s.equals("Option") && question.getShowSelectOne()!= null && question.getShowSelectOne() && m.getOption()!=null) 
				if (value==null)
				{
					value = renderOptionsSingle(m.getOption());
				} else
				{
					value = value.toString();
					value = value + ", " +renderOptionsSingle(m.getOption());
				}
			if (s.equals("Text") && question.getShowText()!= null && question.getShowText() && m.getValueText()!=null)
				if (value==null)
				{
					value = m.getValueText();
				} else
				{
					value = value.toString();
					value = value + ", " +m.getValueText();
				}
		}
		if (value==null) {value="not set";}
		return value;
	}	
	
	public int addQuestionToSection(ANSWER answer)
	{
		SECTION section   = answer.getQuestion().getSection();
		Long    sectionId = section.getId();
		Long   questionId = answer.getQuestion().getId();
		HeaderData headerInfo;
		HeaderData headerInfoForQuestion = new HeaderData();
		headerInfoForQuestion.name = answer.getQuestion().getQuestion();
		headerInfoForQuestion.answer = answer;
		int questionLength = answerTypes.length;
		
		// See if section is already present in map
		if (sectionHeaders.containsKey(sectionId))
		{
			headerInfo = sectionHeaders.get(sectionId);
		}
		else
		{
			headerInfo = new HeaderData();
			headerInfo.name = section.getName().get(localeCode).getLang();
		}
		
		if (fSurvey!=null) {answer = fSurvey.load(answer);}
		// See if Matrix options are to be shown and if so, see if it extends standard range of 9 cells
		
		QUESTION question = answer.getQuestion();
		if (fSurvey!=null) {question = fSurvey.load(question);}
		if (question.getShowMatrix()!=null && question.getShowMatrix() && answer.getMatrix()!=null)
		{
			if (efOption.toColumns(question.getOptions()).size() >= answerTypes.length)
			{
			//	questionLength = efOption.toColumns(question.getOptions()).size();
			}
		}
		headerInfo.width = headerInfo.width + questionLength;
		headerInfoForQuestion.width = questionLength;
		sectionHeaders.put(sectionId, headerInfo);
		questionHeaders.put(questionId, headerInfoForQuestion);
		return questionLength;
	}
	
	private String renderOptionsSingle(OPTION o)
	{
		return o.getName().get(localeCode).getLang();
	}
	
	private String renderOptionsSingle(List<OPTION> list)
	{
		StringBuilder sb = new StringBuilder();
		for (OPTION o : list)
		{
			sb.append("- ");
			sb.append(renderOptionsSingle(o));
			sb.append("\n");
		}
		String formattedList = sb.toString();
		StringUtils.trim(formattedList);
		return formattedList;
	}
	
	private int calculateWidth(QUESTION question)
	{
		int questionLength = answerTypes.length;
		if (question.getShowMatrix()!=null && question.getShowMatrix())
		{
			if (efOption.toColumns(question.getOptions()).size() >= answerTypes.length)
			{
				questionLength = efOption.toColumns(question.getOptions()).size();
			}
		}
		return questionLength;
	}
	
	private class HeaderData
	{
		int offset;
		int width;
		String name;
		ANSWER answer;
	}
}