package filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.RequestMethod;
import entity.JwtProperties;
import entity.SecurityContext;
import entity.User;

@WebFilter("/*")
public class SessionFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
		SecurityContext.createInstance();
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String uri = req.getRequestURI();
		String method = req.getMethod();
		
		System.out.println("------------------------------------");
//		Iterator<String> iterator = req.getHeaderNames().asIterator();
//		while(iterator.hasNext()) {
//			String name = iterator.next();
//			System.out.println(name + " : " + req.getHeader(name));
//		}
		
		System.out.println("sessionFilter : ");
		System.out.println("uri : " + uri);
		System.out.println("method : " + method);
		
		if(uri.equals("/")) return;
		if( uri.contains("static") || uri.contains("templates") || uri.contains("security") ||
			  (uri.contains("userinfo") && method.equals(RequestMethod.GET)) ||
			  (uri.equals("/index") && method.equals(RequestMethod.GET)) ||
			  (uri.contains("signup") && (method.equals(RequestMethod.GET) || method.equals(RequestMethod.POST))) ) {
			chain.doFilter(request, response);
		} else if(uri.contains("signin")) {
			// 유저 인증 및 토큰 발급
			chain.doFilter(request, response);
			User user = (User) request.getAttribute("user");
			if(user == null) {
				resp.sendError(400, "wrong username or password");
			} else {
				String uuid = SecurityContext.generateUUID();
				req.getSession().setAttribute("UUID", uuid);
				String token = SecurityContext.issueToken(user, uuid);
				System.out.println("issueToken : " + token);
				resp.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
			}
		} else if(uri.contains("/verify/token")) {
			// location.href 함수로 이동된 페이지들의 sessionId 로 UUID -> token 검색 및 존재하는 경우 dofilter 아니면 session.invalidate() -> sendError -> alert -> location.replace(/index) 
			String uuid = (String) req.getSession().getAttribute("UUID");
			if(SecurityContext.isLoginedSession(uuid)) {
				chain.doFilter(request, response);
			} else {
				resp.sendRedirect("/index");
			}
		} else {
			// 토큰으로 유저 유효성 검사 및 response
			String token = req.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
			System.out.println("verifyToken : " + token);
			resp.setContentType("text/plain; charset=UTF-8");
			if(SecurityContext.certificateUser(token) == null) {
				resp.getWriter().print(false);
			} else {
				resp.getWriter().print(true);
				return;
			}
			
//			String token = (String) req.getHeader(JwtProperties.HEADER_STRING);
//			System.out.println("token : " + token);
//			if(token != null && ! token.equals("")) {
//				System.out.println("/main uri : " + req.getRequestURI());
//				resp.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + request.getAttribute("token"));
//				chain.doFilter(request, response);
//			} else {
//				System.out.println("asdfasdfasdf");
//				resp.sendRedirect("/index");
//			}
		}
		
//		System.out.println(uri);
//		if(uris.length > 1 && !uris[1].equals("index") && !uris[1].equals("signup") && !uri.contains("signin") && !uris[1].equals("static")
//				 && !uris[1].contains("check") && !uris[1].equals("templates")) {
//			User user = (User) req.getSession().getAttribute("user");
//			if(user == null) {
//				resp.sendRedirect("/index");
//				return;
//			}
//		}
//		chain.doFilter(request, response);
	}
}
