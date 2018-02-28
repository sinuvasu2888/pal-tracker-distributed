package io.pivotal.pal.tracker.backlog;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestOperations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectClient {
    private final Logger logger=LoggerFactory.getLogger(getClass());
    private final RestOperations restOperations;
    private final String endpoint;
    Map<Long,ProjectInfo> clientMap=new ConcurrentHashMap<>();


    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations = restOperations;
        this.endpoint = registrationServerEndpoint;
    }


    public ProjectInfo getProjectFromCache(long projectId){
        logger.info("Getting project with ID{} from cache",projectId);
        return clientMap.get(projectId);

    }

    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        ProjectInfo object = restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class);
            clientMap.put(projectId,object);
            return object;
    }
}
