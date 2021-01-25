# okta-oauth2-example

This project services as "playground" to learn more about JWT authentication in spring and how to use different authorizations servers (in this case okta). I learned the following by this:
 * Using spring built-in oauth2 authentication via jwt() and opaque() security config
 * Testing JWT authentication end-to-end (including configured spring filters), by 
  * generating a RSA key that would "self sign" the JWT during integration tests
  * For that the NimbusJwtAuthenticator was loaded into the spring test context only, using @TestConfiguration
 * How to configure an Authorization Server / IAM system and the JWT claims by using okta
 * Implemented a proof of concept authorization service, that would validate claims within the token, that are configured in okta and map them to roles
