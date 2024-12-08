FROM node:22 AS keycloakify_jar_builder

RUN wget -O - https://apt.corretto.aws/corretto.key | gpg --dearmor -o /usr/share/keyrings/corretto-keyring.gpg && \
    echo "deb [signed-by=/usr/share/keyrings/corretto-keyring.gpg] https://apt.corretto.aws stable main" |  tee /etc/apt/sources.list.d/corretto.list

RUN apt-get update && \
    apt-get install -y java-21-amazon-corretto-jdk && \
    apt-get install -y maven;


COPY ./keycloakify-starter/package.json ./keycloakify-starter/yarn.lock /opt/app/

WORKDIR /opt/app

RUN yarn install --frozen-lockfile

COPY ./keycloakify-starter/ /opt/app/

RUN yarn build-keycloak-theme

RUN ls dist_keycloak/

FROM quay.io/keycloak/keycloak:latest AS keycloak_builder

# Set the working directory inside the Keycloak container
WORKDIR /opt/keycloak

COPY --from=keycloakify_jar_builder /opt/app/dist_keycloak/keycloak-theme-for-kc-22-to-25.jar /opt/keycloak/providers/

RUN /opt/keycloak/bin/kc.sh build


FROM quay.io/keycloak/keycloak:26
COPY --from=keycloak_builder /opt/keycloak/ /opt/keycloak/
COPY data/import/ /opt/keycloak/data/import/
ENV KC_HOSTNAME=localhost
ENV KC_BOOTSTRAP_ADMIN_USERNAME=admin
ENV KC_BOOTSTRAP_ADMIN_PASSWORD=admin
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm"]