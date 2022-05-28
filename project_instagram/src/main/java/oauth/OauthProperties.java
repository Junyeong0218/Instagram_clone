package oauth;

public class OauthProperties {

	public static final String NAVER_TOKEN_URL = "https://nid.naver.com/oauth2.0/token";
	public static final String NAVER_USERINFO_URL = "https://openapi.naver.com/v1/nid/me";
	public static final String NAVER_STATE = "NVAER_OAUTH_LOGIN";
	public static final String NAVER_CLIENT_ID = "1neVQwuiHwavJykhB63G";
	public static final String NAVER_CLIENT_SECRET = "KiOB2IEJUY";
	
	public static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
	public static final String KAKAO_USERINFO_URL = "https://kapi.kakao.com/v2/user/me";
	public static final String KAKAO_STATE = "KAKAO_OAUTH_LOGIN";
	public static final String KAKAO_CLIENT_ID = "0b80930613775d7766608926a629a6b1";
	public static final String KAKAO_CLIENT_SECRET = "3dmSccARbmcwuPnK3ox2ABF0onPGhAQH";
	public static final String KAKAO_REDIRECT_URI = "http://localhost:8080/oauth/signin/kakao";
	
	public static final String GOOGLE_TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
	public static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo"; // ?access_token=
	public static final String GOOGLE_STATE = "GOOGLE_OAUTH_LOGIN";
	public static final String GOOGLE_CLIENT_ID = "269750796517-dnobegf7dhkb93hpker91tpi1mmg9rpo.apps.googleusercontent.com";
	public static final String GOOGLE_CLIENT_SECRET = "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";
	public static final String GOOGLE_REDIRECT_URI = "http://localhost:8080/oauth/signin/google";
	
}
