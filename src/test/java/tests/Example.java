package tests;

import com.opencsv.bean.CsvToBeanBuilder;
import model.CSV;
import org.testng.annotations.Test;
import util.TestBase;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import static common.Constants.CSV_FILE_PATH;

public class Example extends TestBase {

    static final String fileName = CSV_FILE_PATH + "dh_id.csv";
    int val = 0;
    String formattedStr = String.format("%04d", val);

    @Test
    public void articleDeletion() throws FileNotFoundException {


        List<CSV> beans = new CsvToBeanBuilder(new FileReader(fileName))
                .withType(CSV.class)
                .withSkipLines(1)
                .build()
                .parse();


        for (CSV bean : beans) {
            val++;
            if (bean != null) {
                System.out.println("======================== '" + val + "' ======================");
                System.out.println(bean.getId());
                dbHelper.executeUpdateProcedure(bean.getId());
                dbHelper.executeSurvivorShip();
                dbHelper.executeCommit();
            } else {
                System.out.println("empty bean");
            }
        }
    }

}
