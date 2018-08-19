package com.groovycoder.testcontainers.magazineexample;

import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.utility.MountableFile;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;
import static org.testcontainers.containers.Network.newNetwork;

public class FunctionalTest {

    @Rule
    public Network network = newNetwork(); // shared network for all containers to use DNS

    @Rule
    public BrowserWebDriverContainer chrome = (BrowserWebDriverContainer) new BrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withRecordingMode(RECORD_ALL, new File("build")) // save video recordings
            .withNetwork(network);

    @Rule
    public GenericContainer webserver = new GenericContainer("httpd:2.4.33-alpine")
            .withCopyFileToContainer(MountableFile.forClasspathResource("index.html"), "/usr/local/apache2/htdocs")
            .withNetwork(network)
            .withNetworkAliases("app");


    @Test
    public void searchGoogle() {
        WebDriver driver = chrome.getWebDriver();

        driver.get("http://app");
        String title = driver.getTitle();
        assertEquals("Testcontainers Test Page - Index", driver.getTitle());

        driver.quit();
    }

}
