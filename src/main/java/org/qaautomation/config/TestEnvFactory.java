package org.qaautomation.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Objects;

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

    private Config setConfig() {
        log.info("Call setConfig only once for the whole test run!");

        // Standard config load behavior (loads common config from application.conf file)
        // https://github.com/lightbend/config#standard-behavior
        config = ConfigFactory.load();

        Config choicesConfig = ConfigFactory.load("choices");
        config = config.withFallback(choicesConfig);

        config = getAllConfigFromFilesInTheResourcePath("common");

        TestEnv testEnv = config.getEnum(TestEnv.class, "TEST_ENV");
        return getAllConfigFromFilesInTheResourcePath(testEnv.getValue());
    }

    private Config getAllConfigFromFilesInTheResourcePath(String resourceBasePath) {
        try {
            String path = String.format("src/main/resources/%s", resourceBasePath);
            log.info("path: {}", path);

            File testEnvDir = new File(path);
            for (File file : Objects.requireNonNull(testEnvDir.listFiles())) {
                String resourceFileBasePath = String.format("%s/%s", resourceBasePath, file.getName());
                log.info("resourceFileBasePath: {}", resourceFileBasePath);

                Config childConfig = ConfigFactory.load(resourceFileBasePath);
                config = config.withFallback(childConfig);
            }

            return config;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new IllegalStateException("Could not parse config");
        }
    }

//    private void setConfig() {
//        log.info("setConfig called");
//        // Standard config load behaviour: https://github.com/lightbend/config#standard-behavior
//        config = ConfigFactory.load();
//        TestEnv testEnv = config.getEnum(TestEnv.class, "TEST_ENV");
//        String testEnvName = testEnv.toString().toLowerCase();
//        String testEnvDirPath = String.format("src/main/resources/%s", testEnvName);
//        log.info("path: {}", testEnvDirPath);
//        File testEnvDir = new File(testEnvDirPath);
//        for (File file : testEnvDir.listFiles()) {
//            Config childConfig = ConfigFactory.load(String.format("%s/%s", testEnvName, file.getName()));
//            config = config.withFallback(childConfig);
//        }
//    }

}
