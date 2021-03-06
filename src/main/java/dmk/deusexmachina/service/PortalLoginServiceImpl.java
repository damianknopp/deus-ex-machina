package dmk.deusexmachina.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortalLoginServiceImpl implements PortalLoginService{
	Logger logger = LoggerFactory.getLogger(PortalLoginServiceImpl.class);
	
	protected String host = "portal.nextcentury.com/";
	protected String homePage = "portal/";
	protected String checkInPage = "portal/checkin/checkin_ajax.php";
	protected String user;
	protected String pass;
	
	public List<String> userNameToLogin(final String[] userNames){
		List<String> logins = new ArrayList<>();
		for(final String user: userNames){
			if("Damian Knopp".equals(user)){
				logins.add("dknopp");
			}
		}
		return logins;
	}
	
	public String[] checkUsersStatus(String html, final String[] users){
		//TODO: pull in jsoup and parse the html
		final List<String> badStatus = new ArrayList<>();
		
		int startIndex = html.indexOf("id=\'usernameSelect\'");
		int endIndex = html.indexOf("</select", startIndex);
		final String scopedHtml = html.substring(startIndex, endIndex);

		Arrays.stream(users).forEach( user -> {
			logger.trace("log in state for " + user);
			boolean needsCheckin = scopedHtml.contains("x " + user);
			if(needsCheckin){
				logger.trace(user + " needs checked in");
				badStatus.add(user);
			}
		});
		return badStatus.toArray(new String[0]);
	}
	
	public String loginHomePage() {
		CloseableHttpClient httpClient = this.createClient();
		CloseableHttpResponse response = null;
		String respData = null;
		try {
			HttpGet httpget = new HttpGet("https://" + this.host
					+ this.homePage);
			logger.trace("Executing request " + httpget.getRequestLine());
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			respData = IOUtils.toString(entity.getContent());
			EntityUtils.consume(entity);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(response);
			IOUtils.closeQuietly(httpClient);
		}
		return respData;
	}

	public boolean checkIn(final String userName) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		final String tmp = "<span class='blue small'>Checked-in at ";
		try {
			httpClient = this.createClient();
			HttpPost httpPost = new HttpPost("https://" + this.host + this.checkInPage);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("function", "checkin"));
			nvps.add(new BasicNameValuePair("left_displayname", userName));
			nvps.add(new BasicNameValuePair("left_inout", "none"));
			nvps.add(new BasicNameValuePair("left_notes", ""));
			nvps.add(new BasicNameValuePair("username", userName));

			httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			final String respData = IOUtils.toString(entity.getContent());
			EntityUtils.consume(entity);
			final boolean success = respData.contains(tmp);
			return success;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(response);
			IOUtils.closeQuietly(httpClient);
		}
	}

	protected CloseableHttpClient createClient() {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(this.user, this.pass));
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultCredentialsProvider(credsProvider).build();
		return httpClient;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

}