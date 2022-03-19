package by.mark.spotless.wrapper;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "apply", defaultPhase = LifecyclePhase.COMPILE)
public class SpotlessApplyWrapperMojo extends BaseSpotlessWrapperMojo {

    @Override
    protected String getSpotlessGoal() {
        return "apply";
    }
}
