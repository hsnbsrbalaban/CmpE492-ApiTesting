package com.apitesting.builder;

import com.apitesting.model.Project;

public final class ProjectBuilder {
    private String hostname;

    private ProjectBuilder() {
    }

    public static ProjectBuilder aProject() {
        return new ProjectBuilder();
    }

    public ProjectBuilder withHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public Project build() {
        Project project = new Project();
        project.setHostname(hostname);
        return project;
    }
}
