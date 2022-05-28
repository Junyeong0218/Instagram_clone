package service;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import config.FileUploadPathConfig;
import entity.User;
import repository.UserDao;
import request_dto.CheckInputReqDto;

public class AuthServiceImpl implements AuthService {
	
	private UserDao userDao;
	
	public AuthServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public int checkInput(CheckInputReqDto checkInputReqDto) {
		if(checkInputReqDto.getEmail() != null) return userDao.checkEmail(checkInputReqDto.getEmail());
		else if(checkInputReqDto.getPhone() != null) return userDao.checkEmail(checkInputReqDto.getPhone());
		else if(checkInputReqDto.getUsername() != null) return userDao.checkEmail(checkInputReqDto.getUsername());
		else return 0;
	}
	
	@Override
	public boolean checkOriginPassword(User user) {
		String originPassword = userDao.selectOriginPassword(user.getId());
		if(BCrypt.checkpw(user.getPassword(), originPassword)) {
			return true;
		}
		return false;
	}
	
	@Override
	public int selectOauthUsernameCount(String oauth_username) {
		return userDao.selectOauthUserId(oauth_username);
	}
	
	@Override
	public int selectIdByOauthEmail(String oauth_email) {
		return userDao.selectIdByOauthEmail(oauth_email);
	}
	
	@Override
	public User getUserByOauthUsername(String oauth_username) {
		return userDao.getUserByOauthUsername(oauth_username);
	}

	@Override
	public User getUserWithOatuh(String provider, Map<String, String> userData) {
		User user = null;
		String oauth_username = provider + "_" + userData.get("id");
		int user_id = selectOauthUsernameCount(oauth_username);
		if(user_id == 0) {
			// select duplicate email
			user_id = selectIdByOauthEmail(userData.get("email"));
			if(user_id == 0) {
				// insert userData
				int result = userDao.oauthSignup(provider, userData);
				if(result > 0) {
					user = getUserByOauthUsername(oauth_username);
				}
			} else {
				// update oauth_username && provider
				int result = userDao.updateUserConnectOauth(user_id, oauth_username, provider);
				System.out.println(result);
				if(result > 0) {
					user = getUserByOauthUsername(oauth_username);
				}
			}
		} else {
			// select via oauth_userinfo
			user = getUserByOauthUsername(oauth_username);
		}
		System.out.println("final user : " + user);
		return user;
	}
	
	@Override
	public User getUserByUsername(String username) {
		User user = userDao.getUserByUsername(username);
		if(user.getFile_name() != null) {
			user.setFile_name(FileUploadPathConfig.getProfileImagePath(user.getFile_name()));
		}
		return user;
	}
	
	@Override
	public User getUserById(int user_id) {
		User user = userDao.getUserById(user_id);
		if(user.getFile_name() != null) {
			user.setFile_name(FileUploadPathConfig.getProfileImagePath(user.getFile_name()));
		}
		return user;
	}

	@Override
	public User signin(User user) {
		String db_password = userDao.selectPassword(user.getUsername());
		if(BCrypt.checkpw(user.getPassword(), db_password)) {
			User userDetail = userDao.getUserByUsername(user.getUsername());
			return userDetail;
		}
		return null;
	}
	
	@Override
	public int signup(User user) {
		return userDao.signup(user);
	}
	
	@Override
	public int updateUserinfo(User sessionUser, User user) {
		user.setId(sessionUser.getId());
		int result = 0;
		if(user.getFile_name() != null) {
			user.setHas_profile_image(true);
			result += updateUserProfile(user);
		}
		else user.setHas_profile_image(false);

		try {
			result += userDao.updateUserinfo(sessionUser, user);
		} catch (IllegalAccessException e) {
			System.out.println(e.getClass().getName());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getClass().getName());
		} catch (InvocationTargetException e) {
			System.out.println(e.getClass().getName());
		}
		
		return result;
	}
	
	private int updateUserProfile(User user) {
		return userDao.updateUserProfile(user);
	}
	
	@Override
	public int updatePassword(User user) {
		return userDao.updatePassword(user);
	}
	
	@Override
	public boolean registerJwtToken(int user_id, String jwt) {
		return userDao.registerJwtToken(user_id, jwt) > 0;
	}
	
	@Override
	public boolean updateJwtToken(int user_id, String jwt) {
		return userDao.updateJwtToken(user_id, jwt) > 0;
	}
	
	@Override
	public User selectTokenInfo(String jwt) {
		return userDao.selectTokenInfo(jwt);
	}
}
