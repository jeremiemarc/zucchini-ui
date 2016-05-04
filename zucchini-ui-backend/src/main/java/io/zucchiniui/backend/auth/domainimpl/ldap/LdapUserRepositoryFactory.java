package io.zucchiniui.backend.auth.domainimpl.ldap;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.zucchiniui.backend.auth.config.UserRepositoryFactory;
import io.zucchiniui.backend.auth.domain.UserRepository;

import javax.validation.Valid;

@JsonTypeName("ldap")
public class LdapUserRepositoryFactory implements UserRepositoryFactory {

    private String url;

    private String userDn;

    private String password;

    private String base;

    @Valid
    private final LdapAttributeMapping attributeMapping = new LdapAttributeMapping();

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getUserDn() {
        return userDn;
    }

    public void setUserDn(final String userDn) {
        this.userDn = userDn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getBase() {
        return base;
    }

    public void setBase(final String base) {
        this.base = base;
    }

    public LdapAttributeMapping getAttributeMapping() {
        return attributeMapping;
    }

    @Override
    public UserRepository create() {
        return new LdapUserRepositoryImpl.Builder()
            .withUrl(url)
            .withBase(base)
            .withUserDn(userDn)
            .withPassword(password)
            .withAttributeMapping(attributeMapping)
            .create();
    }

}
