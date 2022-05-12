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

import entity.RoomInfo;
import entity.User;
import repository.MessageDao;
import response_dto.RoomSummaryResDto;
import service.MessageService;
import service.MessageServiceImpl;

@WebServlet("/direct/select-rooms")
public class SelectRooms extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MessageService messageService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		messageService = new MessageServiceImpl((MessageDao) servletContext.getAttribute("messageDao"));
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User sessionUser = (User) session.getAttribute("user");
		
		List<RoomSummaryResDto> rooms = messageService.selectRoomInfoForInit(sessionUser.getId());
		StringBuilder sb = new StringBuilder();
		
		sb.append(" { \"user_id\": \"" + sessionUser.getId() + "\", " + 
								"\"room_summary\": [ ");
		for(RoomSummaryResDto room : rooms) {
			sb.append(" { \"room_id\": \"" + room.getRoom_id() + "\", " +
									"\"entered_users\" : [ ");
			List<User> users = room.getEntered_users();
			for(User user : users) {
				sb.append("{ \"user_id\": \"" + user.getId() + "\", " + 
										"\"username\": \"" + user.getUsername() + "\", " +
										"\"name\": \"" + user.getName() + "\", " +
										"\"has_profile_image\": \"" + user.isHas_profile_image() + "\", " +
										"\"file_name\": \"" + user.getFile_name() + "\"}, ");
			}
			if(users.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
			sb.append(" ], \"message\": { " +
									"\"message_id\": \"" + room.getMessage().getId() + "\", " + 
									"\"contents\": \"" + room.getMessage().getContents() + "\", " + 
									"\"create_date\": \"" + room.getMessage().getCreate_date() + "\"}, " +
									"\"all_message_count\": \"" + room.getAll_message_count() + "\", " + 
									"\"read_message_count\": \"" + room.getRead_message_count() + "\" }, ");
		}
		if(rooms.size() > 0) sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append(" ] }");
		System.out.println(sb.toString());
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
		
	}
}
