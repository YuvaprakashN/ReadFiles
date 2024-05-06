package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.io.*;
import java.util.Properties;

public class ApachePOIExcel {

    public static void main(String[] args) throws IOException {

        //Read Property file
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "application.properties";
        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));

        // Read Excel File
        FileInputStream file = new FileInputStream(new File(appProps.getProperty("READ_EXCEL_FILE_PATH")));
        Workbook workbook = new XSSFWorkbook(file);


        Sheet sheet = workbook.getSheetAt(0);

        Map<Integer, List<String>> data = new HashMap<>();
        int i = 0;
        for (Row row : sheet) {
            data.put(i, new ArrayList<String>());
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING: {
                        data.get(i).add(cell.getStringCellValue());
                        break;
                    }
                    case NUMERIC: {
                        if (DateUtil.isCellDateFormatted(cell)) {
                            data.get(i).add(String.valueOf(cell.getDateCellValue()));
                        } else {
                            data.get(i).add(String.valueOf(cell.getNumericCellValue()));
                        }


                        data.get(i).add(String.valueOf(cell.getNumericCellValue()));
                        break;
                    }
                    case BOOLEAN: {
                        data.get(i).add(String.valueOf(cell.getBooleanCellValue()));
                        break;
                    }
                    case FORMULA: {
                        data.get(i).add(String.valueOf(cell.getCellFormula()));
                        break;
                    }
                    default:
                        break;
                }
            }
            i++;
        }

        for (Map.Entry<Integer, List<String>> entry : data.entrySet()) {
            Integer key = entry.getKey();
            List<String> value = entry.getValue();
            System.out.println("Row= " + key + "\n Value=" + value);
        }


        //Write Excel file

        //Create Excel file
        try {
            Workbook writeWorkbook = new XSSFWorkbook();

            //Create Sheet
            Sheet newSheet = workbook.createSheet("Data");

            //Setting Column width
            sheet.setColumnWidth(0, 6000);
            sheet.setColumnWidth(1, 4000);

            //Creating header
            Row header = sheet.createRow(0);

            //Formating header
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);


            //Selecting Header font style and size
            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            font.setFontName("Calibri");
            font.setFontHeightInPoints((short) 11);
            font.setBold(true);
            headerStyle.setFont(font);


            // Naming Header columns
            Cell headerCell = header.createCell(0);
            headerCell.setCellValue("Name");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(1);
            headerCell.setCellValue("Age");
            headerCell.setCellStyle(headerStyle);


            CellStyle style = workbook.createCellStyle();

            int rowIndex = 1;
            for (Map.Entry<Integer, List<String>> entry : data.entrySet()) {
                Integer key = entry.getKey();
                List<String> value = entry.getValue();
                if (key >= 1) {
                    Row row = sheet.createRow(key);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(value.get(0));
                    cell.setCellStyle(style);

                    cell = row.createCell(1);
                    cell.setCellValue(value.get(1));
                    cell.setCellStyle(style);
                }
            }

            File excelFile = new File(appProps.getProperty("WRITE_EXCEL_FILE_PATH"));

            if (excelFile.exists()) {
                new FileWriter(appProps.getProperty("WRITE_EXCEL_FILE_PATH"), false).close();

            }

            FileOutputStream outputStream = new FileOutputStream(appProps.getProperty("WRITE_EXCEL_FILE_PATH"));
            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
