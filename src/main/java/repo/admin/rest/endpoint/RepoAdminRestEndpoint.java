package repo.admin.rest.endpoint;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import loggee.api.Logged;
import repo.admin.model.ERepoItemState;
import repo.admin.service.RepoService;

@Logged
@Path("/repoAdmin")
@RequestScoped
public class RepoAdminRestEndpoint {
    @Inject
    private RepoService repoService;

    @GET
    @Path("/setAttributeValue/{repoItemId}/{attributeName}/{attributeValue}")
    public boolean setAttributeValue(@PathParam("repoItemId") Long repoItemId, @PathParam("attributeName") String attributeName,
            @PathParam("attributeValue") String attributeValue) {

        repoService.setAttributeValue(repoItemId, attributeName, attributeValue);

        return true;
    }

    @GET
    @Path("/setState/{repoItemId}/{state}")
    public boolean setState(@PathParam("repoItemId") Long repoItemId, @PathParam("state") ERepoItemState state) {
        repoService.setState(repoItemId, state);

        return true;
    }

    @GET
    @Path("/createRandomPersonsM/{number}")
    public String createRandomPersonsM(@PathParam("number") int number) {

        long start = System.nanoTime();

        String returnText = repoService.createRandomPersonsM(number);

        long duration = (System.nanoTime() - start) / 1000000;

        return "duration: " + duration + "\n<br>" + returnText;
    }
}
