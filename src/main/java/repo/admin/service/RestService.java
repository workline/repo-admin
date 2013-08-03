package repo.admin.service;

import javax.enterprise.context.ApplicationScoped;

import loggee.api.Logged;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

@Logged
@ApplicationScoped
public class RestService {
    public boolean checkConsistency(String consistencyCheckerId, String repoDefinitionName, Long repoItemId) {
        boolean consistent;

        ClientRequest request = new ClientRequest("http://localhost:8080/repo_consistency_checker/rest/" + consistencyCheckerId + "/" + repoDefinitionName
                + "/" + repoItemId);
        try {
            ClientResponse<Boolean> response = request.get(Boolean.class);

            consistent = response.getEntity();
        } catch (Exception e) {
            throw new RuntimeException("Couldn't get response from remote consistency checker!", e);
        }

        return consistent;
    }

}
