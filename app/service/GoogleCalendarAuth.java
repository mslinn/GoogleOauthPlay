package service;

/* Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;

/**
 * Taken from https://gcs-admin-toolkit.googlecode.com/svn/trunk/src/auth/GCS_Auth.java
 *
 * This script acquires an oauth2 bearer access_token use with authenticated GCS
 * ShoppingAPI and ContentAPI requests.  GCS customers should use one of the
 * libraries shown below to get the token in a production setting.  This script
 * is provided to  demonstrate the encryption and structure of getting an oauth2
 * bearer tokens. Ensure the computer this script is run on is synced with a
 * NTP server.
 * <p/>
 * REFERENCE :
 * https://code.google.com/apis/console/
 * https://developers.google.com/google-apps/calendar/v3/reference/calendars/get
 * https://developers.google.com/commerce-search/docs/shopping_auth
 * https://developers.google.com/accounts/docs/OAuth2ServiceAccount
 * http://code.google.com/p/google-api-java-client/wiki/OAuth2
 * <p/>
 * javac -cp .:commons-codec-1.6.jar GCS_Auth.java
 * <p/>
 * java -cp .:commons-codec-1.6.jar GCS_Auth --client_id=funnyEmailHere --key=blahblahblah
 * <p/>
 * client_id=<clientID Service account 'email'>
 * key = <private key for the service account>
 * <p/>
 * This code is not supported by Google */
public class GoogleCalendarAuth {
    // This enables authentication for two scopes. Is the 2nd scope actually required?
    private final String SCOPE = "https://www.googleapis.com/auth/calendar https://www.googleapis.com/auth/structuredcontent";
    private final String jwt_header = "{\"alg\":\"RS256\",\"typ\":\"JWT\"}";
    private String access_token = null;

    public static void main(String[] args) {
        String client_id = null;
        String key = null;

        for (String a : args) {
            if (a.startsWith("--key="))
                key = a.split("=")[1];
            if (a.startsWith("--client_id="))
                client_id = a.split("=")[1];
        }

        if ((args.length < 2) || client_id == null || key == null) {
            System.out.println("specify --key= --client_id=");
            System.exit(-1);
        }

        File keyFile = new File(key);
        if (!keyFile.exists()) {
            System.out.println("Error: " + key + " does not exist.");
            System.exit(-2);
        }

        if (!keyFile.canRead()) {
            System.out.println("Error: " + key + " cannot be read.");
            System.exit(-3);
        }

        final GoogleCalendarAuth j = new GoogleCalendarAuth(client_id, key);
    }

    public GoogleCalendarAuth(String client_id, String key) {
        final long now = System.currentTimeMillis() / 1000L;
        final long exp = now + 3600;
        final char[] password = "notasecret".toCharArray();
        final String claim = "{\"iss\":\"" + client_id + "\"," +
                              "\"scope\":\"" + SCOPE + "\"," +
                              "\"aud\":\"https://accounts.google.com/o/oauth2/token\"," +
                              "\"exp\":" + exp + "," +
                           // "\"prn\":\"some.user@somecorp.com\"," + // This require some.user to have their email served from a googlemail domain?
                              "\"iat\":" + now + "}";
        try {
            final String jwt = Base64.encodeBase64URLSafeString(jwt_header.getBytes()) + "." + Base64.encodeBase64URLSafeString(claim.getBytes("UTF-8"));
            final byte[] jwt_data = jwt.getBytes("UTF8");
            final Signature sig = Signature.getInstance("SHA256WithRSA");

            final KeyStore ks = java.security.KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(key), password);

            sig.initSign((PrivateKey) ks.getKey("privatekey", password));
            sig.update(jwt_data);
            final byte[] signatureBytes = sig.sign();
            final String b64sig = Base64.encodeBase64URLSafeString(signatureBytes);

            final String assertion = jwt + "." + b64sig;
            //System.out.println("Assertion: " + assertion);
            final String data = "grant_type=assertion" +
               "&assertion_type=" + URLEncoder.encode("http://oauth.net/grant_type/jwt/1.0/bearer", "UTF-8") +
               "&assertion=" + URLEncoder.encode(assertion, "UTF-8");

            // Make the Access Token Request
            URLConnection conn = null;
            try {
                final URL url = new URL("https://accounts.google.com/o/oauth2/token");
                conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    if (line.split(":").length > 0)
                        if (line.split(":")[0].trim().equals("\"access_token\""))
                            access_token = line.split(":")[1].trim().replace("\"", "").replace(",", "");
                    System.out.println(line);
                }
                wr.close();
                rd.close();
            } catch (Exception ex) {
                final InputStream error = ((HttpURLConnection) conn).getErrorStream();
                final BufferedReader br = new BufferedReader(new InputStreamReader(error));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null)
                    sb.append(line);
                System.out.println("Error: " + ex + "\n " + sb.toString());
            }
            System.out.println("access_token=" + access_token);
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }
}
