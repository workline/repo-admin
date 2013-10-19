package repo.admin.external.init;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import repo.admin.constants.Constants;
import repo.admin.model.EInheritenceType;
import repo.admin.model.ERepoItemState;
import repo.admin.tmp.model.PersonData;
import repo.admin.tmp.service.RepoManager;
import vrds.model.Attribute;
import vrds.model.EAttributeType;
import vrds.model.MetaAttribute;
import vrds.model.RepoItem;
import vrds.model.RepoItemAttribute;

@Startup
@Singleton
public class DatabaseInitializer {
    private static final String PERSON_REPO_NAME = "person";
    private static final String PERSON_NAME_ATTRIBUTE_NAME = "name";
    private static final String PERSON_BIRTH_PLACE_ATTRIBUTE_NAME = "birthPlace";
    private static final String PERSON_MOTHER_NAME_ATTRIBUTE_NAME = "motherName";
    private static final String PERSON_ORGANIZATION_ATTRIBUTE_NAME = "organization";

    private static final String ORGANIZATION_REPO_NAME = "organization";
    private static final String ORGANIZATION_NAME_ATTRIBUTE_NAME = "name";
    private static final String ORGANIZATION_MANAGER_ATTRIBUTE_NAME = "manager";

    @Inject
    private EntityManager entityManager;

    private RepoManager repoManager;

    @PostConstruct
    public void initDatabase() {
        // RepoItem
        RepoItem developersOrganization = new RepoItem();
        developersOrganization.setRepoName(ORGANIZATION_REPO_NAME);

        RepoItem johnSmith = new RepoItem();
        johnSmith.setRepoName(PERSON_REPO_NAME);

        entityManager.persist(developersOrganization);
        entityManager.persist(johnSmith);

        // RepoItemAttribute
        // // Organization
        RepoItemAttribute developersOrganizationName = new RepoItemAttribute();
        developersOrganizationName.setRepoItem(developersOrganization);
        developersOrganizationName.setNameAndType(ORGANIZATION_NAME_ATTRIBUTE_NAME, EAttributeType.STRING);
        developersOrganizationName.setValue("Developers");

        RepoItemAttribute developersOrganizationManager = new RepoItemAttribute();
        developersOrganizationManager.setRepoItem(developersOrganization);
        developersOrganizationManager.setNameAndType(ORGANIZATION_MANAGER_ATTRIBUTE_NAME, EAttributeType.REPO_ITEM);
        developersOrganizationName.setValue(johnSmith);

        RepoItemAttribute developersOrganizationState = addAttribute(developersOrganization, Constants.STATE_ATTRIBUTE_NAME, EAttributeType.STRING,
                ERepoItemState.ACTIVE.toString(), EInheritenceType.OVERRIDE, null);

        RepoItemAttribute developersConsistencyChecker = addAttribute(developersOrganization, Constants.CONSISTENCY_CHECKER_ATTRIBUTE_NAME,
                EAttributeType.STRING, "organizationConsistencyChecker", EInheritenceType.OVERRIDE, null);

        entityManager.persist(developersOrganizationName);
        entityManager.persist(developersOrganizationManager);
        entityManager.persist(developersOrganizationState);
        entityManager.persist(developersConsistencyChecker);

        // RepoItemAttribute
        // // Person
        RepoItemAttribute johnSmithName = new RepoItemAttribute();
        johnSmithName.setRepoItem(johnSmith);
        johnSmithName.setNameAndType(PERSON_NAME_ATTRIBUTE_NAME, EAttributeType.STRING);
        johnSmithName.setValue("John Smith");

        RepoItemAttribute johnSmithBirthPlace = new RepoItemAttribute();
        johnSmithBirthPlace.setRepoItem(johnSmith);
        johnSmithBirthPlace.setNameAndType(PERSON_BIRTH_PLACE_ATTRIBUTE_NAME, EAttributeType.STRING);
        johnSmithBirthPlace.setValue("Budapest");

        RepoItemAttribute johnSmithMothersName = new RepoItemAttribute();
        johnSmithMothersName.setRepoItem(johnSmith);
        johnSmithMothersName.setNameAndType(PERSON_MOTHER_NAME_ATTRIBUTE_NAME, EAttributeType.STRING);
        johnSmithMothersName.setValue("Jane Smith");

        RepoItemAttribute johnSmithOrganization = new RepoItemAttribute();
        johnSmithOrganization.setRepoItem(johnSmith);
        johnSmithOrganization.setNameAndType(PERSON_ORGANIZATION_ATTRIBUTE_NAME, EAttributeType.REPO_ITEM);
        johnSmithOrganization.setValue(developersOrganization);

        RepoItemAttribute johnSmithState = addState(johnSmith, null, EInheritenceType.INHERIT, johnSmithOrganization);

        RepoItemAttribute johnSmithConsistencyChecker = addConsistencyChecker(johnSmith, "personConsistencyChecker", EInheritenceType.CUMULATE,
                johnSmithOrganization);

        entityManager.persist(johnSmithName);
        entityManager.persist(johnSmithBirthPlace);
        entityManager.persist(johnSmithMothersName);
        entityManager.persist(johnSmithOrganization);
        entityManager.persist(johnSmithState);
        entityManager.persist(johnSmithConsistencyChecker);
    }

    private void initWorkflowRepo() {
        PersonData personData;

        personData = new PersonData();
        repoManager.createPerson(personData);

    }

    private RepoItemAttribute addState(RepoItem repoItem, String state, EInheritenceType inheritenceType, Attribute attributeReferencingInheritenceSource) {
        return addAttribute(repoItem, Constants.STATE_ATTRIBUTE_NAME, EAttributeType.STRING, state, inheritenceType, attributeReferencingInheritenceSource);
    }

    private RepoItemAttribute addConsistencyChecker(RepoItem repoItem, String consistencyChecker, EInheritenceType inheritenceType,
            Attribute attributeReferencingInheritenceSource) {

        return addAttribute(repoItem, Constants.CONSISTENCY_CHECKER_ATTRIBUTE_NAME, EAttributeType.STRING, consistencyChecker, inheritenceType,
                attributeReferencingInheritenceSource);
    }

    private RepoItemAttribute addAttribute(RepoItem repoItem, String name, EAttributeType attributeType, Object value, EInheritenceType inheritenceType,
            Attribute attributeReferencingInheritenceSource) {

        RepoItemAttribute attribute = new RepoItemAttribute();

        attribute.setRepoItem(repoItem);
        attribute.setNameAndType(name, attributeType);
        attribute.setValue(value);

        setInheritencyType(attribute, inheritenceType);
        setInheritencySource(attribute, attributeReferencingInheritenceSource);

        return attribute;
    }

    private void setInheritencyType(RepoItemAttribute attribute, EInheritenceType inheritenceType) {
        MetaAttribute inheritenceTypeAttribute = new MetaAttribute();

        inheritenceTypeAttribute.setOwnerAttribute(attribute);
        inheritenceTypeAttribute.setNameAndType(Constants.INHERITENCE_TYPE_META_ATTRIBUTE_NAME, EAttributeType.STRING);
        inheritenceTypeAttribute.setValue(inheritenceType.toString());

        entityManager.persist(inheritenceTypeAttribute);
    }

    private void setInheritencySource(RepoItemAttribute attribute, Attribute attributeReferencingInheritenceSource) {
        MetaAttribute inheritenceSourceAttribute = new MetaAttribute();

        inheritenceSourceAttribute.setOwnerAttribute(attribute);
        inheritenceSourceAttribute.setNameAndType(Constants.INHERITENCE_SOURCE_META_ATTRIBUTE_NAME, EAttributeType.ATTRIBUTE);
        inheritenceSourceAttribute.setValue(attributeReferencingInheritenceSource);

        entityManager.persist(inheritenceSourceAttribute);
    }
}
