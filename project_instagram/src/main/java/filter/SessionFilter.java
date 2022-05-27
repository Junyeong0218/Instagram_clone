package filter;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

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
		FileUploadPathConfig.setLog_file_path(filterConfig.getServletContext().getRealPath("/logs"));
		System.out.println(FileUploadPathConfig.getFileUploadPath());
		try {
			DBLoggingFilter.readLogFile(FileUploadPathConfig.getLogFilePath() + "/");
		} catch (IOException e) {
			System.out.println("log file creation Exception");
		}
		UserDao userDao = DBLoggingFilter.makeNewProxy(new UserDaoImpl());
		ArticleDao articleDao = DBLoggingFilter.makeNewProxy(new ArticleDaoImpl());
		FollowDao followDao = DBLoggingFilter.makeNewProxy(new FollowDaoImpl());
		StoryDao storyDao = DBLoggingFilter.makeNewProxy(new StoryDaoImpl());
		SearchDao searchDao = DBLoggingFilter.makeNewProxy(new SearchDaoImpl());
		MessageDao messageDao = DBLoggingFilter.makeNewProxy(new MessageDaoImpl());
		NewActivityDao newActivityDao = DBLoggingFilter.makeNewProxy(new NewActivityDaoImpl());
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
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String uri = req.getRequestURI();
		String method = req.getMethod();
		
		if(uri.equals("/")) return;
		if( uri.contains("static") || uri.contains("templates") || uri.contains("favicon") ||
			  (uri.contains("security") && method.equals(RequestMethod.GET)) ||
			  (uri.equals("/index") && method.equals(RequestMethod.GET)) ||
			  (uri.equals("/auth/userinfo") && method.equals(RequestMethod.GET)) ||
			  (uri.contains("signup") && (method.equals(RequestMethod.GET) || method.equals(RequestMethod.POST))) ) {
			chain.doFilter(request, response);
		} else if(uri.contains("/oauth/signin")) {
			System.out.println("요청 날아옴");
			System.out.println(uri);
			if(method.equals(RequestMethod.GET)) {
				String code = request.getParameter("code");
				String state = request.getParameter("state");
				System.out.println(code);
				System.out.println(state);
				response.setContentType("text/html; charset=UTF-8");
				resp.setHeader("Access-Control-Allow-Origin", "*");
				resp.setHeader("Access-Control-Allow-Credentials", "true");
				resp.setHeader("Access-Control-Allow-Methods","*");
				resp.setHeader("Access-Control-Max-Age", "3600");
				resp.setHeader("Access-Control-Allow-Headers",
		                "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization");
				Writer out = response.getWriter();
				out.write("<script type=\"text/javascript\" src=\"http://code.jquery.com/jquery-latest.min.js\"></script>"
								+ "<script>");
				out.write("$.ajax({"
						+ "	type: \"get\","
						+ "	url: \"https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=1neVQwuiHwavJykhB63G&client_secret=KiOB2IEJUY&code=" + code + "&state=" + state + "\","
						+ "	headers: { \"origin\": \"http://localhost:8080\" },"
						+ "	dataType: \"json\","
						+ "	success: function (data) {"
						+ "		$.ajax(  { type:\"post\", url:\"/oauth/signin\", data: data, dataType:\"text\", success: function (data) {}, error: function (xhr, status) { console.log(xhr); console.log(status); } }    );"
						+ "	},"
						+ "	error: function (xhr, stauts) {"
						+ "		console.log(xhr);"
						+ "		console.log(status);"
						+ "	}"
						+ "});");
				out.write("</script>");
			} else if(method.equals(RequestMethod.POST)) {
				Iterator<String> paramNames = request.getParameterNames().asIterator();
				while(paramNames.hasNext()) {
					String paramName = paramNames.next();
					String value = request.getParameter(paramName);
					System.out.println(paramName + " = " + value);
				}
			}
//			String requestBody = new String(request.getInputStream().readAllBytes(), "UTF-8");
//			System.out.println(requestBody);
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
		} else if((uri.equals("/direct/message") && method.equals(RequestMethod.GET)) ||
						  uri.equals("/main") && method.equals(RequestMethod.GET) ||
						  uri.startsWith("/profile") && method.equals(RequestMethod.GET) ||
						  uri.contains("/search") && method.equals(RequestMethod.GET) ||
						  uri.contains("/logout") && method.equals(RequestMethod.GET) ||
						  uri.contains("/userinfo") && method.equals(RequestMethod.GET)) {
			// location.href 함수로 이동된 페이지들의 sessionId 로 UUID -> token 검색 및 존재하는 경우 dofilter 아니면 session.invalidate() -> sendError -> alert -> location.replace(/index) 
			String user_secret_key = (String) req.getSession().getAttribute("user_secret_key");
			if(security.isLoginedSession(user_secret_key)) {
				String token = security.getToken(user_secret_key);
				User sessionUser = security.certificateUser(token, user_secret_key);
				request.setAttribute("sessionUser", sessionUser);
				chain.doFilter(request, response);
			} else {
				resp.sendRedirect("/index");
			}
		} else {
			// 토큰으로 유저 유효성 검사 및 response
			if(!uri.contains("security") && !uri.contains("principal") && !uri.contains("alert")) {
				System.out.println("------------------------------------");
				System.out.println("request URI : " + uri + " - " + method);
			}
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
