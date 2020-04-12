package com.apitesting.builder;

import com.apitesting.model.Project;

public final class ProjectBuilder {
    private String name;

    private ProjectBuilder() {
    }

    public static ProjectBuilder aProject() {
        return new ProjectBuilder();
    }

    public ProjectBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Project build() {
        Project project = new Project();
        project.setName(name);
        return project;
    }
}
