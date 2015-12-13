package example.reporting.application;

import com.google.common.io.Files;
import example.reporting.testrun.view.CreateTestRunRequest;
import io.dropwizard.Application;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;

public class ImportCommand extends EnvironmentCommand<CucumberReportConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportCommand.class);

    public ImportCommand(final Application<CucumberReportConfiguration> application) {
        super(application, "import", "Import a Cucumber result file");
    }

    @Override
    public void configure(final Subparser subparser) {
        super.configure(subparser);

        subparser.addArgument("report")
                .nargs("?")
                .required(true)
                .help("Report filename");
    }

    @Override
    protected void run(
            final Environment environment,
            final Namespace namespace,
            final CucumberReportConfiguration configuration
    ) throws Exception {

        final Client client = new JerseyClientBuilder(environment)
                .using(configuration.getSelfClientConfig())
                .build("self");
        final WebTarget target = client.target(configuration.getSelfUrl()).path("test-runs");

        final CreateTestRunRequest createRequest = new CreateTestRunRequest();
        createRequest.setEnv("TEST");
        final Response createResponse = target.path("create").request().post(Entity.json(createRequest));
        LOGGER.info("Create response: {}", createResponse);
        final String testRunUrl = createResponse.getHeaderString(HttpHeaders.LOCATION);
        LOGGER.info("Test run URL: {}", testRunUrl);

        final WebTarget testRunTarget = client.target(testRunUrl);

        final File reportFile = new File(namespace.getString("report"));
        try (InputStream reportStream = Files.asByteSource(reportFile).openBufferedStream()) {
            final Response importResponse = testRunTarget.path("import").request().post(Entity.entity(reportStream, MediaType.APPLICATION_JSON_TYPE));
            LOGGER.info("Import response: {}", importResponse);
        }

        final Response closeResponse = testRunTarget.path("close").request().post(null);
        LOGGER.info("Close response: {}", closeResponse);
    }

}