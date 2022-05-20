package entity;

public class JwtProperties {

	public static final String SECRET = "cos";
	public static final int EXPIRATION_TIME = 1000 * 60 * 10;
	public static final String TOKEN_PREFIX = "Bearer.";
	public static final String HEADER_STRING = "Authorization";
}
