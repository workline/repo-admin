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
        public Set<IValue<Object>> getValues(RepoItemHandler repoItemHandler, RepoItemAttribute attribute) {
            RepoItem inheritenceSourceRepoItem = (RepoItem) attribute.getMetaAttribute(Constants.INHERITENCE_SOURCE_META_ATTRIBUTE_NAME).getValue();
            Set<IValue<Object>> values = repoItemHandler.getValues(inheritenceSourceRepoItem, attribute.getDefinition().getName());
            return values;
        }
    },
    OVERRIDE {
        @Override
        public Set<IValue<Object>> getValues(RepoItemHandler repoItemHandler, RepoItemAttribute attribute) {
            return attribute.getValues();
        }
    },
    CUMULATE {
        @Override
        public Set<IValue<Object>> getValues(RepoItemHandler repoItemHandler, RepoItemAttribute attribute) {
            Set<IValue<Object>> values = new HashSet<>();

            values.addAll(INHERIT.getValues(repoItemHandler, attribute));
            values.addAll(OVERRIDE.getValues(repoItemHandler, attribute));

            return values;
        }
    };

    public abstract Set<IValue<Object>> getValues(RepoItemHandler repoItemHandler, RepoItemAttribute attribute);
}
