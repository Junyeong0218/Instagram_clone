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
import javax.servlet.http.HttpSession;

import entity.User;

@WebFilter("/profile/*")
public class ProfileControllerParamFilter implements Filter {
	
	private final String PROFILE = "/profile";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		String uri = req.getRequestURI();
		System.out.println("uri : " + uri);
		
		if(uri.contains(PROFILE)) {
			chain.doFilter(request, response);
			return;
		}
		
		uri = uri.replace("/profile", "");
		String[] uris = uri.split("/");
		
		String method = req.getMethod();
		System.out.println("method : " + method);
		
		if(uris.length > 1) {
			HttpSession session = req.getSession();
			User sessionUser = (User) session.getAttribute("user");
			String username = uris[1];
			request.setAttribute("username", username);
			request.setAttribute("sessionUserId", sessionUser.getId());
			request.getRequestDispatcher(PROFILE);
		} else {
			resp.sendError(409, "wrong uri");
		}
	}
}
