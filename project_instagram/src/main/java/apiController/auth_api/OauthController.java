package apiController.auth_api;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.NonReadActivities;
import entity.User;
import oauth.OauthAccessTokenController;
import oauth.OauthProfileController;
import repository.NewActivityDao;
import repository.UserDao;
import request_dto.CheckInputReqDto;
import service.AuthService;
import service.AuthServiceImpl;
import service.NewActivityService;
import service.NewActivityServiceImpl;

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
			
		} else if(provider.equals("google")) {
			
		}
	
		if(user != null) {
			System.out.println("user is not null : " + user);
			NonReadActivities.setUser(user.getId(), newActivityService.selectNonReadActivities(user.getId()));
		}
		return user;
	}
	
}
