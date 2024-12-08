package org.example.willwill96.keycloakify;

import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;

import org.keycloak.forms.login.LoginFormsPages;
import org.keycloak.forms.login.freemarker.FreeMarkerLoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.keycloak.theme.Theme;

import jakarta.ws.rs.core.UriBuilder;

public class CustomLoginFormsProvider extends FreeMarkerLoginFormsProvider {

    private static final String AB_TEST_KEYCLOAK_ATTRIBUTE = "exampleAbTest";

    private static final List<String> WHITELISTED_REALMS = List.of("test-realm");

    private static final String AB_TEST_VALUE_ON = "on";

    private static final String AB_TEST_VALUE_OFF = "off";
    
    public CustomLoginFormsProvider(KeycloakSession session) {
        super(session);
    }

    @Override
    protected void createCommonAttributes(Theme theme, Locale locale, Properties messagesBundle,
            UriBuilder baseUriBuilder, LoginFormsPages page) {
        super.createCommonAttributes(theme, locale, messagesBundle, baseUriBuilder, page);
        if (!WHITELISTED_REALMS.contains(this.session.getContext().getRealm().getName()))
            return;
        
        this.attributes.put(AB_TEST_KEYCLOAK_ATTRIBUTE, this.getOrSetABTest());
    }

    private String getOrSetABTest() {
        AuthenticationSessionModel authSession = this.session.getContext().getAuthenticationSession();
        if (authSession == null) return AB_TEST_VALUE_OFF;
        
        String existingValue = authSession.getAuthNote(AB_TEST_KEYCLOAK_ATTRIBUTE); 
        if (existingValue == null) {
            existingValue = (new Random()).nextBoolean() ? AB_TEST_VALUE_ON : AB_TEST_VALUE_OFF;
            authSession.setAuthNote(AB_TEST_KEYCLOAK_ATTRIBUTE, existingValue);
        }
        return existingValue;
    }
}
