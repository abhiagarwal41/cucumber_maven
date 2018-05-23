 public String postJSONToMagmaViaWebSSO(String dataToBePosted, String url, String rcasURL, String id, String password) throws Exception {
        String responseJSON = "";
        org.apache.http.client.HttpClient client = null;
        int timeoutMillis = 90000;

        // Prepare a request object
        HttpUriRequest httpUriRequest = new HttpPost(url);
        try {
            httpUriRequest.addHeader(SSOLOGINID, id);

            // authenticate to the HTTP server
            client = RCASAuthenticatorUtility.authenticatHttpClient(httpUriRequest, id, password, rcasURL);

            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, timeoutMillis);
            HttpConnectionParams.setSoTimeout(params, timeoutMillis);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("Format", "Json"));
            nameValuePairs.add(new BasicNameValuePair("ServiceType", "TradeBlotter"));
            nameValuePairs.add(new BasicNameValuePair("Message", dataToBePosted));
            ((HttpPost) httpUriRequest).setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(httpUriRequest);

            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                InputStream in = httpEntity.getContent();
                responseJSON = IOUtils.toString(in);
            }
        } catch (Exception e) {
            throw new Exception("Error while authenticating with site minder.", e);
        }

        return responseJSON;

    }
    
    
     public static HttpClient authenticatHttpClient(HttpUriRequest httpUriRequest, String id, String password, String rcasURL) throws Exception {
        RCASAuthenticator rcasAuthenticator = new RCASAuthenticator(id, password, rcasURL);
        DefaultHttpClient client = rcasAuthenticator.getDefaultHttpClient();
        client.setCookieStore(rcasAuthenticator.authenticate(httpUriRequest));
        return client;
    }
    
    
    package com.ptx.automation.magma;//

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

public class RCASAuthenticator {
    private static final String HTTP_SCHEME = "http";
    private static final int HTTP_PORT = 80;
    private static final String HTTPS_SCHEME = "https";
    private static final int HTTPS_PORT = 443;
    private static final long DEFAULT_TIMEOUT_SECONDS = 60L;
    private static final long timeout;
    private static final boolean followRedirects = true;
    private static final int maxConnectionsPerRoute = 20;
    private static final int maxTotalConnections = 20;

    static {
        timeout = TimeUnit.SECONDS.toMillis(60L);
    }

    private String userName;
    private String password;
    private DefaultHttpClient defaultHttpClient;
    private URI rcasURI;

    public RCASAuthenticator(String userName, String password, String rcasURI) throws Exception {
        this.userName = userName;
        this.password = password;
        this.rcasURI = URI.create(rcasURI);
        SSLSocketFactory sslSocketFactory = new SSLSocketFactory(new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        });
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        registry.register(new Scheme("https", 443, sslSocketFactory));
        ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager(registry, timeout, TimeUnit.MILLISECONDS);
        connectionManager.setDefaultMaxPerRoute(20);
        connectionManager.setMaxTotal(20);
        HttpParams httpParameters = new SyncBasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, (int) timeout);
        HttpConnectionParams.setSoTimeout(httpParameters, (int) timeout);
        httpParameters.setParameter("http.protocol.handle-redirects", true);
        httpParameters.setParameter("http.protocol.allow-circular-redirects", true);
        httpParameters.setParameter("http.protocol.handle-authentication", true);
        httpParameters.setParameter("http.protocol.expect-continue", true);
        this.defaultHttpClient = new DefaultHttpClient(connectionManager, httpParameters);
    }

    public RCASAuthenticator(String userName, String password, String rcasURI, DefaultHttpClient defaultHttpClient) throws Exception {
        this.userName = userName;
        this.password = password;
        this.rcasURI = URI.create(rcasURI);
        this.defaultHttpClient = defaultHttpClient;
    }

    public CookieStore authenticate(HttpUriRequest protectedURI) throws Exception {
        this.defaultHttpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(this.userName, this.password));
        HttpUriRequest rcasURIRequest = new HttpGet(this.rcasURI);
        HttpClientParams.setRedirecting(rcasURIRequest.getParams(), true);
        HttpResponse rcasURIResponse = this.defaultHttpClient.execute(rcasURIRequest);
        EntityUtils.consume(rcasURIResponse.getEntity());
        HttpGet protectedURIRequest = new HttpGet(protectedURI.getURI());
        HttpResponse protectedURIResponse = this.defaultHttpClient.execute(protectedURIRequest);
        HttpEntity httpEntity = protectedURIResponse.getEntity();
        if (httpEntity != null) {
            InputStream var7 = httpEntity.getContent();
        }

        EntityUtils.consume(protectedURIResponse.getEntity());
        return this.defaultHttpClient.getCookieStore();
    }

    public DefaultHttpClient getDefaultHttpClient() {
        return this.defaultHttpClient;
    }
}

    
    
