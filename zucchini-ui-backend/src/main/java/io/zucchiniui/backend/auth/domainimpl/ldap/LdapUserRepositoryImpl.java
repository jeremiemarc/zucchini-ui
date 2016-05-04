package io.zucchiniui.backend.auth.domainimpl.ldap;

import io.zucchiniui.backend.auth.domain.User;
import io.zucchiniui.backend.auth.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapEntryIdentification;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import java.util.Optional;

class LdapUserRepositoryImpl implements UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(LdapUserRepositoryImpl.class);

    private final LdapTemplate ldapTemplate;

    private final LdapAttributeMapping attributeMapping;

    public static class Builder {

        private final LdapContextSource ldapContextSource = new LdapContextSource();

        private String url;

        private String userDn;

        private String password;

        private String base;

        private LdapAttributeMapping attributeMapping;

        public Builder withUrl(final String url) {
            this.url = url;
            return this;
        }

        public Builder withUserDn(final String userDn) {
            this.userDn = userDn;
            return this;
        }

        public Builder withPassword(final String password) {
            this.password = password;
            return this;
        }

        public Builder withBase(final String base) {
            this.base = base;
            return this;
        }

        public Builder withAttributeMapping(final LdapAttributeMapping attributeMapping) {
            this.attributeMapping = attributeMapping;
            return this;
        }

        public LdapUserRepositoryImpl create() {
            ldapContextSource.setUrl(url);

            if (userDn != null) {
                ldapContextSource.setUserDn(userDn);
            }
            if (password != null) {
                ldapContextSource.setPassword(password);
            }

            ldapContextSource.setBase(base);
            ldapContextSource.afterPropertiesSet();



            final LdapTemplate ldapTemplate = new LdapTemplate(ldapContextSource);
            return new LdapUserRepositoryImpl(ldapTemplate, attributeMapping);
        }

    }

    private LdapUserRepositoryImpl(final LdapTemplate ldapTemplate, final LdapAttributeMapping attributeMapping) {
        this.ldapTemplate = ldapTemplate;
        this.attributeMapping = attributeMapping;
    }

    @Override
    public Optional<User> findByNameAndPassword(final String name, final String password) {
        try {
            final User user = ldapTemplate.authenticate(queryForName(name), password, (DirContext ctx, LdapEntryIdentification ldapEntryIdentification) -> {
                try {
                    final DirContextOperations operations = (DirContextOperations) ctx.lookup(ldapEntryIdentification.getRelativeName());

                    String displayName = operations.getStringAttribute(attributeMapping.getDisplayName());
                    if (displayName == null) {
                        displayName = name;
                    }

                    return new User(name, displayName);
                } catch (final NamingException e) {
                    throw new RuntimeException("Failed to lookup " + ldapEntryIdentification.getRelativeName(), e);
                }
            });
            return Optional.of(user);
        } catch (EmptyResultDataAccessException | AuthenticationException | NameNotFoundException e) {
            LOGGER.warn("Can't authenticate user {}", name, e);
        }

        return Optional.empty();
    }

    private LdapQuery queryForName(final String name) {
        return LdapQueryBuilder.query().where(attributeMapping.getName()).is(name);
    }

}
