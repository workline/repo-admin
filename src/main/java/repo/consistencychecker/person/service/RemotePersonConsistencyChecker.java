package repo.consistencychecker.person.service;

import vrds.model.RepoItem;

public interface RemotePersonConsistencyChecker {

    public abstract boolean checkConsistency(RepoItem person);

}
