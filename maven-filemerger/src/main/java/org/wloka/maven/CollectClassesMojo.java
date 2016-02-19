package org.wloka.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import static org.wloka.maven.MergeTaskMojo.MERGE_BUILD_PROPERTY;

/**
 * Goal which collect all class names in a plugins classpath.
 *
 * @goal collect
 * @phase process-sources
 */
public class CollectClassesMojo extends AbstractMojo {
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
     */
    @Parameter(defaultValue = "${project.build.directory}/classes.log")
    private File targetFile;

    @SuppressWarnings("unused")
    public CollectClassesMojo() {
        super();
    }

    public CollectClassesMojo(File targetFile, MavenProject project) {
        this.targetFile = targetFile;
        this.project = project;
    }

    @Override
    public void execute() throws MojoExecutionException {
        String content = collectClasses();

        File mergeFile = (File) readFileProperty(MERGE_BUILD_PROPERTY);
        if (mergeFile != null) {
            writeFile(content, mergeFile, true);
        }
        if (!targetFile.equals(mergeFile)) {
            writeFile(content, targetFile, false);
        }
    }

    protected String collectClasses() {
        // TODO Find all classes in classpath
        return "Hi there!";
    }

    protected Object readFileProperty(Object key) {
        Object result = null;
        MavenProject targetProject = findProperties(project, key);
        if (targetProject != null) {
            Properties props = targetProject.getProperties();
            result = props.get(key);
            if (result == null && props.containsKey(MERGE_BUILD_PROPERTY)) {
                getLog().warn("Invalid file path in property " + MERGE_BUILD_PROPERTY);
            }
        }
        return result;
    }

    protected MavenProject findProperties(MavenProject project, Object key) {
        if (project.getProperties().containsKey(key) || project.getParent() == null) {
            return project;
        } else {
            return findProperties(project.getParent(), key);
        }

    }

    protected File writeFile(String content, File file, boolean doAppend) throws MojoExecutionException {
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            parentDir.mkdirs();
        }
        try (FileWriter w = new FileWriter(file, doAppend)) {
            getLog().info(String.format("%s classes to: %s", doAppend ? "Append" : "Write", file.getAbsolutePath()));
            w.write(content);
        } catch (IOException ex) {
            throw new MojoExecutionException("Error writing to file " + file.getAbsolutePath(), ex);
        }

        return file;
    }
}
