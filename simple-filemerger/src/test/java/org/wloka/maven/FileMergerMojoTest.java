package org.wloka.maven;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileMergerMojoTest {

    public static final String TEST_FILE_PATH = "src/test/resources/filemerger-test.log";

    @After
    public void tearDown() throws Exception {
        assertTrue(FileUtils.deleteQuietly(new File(TEST_FILE_PATH)));
    }

    @Test
    public void executeWithValidPathCreatesFileAtExpectedPath() throws Exception {
        File target = new File(TEST_FILE_PATH);
        FileMergerMojo testObj = new FileMergerMojo(target);

        testObj.execute();

        assertTrue(target.exists());
    }

    @Test
    public void executeWithValidPathWritesStringToFile() throws Exception {
        File target = new File(TEST_FILE_PATH);
        FileMergerMojo testObj = new FileMergerMojo(target);

        testObj.execute();

        try (BufferedReader reader = new BufferedReader(new FileReader(target))) {
            assertEquals("Hi there!", reader.readLine());
        }
    }


}