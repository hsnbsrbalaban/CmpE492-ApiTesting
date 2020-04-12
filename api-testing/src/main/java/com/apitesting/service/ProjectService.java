package com.apitesting.service;

import com.apitesting.model.Project;
import com.apitesting.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final Random rand = new Random();

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public boolean exists(String name) {
        return projectRepository.exists(name);
    }

    public Project getProject(String name) {
        return projectRepository.findProjectByName(name).orElse(null);
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public void createProject(String name) {
        Project project = new Project();
        project.setName(name);
        project.setImportance(rand.nextInt());

        projectRepository.save(project);
    }

    public void deleteProject(String name) {
        projectRepository.delete(name);
    }
}
