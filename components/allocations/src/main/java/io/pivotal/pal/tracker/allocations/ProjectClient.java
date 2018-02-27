package io.pivotal.pal.tracker.allocations;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.client.RestOperations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectClient {

    private final RestOperations restOperations;
    private final String registrationServerEndpoint;
    Map<Long,ProjectInfo> clientMap=new ConcurrentHashMap<>();

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations= restOperations;
        this.registrationServerEndpoint = registrationServerEndpoint;
    }


    public ProjectInfo returnCache(long projectId){
        return clientMap.get(projectId);

    }
    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        ProjectInfo object = restOperations.getForObject(registrationServerEndpoint + "/projects/" + projectId, ProjectInfo.class);
        clientMap.put(projectId,object);
        return object;
    }
}
