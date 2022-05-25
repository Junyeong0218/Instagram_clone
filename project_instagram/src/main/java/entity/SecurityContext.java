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

public class SecurityContext {

	private static SecurityContext instance;
	private static Map<String, String> user_role;
	private static Map<String, String> sessionTokens;
	
	private SecurityContext() {
		user_role = new HashMap<String, String>();
		sessionTokens = new HashMap<String, String>();
	}
	
	public static void createInstance() {
		if(instance == null) {
			instance = new SecurityContext();
		}
	}
	
	private static DecodedJWT verifyToken(String jwtToken) {
		try {
			DecodedJWT decoded = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken);
			return decoded;
		} catch (SignatureVerificationException e) {
			System.out.println(e.getClass().getName());
			return null;
		} catch (TokenExpiredException e) {
			System.out.println(e.getClass().getName());
			return null;
		} catch (InvalidClaimException e) {
			System.out.println(e.getClass().getName());
			return null;
		} catch (JWTVerificationException e) {
			System.out.println(e.getClass().getName());
			return null;
		}
	}
	
	public static User certificateUser(String jwtToken) {
		DecodedJWT decoded = verifyToken(jwtToken);
		int user_id = decoded.getClaim("id").asInt();
		String username = decoded.getClaim("username").asString();
		String name = decoded.getClaim("name").asString();
		User user = User.builder().id(user_id)
														.username(username)
														.name(name)
														.build();
		return user;
	}
	
	public static String getUserRole(String jwtToken) {
		DecodedJWT decoded = verifyToken(jwtToken);
		if(decoded != null) {
			String role = user_role.get(jwtToken);
			
			return role;
		} else {
			return null;
		}
	}
	
	public static String issueToken(User user, String uuid) {
		String token =  JWT.create()
											  .withSubject("authUser")
											  .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
											  .withClaim("id", user.getId())
											  .withClaim("username", user.getUsername())
											  .withClaim("name", user.getName())
											  .sign(Algorithm.HMAC512(JwtProperties.SECRET));
		user_role.put(token, user.getRole());
		sessionTokens.put(uuid, token);
		
		return token;
	}
	
	public static String reIssueToken(User user, String uuid) {
		String token =  JWT.create()
											  .withSubject("authUser")
											  .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
											  .withClaim("id", user.getId())
											  .withClaim("username", user.getUsername())
											  .withClaim("name", user.getName())
											  .sign(Algorithm.HMAC512(JwtProperties.SECRET));
		String prevToken = sessionTokens.put(uuid, token);
		user_role.remove(prevToken);
		user_role.put(token, user.getRole());
		
		return token;
	}
	
	public static String generateUUID() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
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
				uuid = UUID.randomUUID().toString().replaceAll("-", "");
				isExist = false;
			} else {
				break;
			}
		}
		return uuid;
	}
	
	public static boolean isLoginedSession(String uuid) {
		String token = sessionTokens.get(uuid);
		if(token == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public static String getToken(String uuid) {
		return sessionTokens.get(uuid);
	}
	
	public static void invalidateUser(User user, String uuid) {
		String token = sessionTokens.remove(uuid);
		user_role.remove(token);
	}
}
