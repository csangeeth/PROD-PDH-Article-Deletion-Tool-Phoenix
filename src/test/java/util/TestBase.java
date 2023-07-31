package util;

import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import static common.Constants.*;

public class TestBase {

    public static DBHelper dbHelper = new DBHelper(DB_URL, DB_USERNAME, DB_PASSWORD);


    @BeforeMethod
    public void beforeTest() {
        //LoggerUtil.logINFO("TestClass Running " + this.getClass().toString());
           try (InputStream inputStream = Example.class.getResourceAsStream("/banner.txt")) {
            if (inputStream != null) {
                Scanner scanner = new Scanner(inputStream);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    System.out.println(line);
                }
            } else {
                System.out.println("Banner file not found.");
            }
        } catch (IOException e) {
            System.out.println("Error reading banner file: " + e.getMessage());
        }
    }
}
