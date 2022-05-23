package apiController.auth_api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.JwtProperties;
import entity.SecurityContext;
import entity.User;

@WebServlet("/auth/principal")
public class PrincipalController extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User sessionUser = SecurityContext.certificateUser(request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, ""));
		StringBuilder sb = new StringBuilder();
		sb.append(" { \"id\": \"" + sessionUser.getId() + "\", " + 
							"\"username\": \"" + sessionUser.getUsername() + "\", " + 
							"\"name\": \"" + sessionUser.getName() + "\", " + 
							"\"email\": \"" + sessionUser.getEmail() + "\", " + 
							"\"phone\": \"" + sessionUser.getPhone() + "\", " + 
							"\"website\": \"" + sessionUser.getWebsite() + "\", " + 
							"\"description\": \"" + sessionUser.getDescription() + "\", " + 
							"\"gender\": \"" + sessionUser.getGender() + "\", " + 
							"\"has_profile_image\": \"" + sessionUser.isHas_profile_image() + "\", " + 
							"\"last_username_update_date\": \"" + sessionUser.getLast_username_update_date() + "\", " + 
							"\"file_name\": \"" + sessionUser.getFile_name() + "\" }");
		
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(sb.toString());
	}
}
