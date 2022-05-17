package filter.controller_dispatcher;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.RequestMethod;

@WebFilter("/auth/*")
public class AuthControllerMethodFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		System.out.println("authfilter : ");
		String uri = req.getRequestURI();
		System.out.println("uri : " + uri);
		
		String method = req.getMethod();
		System.out.println("method : " + method);
		
		if(uri.equals("/auth/signin") && method.equals(RequestMethod.POST)) {
			chain.doFilter(request, response);
		} else if(uri.equals("/auth/logout") && method.equals(RequestMethod.GET)) {
			chain.doFilter(request, response);
		} else if(uri.equals("/auth/password") && (method.equals(RequestMethod.GET) || method.equals(RequestMethod.PUT))) {
			chain.doFilter(request, response);
		} else if(uri.equals("/auth/userinfo") && (method.equals(RequestMethod.GET) || method.equals(RequestMethod.PUT))) {
			chain.doFilter(request, response);
		} else {
			resp.sendError(404, "invalid method");
		}
	}
}
