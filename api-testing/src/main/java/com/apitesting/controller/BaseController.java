package com.apitesting.controller;

import com.apitesting.model.CapturedFlow;
import com.apitesting.model.Project;
import com.apitesting.model.Response;
import com.apitesting.repository.DynamoDBRepository;
import com.apitesting.service.ProjectService;
import com.apitesting.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
public class BaseController {
    private final ProjectService projectService;
    private final RestService restService;
    private final DynamoDBRepository repository;

    @Autowired
    public BaseController(ProjectService projectService, RestService restService, DynamoDBRepository repository) {
        this.projectService = projectService;
        this.restService = restService;
        this.repository = repository;
    }

    @GetMapping("/")
    public ResponseEntity welcome() {
        return new ResponseEntity("Welcome", HttpStatus.OK);
    }

    @PostMapping("/capture")
    public ResponseEntity capture(@RequestBody CapturedFlow flow) {
        List<Project> projects = repository.getProjects();

        if (!CollectionUtils.isEmpty(projects) &&
                projects.stream().map(Project::getHostname).anyMatch(host -> host.equals(flow.getHostname()))) {
            repository.insertRequest(flow);
            return new ResponseEntity("Request saved", HttpStatus.OK);
        }

        return new ResponseEntity("Request not saved, no matching project with request's hostname", HttpStatus.OK);
    }

    @GetMapping("/requests/{hostname}")
    public ResponseEntity getRequestByHostname(@PathVariable String hostname) {
        return new ResponseEntity(repository.getRequests(hostname), HttpStatus.OK);
    }

    @GetMapping("/requests/{hostname}/id/{id}")
    public ResponseEntity getRequestById(@PathVariable String hostname, @PathVariable String id) {
        return new ResponseEntity(repository.getRequest(hostname, id), HttpStatus.OK);
    }

    @PostMapping("/createProject")
    public ResponseEntity createProject(@RequestParam(value = "hostname") String hostname) {
        if (projectService.exists(hostname)) {
            return new ResponseEntity("Project " + hostname + " already exists", HttpStatus.BAD_REQUEST);
        }

        projectService.createProject(hostname);
        return new ResponseEntity("Project " + hostname + " created", HttpStatus.OK);
    }

    @DeleteMapping("/deleteProject")
    public ResponseEntity deleteProject(@RequestParam(value = "hostname") String hostname) {
        if (!projectService.exists(hostname)) {
            return new ResponseEntity("Project " + hostname + " does not exists", HttpStatus.NOT_FOUND);
        }

        projectService.deleteProject(hostname);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getProjects() {
        return new ResponseEntity<>(projectService.getProjects(), HttpStatus.OK);
    }

    @PostMapping("/fuzz")
    public ResponseEntity fuzz(@RequestBody CapturedFlow flow) {
        Response response = restService.sendRequest(flow);

        if (Objects.isNull(response)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        flow.updateResponseInfo(response);
        repository.insertRequest(flow);

        return new ResponseEntity(HttpStatus.OK);
    }
}
