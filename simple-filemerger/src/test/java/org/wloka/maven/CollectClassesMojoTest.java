package org.wloka.maven;

import org.apache.commons.io.FileUtils;
import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.After;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.wloka.maven.MergeTaskMojo.MERGE_BUILD_PROPERTY;

public class CollectClassesMojoTest {

    public static final String TEST_FILE_PATH = "src/test/resources/filemerger-test.log";

    @After
    public void tearDown() throws Exception {
        File testFile = new File(TEST_FILE_PATH);
        assertTrue(!testFile.exists() || FileUtils.deleteQuietly(testFile));
    }

    @Test
    public void executeWithValidFileAndProjectCreatesFileAtExpectedPath() throws Exception {
        File target = new File(TEST_FILE_PATH);

        aCollectClassesMojo(target, aProject()).execute();

        assertTrue(target.exists());
    }

    @Test
    public void executeWithValidFileAndProjectWritesStringToFile() throws Exception {
        File target = new File(TEST_FILE_PATH);

        aCollectClassesMojo(target, aProject()).execute();

        assertEquals("Hi there!", readFile(target));
    }

    @Test
    public void executeWithValidFileAndProjectReplacesFileContent() throws Exception {
        File target = new File(TEST_FILE_PATH);
        CollectClassesMojo testObj = aCollectClassesMojo(target, aProject());

        testObj.execute();
        testObj.execute();
        testObj.execute();

        assertEquals("Hi there!", readFile(target));
    }

    @Test
    public void executeWithValidFileAndMergePropertyInProjectAppendsToFileContent() throws Exception {
        File target = new File(TEST_FILE_PATH);
        CollectClassesMojo testObj = aCollectClassesMojo(target, aProject(MERGE_BUILD_PROPERTY, target));

        testObj.execute();
        testObj.execute();
        testObj.execute();

        assertEquals("Hi there!Hi there!Hi there!", readFile(target));
    }

    @Test
    public void executeWithValidFileAndMergePropertyToDifferentFileInProjectCreatesTargetFileAndMergeFile() throws Exception {
        File target = new File(TEST_FILE_PATH);
        File mergeTarget = new File("src/test/resources/filemerger-merge-test.log");
        CollectClassesMojo testObj = aCollectClassesMojo(target, aProject(MERGE_BUILD_PROPERTY, mergeTarget));

        testObj.execute();

        assertTrue(target.exists());
        assertTrue(mergeTarget.exists());
        assertTrue(FileUtils.deleteQuietly(mergeTarget));
    }

    @Test
    public void executeWithValidFileAndMergePropertyToDifferentFileInProjectReplacesTargetFileAndAppendsMergeFile() throws Exception {
        File target = new File(TEST_FILE_PATH);
        File mergeTarget = new File("src/test/resources/filemerger-merge-test.log");
        CollectClassesMojo testObj = aCollectClassesMojo(target, aProject(MERGE_BUILD_PROPERTY, mergeTarget));

        testObj.execute();
        testObj.execute();
        testObj.execute();

        assertEquals("Hi there!", readFile(target));
        assertEquals("Hi there!Hi there!Hi there!", readFile(mergeTarget));
        assertTrue(FileUtils.deleteQuietly(mergeTarget));
    }

    @Test
    public void findPropertiesWithPropertyInSameProjectReturnsProperties() throws Exception {
        MavenProject target = aProject(MERGE_BUILD_PROPERTY, new File("whatever"));
        CollectClassesMojoTestClass testObj = aCollectClassesMojo(new File(TEST_FILE_PATH), target);

        MavenProject actual = testObj.findProperties(target, MERGE_BUILD_PROPERTY);

        assertTrue(actual.getProperties().containsKey(MERGE_BUILD_PROPERTY));
    }

    @Test
    public void findPropertiesWithPropertyInParentProjectReturnsProperties() throws Exception {
        MavenProject target = aProject(aProject(MERGE_BUILD_PROPERTY, new File("whatever")));
        CollectClassesMojoTestClass testObj = aCollectClassesMojo(new File(TEST_FILE_PATH), target);

        MavenProject actual = testObj.findProperties(target, MERGE_BUILD_PROPERTY);

        assertTrue(actual.getProperties().containsKey(MERGE_BUILD_PROPERTY));
    }

    @Test
    public void findPropertiesWithPropertyInTransitiveParentProjectReturnsProperties() throws Exception {
        MavenProject target = aProject(aProject(aProject(MERGE_BUILD_PROPERTY, new File("whatever"))));
        CollectClassesMojoTestClass testObj = aCollectClassesMojo(new File(TEST_FILE_PATH), target);

        MavenProject actual = testObj.findProperties(target, MERGE_BUILD_PROPERTY);

        assertTrue(actual.getProperties().containsKey(MERGE_BUILD_PROPERTY));
    }

    private CollectClassesMojoTestClass aCollectClassesMojo(File file, MavenProject project) {
        CollectClassesMojoTestClass result = new CollectClassesMojoTestClass(file, project);
        result.setLog(new DefaultLog(new ConsoleLogger(5, "disabled")));
        return result;
    }

    private MavenProject aProject(Object... pairs) {
        MavenProject result = new MavenProject();
        for (int idx = 0; idx + 1 < pairs.length; idx += 2) {
            result.getProperties().put(pairs[idx], pairs[idx + 1]);
        }
        return result;
    }

    private MavenProject aProject(MavenProject parent, Object... pairs) {
        MavenProject result = aProject(pairs);
        result.setParent(parent);
        return result;
    }

    private String readFile(File file) throws IOException {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            result.append(reader.readLine());
        }
        return result.toString();
    }

    private static class CollectClassesMojoTestClass extends CollectClassesMojo {
        public CollectClassesMojoTestClass(File targetFile, MavenProject project) {
            super(targetFile, project);
        }

        @Override
        public MavenProject findProperties(MavenProject project, Object key) {
            return super.findProperties(project, key);
        }
    }
}