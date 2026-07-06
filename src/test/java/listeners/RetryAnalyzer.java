package listeners;

import config.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Retries a failed scenario up to retry.count times (see ConfigReader.getRetryCount()).
 * Attached to Cucumber's generated runScenario method by AnnotationTransformer, since that
 * method can't be annotated directly.
 *
 * TestNG can reuse a single retryAnalyzer instance across every row of a @DataProvider method,
 * so an instance field would count retries cumulatively across all scenarios instead of per
 * scenario. Keying attempts by method + parameters (which include the scenario name/uri) keeps
 * counts isolated per scenario and safe under parallel data-provider execution.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Map<String, Integer> ATTEMPTS_BY_SCENARIO = new ConcurrentHashMap<>();

    @Override
    public boolean retry(ITestResult result) {
        int maxRetries = ConfigReader.getInstance().getRetryCount();
        String key = result.getMethod().getQualifiedName() + Arrays.toString(result.getParameters());

        int attempts = ATTEMPTS_BY_SCENARIO.getOrDefault(key, 0);
        if (attempts < maxRetries) {
            ATTEMPTS_BY_SCENARIO.put(key, attempts + 1);
            return true;
        }
        ATTEMPTS_BY_SCENARIO.remove(key);
        return false;
    }
}
