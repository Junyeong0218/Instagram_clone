package apiController.follow_api;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.User;
import repository.FollowDao;
import repository.NewActivityDao;
import service.FollowService;
import service.FollowServiceImpl;

@WebServlet("/follow/user")
public class FollowUserController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private FollowService followService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		followService = new FollowServiceImpl((FollowDao) servletContext.getAttribute("followDao"),
																					(NewActivityDao) servletContext.getAttribute("newActivityDao"));
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = (User) request.getAttribute("sessionUser");
		
		int partner_user_id = (Integer) request.getAttribute("partner_user_id");
		
		int result = followService.insertFollowUser(partner_user_id, sessionUser.getId());
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(result);
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = (User) request.getAttribute("sessionUser");
		
		int partner_user_id = (Integer) request.getAttribute("partner_user_id"); 
		
		int result = followService.deleteFollowUser(partner_user_id, sessionUser.getId());
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(result);
	}
}
