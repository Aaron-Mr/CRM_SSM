package poi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CreateExcelByPoi {
    public static void main(String[] args) throws IOException {
        /*
            文件---------HSSFWorkbook
            页-----------HSSFSheet
            行-----------HSSFRow
            列-----------HSSFCell
            样式---------HSSFCellStyle
        */
        //工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        //工作表
        HSSFSheet sheet = workbook.createSheet("学生表");
        //行
        HSSFRow row = sheet.createRow(0);
        //单元格
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("姓名");
        cell = row.createCell(1);
        cell.setCellValue("学号");
        cell = row.createCell(2);
        cell.setCellValue("年龄");

        //行
        row = sheet.createRow(1);
        //单元格
        cell = row.createCell(0);
        cell.setCellValue("张三");
        cell = row.createCell(1);
        cell.setCellValue("101");
        cell = row.createCell(2);
        cell.setCellValue("20");

        OutputStream out = new FileOutputStream("D:\\student.xls");
        workbook.write(out);

        out.close();
        workbook.close();
        System.out.println("===========create ok==========");
    }
}
