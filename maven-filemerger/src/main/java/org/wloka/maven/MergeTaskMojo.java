package org.wloka.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 * Goal which collect all class names in a plugins classpath.
 *
 * @goal merge
 * @phase process-sources
 */
public class MergeTaskMojo extends AbstractMojo {

    public static final String MERGE_BUILD_PROPERTY = "file-merger-merge-target";

    /**
     * The Maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    @Parameter(defaultValue = "${project}")
    private MavenProject project;


    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}/classes.log"
     * @required
     */
    @Parameter(defaultValue = "${project.build.directory}/classes.log")
    private File targetFile;

    @SuppressWarnings("unused")
    public MergeTaskMojo() {
        super();
    }

    public MergeTaskMojo(File targetFile, MavenProject project) {
        this.targetFile = targetFile;
        this.project = project;
    }

    @Override
    public void execute() throws MojoExecutionException {
        project.getProperties().put(MERGE_BUILD_PROPERTY, targetFile);
    }
}
