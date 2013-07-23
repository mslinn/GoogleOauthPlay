## Server-to-Server GoogleCalendarAuth ##
[Sample program](src/main/java/GoogleCalendarAuth.java) taken from
[GoogleCalendarAuth.java](https://code.google.com/p/gcs-admin-toolkit/source/browse/trunk/src/auth/GoogleCalendarAuth.java)

[See documentation](https://code.google.com/p/gcs-admin-toolkit/wiki/GCSAuthentication)

Run as a console app this way:

    $ sbt 'run-main service.GoogleCalendarAuth --key=MyPKCS12 --client_id=CLIENT_ID'

where:

 - `MyPKCS12` is a PKCS12 keystore file.

 - `CLIENT_ID` is the assigned Google client ID.

Also run as a Play app this way:

    $ play run

Be sure to set key and client_id in `conf/application.conf` first!
