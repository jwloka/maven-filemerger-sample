package org.wloka.maven;

import org.apache.maven.project.MavenProject;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class MergeTaskMojoTest {

    public static final String TEST_FILE_PATH = "src/test/resources/filemerger-test.log";

    @Test
    public void executeWithValidFileAndProjectYieldsBuildPropertyInProject() throws Exception {
        File expected = new File(TEST_FILE_PATH);
        MavenProject target = new MavenProject();

        new MergeTaskMojo(expected, target).execute();

        assertEquals(expected, target.getProperties().get(MergeTaskMojo.MERGE_BUILD_PROPERTY));
    }
}