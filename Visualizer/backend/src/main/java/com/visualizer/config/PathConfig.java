package com.visualizer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@ConfigurationProperties(prefix = "visualizer")
public class PathConfig {
    private String leetcodePath;
    private String lldPath;

    public String getLeetcodePath() {
        return leetcodePath;
    }

    public void setLeetcodePath(String leetcodePath) {
        this.leetcodePath = leetcodePath;
    }

    public String getLldPath() {
        return lldPath;
    }

    public void setLldPath(String lldPath) {
        this.lldPath = lldPath;
    }

    public Path getLeetcodeAbsolutePath() {
        return Paths.get(leetcodePath).toAbsolutePath().normalize();
    }

    public Path getLldAbsolutePath() {
        return Paths.get(lldPath).toAbsolutePath().normalize();
    }
}
