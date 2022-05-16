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

import config.RequestMethod;

@WebFilter("/article/*")
public class ArticleControllersParamFilter implements Filter {

	private final String ARTICLE = "/article";
	private final String ARTICLE_COMMENT = "/article/comment"; 
	private final String ARTICLE_REACTION = "/article/reaction";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		String uri = req.getRequestURI();
		System.out.println("uri : " + uri);
		
		if(uri.equals(ARTICLE) || uri.equals(ARTICLE_REACTION) || uri.equals(ARTICLE_COMMENT)) {
			chain.doFilter(request, response);
			return;
		}
		
		uri = uri.replace("/article", "");
		String[] uris = uri.split("/");
		
		int comment_id = 0;
		int article_id = 0;
		String comment = null;
		System.out.println("after replace : " + uri);
		System.out.println("uris.length : " + uris.length);
		for(String s : uris) {
			System.out.println(s);
		}
		
		String method = req.getMethod();
		System.out.println("method : " + method);

		try {
			article_id = Integer.parseInt(uris[1]);
			request.setAttribute("article_id", article_id);
			System.out.println("article_id : " + article_id);
			
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
				System.out.println("enter to comment if clause : ");
				if(uris.length == 3) {
					// /article/comment allows GET, POST, PUT, DELETE
					if(method.equals(RequestMethod.POST)) {
						System.out.println("length 3 : post to comment");
						req.getRequestDispatcher(ARTICLE_COMMENT).forward(request, response);
					} 
				} else if(uris.length == 4) {
					System.out.println("length 4 : get to comment");
					comment_id = Integer.parseInt(uris[3]);
					request.setAttribute("comment_id", comment_id);
					
					if(method.equals(RequestMethod.GET) ||
						method.equals(RequestMethod.PUT) ||
						method.equals(RequestMethod.DELETE)) {
						System.out.println(uri);
						req.getRequestDispatcher(ARTICLE_COMMENT).forward(request, response);
					}
				}
			} else {
				resp.sendError(404, "not supported method!");
			}
		} catch (NumberFormatException e) {
			System.out.println("/article/comments/* filter :::: throws NumberFormatException");
			System.out.println("/article/comments/" + uri + " :::: is not number!");
			resp.sendError(409, "wrong comment id");
		}
	}
}
