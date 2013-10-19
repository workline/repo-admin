package repo.admin.tmp.service;

import repo.admin.tmp.model.AccessRightData;
import repo.admin.tmp.model.IssuedAccessRightData;
import repo.admin.tmp.model.OrganizationData;
import repo.admin.tmp.model.PersonData;

public interface RepoManager {
    Long createPerson(PersonData personData);

    Long createOrganization(OrganizationData organizationData);

    Long createAccessRight(AccessRightData accessRightData);

    Long createIssuedAccessRight(IssuedAccessRightData issuedAccessRightData);
}
