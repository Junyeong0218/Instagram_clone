package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import config.FileUploadPathConfig;
import entity.NonReadActivities;
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

public class CharsetEncodingFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("charset filter initialize!");
		
		System.out.println("charset filter initialize finish! ----------------------------");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("------------------------------------");
		System.out.println("CharsetFilter executed!");
		request.setCharacterEncoding("UTF-8");
		
		chain.doFilter(request, response);
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
	}

}
