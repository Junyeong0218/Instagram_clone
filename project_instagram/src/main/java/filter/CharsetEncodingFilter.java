package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import repository.ArticleDao;
import repository.ArticleDaoImpl;
import repository.FollowDao;
import repository.FollowDaoImpl;
import repository.MessageDao;
import repository.MessageDaoImpl;
import repository.SearchDao;
import repository.SearchDaoImpl;
import repository.StoryDao;
import repository.StoryDaoImpl;
import repository.UserDao;
import repository.UserDaoImpl;

@WebFilter("/*")
public class CharsetEncodingFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext servletContext = filterConfig.getServletContext();
		UserDao userDao = new UserDaoImpl();
		ArticleDao articleDao = new ArticleDaoImpl();
		FollowDao followDao = new FollowDaoImpl();
		StoryDao storyDao = new StoryDaoImpl();
		SearchDao searchDao = new SearchDaoImpl();
		MessageDao messageDao = new MessageDaoImpl();
		servletContext.setAttribute("userDao", userDao);
		servletContext.setAttribute("articleDao", articleDao);
		servletContext.setAttribute("followDao", followDao);
		servletContext.setAttribute("storyDao", storyDao);
		servletContext.setAttribute("searchDao", searchDao);
		servletContext.setAttribute("messageDao", messageDao);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		
		chain.doFilter(request, response);
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
	}

}
