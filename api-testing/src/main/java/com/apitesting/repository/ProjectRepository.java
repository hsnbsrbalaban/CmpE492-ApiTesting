package com.apitesting.repository;

import com.apitesting.model.Project;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProjectRepository {

    List<Project> projects = new ArrayList<>();

    public boolean exists(String name) {
        if (projects.isEmpty())
            return false;

        return projects.stream().map(Project::getName).anyMatch(pName -> name.equals(pName));
    }

    public Optional<Project> findProjectByName(String name) {
        if (projects.isEmpty())
            return Optional.empty();

        return projects.stream().filter(project -> name.equals(project.getName())).findFirst();
    }

    public void save(Project project) {
        if (!exists(project.getName()))
            projects.add(project);
    }

    public void delete(String name) {
        if (exists(name)) {
            projects.removeIf(project -> name.equals(project.getName()));
        }
    }

    public List<Project> findAll() {
        return projects;
    }
}
