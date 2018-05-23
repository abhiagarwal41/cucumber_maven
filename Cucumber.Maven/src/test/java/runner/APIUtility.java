package com.counterparty.automation.utilities;

import static io.restassured.RestAssured.given;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.junit.Assert;


import static org.hamcrest.CoreMatchers.*;

import io.restassured.response.Response;
import io.restassured.response.ResponseBody;


public class APIUtility {

	final static Logger logger = Logger.getLogger(APIUtility.class);

	public static Response getRequestRestAssured(String url, Map params, String username, String password) {
        
		try{
		Response response = given().relaxedHTTPSValidation().auth().preemptive().basic(username, password)
				.params(params).when().get(url);
		return response;
		}
		catch(Exception e){
			return null;
		}		

	}
	

	
	public static HttpResponse getRequest(String url) throws Exception {


		HttpClient client =  HttpClients.createDefault();
		HttpGet request = new HttpGet(url);
		request.addHeader("User-Agent", "Mozilla/5.0");
        try{
		HttpResponse response = client.execute(request);
		return response;
        }
        catch(Exception e){
			return null;
		}	

	}

	public static HttpResponse postRequest(String urlToPost, List<BasicNameValuePair> urlParameters) {

		try {
			logger.info("Posting to url:" + urlToPost);
			HttpClient client = HttpClients.createDefault();
			// HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(urlToPost);
			post.setHeader("User-Agent", "Mozilla/5.0");
			HttpResponse response = null;
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			response = client.execute(post);
			post.releaseConnection();
			return response;
		} catch (Exception e) {
			logger.error("Post request failed :" + e.getMessage());
			return null;
		}

		// System.out.println("Response Code : " +
		// response.getStatusLine().getStatusCode());

	}

	public static void postRequestRA() {

		/*
		 * String tradingAccountMessage =
		 * FileUtility.getTradingAccountMessage(); String server_name =
		 * "http://xhkg6002dap.hkg.swissbank.com:10041/Dragon/tmpl/Counterparty/XML/submit";
		 * String xrefHeader = "36,38,90"; String cConsolVersionHeader =
		 * "MF_ACCOUNT:8215583:1"; String tradingAccountCConsol = "8215583";
		 * String APIUrl = Constants.COUNETRPARTY_URL; String APIBody =
		 * "{\"tradingAccountMessage\":" +
		 * "\\" + tradingAccountMessage + "\\" + ",\"xrefHeader\":" + "\\" +
		 * xrefHeader + "\\" + ",\"cConsolVersionHeader\":" +
		 * "\\" + cConsolVersionHeader + "\\" + ",\"tradingAccountCConsol\":" +
		 * "\\" + tradingAccountCConsol + "\\" + "}";
		 * 
		 * 
		 * Counterparty counterparty = new Counterparty();
		 * counterparty.setTradingAccountMessage(tradingAccountMessage);
		 * counterparty.setXrefHeader(xrefHeader);
		 * counterparty.setcConsolVersionHeader(cConsolVersionHeader);
		 * counterparty.setTradingAccountCConsol(tradingAccountCConsol);
		 * 
		 * 
		 * Map<String, Object> map = new HashMap<>();
		 * map.put("tradingAccountMessage", tradingAccountMessage);
		 * map.put("xrefHeader", xrefHeader); map.put("cConsolVersionHeader",
		 * cConsolVersionHeader); map.put("tradingAccountCConsol",
		 * tradingAccountCConsol);
		 * 
		 * Response response = given() // .accept(ContentType.ANY) //
		 * .contentType(ContentType.JSON) .body(map).header("User-Agent",
		 * "Mozilla/5.0") .header("Content-Type",
		 * "application/x-www-form-urlencoded").when().post(APIUrl);
		 * 
		 * System.out.println(response.getStatusCode());
		 * 
		 * String body = response.getBody().toString();
		 * System.out.println(body); System.out.println("done");
		 */

	}

}
