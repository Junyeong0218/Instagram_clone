package apiController.direct_api;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.User;
import repository.MessageDao;
import service.MessageService;
import service.MessageServiceImpl;

@WebServlet("/direct/toggle-message-reaction")
public class ToggleMessageReaction extends HttpServlet{
	private static final long serialVersionUID = 1L;

	private MessageService messageService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		messageService = new MessageServiceImpl((MessageDao) servletContext.getAttribute("messageDao"));
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User sessionUser = (User) session.getAttribute("user");
		
		int message_id = Integer.parseInt(request.getParameter("message_id"));
		
		List<Integer> like_users = messageService.toggleMessageReaction(sessionUser.getId(), message_id);
		StringBuilder sb = new StringBuilder();
		
		sb.append("[ ");
		for(int user_id : like_users) {
			sb.append(user_id + ", ");
		}
		if(like_users.size() > 0) sb.replace(sb.lastIndexOf(", "), sb.length(), "");
		sb.append(" ]");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
}
