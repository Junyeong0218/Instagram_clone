package entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import exception.JWTRegisterException;
import repository.UserDao;
import service.AuthService;
import service.AuthServiceImpl;

public class SecurityContext {

	private static SecurityContext instance;
	private Map<String, String> user_role;
	private Map<String, String> sessionTokens;
	private AuthService authService;
	
	private SecurityContext(UserDao userDao) {
		user_role = new HashMap<String, String>();
		sessionTokens = new HashMap<String, String>();
		authService = new AuthServiceImpl(userDao);
	}
	
	public static SecurityContext createInstance(UserDao userDao) {
		if(instance == null) {
			instance = new SecurityContext(userDao);
		}
		return instance;
	}
	
	public static SecurityContext getInstance() {
		return instance;
	}
	
	private DecodedJWT verifyToken(String jwtToken, String user_secret_key) {
		try {
			DecodedJWT decoded = JWT.require(Algorithm.HMAC512(user_secret_key)).build().verify(jwtToken);
			return decoded;
		} catch (SignatureVerificationException e) {
			System.out.println(e.getClass().getName());
			return null;
		} catch (TokenExpiredException e) {
			System.out.println(e.getClass().getName());
			System.out.println("토큰 재발급 시작");
			User user = authService.selectTokenInfo(jwtToken);
			System.out.println("selected User with prev Token : " + user);
			String newToken = reIssueToken(user, user.getSecret_key());
			System.out.println("newToken : " + newToken);
			DecodedJWT result = JWT.require(Algorithm.HMAC512(user_secret_key)).build().verify(newToken);
			if(result != null) {
				authService.updateJwtToken(user.getId(), newToken);
				return result;
			} else {
				System.out.println("토큰 검증 실패!");
				return null;
			}
		} catch (InvalidClaimException e) {
			System.out.println(e.getClass().getName());
			return null;
		} catch (JWTVerificationException e) {
			System.out.println(e.getClass().getName());
			return null;
		}
	}
	
	public User certificateUser(String jwtToken, String user_secret_key) {
		DecodedJWT decoded = verifyToken(jwtToken, user_secret_key);
		int user_id = decoded.getClaim("id").asInt();
		String username = decoded.getClaim("username").asString();
		String name = decoded.getClaim("name").asString();
		User user = User.builder().id(user_id)
														.username(username)
														.name(name)
														.build();
		return user;
	}
	
	public String getUserRole(String jwtToken, String user_secret_key) {
		DecodedJWT decoded = verifyToken(jwtToken, user_secret_key);
		if(decoded != null) {
			String role = user_role.get(jwtToken);
			
			return role;
		} else {
			return null;
		}
	}
	
	public String issueToken(User user, String user_secret_key) {
		String token =  JWT.create()
											  .withSubject("authUser")
											  .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
											  .withClaim("id", user.getId())
											  .withClaim("username", user.getUsername())
											  .withClaim("name", user.getName())
											  .sign(Algorithm.HMAC512(user_secret_key));
		user_role.put(token, user.getRole());
		sessionTokens.put(user_secret_key, token);
		boolean result = authService.registerJwtToken(user.getId(), token);
		System.out.println("update token result = " + result);
		if(! result) {
			throw new JWTRegisterException("jwt token register error ::: via signin");
		}
		
		return token;
	}
	
	public String reIssueToken(User user, String user_secret_key) {
		String token =  JWT.create()
											  .withSubject("authUser")
											  .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
											  .withClaim("id", user.getId())
											  .withClaim("username", user.getUsername())
											  .withClaim("name", user.getName())
											  .sign(Algorithm.HMAC512(user_secret_key));
		String prevToken = sessionTokens.put(user_secret_key, token);
		user_role.remove(prevToken);
		user_role.put(token, user.getRole());
		
		return token;
	}
	
	public String generateUUID() {
		String uuid = UUID.randomUUID().toString();
		boolean isExist = false;
		while(! isExist) {
			Iterator<String> uuids = sessionTokens.keySet().iterator();
			while(uuids.hasNext()) {
				String value = uuids.next();
				if(value.equals(uuid)) {
					isExist = true;
					break;
				} else {
					isExist = false;
				}
			}
			if(isExist) {
				uuid = UUID.randomUUID().toString();
				isExist = false;
			} else {
				break;
			}
		}
		return uuid;
	}
	
	public boolean isLoginedSession(String user_secret_key) {
		String token = getToken(user_secret_key);
		if(token == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public String getToken(String user_secret_key) {
		return sessionTokens.get(user_secret_key);
	}
	
	public void invalidateUser(User user, String user_secret_key) {
		String token = sessionTokens.remove(user_secret_key);
		user_role.remove(token);
	}
}
