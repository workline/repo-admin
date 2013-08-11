package repo.admin.service;

import vrds.model.RepoItem;

public interface ConsistencyChecker {

	public abstract boolean checkConsistency(String consistencyCheckerId,
			RepoItem repoItem);

}