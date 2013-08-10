package repo.admin.model;

import java.util.HashSet;
import java.util.Set;

import repo.admin.constants.Constants;
import repo.admin.service.RepoItemHandler;
import vrds.model.IValue;
import vrds.model.RepoItem;
import vrds.model.RepoItemAttribute;

public enum EInheritenceType {
    INHERIT {
        @Override
        public Set<IValue<Object>> getValues(RepoItem childRepoItem, RepoItemHandler repoItemHandler, RepoItemAttribute attribute) {
            String inheritenceSourceAttributeName = (String) attribute.getMetaAttribute(Constants.INHERITENCE_SOURCE_META_ATTRIBUTE_NAME).getValue();
            RepoItem inheritenceSourceRepoItem = (RepoItem) childRepoItem.getValue(inheritenceSourceAttributeName);
            Set<IValue<Object>> values = repoItemHandler.getValues(inheritenceSourceRepoItem, attribute.getDefinition().getName());
            return values;
        }
    },
    OVERRIDE {
        @Override
        public Set<IValue<Object>> getValues(RepoItem childRepoItem, RepoItemHandler repoItemHandler, RepoItemAttribute attribute) {
            return attribute.getValues();
        }
    },
    CUMULATE {
        @Override
        public Set<IValue<Object>> getValues(RepoItem childRepoItem, RepoItemHandler repoItemHandler, RepoItemAttribute attribute) {
            Set<IValue<Object>> values = new HashSet<>();

            values.addAll(INHERIT.getValues(childRepoItem, repoItemHandler, attribute));
            values.addAll(OVERRIDE.getValues(childRepoItem, repoItemHandler, attribute));

            return values;
        }
    };

    public abstract Set<IValue<Object>> getValues(RepoItem childRepoItem, RepoItemHandler repoItemHandler, RepoItemAttribute attribute);
}
