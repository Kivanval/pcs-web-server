package com.example.pcswebserver.domain;

import java.util.Collection;
import java.util.Set;

public enum StorePermissionType {
    CREATOR {
        @Override
        public Collection<StorePermissionType> getChildren() {
            return Set.of(MODIFY, READ);
        }
    }, MODIFY {
        @Override
        public Collection<StorePermissionType> getChildren() {
            return Set.of(READ);
        }
    }, READ {
        @Override
        public Collection<StorePermissionType> getChildren() {
            return Set.of();
        }
    };

    public abstract Collection<StorePermissionType> getChildren();

}
