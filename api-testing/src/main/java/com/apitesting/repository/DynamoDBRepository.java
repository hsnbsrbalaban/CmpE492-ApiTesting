package com.apitesting.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.apitesting.model.CapturedFlow;
import com.apitesting.model.Project;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DynamoDBRepository {

    private DynamoDBMapper mapper =
            new DynamoDBMapper(AmazonDynamoDBClientBuilder.standard().build());

    public void insertRequest(CapturedFlow request) {
        request.setTimestamp(System.currentTimeMillis());
        mapper.save(request);
    }

    public void deleteRequest(CapturedFlow request) {
        mapper.delete(request);
    }

    public CapturedFlow getRequest(String hostname, String id) {
        return mapper.load(CapturedFlow.class, hostname, id);
    }

    public List<CapturedFlow> getRequests(String hostname) {
        CapturedFlow flow = new CapturedFlow();
        flow.setHostname(hostname);

        return mapper.query(CapturedFlow.class, new DynamoDBQueryExpression().withHashKeyValues(flow));
    }

    public void insertProject(Project project) {
        mapper.save(project);
    }

    public void deleteProject(Project project) {
        mapper.delete(project);
    }

    public Project getProject(String hostname) {
        return mapper.load(Project.class, hostname);
    }

    public List<Project> getProjects() {
        return mapper.scan(Project.class, new DynamoDBScanExpression());
    }
}
