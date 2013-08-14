package repo.admin.service;

import javax.enterprise.context.ApplicationScoped;

import loggee.api.Logged;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import vrds.model.RepoItem;

@REST
@Logged
@ApplicationScoped
public class RESTConstistencyChecker implements ConsistencyChecker {
    @Override
    public boolean checkConsistency(String consistencyCheckerId, RepoItem repoItem) {
        boolean consistent;

        String repoDefinitionName = repoItem.getDefinition().getName();
        ClientRequest request = new ClientRequest("http://localhost:8080/repo-consistency_checker/rest/" + consistencyCheckerId + "/" + repoDefinitionName);
        request.accept("application/json");
        // request.body("application/json", repoItem);

        try {
            // ClientResponse<Boolean> response = request.post(Boolean.class);
            ClientResponse<Boolean> response = request.get(Boolean.class);

            consistent = response.getEntity();
        } catch (Exception e) {
            throw new RuntimeException("Couldn't get response from remote consistency checker!", e);
        }

        return consistent;
    }

}
