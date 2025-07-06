# Barclays
Take Home Test for Barclays

I have implemented the CRUD operations as defined in the pdf.
Some integration tests have been written to cover some of the User CRUD operations.
the openapi.yaml had been updated to reflect the auhtentication endpoints:
1. User can be created using POST /v1/users and sending in a username and password
2. User can authenticate using POST /v1/auth/signin and this will return a JWT token

Some of the specifications in openapi.yaml haven't been implemented, such as validation of format fo some fields, such as phone numbers. And although I have created the insfrastucure for Address entities, I ran out of time to actually implement their creation and relationship to Users.