package io.zucchiniui.backend.auth.domainimpl.ldap;

import javax.validation.constraints.NotNull;

public class LdapAttributeMapping {

    @NotNull
    private String name;

    @NotNull
    private String displayName;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

}
