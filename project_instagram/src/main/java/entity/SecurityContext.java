package entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class SecurityContext {

	private static SecurityContext instance;
	private static Principal principal;
	private static Map<String, String> user_role;
	
	private SecurityContext() {
		principal = Principal.createPrincipal();
		user_role = new HashMap<String, String>(); 
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
		User user = principal.getUser(user_id);

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
	
	public static String issueToken(User user) {
		String token =  JWT.create()
											  .withSubject("authUser")
											  .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
											  .withClaim("id", user.getId())
											  .withClaim("username", user.getUsername())
											  .sign(Algorithm.HMAC512(JwtProperties.SECRET));
		principal.registerUser(user);
		user_role.put(token, user.getRole());
		
		return token;
	}
}
