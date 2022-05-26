package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.FileUploadPathConfig;
import config.RequestMethod;
import entity.JwtProperties;
import entity.NonReadActivities;
import entity.SecurityContext;
import entity.User;
import exception.JWTRegisterException;
import repository.ArticleDao;
import repository.ArticleDaoImpl;
import repository.FollowDao;
import repository.FollowDaoImpl;
import repository.MessageDao;
import repository.MessageDaoImpl;
import repository.NewActivityDao;
import repository.NewActivityDaoImpl;
import repository.SearchDao;
import repository.SearchDaoImpl;
import repository.StoryDao;
import repository.StoryDaoImpl;
import repository.UserDao;
import repository.UserDaoImpl;

public class SessionFilter implements Filter {
	
	private SecurityContext security;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("session filter initialize!");
		ServletContext servletContext = filterConfig.getServletContext();
		FileUploadPathConfig.setFile_upload_path(filterConfig.getServletContext().getRealPath("/static/file_upload"));
		System.out.println(FileUploadPathConfig.getFileUploadPath());
		UserDao userDao = new UserDaoImpl();
		ArticleDao articleDao = new ArticleDaoImpl();
		FollowDao followDao = new FollowDaoImpl();
		StoryDao storyDao = new StoryDaoImpl();
		SearchDao searchDao = new SearchDaoImpl();
		MessageDao messageDao = new MessageDaoImpl();
		NewActivityDao newActivityDao = new NewActivityDaoImpl();
		NonReadActivities.createInstance();
		servletContext.setAttribute("userDao", userDao);
		servletContext.setAttribute("articleDao", articleDao);
		servletContext.setAttribute("followDao", followDao);
		servletContext.setAttribute("storyDao", storyDao);
		servletContext.setAttribute("searchDao", searchDao);
		servletContext.setAttribute("messageDao", messageDao);
		servletContext.setAttribute("newActivityDao", newActivityDao);
		security = SecurityContext.createInstance(userDao);
		System.out.println("session filter initialize finish! ----------------------------");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("SessionFilter executed!");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String uri = req.getRequestURI();
		String method = req.getMethod();
		
		System.out.println("uri : " + uri);
		System.out.println("method : " + method);
		
		if(uri.equals("/")) return;
		if( uri.contains("static") || uri.contains("templates") || uri.contains("favicon") ||
			  (uri.contains("security") && method.equals(RequestMethod.GET)) ||
			  (uri.equals("/index") && method.equals(RequestMethod.GET)) ||
			  (uri.equals("/auth/logout") && method.equals(RequestMethod.GET)) ||
			  (uri.equals("/auth/userinfo") && method.equals(RequestMethod.GET)) ||
			  (uri.contains("signup") && (method.equals(RequestMethod.GET) || method.equals(RequestMethod.POST))) ) {
			chain.doFilter(request, response);
		} else if(uri.contains("signin")) {
			// 유저 인증 및 토큰 발급
			chain.doFilter(request, response);
			User user = (User) request.getAttribute("user");
			if(user == null) {
				resp.sendError(400, "wrong username or password");
			} else {
				req.getSession().setAttribute("user_secret_key", user.getSecret_key());
				resp.setContentType("text/plain; charset=UTF-8");
				try {
					String token = security.issueToken(user, user.getSecret_key());
					System.out.println("issueToken : " + token);
					System.out.println("userSecretKey : " + user.getSecret_key());
					resp.getWriter().print(true);
				} catch (JWTRegisterException e) {
					resp.getWriter().print(false);
				}
			}
		} else if((uri.contains("/direct/message") && method.equals(RequestMethod.GET)) ||
						  uri.contains("/main") && method.equals(RequestMethod.GET) ||
						  uri.contains("/profile") && method.equals(RequestMethod.GET) ||
						  uri.contains("/search") && method.equals(RequestMethod.GET) ||
						  uri.contains("/userinfo") && method.equals(RequestMethod.GET)) {
			// location.href 함수로 이동된 페이지들의 sessionId 로 UUID -> token 검색 및 존재하는 경우 dofilter 아니면 session.invalidate() -> sendError -> alert -> location.replace(/index) 
			String user_secret_key = (String) req.getSession().getAttribute("user_secret_key");
			if(security.isLoginedSession(user_secret_key)) {
				chain.doFilter(request, response);
			} else {
				resp.sendRedirect("/index");
			}
		} else {
			// 토큰으로 유저 유효성 검사 및 response
			String token = req.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
			String user_secret_key = (String) req.getSession().getAttribute("user_secret_key");
			System.out.println("verifyToken : " + token);
			User sessionUser = security.certificateUser(token, user_secret_key);
			if(sessionUser != null) {
				request.setAttribute("sessionUser", sessionUser);
				chain.doFilter(request, response);
			} else {
				resp.setContentType("text/plain; charset=UTF-8");
				resp.sendError(420, "requested User has no Authorization!");
			}
		}
	}
}
