# Server-to-Server GoogleCalendarAuth #

This project works as a console app, but when I discovered that services cannot modify user's Google Calendars without the users being part of an Google administered domain I abandoned this project. The project has the structure of a Play Framework app, but was not implemented that way.

## Background ##

[Sample program](src/main/java/GoogleCalendarAuth.java) taken from
[GoogleCalendarAuth.java](https://code.google.com/p/gcs-admin-toolkit/source/browse/trunk/src/auth/GoogleCalendarAuth.java)

[See documentation](https://code.google.com/p/gcs-admin-toolkit/wiki/GCSAuthentication)

[Read Google's docs on how this works.](https://developers.google.com/accounts/docs/OAuth2#serviceaccount) and also
[Java API docs on Service Accounts](https://code.google.com/p/google-api-java-client/wiki/OAuth2#Service_Accounts).

## Setting up ##

 1. In the [Google APIs Access page](https://code.google.com/apis/console/?pli=1#project:552677350300:access),
 click the button titled **Create a new client ID...** and:

  a. Select **Service account**.

  b. Click **Create client ID**. The following message appears. I can't figure out how to change the password:

> Your private key's password is `notasecret`. You must present this password to use the key. There is only one copy of this key.
 You are responsible for downloading it and storing it securely. Click **Download private key** and save it with the name **googleKey.p12**.

  c. Our service needs to access user data, and access via API keys created by **Simple API Access** cannot do that, so ignore this section.
  Do not create a server key, a browser key, and Android key or an iOS key.

## Run as a console app ##

    $ sbt "run-main service.GoogleCalendarAuth --key=conf/googleKey.p12 --client_id=$GOOGLE_CLIENT_EMAIL"

where:

 - `conf/googleKey.p12` is a PKCS12 keystore file generated as described in the previous section.
 - `$GOOGLE_CLIENT_EMAIL` is the assigned Google client ID email from the the previous section, of the form `123456789012-akwlekfjlekfjselfislefjlskejfsel@developer.gserviceaccount.com`

## Run as a Local Play app ##
 2. Set environment variables for the key and client id shown on the same Google API page:

    `export GOOGLE_APP_KEY=blahblahblah`

    `export GOOGLE_CLIENT_EMAIL=123456.apps.googleusercontent.com`

 3. Run Play:

    `$ play debug run`


## Run as a Play app on Heroku ##

Not implemented

 2. Set environment variables for the key and client id shown on the same Google API page:

    `heroku config:add GOOGLE_APP_KEY=blahblahblah --app gcalauth`

    `heroku config:add GOOGLE_CLIENT_ID=123456.apps.googleusercontent.com --app gcalauth`

 3. Run on Heroku:

    `git remote add heroku git@heroku.com:gcalauth.git`

    `git push heroku master`
