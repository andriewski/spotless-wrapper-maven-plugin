package by.mark.spotless.wrapper;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.project.MavenProject;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Objects.requireNonNull;
import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executeMojo;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.groupId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;
import static org.twdata.maven.mojoexecutor.MojoExecutor.version;

public abstract class BaseSpotlessWrapperMojo extends AbstractMojo {

    @Component
    private MavenProject mavenProject;

    @Component
    private MavenSession mavenSession;

    @Component
    private BuildPluginManager pluginManager;

    @Override
    public void execute() {
        try {
            executeWithFileCopy();
        } catch (Exception e) {
            getLog().error("Can not execute goal: " + getSpotlessGoal(), e);
        }
    }

    private void executeWithFileCopy() throws Exception {
        Path formatFileTempCopy = null;

        try (InputStream is = requireNonNull(this.getClass().getClassLoader().getResourceAsStream("eclipse-formatter.xml"))) {
            byte[] bytes = is.readAllBytes();

            formatFileTempCopy = Files.createTempFile("spotless-formatter", ".xml");
            Files.write(formatFileTempCopy, bytes);

            executeGoal(getSpotlessGoal(), formatFileTempCopy.toString());
        } finally {
            if (formatFileTempCopy != null) {
                Files.delete(formatFileTempCopy);
            }
        }
    }

    private void executeGoal(String goal, String formatterFilePath) throws MojoExecutionException {
        executeMojo(
                plugin(
                        groupId("com.diffplug.spotless"),
                        artifactId("spotless-maven-plugin"),
                        version("2.21.0")
                ),
                goal,
                configuration(
                        element("java",
                                element("includes",
                                        element("include", "src/main/java/**/*.java"),
                                        element("include", "src/test/java/**/*.java")
                                ),
                                element("removeUnusedImports"),
                                element("eclipse",
                                        element("version", "4.13.0"),
                                        element("file", formatterFilePath)
                                )
                        )
                ),
                executionEnvironment(
                        mavenProject,
                        mavenSession,
                        pluginManager
                )
        );
    }

    protected abstract String getSpotlessGoal();
}
