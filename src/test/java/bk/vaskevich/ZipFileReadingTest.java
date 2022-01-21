package bk.vaskevich;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipFileReadingTest {

    private ClassLoader cl = ZipFileReadingTest.class.getClassLoader();

    @Test
    public void ZipFileToReadTest() throws Exception {
        try(ZipFile zipFile = new ZipFile(new File(cl.getResource("Files.zip").toURI()))){
            ZipEntry zipEntry = zipFile.getEntry("selenium.pdf");

            //работа с PDF файлом
            try(InputStream stream = zipFile.getInputStream(zipEntry)){
                PDF parsedPDF = new PDF(stream);
                assertThat(parsedPDF.text).contains("Selenium Documentation");
            }

            //работа с xls файлом
            zipEntry = zipFile.getEntry("test.xls");
            try(InputStream stream = zipFile.getInputStream(zipEntry)){
                XLS parsedXls = new XLS(stream);
                assertThat(parsedXls.excel.getSheetAt(0).getRow(19).getCell(1)
                        .getStringCellValue()).isEqualTo("Loreta");
            }

            //работа с cvs файлом
            zipEntry = zipFile.getEntry("price-index.csv");
            try(InputStream stream = zipFile.getInputStream(zipEntry)){
                CSVReader reader = new CSVReader(new InputStreamReader(stream));
                List<String[]> list = reader.readAll();
                assertThat(list).contains(new String[]
                        {"CPIQ.SE9A",
                        "1914.06",
                        "12.8696737357259",
                        "FINAL",
                        "Index",
                        "CPI",
                        "CPI All Groups for New Zealand",
                        "All groups"});
            }
        }
    }
}


