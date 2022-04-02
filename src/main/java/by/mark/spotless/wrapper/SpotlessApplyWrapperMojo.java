package by.mark.spotless.wrapper;

import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "apply")
public class SpotlessApplyWrapperMojo extends BaseSpotlessWrapperMojo {

    @Override
    protected String getSpotlessGoal() {
        return "apply";
    }
}
