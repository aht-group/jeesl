package org.jeesl.model.pojo.iot;

public class MatrixProcessorCursor
{
	public enum Orientation {horizontal,vertical}
	
	private Orientation orientation;
	private int row; public int getRow() {return row;}
	private int col; public int getColumn() {return col;}

	public static MatrixProcessorCursor clone(MatrixProcessorCursor cursor) {return new MatrixProcessorCursor(cursor.getRow(),cursor.getColumn());}
	public static MatrixProcessorCursor instance(int row, int col) {return new MatrixProcessorCursor(row,col);}
	private MatrixProcessorCursor(int row, int col)
	{
		this.row=row;
		this.col=col;
		orientation = Orientation.horizontal;
	}
	
	public MatrixProcessorCursor vertical() {orientation=Orientation.vertical; return this;}
	public MatrixProcessorCursor horizontal() {orientation=Orientation.horizontal; return this;}
	
	public MatrixProcessorCursor jump(int row, int col) {this.row=row; this.col=col; return this;}
	public MatrixProcessorCursor move(int rows, int cols) {row=row+rows; col=col+cols; return this;}
	
	public MatrixProcessorCursor move(MatrixProcessorCursor cursor, int row, int col) {this.row=cursor.getRow()+row; this.col=cursor.getColumn()+col; return this;}
	public MatrixProcessorCursor row(MatrixProcessorCursor cursor, int i) {row=cursor.getRow()+i; return this;}
	public MatrixProcessorCursor moveCol(MatrixProcessorCursor cursor, int i) {this.col=cursor.getColumn()+i; return this;}
	
	public MatrixProcessorCursor moveRows(int i) {row=row+i; return this;}
	public MatrixProcessorCursor moveColumns(int i) {col=col+i; return this;}
	
	public MatrixProcessorCursor crlf() {row=row+1;col=1; return this;}
	
	public MatrixProcessorCursor previous()
	{
		switch(orientation)
		{
			case horizontal: return this.moveColumns(-1);
			case vertical: return this.moveRows(-1);
		}
		return this;
	}
	public MatrixProcessorCursor next()
	{
		switch(orientation)
		{
			case horizontal: return this.moveColumns(1);
			case vertical: return this.moveRows(1);
		}
		return this;
	}
	public MatrixProcessorCursor nextCol() {col=col+1; return this;}
	
	public MatrixProcessorCursor apply(MatrixProcessorCursor other)
	{
		this.row=other.getRow();
		this.col=other.getColumn();
		return this;
	}
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(row);
		sb.append(",");
		sb.append(col).append("]");
		return sb.toString();
	}
}