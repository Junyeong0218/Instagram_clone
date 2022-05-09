package apiController.direct_api;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.Message;
import entity.User;
import repository.MessageDao;
import service.MessageService;
import service.MessageServiceImpl;

@WebServlet("/direct/insert-new-room")
public class InsertNewRoom extends HttpServlet {
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
		
		String[] target_users = request.getParameterValues("target_users[]");
		List<Integer> target_user_ids = Arrays.stream(target_users).map(e -> Integer.parseInt(e))
																															.collect(Collectors.toList());
		System.out.println(target_user_ids);
		
		List<Message> messages = messageService.insertNewRoom(sessionUser.getId(), target_user_ids);
		StringBuilder sb = new StringBuilder();
		System.out.println(messages);
		
		sb.append("[ ");
		for(Message message : messages) {
			sb.append(" { \"id\": \"" + message.getId() + "\", " + 
									"\"room_id\": \"" + message.getRoom_id() + "\", " +
									"\"user_id\": \"" + message.getUser_id() + "\", " +
									"\"contents\": \"" + message.getContents() + "\", " +
									"\"is_image\": \"" + message.is_image() + "\", " +
									"\"image_id\": \"" + message.getImage_id() + "\", " +
									"\"file_name\": \"" + message.getFile_name() + "\", " +
									"\"reaction_flag\": \"" + message.isReaction_flag() + "\", " +
									"\"create_date\": \"" + message.getCreate_date() + "\"} , ");
		}
		if(messages.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ]");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
}
