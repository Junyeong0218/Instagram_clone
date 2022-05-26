package apiController.message_api;

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

import entity.User;
import repository.MessageDao;
import repository.NewActivityDao;
import response_dto.MessageResDto;
import service.MessageService;
import service.MessageServiceImpl;

@WebServlet("/message/room")
public class RoomController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MessageService messageService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		messageService = new MessageServiceImpl((MessageDao) servletContext.getAttribute("messageDao"),
																							   (NewActivityDao) servletContext.getAttribute("newActivityDao"));
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = (User) request.getAttribute("sessionUser");
		int room_id = (Integer) request.getAttribute("room_id");
		
		List<MessageResDto> messages = messageService.selectMessages(sessionUser.getId(), room_id);
		StringBuilder sb = new StringBuilder();
		System.out.println(messages);
		
		sb.append("[ ");
		for(MessageResDto message : messages) {
			sb.append(" { \"id\": \"" + message.getId() + "\", " + 
									"\"room_id\": \"" + message.getRoom_id() + "\", " +
									"\"user_id\": \"" + message.getUser_id() + "\", " +
									"\"contents\": \"" + message.getContents() + "\", " +
									"\"is_image\": \"" + message.is_image() + "\", " +
									"\"image_id\": \"" + message.getImage_id() + "\", " +
									"\"file_name\": \"" + message.getFile_name() + "\", " +
									"\"create_date\": \"" + message.getCreate_date() + "\", \"like_users\": [ ");
			for(int like_user : message.getLike_users()) {
				sb.append(like_user + ", ");
			}
			if(message.getLike_users().size() > 0) sb.replace(sb.lastIndexOf(", "), sb.length(), "");
			sb.append(" ] }, ");
		}
		if(messages.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ]");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = (User) request.getAttribute("sessionUser");
		
		String[] target_users = request.getParameterValues("target_users[]");
		List<Integer> target_user_ids = Arrays.stream(target_users).map(e -> Integer.parseInt(e))
																															.collect(Collectors.toList());
		System.out.println(target_user_ids);
		
		List<MessageResDto> messages = messageService.insertNewRoom(sessionUser.getId(), target_user_ids);
		StringBuilder sb = new StringBuilder();
		System.out.println(messages);
		
		sb.append("[ ");
		for(MessageResDto message : messages) {
			sb.append(" { \"id\": \"" + message.getId() + "\", " + 
									"\"room_id\": \"" + message.getRoom_id() + "\", " +
									"\"user_id\": \"" + message.getUser_id() + "\", " +
									"\"contents\": \"" + message.getContents() + "\", " +
									"\"is_image\": \"" + message.is_image() + "\", " +
									"\"image_id\": \"" + message.getImage_id() + "\", " +
									"\"file_name\": \"" + message.getFile_name() + "\", " +
									"\"create_date\": \"" + message.getCreate_date() + "\", \"like_users\": [ ");
			for(int like_user : message.getLike_users()) {
				sb.append(like_user + ", ");
			}
			if(message.getLike_users().size() > 0) sb.replace(sb.lastIndexOf(", "), sb.length(), "");
			sb.append(" ] }, ");
		}
		if(messages.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ]");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
}
