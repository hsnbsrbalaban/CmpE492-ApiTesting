package com.apitesting.service;

import com.apitesting.model.Project;
import com.apitesting.repository.DynamoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final DynamoDBRepository repository;

    @Autowired
    public ProjectService(DynamoDBRepository repository) {
        this.repository = repository;
    }

    public boolean exists(String name) {
        return getProject(name) != null;
    }

    public Project getProject(String name) {
        return repository.getProject(name);
    }

    public List<Project> getProjects() {
        return repository.getProjects();
    }

    public void createProject(String name) {
        Project project = new Project();
        project.setHostname(name);

        repository.insertProject(project);
    }

    public void deleteProject(String name) {
        Project project = new Project();
        project.setHostname(name);

        repository.deleteProject(project);
    }
}
