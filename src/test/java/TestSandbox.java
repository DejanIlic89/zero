import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.RepeatedTest;
import org.qaautomation.annotations.FailingTest;
import org.qaautomation.annotations.FlakyTest;
import org.qaautomation.annotations.SmokeTest;
import org.qaautomation.config.TestEnvFactory;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TestSandbox {

    Config CONFIG = TestEnvFactory.getInstance().getConfig();

    @RepeatedTest(10)
    void assertThatWeCanGetUserConfig() {
        assertAll("Config test",
                () -> assertEquals("DEVELOP", CONFIG.getString("TEST_ENV"), "TEST_ENV"),
                () -> assertEquals("/employee/create", CONFIG.getString("CREATE_EMPLOYEE_ENDPOINT"), "CREATE_EMPLOYEE_ENPOINT"),
                () -> assertEquals("develop-admin", CONFIG.getString("ADMIN_LOGIN"), "ADMIN_LOGIN")
        );
    }

    @SmokeTest
    void assertThatTrueIsTrue() {
        assertTrue(true, "true is true");
    }

    @FailingTest
    void assertThatADayIsADay() {
        assertEquals("day", "night", "true is true");
    }

    @FlakyTest
    void createAFlakyTestCase() {
        long currentTimeStamp = System.currentTimeMillis();
        // TODO: Remove this line with a logging statement
        System.out.println(currentTimeStamp);
        if (currentTimeStamp % 2 == 0) {
            assertTrue(true, "time is even");
        } else {
            assertTrue(false, "time is odd");
        }
    }

}
