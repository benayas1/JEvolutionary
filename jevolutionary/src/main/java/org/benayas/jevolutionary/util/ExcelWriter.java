package org.benayas.jevolutionary.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.charts.AxisCrosses;
import org.apache.poi.ss.usermodel.charts.AxisPosition;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.ss.usermodel.charts.ChartLegend;
import org.apache.poi.ss.usermodel.charts.DataSources;
import org.apache.poi.ss.usermodel.charts.LegendPosition;
import org.apache.poi.ss.usermodel.charts.LineChartData;
import org.apache.poi.ss.usermodel.charts.ValueAxis;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {
	
	private Workbook workbook;
	private CreationHelper helper;
	private boolean hasHeader = false;
	private static final  String dateFormat = "yyyy-MM-dd";
	
	private Map<Sheet, ChartSettings> drawChart = new HashMap<>();
	
	public ExcelWriter() {
		// Create a Workbook
        workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating 'xls' file
        helper = workbook.getCreationHelper();        
	}

	public void writeData( String sheetName, List<Object[]> data, ChartSettings draw, String... headers  ) {
		Object[][] matrix = new Object[data.size()][];
		for ( int i = 0; i < data.size(); i++ ) {
			matrix[i] = data.get( i );
		}
		
		writeData( sheetName, matrix, draw, headers );
	}
	
	public void writeData( String sheetName, Object[][] data, ChartSettings draw, String... headers  ) {
		Sheet sheet = workbook.getSheet( sheetName );
		if ( sheet == null ) {
			sheet = workbook.createSheet( sheetName );
		}
		
		if ( headers.length > 0 )
			writeHeaders( sheet, headers );
		
		// Create Other rows and cells with data
        int rowNum = sheet.getRow( sheet.getLastRowNum() ) == null ? 0 : sheet.getLastRowNum() + 1;
        for(Object[] r: data) {
            Row row = sheet.createRow( rowNum++ );
            int j = 0;
            for( Object o : r ) {
            	setCell( row.createCell(j++), o );
            }
        }
        
        // Resize all columns to fit the content size
        if ( data.length > 0 && data[0] != null ) {
	        for(int i = 0; i < data[0].length; i++) {
	            sheet.autoSizeColumn(i);
	        }	        
        }
        
        if ( draw != null )
        	drawChart.put( sheet, draw );      
	}
	
	private void writeHeaders( Sheet sheet, String[] headers ) {
		hasHeader  = true;
		// Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        //headerFont.setColor( IndexedColors.RED.getIndex() );
		
		// Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);
        for(int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerCellStyle);
        }
	}
	
	public void setCell( Cell c, Object o ) {
		if ( o == null )
			return;
		
		if ( o instanceof String ) {
			c.setCellValue( (String)o );
			return;
		}
		
		if ( o instanceof LocalDate ) {
			c.setCellValue( Date.from(((LocalDate)o).atStartOfDay(ZoneId.systemDefault()).toInstant()) );
			CellStyle dateStyle = workbook.createCellStyle();
	        dateStyle.setDataFormat(helper.createDataFormat().getFormat( dateFormat ));
			c.setCellStyle( dateStyle );
			return;
		}
		
		if ( o instanceof Date ) {
			c.setCellValue( (Date)o );
			CellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setDataFormat(helper.createDataFormat().getFormat( dateFormat ) );
			c.setCellStyle( dateStyle );
			return;
		}
		
		if ( o instanceof Double ) {
			c.setCellValue( (Double)o );
			return;
		}
		
		if ( o instanceof Integer ) {
			c.setCellValue( (Integer)o );
			return;
		}
		
		if ( o instanceof Long ) {
			c.setCellValue( (Long)o );
			return;
		}
		
		if ( o instanceof Float ) {
			c.setCellValue( (Float)o );
			return;
		}
		
		if ( o instanceof Boolean ) {
			c.setCellValue((Boolean)o);
			return;
		}
	}
	
	public void setSheetOrder( String sheet, int pos ) {
		workbook.setSheetOrder( sheet, pos );
	}
	
	public void drawChart( Sheet sheet, ChartSettings settings ) {
		
		int from = sheet.getRow(0).getFirstCellNum();
		int to = sheet.getRow(0).getLastCellNum();
		
		Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, to + 1, to + 1 + 5, to + 1 + 10, to + 1 + 20);

        Chart chart = drawing.createChart(anchor);
        ChartLegend legend = chart.getOrCreateLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);

        LineChartData data = chart.getChartDataFactory().createLineChartData();

        // Use a category axis for the bottom axis.
        ChartAxis bottomAxis = chart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
        ValueAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

        ChartDataSource<Number> xs  = DataSources.fromNumericCellRange(sheet, new CellRangeAddress( hasHeader ? 1 : 0, sheet.getLastRowNum(), settings.getAxisX(), settings.getAxisX() ) );
        List<ChartDataSource<Number>> ys = new ArrayList<>();
        
        int[] columns = settings.getColumns();
        if ( columns != null ){
        	for ( int i = 0; i < columns.length; i++ ) {
        		if ( columns[i] != settings.getAxisX() )
        			ys.add( DataSources.fromNumericCellRange(sheet, new CellRangeAddress( hasHeader ? 1 : 0, sheet.getLastRowNum(), columns[i], columns[i] ) ) );
            }
        }else {
        	for ( int i = from; i < to; i++ ) {
        		if ( i != settings.getAxisX() )
        			ys.add( DataSources.fromNumericCellRange(sheet, new CellRangeAddress( hasHeader ? 1 : 0, sheet.getLastRowNum(), i, i ) ) );
            }
        }

        ys.forEach( y -> data.addSeries( xs, y ) );

        chart.plot(data, bottomAxis, leftAxis);
	}
	
	public void dumpFile( String filename ) {
		// Write the output to a file       
		try {
			
			workbook.forEach( sheet -> {if ( drawChart.containsKey( sheet ) ) {
		        						 drawChart( sheet, drawChart.get( sheet ) );         
			}});
			
			FileOutputStream fileOut;		
			fileOut = new FileOutputStream( filename + ".xlsx" );
			
			workbook.write( fileOut );
	        fileOut.close();

	        // Closing the workbook
	        workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}       
	}
	
	public static void main (String[] args) {
		Object[][] obs = new Object[3][3];
		obs[0][0] = 1;
		obs[0][1] = 2;
		obs[0][2] = 3;
		obs[1][0] = 2;
		obs[1][1] = 5;
		obs[1][2] = 8;
		obs[2][0] = 3;
		obs[2][1] = 11;
		obs[2][2] = 4;
		
		ExcelWriter excel = new ExcelWriter();
		excel.writeData("Test Sheet", obs, null, "C1","C2","C3");
		excel.dumpFile("TestExcel");		
	}
	
	
	public class ChartSettings {

		private int axisX;
		private int[] columns;
		
		public ChartSettings( int axisX, int... columns ) {
			this.axisX = axisX;
			this.columns = columns;
		}
		
		public ChartSettings( int axisX ) {
			this.axisX = axisX;
		}
		
		public int getAxisX() {
			return axisX;
		}
		
		public boolean allColumns() {
			return columns == null;
		}
		
		public int[] getColumns() {
			return columns;
		}
	}

}
