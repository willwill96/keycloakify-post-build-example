# Keycloakify-Post-Build-Example

This repo serves as an example of how to dynamically render in your keycloakify app at runtime and buildtime. In this example, I'm displaying in this functionality in the context of A/B tests, but this approach could also be used to dynamically render based on other criteria, like environment variables or cookies on the user's session.

> Note: This `README` assumes that you have a basic knowledge of [Keycloak](https://www.keycloak.org/) & [Keycloakify](https://www.keycloakify.dev/)

# Running

In order to run this demo you can run the following:

```sh
docker-compose up --build
```

This will spin up a keycloak server on `http://localhost:8080` that you can log into with `admin:admin`.

If you navigate to `http://localhost:8080/realms/test-realm`, you will be taken to a login flow that has two dynamically rendered pieces of content:
1. The login page should have a header at the top displaying the AB test value.
2. The favicon on the page should be dynamic based on the result of the AB test
    - If the AB test is off, the keycloakify favicon will be displayed.
    - If the AB test is on, a custom favicon will be displayed.

# Project Structure

This demo consists of three parts:
- A keycloakify theme which allows for customizing login forms (as well as some other pages within keycloak).
- A custom jar implementation of [FreemarkerLoginFormsProvider](https://github.com/keycloak/keycloak/blob/main/services/src/main/java/org/keycloak/forms/login/freemarker/FreeMarkerLoginFormsProvider.java) so that we can add additionally logic to the rendering of forms.
- A keycloak app setup to consume the keycloakify theme & custom jar implementation.

> Note: `FreemarkerLoginFormsProvider` is part of Keycloak's private API. Overriding it means you may not be able to rely on breaking changes to this file being documented.