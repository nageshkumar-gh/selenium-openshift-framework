package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/*
 * Cucumber runner entry point for TestNG execution.
 *
 * Note: the features path is currently filesystem-relative. If you intend to run this from
 * different working directories or in CI, consider switching to a classpath-based path later.
 */
@CucumberOptions(
        features = "classpath:features",
        glue = {"steps", "hooks"},
        plugin = {"pretty", "summary", "html:target/cucumber-reports/report.html", "json:target/cucumber.json"},
        tags = ""
)
public class TestRunner extends AbstractTestNGCucumberTests {

    /*
     * Cross-browser: testng.xml can run this same class from multiple <test> blocks, each with
     * its own <parameter name="browser" .../>. TestNG creates a separate instance per <test>
     * block (AbstractTestNGCucumberTests keeps its Cucumber runtime as an instance field, so this
     * is safe), and this hook stores that block's browser as a JVM-wide system property before
     * any scenario runs. Blocks must run sequentially (no parallel="tests") since a system
     * property is global JVM state and would race if two blocks ran concurrently.
     */
    @BeforeClass(alwaysRun = true)
    @Parameters({"browser", "headless"})
    public void configureBrowser(@Optional("") String browser, @Optional("") String headless) {
        if (!browser.isEmpty()) {
            System.setProperty("browser", browser);
        }
        if (!headless.isEmpty()) {
            System.setProperty("headless", headless);
        }
    }

    /*
     * Enables parallel scenario execution: TestNG farms rows of this data provider out across
     * the thread pool sized by data-provider-thread-count in testng.xml. Safe because
     * DriverFactory keeps one WebDriver per thread.
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
