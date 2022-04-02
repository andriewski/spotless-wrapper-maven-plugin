package by.mark.spotless.wrapper;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "check", defaultPhase = LifecyclePhase.TEST)
public class SpotlessCheckWrapperMojo extends BaseSpotlessWrapperMojo {

    @Override
    protected String getSpotlessGoal() {
        return "check";
    }
}
