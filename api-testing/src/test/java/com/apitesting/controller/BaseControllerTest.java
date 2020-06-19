package com.apitesting.controller;

import com.apitesting.builder.ProjectBuilder;
import com.apitesting.model.Project;
import com.apitesting.service.ProjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class BaseControllerTest {

    @InjectMocks
    BaseController baseController;

    @Mock
    ProjectService projectService;

    private static final String NAME = "test-app-name";

    @Test
    public void it_should_get_projects() {
        // setup
        Project project1 = ProjectBuilder.aProject().withHostname("project1").build();
        Project project2 = ProjectBuilder.aProject().withHostname("project2").build();

        given(projectService.getProjects()).willReturn(Arrays.asList(project1, project2));

        // execute
        ResponseEntity<List<Project>> response = baseController.getProjects();

        // check
        assertThat(response.getStatusCode().equals(HttpStatus.OK));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
    }

    @Test
    public void it_should_not_create_project_when_project_already_exists() {
        // setup
        given(projectService.exists(NAME)).willReturn(true);

        // execute
        ResponseEntity response = baseController.createProject(NAME);

        // check
        assertThat(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void it_should_create_project_when_project_does_not_exist() {
        // setup
        when(projectService.exists(anyString())).thenReturn(false);

        // execute
        ResponseEntity response = baseController.createProject(NAME);

        // check
        assertThat(response.getStatusCode().equals(HttpStatus.OK));
    }

    @Test
    public void it_should_not_delete_project_when_project_does_not_exist() {
        // setup
        when(projectService.exists(anyString())).thenReturn(false);

        // execute
        ResponseEntity response = baseController.deleteProject(NAME);

        // check
        assertThat(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void it_should_delete_project_when_project_exists() {
        // setup
        when(projectService.exists(anyString())).thenReturn(true);

        // execute
        ResponseEntity response = baseController.deleteProject(NAME);

        // check
        assertThat(response.getStatusCode().equals(HttpStatus.OK));
    }
}
