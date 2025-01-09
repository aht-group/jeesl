package org.jeesl.jsf.components;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.jsf.jx.util.ComponentAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@FacesComponent("org.jeesl.jsf.components.QrCode")
public class QrCode extends UIOutput
{
	final static Logger logger = LoggerFactory.getLogger(QrCode.class);
	private static enum Properties {styleClass,qrValue}
	
	@Override public boolean getRendersChildren(){return true;}
	
	private BitMatrix generateQrCode(String content) throws WriterException
	{
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
		
		return qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 0, 0);
	}
	
	// None of the options in the QRCodeWriter to remove the margin work, so we need to find the amount
	// of empty rows to determine the margin manually (it's the same on all sides), then apply that to the SVG drawing.
	private int findQrMargin(BitMatrix qrBitMatrix)
	{
		int matrixWidth = qrBitMatrix.getWidth();
		int matrixHeight = qrBitMatrix.getHeight();
		BitArray row = new BitArray(matrixWidth);
		
		int margin = 0;
		boolean isRowEmpty = false;
		for (int yIndex = 0; yIndex < matrixHeight; ++yIndex)
		{
			row = qrBitMatrix.getRow(yIndex, row);
			for (int xIndex = 0; xIndex < matrixWidth; ++xIndex)
			{
				isRowEmpty = !row.get(xIndex);
				if (!isRowEmpty) { break; }
			}
			if (!isRowEmpty) { break; }
			margin++;
		}
		return margin;
	}
	
	private void encodePath(String content, FacesContext context) throws IOException, WriterException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		BitMatrix qrBitMatrix = generateQrCode(content);
		int margin = findQrMargin(qrBitMatrix);
		int matrixWidth = qrBitMatrix.getWidth() - 2 * margin;
		int matrixHeight = qrBitMatrix.getHeight() - 2 * margin;
		BitArray row = new BitArray(matrixWidth);
		
		double rectWidth = 100f / matrixWidth;
		double rectHeight = 100f / matrixHeight;
		
		for(int yIndex=0; yIndex<matrixHeight; ++yIndex)
		{
			row = qrBitMatrix.getRow(yIndex + margin, row);
			for(int xIndex = 0; xIndex < matrixWidth; ++xIndex)
			{
				if(row.get(xIndex + margin))
				{
					responseWriter.startElement("rect", this);
					responseWriter.writeAttribute("width", String.valueOf(rectWidth) + "%", null);
					responseWriter.writeAttribute("height", String.valueOf(rectHeight) + "%", null);
					responseWriter.writeAttribute("x", String.valueOf(xIndex * rectWidth) + "0%", null);
					responseWriter.writeAttribute("y", String.valueOf(yIndex * rectHeight) + "0%", null);
					responseWriter.endElement("rect");
				}
			}
		}
	}
	
	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.startElement("svg", this);
		responseWriter.writeAttribute("viewbox", "0 0 100 100", null);
		responseWriter.writeAttribute("preserveAspectRatio", "xMinYMin slice", null);
		
		try
		{
    		if(ComponentAttribute.available(Properties.styleClass, context, this))
    		{
    			responseWriter.writeAttribute("class", ComponentAttribute.get(Properties.styleClass.toString(), context, this), null);
    		}
    		if(ComponentAttribute.available(Properties.qrValue, context, this))
    		{
				encodePath(ComponentAttribute.get(Properties.qrValue.toString(), context, this), context);
    		}
		}
		catch (IOException | WriterException | JeeslNotFoundException e) {e.printStackTrace();}
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.endElement("svg");
	}
}