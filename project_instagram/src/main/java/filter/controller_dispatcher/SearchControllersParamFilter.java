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

public class SearchControllersParamFilter implements Filter {
	
	private final String SEARCH = "/search";
	private final String SEARCH_LOG = "/search/log";
	private final String SEARCH_USERS = "/search/users";
	private final String SEARCH_KEYWORD = "/search/keyword";
	private final String SEARCH_HASHTAG = "/search/hashtag";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String uri = req.getRequestURI();
		String method = req.getMethod();
		
		if(uri.equals(SEARCH) || uri.equals(SEARCH_LOG) || uri.equals(SEARCH_USERS) || uri.equals(SEARCH_KEYWORD) || uri.equals(SEARCH_HASHTAG)) {
			chain.doFilter(request, response);
			return;
		}
		
		uri = uri.replace("/search", "");
		String[] uris = uri.split("/");
		
		if(uris.length == 2) {
			String tag_name = uris[1];
			request.setAttribute("tag_name", tag_name);
			if(method.equals(RequestMethod.GET)) {
				request.getRequestDispatcher(SEARCH).forward(request, response);
			}
		} else if(uris[1].equals("hashtag")) {
			String tag_name = uris[2];
			request.setAttribute("tag_name", tag_name);
			if(method.equals(RequestMethod.GET)) {
				request.getRequestDispatcher(SEARCH_HASHTAG).forward(request, response);
			}
		} else if(uris[1].equals("users")) {
			String keyword = uris[2];
			request.setAttribute("keyword", keyword);
			if(method.equals(RequestMethod.GET)) {
				request.getRequestDispatcher(SEARCH_USERS).forward(request, response);
			}
		} else if(uris[1].equals("keyword")) {
			String keyword = uris[2];
			request.setAttribute("keyword", keyword);
			if(method.equals(RequestMethod.GET)) {
				request.getRequestDispatcher(SEARCH_KEYWORD).forward(request, response);
			}
		} else {
			resp.sendError(404, "bad request");
		}
	}
}
