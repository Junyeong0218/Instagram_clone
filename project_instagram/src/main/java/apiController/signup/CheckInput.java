package apiController.signup;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import repository.UserDao;
import request_dto.CheckInputReqDto;
import service.AuthService;
import service.AuthServiceImpl;

@WebServlet("/check-input")
public class CheckInput extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private AuthService authService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(getServletConfig());
		ServletContext servletContext = config.getServletContext();
		authService = new AuthServiceImpl((UserDao) servletContext.getAttribute("userDao"));
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String paramName = request.getParameterNames().nextElement();
		String value = request.getParameter(paramName);
		CheckInputReqDto checkInputReqDto = CheckInputReqDto.of(paramName, value);
		// getClass().getDeclaredFields() 
		
		int result = authService.checkInput(checkInputReqDto);

		response.getWriter().print(result);
	}
}
