package repo.admin.service;

import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import loggee.api.Logged;
import repo.admin.constants.Constants;
import repo.admin.model.EInheritenceType;
import repo.admin.model.ERepoItemState;
import vrds.model.IValue;
import vrds.model.RepoItem;
import vrds.model.RepoItemAttribute;

@Logged
@Stateless
public class RepoItemHandler {
	@Inject
	private EntityManager entityManager;

	@Inject
	private MessagingService messagingService;
	@Inject
	@RMI
	private ConsistencyChecker consistencyChecker;

	public Set<IValue<Object>> getValues(Long repoItemId, String attributeName) {
		RepoItem repoItem = entityManager.find(RepoItem.class, repoItemId);

		return getValues(repoItem, attributeName);
	}

	public Set<IValue<Object>> getValues(RepoItem repoItem, String attributeName) {
		RepoItemAttribute repoItemAttribute = repoItem
				.getAttribute(attributeName);
		String inheritenceTypeAsString = (String) repoItemAttribute
				.getMetaAttribute(
						Constants.INHERITENCE_TYPE_META_ATTRIBUTE_NAME)
				.getValue();
		EInheritenceType inheritenceType = EInheritenceType
				.valueOf(inheritenceTypeAsString);
		Set<IValue<Object>> values = inheritenceType.getValues(repoItem, this,
				repoItemAttribute);

		return values;
	}

	public void setValue(RepoItem repoItem, String attributeName,
			Object attributeValue) {
		Object oldValue = repoItem.getAttribute(attributeName).getValue();
		repoItem.setValue(attributeName, attributeValue);

		messagingService.sendChangeEvent(repoItem.getId(), attributeName,
				oldValue, attributeValue);
		assertConsistencyIfActive(repoItem);
	}

	public ERepoItemState getState(RepoItem repoItem) {
		ERepoItemState state = ERepoItemState.valueOf(getValues(repoItem,
				Constants.STATE_ATTRIBUTE_NAME).iterator().next().getValue()
				.toString());
		return state;
	}

	public void setState(RepoItem repoItem, ERepoItemState state) {
		setValue(repoItem, Constants.STATE_ATTRIBUTE_NAME, state.toString());
		assertConsistencyIfActive(repoItem);
	}

	public boolean isActive(RepoItem repoItem) {
		boolean active = getState(repoItem) == ERepoItemState.ACTIVE;

		return active;
	}

	public void checkConsistency(RepoItem repoItem) {
		Long repoItemId = repoItem.getId();
		String repoDefinitionName = repoItem.getDefinition().getName();
		Set<IValue<Object>> consistencyCheckerValues = getValues(repoItem,
				Constants.CONSISTENCY_CHECKER_ATTRIBUTE_NAME);
		for (IValue<Object> consistencyCheckerValue : consistencyCheckerValues) {
			String consistencyCheckerId = (String) consistencyCheckerValue
					.getValue();
			boolean consistent = consistencyChecker.checkConsistency(
					consistencyCheckerId, repoItem);
			if (!consistent) {
				throw new IllegalStateException(repoItemId + " of the "
						+ repoDefinitionName + " repository is inconsistent!");
			}
		}
	}

	private void assertConsistencyIfActive(RepoItem repoItem) {
		if (isActive(repoItem)) {
			checkConsistency(repoItem);
		}
	}
}
