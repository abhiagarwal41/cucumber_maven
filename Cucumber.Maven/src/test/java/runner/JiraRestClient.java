package ecommerce;

import com.sun.jersey.api.client.Client; 
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.naming.AuthenticationException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection; import javax.net.ssl.SSLContext; import javax.net.ssl.SSLSession; import javax.net.ssl.TrustManager; import javax.net.ssl.X509TrustManager; import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JiraRestClient {

	 private final String host;
	 private final String authHeader;
	 
	  public JiraRestClient(String host, String username, String password) {
	        this.authHeader = getAuth(username, password);
	        if (host.endsWith("/")) {
	            this.host = host.substring(0, host.length() - 1);
	        } else {
	            this.host = host;
	        }
	    }
	    
    public static void main(String... args) {
        try {
            JiraRestClient jrc = new JiraRestClient("https://tools.host.com/jira", "username", "password");
            //String issue = jrc.getIssue("EQSVALTICKET-3614");
            String issue = jrc.createIssue("EQSVALTICKET", "Bug", "Rest api", "description java");
            System.out.println(issue);
        } catch (JSONException ex) {
            System.err.println(ex);
        }
    }

    private static String getAuth(String username, String password) {
        try {
            String s = username + ":" + password;
            byte[] byteArray = s.getBytes();
            String auth;
            auth = Base64.encodeBase64String(byteArray);

            return auth;
        } catch (Exception ignore) {
            return "";
        }
    }


    public String getIssue(String issuekey) throws JSONException {
        JSONObject result = doREST("GET", "/rest/api/2/issue", issuekey + "?*all,-comment");

        // TODO: format the JSON object to your own favorite object format.
        return result.toString();
    }

    
    public String createIssue(String projectid, String issuetypeid, String summary, String description) throws JSONException {
        JSONObject fields = new JSONObject();
        fields.put("project", new JSONObject().put("key", projectid));
        fields.put("issuetype", new JSONObject().put("name", issuetypeid));
        fields.put("priority", new JSONObject().put("name", "High"));
        fields.put("reporter", new JSONObject().put("name", "agarwaat"));
        JSONArray myArray = new JSONArray();
        JSONObject j = new JSONObject();
        j.put("name","Roll up Europe");
        myArray.put(j);
        fields.put("components", myArray);
        fields.put("summary", summary);
        fields.put("customfield_13770", new JSONObject().put("value", "EMEA"));
        fields.put("description", description);

        JSONObject issue = new JSONObject();
        issue.put("fields", fields);

        JSONObject result = doREST("POST", "/rest/api/2/issue", issue.toString());

        return result.toString();
    }

     
    private JSONObject doREST(String method, String context, String arg) throws JSONException {
        try {
            ClientConfig config = getClientConfig();
            Client client = Client.create(config);

            if (!context.endsWith("/")) {
                context = context.concat("/");
            }

            WebResource webResource;
            if ("GET".equalsIgnoreCase(method)) {
                webResource = client.resource(this.host + context + arg);
            } else {
                webResource = client.resource(this.host + context);
            }

            WebResource.Builder builder = webResource.header("Authorization", "Basic " + this.authHeader).type("application/json").accept("application/json");

            ClientResponse response;

            if ("GET".equalsIgnoreCase(method)) {
                response = builder.get(ClientResponse.class);
            } else if ("POST".equalsIgnoreCase(method)) {
                response = builder.post(ClientResponse.class, arg);
            } else {
                response = builder.method(method, ClientResponse.class);
            }

            if (response.getStatus() == 401) {
                throw new AuthenticationException("HTTP 401 received: Invalid Username or Password.");
            }

            String jsonResponse = response.getEntity(String.class);
            JSONObject responseJson = new JSONObject(jsonResponse);

            return responseJson;
        } catch (JSONException ex) {
            throw new JSONException(ex);
        } catch (AuthenticationException ex) {
            throw new JSONException(ex);
        }
    }

    /**
     *
     * @return A clientconfig accepting all hosts and all ssl certificates
     * unconditionally.
     */
    private ClientConfig getClientConfig() {
        try {
            TrustManager[] trustAllCerts;
            trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }};

            // Ignore differences between given hostname and certificate hostname
            HostnameVerifier hv = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            HTTPSProperties prop = new HTTPSProperties(hv, sc);

            ClientConfig config = new DefaultClientConfig();
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, prop);

            return config;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            System.err.println(e);
        }

        return null;
    }
}
