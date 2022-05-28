package oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class OauthProfileController {

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
	
	public Map<String, String> getUserDataByNaver(String accessToken) throws IOException {
		String naverTokenUrl = OauthProperties.NAVER_USERINFO_URL;
		System.out.println(naverTokenUrl);
		if(setUrl(naverTokenUrl)) {
			connection.setRequestProperty("Authorization", "Bearer " + accessToken);
			int responseCode = connection.getResponseCode();
			if(responseCode == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line = br.readLine();
				line = line.substring(line.indexOf("response") + 10, line.length()).replaceAll("[\\{\\}]", "");
				System.out.println(line);
				String[] claims = line.split(",");
				Map<String, String> userData = new HashMap<String, String>();
				for(String claim : claims) {
					String key = claim.substring(0, claim.indexOf(":")).replace("\"", "");
					String value = claim.substring(claim.indexOf(":") + 1, claim.length()).replace("\"", "");
					System.out.println(key + " : " + value);
					userData.put(key, value);
				}
				br.close();
				connection.disconnect();
				return userData;
			}
		}
		return null;
	}
	
	public Map<String, String> getUserDataByKakao(String accessToken) throws IOException {
		String kakaoTokenUrl = OauthProperties.KAKAO_USERINFO_URL;
		System.out.println(kakaoTokenUrl);
		if(setUrl(kakaoTokenUrl)) {
			connection.setRequestProperty("Authorization", "Bearer " + accessToken);
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			int responseCode = connection.getResponseCode();
			System.out.println(responseCode);
			if(responseCode == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line = br.readLine();
				System.out.println(line);
				Map<String, String> userData = new HashMap<String, String>();
				String idClaim = line.substring(1, line.indexOf(","));
				userData.put("id", idClaim.split(":")[1].replaceAll("\"", ""));
				System.out.println("id : " + idClaim.split(":")[1].replaceAll("\"", ""));
				
				line = line.substring(line.indexOf("\"profile\"") + 10, line.length()).replaceAll("[\\{\\}]", "");
				System.out.println(line);
				String[] claims = line.split(",");
				for(String claim : claims) {
					String key = claim.substring(0, claim.indexOf(":")).replace("\"", "");
					String value = claim.substring(claim.indexOf(":") + 1, claim.length()).replace("\"", "");
					System.out.println(key + " : " + value);
					userData.put(key, value);
				}
				br.close();
				connection.disconnect();
				return userData;
			}
		}
		return null;
	}
}
