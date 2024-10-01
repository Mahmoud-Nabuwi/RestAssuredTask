import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;
import com.aventstack.extentreports.*;
import static io.restassured.RestAssured.given;

public class AddNewDeviceValidationTest {

    ExtentReports extent;
    ExtentTest test;
    Response response;
    String requestBody;

    @BeforeClass
    public void setupExtentReport() {
        ExtentSparkReporter report = new ExtentSparkReporter("API_Test_Report.html");
        extent = new ExtentReports();
        extent.attachReporter(report);
    }

    @BeforeTest
    public void setup() {

        RestAssured.baseURI = "https://api.restful-api.dev/objects";
    }

    @Test
    public void addNewDeviceTest() {
        test = extent.createTest("Validate Adding new device");

         requestBody = "{\n" +
                "\"name\": \"Apple Max Pro 1TB\",\n" +
                "\"data\": {\n" +
                "\"year\": 2023,\n" +
                "\"price\": 7999.99,\n" +
                "\"CPU model\": \"Apple ARM A7\",\n" +
                "\"Hard disk size\": \"1 TB\"\n" +
                "}\n" +
                "}";

        response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post()
                .then()
                .extract()
                .response();
    }

    @Test (priority = 1)
    public void validateID(){
        String id = response.jsonPath().getString("id");
        Assert.assertNotNull(id, "ID is a number");
        test.log(Status.PASS, "ID is: " + id);
    }
    @Test (priority = 2)
    public void validateName(){
        String name = response.jsonPath().getString("name");
        Assert.assertEquals(name, "Apple Max Pro 1TB", "Name not found'");
        test.log(Status.PASS, "Name is: " + name);

    }
    @Test (priority = 3)
    public void validateCreatedAt(){
        String createdAt = response.jsonPath().getString("createdAt");
        Assert.assertNotNull(createdAt, "createdAt is a date");
        test.log(Status.PASS, "Created At is: " + createdAt);
    }
    @Test (priority = 4)
    public void validateYear(){
        int year = response.jsonPath().getInt("data.year");
        Assert.assertEquals(year, 2023, "Year not found");
        test.log(Status.PASS, "Year is: " + year);
    }
    @Test (priority = 5)
    public void validatePrice(){
        double price = response.jsonPath().getFloat("data.price");
        Assert.assertEquals(price, 7999.99f, "Price not found");
        test.log(Status.PASS, "Price is: " + price);
    }
    @Test (priority = 6)
    public void validateCpuModel(){
        String cpuModel = response.jsonPath().getString("data['CPU model']");
        Assert.assertEquals(cpuModel, "Apple ARM A7", "CPU model not found");
        test.log(Status.PASS, "CPU model is: " + cpuModel);
    }
    @Test (priority = 7)
    public void validateHardDiskSize(){
        String hardDiskSize = response.jsonPath().getString("data['Hard disk size']");
        Assert.assertEquals(hardDiskSize, "1 TB", "hard disk size not found");
        test.log(Status.PASS, "Hard Disk Size is: " + hardDiskSize);
    }


    @AfterTest
    public void tearDown() {
        extent.flush();
    }
}