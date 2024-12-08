package org.example.willwill96.keycloakify;

import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.forms.login.freemarker.FreeMarkerLoginFormsProviderFactory;
import org.keycloak.models.KeycloakSession;

public class CustomLoginFormsProviderFactory extends FreeMarkerLoginFormsProviderFactory {

    @Override
    public LoginFormsProvider create(KeycloakSession session) {
        return new CustomLoginFormsProvider(session);
    }

    @Override
    public String getId() {
        return "custom-provider";
    }

    @Override
    public int order() {
        return 1;
    }

}