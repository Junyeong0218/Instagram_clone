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

public class ArticleControllersParamFilter implements Filter {

	private final String ARTICLE = "/article";
	private final String ARTICLE_COMMENT = "/article/comment"; 
	private final String ARTICLE_REACTION = "/article/reaction";
	private final String ARTICLE_LIST = "/article/list";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		String uri = req.getRequestURI();
		
		if(uri.equals(ARTICLE) || uri.equals(ARTICLE_REACTION) || uri.equals(ARTICLE_COMMENT) || uri.equals(ARTICLE_LIST)) {
			chain.doFilter(request, response);
			return;
		}
		
		uri = uri.replace("/article", "");
		String[] uris = uri.split("/");
		
		int comment_id = 0;
		int article_id = 0;
		
		String method = req.getMethod();

		try {
			article_id = Integer.parseInt(uris[1]);
			request.setAttribute("article_id", article_id);
			
			if(uris.length == 2) {
				// /article/article-id allows only GET -> /article
				if(method.equals(RequestMethod.GET)) {
					req.getRequestDispatcher(ARTICLE).forward(request, response);
				}
			} else if(uris[2].equals("reaction")) {
				// /article/reaction allows POST, DELETE
				if(method.equals(RequestMethod.POST) ||
					method.equals(RequestMethod.DELETE)) {
					req.getRequestDispatcher(ARTICLE_REACTION).forward(request, response);
				}
			} else if(uris[2].equals("comment")) {
				if(uris.length == 3) {
					// /article/comment allows GET, POST, PUT, DELETE
					if(method.equals(RequestMethod.POST)) {
						req.getRequestDispatcher(ARTICLE_COMMENT).forward(request, response);
					} 
				} else if(uris.length == 4) {
					comment_id = Integer.parseInt(uris[3]);
					request.setAttribute("comment_id", comment_id);
					
					if(method.equals(RequestMethod.GET) ||
						method.equals(RequestMethod.PUT) ||
						method.equals(RequestMethod.DELETE)) {
						req.getRequestDispatcher(ARTICLE_COMMENT).forward(request, response);
					}
				}
			} else {
				resp.sendError(404, "not supported method!");
			}
		} catch (NumberFormatException e) {
			if(uris[1].equals("list")) {
				try {
					int page_indicator = Integer.parseInt(uris[2]);
					request.setAttribute("page_indicator", page_indicator);
					if(method.equals(RequestMethod.GET)) {
						request.getRequestDispatcher(ARTICLE_LIST).forward(request, response);
					} else {
						resp.sendError(404, "not supported method!");
					}
				} catch (NumberFormatException e2) {
					resp.sendError(404, "invalid parameter");
				}
			} else {
				resp.sendError(404, "bad request");
			}
		}
	}
}