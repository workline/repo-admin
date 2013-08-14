package repo.admin.external.init;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import repo.admin.constants.Constants;
import repo.admin.model.EInheritenceType;
import repo.admin.model.ERepoItemState;
import vrds.model.Attribute;
import vrds.model.AttributeDefinition;
import vrds.model.EAttributeType;
import vrds.model.MetaAttribute;
import vrds.model.MetaAttributeDefinition;
import vrds.model.RepoAttributeDefinition;
import vrds.model.RepoDefinition;
import vrds.model.RepoItem;
import vrds.model.RepoItemAttribute;

@Startup
@Singleton
public class DatabaseInitializer {
    @Inject
    private EntityManager entityManager;

    private MetaAttributeDefinition inheritenceTypeMetaAttributeDefinition;
    private MetaAttributeDefinition inheritenceSourceMetaAttributeDefinition;

    @PostConstruct
    public void initDatabase() {
        // RepoDefinitions
        RepoDefinition organizationDefinition = new RepoDefinition();
        organizationDefinition.setName("organization");

        RepoDefinition personDefinition = new RepoDefinition();
        personDefinition.setName("person");

        entityManager.persist(organizationDefinition);
        entityManager.persist(personDefinition);

        // MetaAttributeDefinitions
        inheritenceTypeMetaAttributeDefinition = new MetaAttributeDefinition();
        inheritenceTypeMetaAttributeDefinition.setName(Constants.INHERITENCE_TYPE_META_ATTRIBUTE_NAME);
        inheritenceTypeMetaAttributeDefinition.setType(EAttributeType.STRING);

        inheritenceSourceMetaAttributeDefinition = new MetaAttributeDefinition();
        inheritenceSourceMetaAttributeDefinition.setName(Constants.INHERITENCE_SOURCE_META_ATTRIBUTE_NAME);
        inheritenceSourceMetaAttributeDefinition.setType(EAttributeType.ATTRIBUTE);

        entityManager.persist(inheritenceTypeMetaAttributeDefinition);
        entityManager.persist(inheritenceSourceMetaAttributeDefinition);

        // RepoAttributeDefinitions
        // // organization
        RepoAttributeDefinition organizationNameDefinition = new RepoAttributeDefinition();
        organizationNameDefinition.setName("name");
        organizationNameDefinition.setType(EAttributeType.STRING);
        organizationNameDefinition.setOwnerRepoDefinition(organizationDefinition);

        RepoAttributeDefinition organizationManagerDefinition = new RepoAttributeDefinition();
        organizationManagerDefinition.setName("manager");
        organizationManagerDefinition.setType(EAttributeType.REPO_ITEM);
        organizationManagerDefinition.setOwnerRepoDefinition(organizationDefinition);
        organizationManagerDefinition.setValueRepoType(personDefinition);

        RepoAttributeDefinition organizationStateDefinition = addStateAttributeDefinition(organizationDefinition, inheritenceTypeMetaAttributeDefinition,
                inheritenceSourceMetaAttributeDefinition);

        RepoAttributeDefinition organizationConsistencyCheckerDefinition = addConsistencyCheckerAttributeDefinition(organizationDefinition,
                inheritenceTypeMetaAttributeDefinition, inheritenceSourceMetaAttributeDefinition);

        entityManager.persist(organizationNameDefinition);
        entityManager.persist(organizationManagerDefinition);
        entityManager.persist(organizationStateDefinition);
        entityManager.persist(organizationConsistencyCheckerDefinition);

        // // person
        RepoAttributeDefinition personNameDefinition = new RepoAttributeDefinition();
        personNameDefinition.setName("name");
        personNameDefinition.setType(EAttributeType.STRING);
        personNameDefinition.setOwnerRepoDefinition(personDefinition);

        RepoAttributeDefinition personBirthPlaceDefinition = new RepoAttributeDefinition();
        personBirthPlaceDefinition.setName("birthPlace");
        personBirthPlaceDefinition.setType(EAttributeType.STRING);
        personBirthPlaceDefinition.setOwnerRepoDefinition(personDefinition);

        RepoAttributeDefinition personMothersNameDefinition = new RepoAttributeDefinition();
        personMothersNameDefinition.setName("motherName");
        personMothersNameDefinition.setType(EAttributeType.STRING);
        personMothersNameDefinition.setOwnerRepoDefinition(personDefinition);

        RepoAttributeDefinition personOrganizationDefinition = new RepoAttributeDefinition();
        personOrganizationDefinition.setName("organization");
        personOrganizationDefinition.setType(EAttributeType.REPO_ITEM);
        personOrganizationDefinition.setOwnerRepoDefinition(personDefinition);
        personOrganizationDefinition.setValueRepoType(organizationDefinition);

        RepoAttributeDefinition personStateDefinition = addStateAttributeDefinition(personDefinition, inheritenceTypeMetaAttributeDefinition,
                inheritenceSourceMetaAttributeDefinition);

        RepoAttributeDefinition personConsistencyCheckerDefinition = addConsistencyCheckerAttributeDefinition(personDefinition,
                inheritenceTypeMetaAttributeDefinition, inheritenceSourceMetaAttributeDefinition);

        entityManager.persist(personNameDefinition);
        entityManager.persist(personBirthPlaceDefinition);
        entityManager.persist(personMothersNameDefinition);
        entityManager.persist(personOrganizationDefinition);
        entityManager.persist(personStateDefinition);
        entityManager.persist(personConsistencyCheckerDefinition);

        // RepoItem
        RepoItem developersOrganization = new RepoItem();
        developersOrganization.setDefinition(organizationDefinition);

        RepoItem johnSmith = new RepoItem();
        johnSmith.setDefinition(personDefinition);

        entityManager.persist(developersOrganization);
        entityManager.persist(johnSmith);

        // RepoItemAttribute
        // // Organization
        RepoItemAttribute developersOrganizationName = new RepoItemAttribute();
        developersOrganizationName.setRepoItem(developersOrganization);
        developersOrganizationName.setDefinition(organizationNameDefinition);
        developersOrganizationName.setValue("Developers");

        RepoItemAttribute developersOrganizationManager = new RepoItemAttribute();
        developersOrganizationManager.setRepoItem(developersOrganization);
        developersOrganizationManager.setDefinition(organizationManagerDefinition);
        developersOrganizationName.setValue(johnSmith);

        RepoItemAttribute developersOrganizationState = setAttribute(developersOrganization, organizationStateDefinition, ERepoItemState.ACTIVE.toString(),
                EInheritenceType.OVERRIDE, null);

        RepoItemAttribute developersConsistencyChecker = setAttribute(developersOrganization, organizationConsistencyCheckerDefinition,
                "organizationConsistencyChecker", EInheritenceType.OVERRIDE, null);

        entityManager.persist(developersOrganizationName);
        entityManager.persist(developersOrganizationManager);
        entityManager.persist(developersOrganizationState);
        entityManager.persist(developersConsistencyChecker);

        // RepoItemAttribute
        // // Person
        RepoItemAttribute johnSmithName = new RepoItemAttribute();
        johnSmithName.setRepoItem(johnSmith);
        johnSmithName.setDefinition(personNameDefinition);
        johnSmithName.setValue("John Smith");

        RepoItemAttribute johnSmithBirthPlace = new RepoItemAttribute();
        johnSmithBirthPlace.setRepoItem(johnSmith);
        johnSmithBirthPlace.setDefinition(personBirthPlaceDefinition);
        johnSmithBirthPlace.setValue("Budapest");

        RepoItemAttribute johnSmithMothersName = new RepoItemAttribute();
        johnSmithMothersName.setRepoItem(johnSmith);
        johnSmithMothersName.setDefinition(personMothersNameDefinition);
        johnSmithMothersName.setValue("Jane Smith");

        RepoItemAttribute johnSmithOrganization = new RepoItemAttribute();
        johnSmithOrganization.setRepoItem(johnSmith);
        johnSmithOrganization.setDefinition(personOrganizationDefinition);
        johnSmithOrganization.setValue(developersOrganization);

        RepoItemAttribute johnSmithState = setAttribute(johnSmith, personStateDefinition, null, EInheritenceType.INHERIT, johnSmithOrganization);

        RepoItemAttribute johnSmithConsistencyChecker = setAttribute(johnSmith, personConsistencyCheckerDefinition, "personConsistencyChecker",
                EInheritenceType.CUMULATE, johnSmithOrganization);

        entityManager.persist(johnSmithName);
        entityManager.persist(johnSmithBirthPlace);
        entityManager.persist(johnSmithMothersName);
        entityManager.persist(johnSmithOrganization);
        entityManager.persist(johnSmithState);
        entityManager.persist(johnSmithConsistencyChecker);
    }

    private RepoItemAttribute setAttribute(RepoItem repoItem, RepoAttributeDefinition definition, Object value, EInheritenceType inheritenceType,
            Attribute attributeReferencingInheritenceSource) {

        RepoItemAttribute attribute = new RepoItemAttribute();

        attribute.setRepoItem(repoItem);
        attribute.setDefinition(definition);
        attribute.setValue(value);

        setInheritencyType(attribute, inheritenceType);
        setInheritencySource(attribute, attributeReferencingInheritenceSource);

        return attribute;
    }

    private void setInheritencyType(RepoItemAttribute attribute, EInheritenceType inheritenceType) {
        MetaAttribute inheritenceTypeAttribute = new MetaAttribute();

        inheritenceTypeAttribute.setDefinition(inheritenceTypeMetaAttributeDefinition);
        inheritenceTypeAttribute.setOwnerAttribute(attribute);
        inheritenceTypeAttribute.setValue(inheritenceType.toString());

        entityManager.persist(inheritenceTypeAttribute);
    }

    private void setInheritencySource(RepoItemAttribute attribute, Attribute attributeReferencingInheritenceSource) {
        MetaAttribute inheritenceSourceAttribute = new MetaAttribute();

        inheritenceSourceAttribute.setDefinition(inheritenceSourceMetaAttributeDefinition);
        inheritenceSourceAttribute.setOwnerAttribute(attribute);
        inheritenceSourceAttribute.setValue(attributeReferencingInheritenceSource);

        entityManager.persist(inheritenceSourceAttribute);
    }

    private RepoAttributeDefinition addConsistencyCheckerAttributeDefinition(RepoDefinition ownerRepoDefinition,
            MetaAttributeDefinition ... metaAttributeDefinitions) {

        return addAttributeDefinition(ownerRepoDefinition, Constants.CONSISTENCY_CHECKER_ATTRIBUTE_NAME, EAttributeType.STRING, metaAttributeDefinitions);
    }

    private RepoAttributeDefinition addStateAttributeDefinition(RepoDefinition ownerRepoDefinition, MetaAttributeDefinition ... metaAttributeDefinitions) {
        return addAttributeDefinition(ownerRepoDefinition, Constants.STATE_ATTRIBUTE_NAME, EAttributeType.STRING, metaAttributeDefinitions);
    }

    private RepoAttributeDefinition addAttributeDefinition(RepoDefinition ownerRepoDefinition, String attributeName, EAttributeType attributeType,
            MetaAttributeDefinition ... metaAttributeDefinitions) {

        RepoAttributeDefinition attributeDefinition = new RepoAttributeDefinition();

        attributeDefinition.setName(attributeName);
        attributeDefinition.setType(attributeType);
        attributeDefinition.setOwnerRepoDefinition(ownerRepoDefinition);

        addMetaAttributeDefinitions(attributeDefinition, metaAttributeDefinitions);

        return attributeDefinition;
    }

    private void addMetaAttributeDefinitions(AttributeDefinition attributeDefinition, MetaAttributeDefinition ... metaAttributeDefinitions) {
        Set<MetaAttributeDefinition> metaAttributeDefinitionSet = new HashSet<>(Arrays.asList(metaAttributeDefinitions));

        attributeDefinition.setMetaAttributeDefinitions(metaAttributeDefinitionSet);
    }
}
