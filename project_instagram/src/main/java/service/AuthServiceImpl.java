package service;

import entity.User;
import repository.UserDao;
import repository.UserDaoImpl;

public class AuthServiceImpl implements AuthService {
	
	private UserDao userDao;
	
	public AuthServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public int checkUsername(String username) {
		return userDao.checkUsername(username);
	}
	
	@Override
	public int checkOriginPassword(User user) {
		return userDao.checkOriginPassword(user);
	}

	@Override
	public User getUser(String username) {
		return userDao.getUser(username);
	}

	@Override
	public User signin(User user) {
		int result = userDao.signin(user);
		
		if(result == 2) {
			User userDetail = userDao.getUser(user.getUsername());
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
		int result = 0;
		if(user.getFile_name() != null) {
			user.setHas_profile_image(true);
			result += updateUserProfile(user);
		}
		else user.setHas_profile_image(false);

		result += userDao.updateUserinfo(user);
		
		return result;
	}
	
	private int updateUserProfile(User user) {
		return userDao.updateUserProfile(user);
	}
	
	@Override
	public int updatePassword(User user) {
		return userDao.updatePassword(user);
	}
}
