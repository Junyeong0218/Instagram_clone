package apiController.auth_api;

import java.io.IOException;
import java.util.Map;

import entity.NonReadActivities;
import entity.User;
import oauth.OauthAccessTokenController;
import oauth.OauthProfileController;
import service.AuthService;
import service.NewActivityService;

public class OauthController  {
	
	private AuthService authService;
	private NewActivityService newActivityService;
	
	
	public OauthController(AuthService authService, NewActivityService newActivityService) {
		this.authService = authService;
		this.newActivityService = newActivityService;
	}
	
	public User OauthSignin(String provider, String code) throws IOException {
		User user = null;
		if(provider.equals("naver")) {
			System.out.println(code);
			OauthAccessTokenController tokenController = new OauthAccessTokenController();
			String accessToken = tokenController.getTokenByNaver(code);
			System.out.println("accessToken : " + accessToken);
			
			OauthProfileController profileController = new OauthProfileController();
			Map<String,String> userData = profileController.getUserDataByNaver(accessToken);
			
			user = authService.getUserWithOatuh(provider, userData);
			System.out.println("controller user : " + user);
		} else if(provider.equals("kakao")) {
			System.out.println(code);
			OauthAccessTokenController tokenController = new OauthAccessTokenController();
			String accessToken = tokenController.getTokenByKakao(code);
			System.out.println("accessToken : " + accessToken);
			
			OauthProfileController profileController = new OauthProfileController();
			Map<String,String> userData = profileController.getUserDataByKakao(accessToken);
			
			user = authService.getUserWithOatuh(provider, userData);
			System.out.println("controller user : " + user);
		} else if(provider.equals("google")) {
			System.out.println(code);
			OauthAccessTokenController tokenController = new OauthAccessTokenController();
			String accessToken = tokenController.getTokenByGoogle(code);
			System.out.println("accessToken : " + accessToken);
			
			OauthProfileController profileController = new OauthProfileController();
			Map<String,String> userData = profileController.getUserDataByGoogle(accessToken);
			
			user = authService.getUserWithOatuh(provider, userData);
			System.out.println("controller user : " + user);
		}
	
		if(user != null) {
			System.out.println("user is not null : " + user);
			NonReadActivities.setUser(user.getId(), newActivityService.selectNonReadActivities(user.getId()));
		}
		return user;
	}
	
}
