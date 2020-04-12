package com.apitesting.controller;

import com.apitesting.model.CapturedRequest;
import com.apitesting.model.Project;
import com.apitesting.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BaseController {
    private final ProjectService projectService;

    @Autowired
    public BaseController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/capture")
    public ResponseEntity capture(@RequestParam(value = "method") String method, @RequestParam(value = "path") String path,
                                  @RequestParam(value = "headers") String headers, @RequestParam(value = "content") String content) {
        CapturedRequest request = new CapturedRequest();
        request.setMethod(method);
        request.setPath(path);
        request.setHeaders(headers);
        request.setContent(content);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PostMapping("/create-project")
    public ResponseEntity createProject(@RequestParam(value = "app-name") String appName) {
        if (projectService.exists(appName)) {
            return new ResponseEntity("Project " + appName + " already exists", HttpStatus.BAD_REQUEST);
        }

        projectService.createProject(appName);
        return new ResponseEntity("Project " + appName + " created", HttpStatus.OK);
    }

    @DeleteMapping("/delete-project")
    public ResponseEntity deleteProject(@RequestParam(value = "app-name") String appName) {
        if (!projectService.exists(appName)) {
            return new ResponseEntity("Project " + appName + " does not exists", HttpStatus.NOT_FOUND);
        }

        projectService.deleteProject(appName);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getProjects() {
        return new ResponseEntity<>(projectService.getProjects(), HttpStatus.OK);
    }


}
