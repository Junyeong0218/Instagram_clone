package filter.controller_dispatcher;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.RequestMethod;

public class ProfileControllersParamFilter implements Filter {
	
	private final String PROFILE = "/profile";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String uri = req.getRequestURI();
		String method = req.getMethod();
		
		if(uri.equals(PROFILE)) {
			chain.doFilter(request, response);
			return;
		}
		
		uri = uri.replace("/profile", "");
		String[] uris = uri.split("/");
		
		if(uris.length > 1 && method.equals(RequestMethod.GET)) {
			String username = uris[1];
			request.setAttribute("username", username);
			req.getRequestDispatcher(PROFILE).forward(request, response);
		} else {
			resp.sendError(409, "wrong uri");
		}
	}
}
