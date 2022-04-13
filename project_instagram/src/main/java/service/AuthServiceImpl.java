package service;

import entity.User;
import repository.UserDao;
import repository.UserDaoImpl;

public class AuthServiceImpl implements AuthService {
	
	private UserDao userDao;
	
	public AuthServiceImpl() {
		userDao = new UserDaoImpl();
	}
	
	@Override
	public int checkUsername(String username) {
		return userDao.checkUsername(username);
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
}
