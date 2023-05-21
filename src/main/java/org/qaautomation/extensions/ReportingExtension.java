package org.qaautomation.extensions;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.qaautomation.config.TestEnvFactory;
import org.qaautomation.extensions.report.PublishResults;
import org.qaautomation.extensions.report.TestRunMetaData;

import java.io.IOException;

@Slf4j
public class ReportingExtension implements AfterEachCallback {

    private static final Config CONFIG = TestEnvFactory.getInstance().getConfig();
    private static final Boolean PUBLISH_RESULTS_TO_ELASTIC =
            CONFIG.getBoolean("PUBLISH_RESULTS_TO_ELASTIC");

    @Override
    public void afterEach(ExtensionContext context) throws IOException {
        if (PUBLISH_RESULTS_TO_ELASTIC) {
            log.info("publishing results to elastic");
            TestRunMetaData testRunMetaData = new TestRunMetaData().setBody(context);
            PublishResults.toElastic(testRunMetaData);
        }
    }

}
