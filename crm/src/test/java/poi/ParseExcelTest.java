package poi;

import com.wangxiaoqi.crm.commons.utils.HSSFUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ParseExcelTest {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = new FileInputStream("D:\\JavaProject\\CRM_SSM\\serverDir\\student.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

        HSSFSheet sheet = workbook.getSheetAt(0);

        HSSFRow row = null;
        HSSFCell cell = null;

        for (int i=0; i<=sheet.getLastRowNum(); ++i){
            row = sheet.getRow(i);

            for (int j=0; j<row.getLastCellNum(); ++j){
                cell = row.getCell(j);
                System.out.print(HSSFUtils.getCellValueForStr(cell)+" ");
            }
            System.out.println();
        }

    }
}
