package oauth;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import config.RequestMethod;

public class OauthAccessTokenController {

	private URL url;
	private HttpURLConnection connection;
	
	private boolean setUrl(String url) {
		try {
			this.url = new URL(url);
			connection = (HttpURLConnection) this.url.openConnection();
			return true;
		} catch (MalformedURLException e) {
			System.out.println(e.getClass());
			return false;
		} catch (IOException e) {
			System.out.println(e.getClass());
			return false;
		}
	}
	
	public String getTokenByNaver(String code) throws IOException {
		String naverTokenUrl = OauthProperties.NAVER_TOKEN_URL
														+ "?grant_type=authorization_code"
														+ "&client_id=" + OauthProperties.NAVER_CLIENT_ID
														+ "&client_secret=" + OauthProperties.NAVER_CLIENT_SECRET
														+ "&code=" + code
														+ "&state=" + OauthProperties.NAVER_STATE;
		System.out.println(naverTokenUrl);
		if(setUrl(naverTokenUrl)) {
			int responseCode = connection.getResponseCode();
			if(responseCode == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line = br.readLine();
				line = line.replaceAll("[\\{\\}]", "");
				String[] claims = line.split(",");
				for(String claim : claims) {
					String key = claim.split(":")[0].replace("\"", "");
					if(key.equals("access_token")) {
						return claim.split(":")[1].replace("\"", "");
					}
				}
				br.close();
				connection.disconnect();
			}
		}
		return null;
	}
	
	public String getTokenByKakao(String code) throws IOException {
		String kakaoTokenUrl = OauthProperties.KAKAO_TOKEN_URL;
		StringBuilder sb = new StringBuilder();
		sb.append("grant_type=authorization_code&" + 
								"client_id=" + OauthProperties.KAKAO_CLIENT_ID + "&" +
								"redirect_uri=" + URLEncoder.encode(OauthProperties.KAKAO_REDIRECT_URI, "UTF-8") + "&" +
								"code=" + code + "&" +
								"client_secret=" + OauthProperties.KAKAO_CLIENT_SECRET );
		System.out.println(kakaoTokenUrl);
		System.out.println(sb.toString());
		if(setUrl(kakaoTokenUrl)) {
			connection.setRequestMethod(RequestMethod.POST);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			DataOutputStream bw = new DataOutputStream(connection.getOutputStream());
			bw.writeBytes(sb.toString());
			bw.flush();
			bw.close();
			
			int responseCode = connection.getResponseCode();
			if(responseCode == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line = br.readLine();
				System.out.println(line);
				line = line.replaceAll("[\\{\\}]", "");
				String[] claims = line.split(",");
				for(String claim : claims) {
					String key = claim.split(":")[0].replace("\"", "");
					if(key.equals("access_token")) {
						return claim.split(":")[1].replace("\"", "");
					}
				}
				br.close();
				connection.disconnect();
			}
		}
		return null;
	}
	
	public String getTokenByGoogle(String code) throws IOException {
		String googleTokenUrl = OauthProperties.GOOGLE_TOKEN_URL;
		StringBuilder sb = new StringBuilder();
		sb.append("grant_type=authorization_code&" + 
								"client_id=" + OauthProperties.GOOGLE_CLIENT_ID + "&" +
								"redirect_uri=" + URLEncoder.encode(OauthProperties.GOOGLE_REDIRECT_URI, "UTF-8") + "&" +
								"code=" + code + "&" +
								"client_secret=" + OauthProperties.GOOGLE_CLIENT_SECRET );
		System.out.println(googleTokenUrl);
		System.out.println(sb.toString());
		if(setUrl(googleTokenUrl)) {
			connection.setRequestMethod(RequestMethod.POST);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			DataOutputStream bw = new DataOutputStream(connection.getOutputStream());
			bw.writeBytes(sb.toString());
			bw.flush();
			bw.close();
			
			int responseCode = connection.getResponseCode();
			System.out.println(responseCode);
			if(responseCode == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				br.readLine();
				String line = br.readLine().replace(",", "").trim();
				System.out.println(line);
				String key = line.split(":")[0].replace("\"", "");
				if(key.equals("access_token")) {
					return line.split(":")[1].replace("\"", "");
				}
				br.close();
				connection.disconnect();
			}
		}
		return null;
	}
}
