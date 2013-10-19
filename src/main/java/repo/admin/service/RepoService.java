package repo.admin.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import loggee.api.Logged;
import repo.admin.model.ERepoItemState;
import vrds.model.EAttributeType;
import vrds.model.RepoItem;
import vrds.model.RepoItemAttribute;

@Logged
@Stateless
public class RepoService {
    @Inject
    private EntityManager entityManager;

    @Inject
    private RepoItemHandler repoItemHandler;

    public void setAttributeValue(Long repoItemId, String attributeName, String attributeValue) {
        RepoItem repoItem = entityManager.find(RepoItem.class, repoItemId);

        repoItemHandler.setValue(repoItem, attributeName, attributeValue);
    }

    public void setState(Long repoItemId, ERepoItemState state) {
        RepoItem repoItem = entityManager.find(RepoItem.class, repoItemId);

        repoItemHandler.setState(repoItem, state);
    }

    public String createRandomPersonsM(int number) {

        String lastPerson = "";
        for (int i = 0; i < number; i++) {
            RepoItem johnSmith = new RepoItem();
            johnSmith.setRepoName("person");
            entityManager.persist(johnSmith);

            RepoItemAttribute johnSmithName = new RepoItemAttribute();
            johnSmithName.setRepoItem(johnSmith);

            johnSmithName.setNameAndType("name", EAttributeType.STRING);
            johnSmithName.setValue("John Smith_" + i);

            RepoItemAttribute johnSmithBirthPlace = new RepoItemAttribute();
            johnSmithBirthPlace.setRepoItem(johnSmith);

            johnSmithBirthPlace.setNameAndType("birthPlace", EAttributeType.STRING);
            johnSmithBirthPlace.setValue("Budapest_" + i);

            entityManager.persist(johnSmithName);
            entityManager.persist(johnSmithBirthPlace);

            lastPerson = johnSmith.toString();

        }

        return lastPerson;

    }
}
