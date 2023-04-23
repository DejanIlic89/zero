package org.qaautomation.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class TestEnvFactory {

    private static final TestEnvFactory UNIQUE_INSTANCE = new TestEnvFactory();
    private Config config;

    private TestEnvFactory() {
        setConfig();
    }

    public static TestEnvFactory getInstance() {
        return UNIQUE_INSTANCE;
    }

    public Config getConfig() {
        return config;
    }

    private void setConfig() {
        log.info("setConfig called");
        // Standard config load behaviour: https://github.com/lightbend/config#standard-behavior
        config = ConfigFactory.load();
        TestEnv testEnv = config.getEnum(TestEnv.class, "TEST_ENV");
        String testEnvName = testEnv.toString().toLowerCase();
        String testEnvDirPath = String.format("src/main/resources/%s", testEnvName);
        log.info("path: {}", testEnvDirPath);
        File testEnvDir = new File(testEnvDirPath);
        for (File file : testEnvDir.listFiles()) {
            Config childConfig = ConfigFactory.load(String.format("%s/%s", testEnvName, file.getName()));
            config = config.withFallback(childConfig);
        }
    }

}
