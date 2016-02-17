package org.wloka.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Goal which touches a timestamp file.
 *
 * @goal merge
 * @phase process-sources
 */
public class FileMergerMojo extends AbstractMojo {
    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}/file-merger.log"
     * @required
     */
    @Parameter(property = "targetFile", defaultValue = "${project.build.directory}/file-merger.log")
    private File targetFile;

    public FileMergerMojo() {
        super();
    }

    public FileMergerMojo(File targetFile) {
        this.targetFile = targetFile;
    }

    public void execute() throws MojoExecutionException {
        File parentDir = targetFile.getParentFile();

        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter w = new FileWriter(targetFile)) {
            w.write("Hi there!");
        } catch (IOException ex) {
            throw new MojoExecutionException("Error writing to file " + targetFile, ex);
        }
    }
}
