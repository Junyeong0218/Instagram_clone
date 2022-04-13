package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.User;

@WebFilter("/*")
public class SessionFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String uri = req.getRequestURI();
		String[] uris = uri.split("/");
		
		if(uris.length > 1 && !uris[1].equals("index") && !uris[1].equals("signup") && !uris[1].equals("signin") && !uris[1].equals("static")
				 && !uris[1].equals("check-username") && !uris[1].equals("templetes")) {
			User user = (User) req.getSession().getAttribute("user");
			if(user == null) {
				resp.sendRedirect("/index");
			} else {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}
}
