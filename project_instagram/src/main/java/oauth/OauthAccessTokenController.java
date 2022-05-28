package oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
}
