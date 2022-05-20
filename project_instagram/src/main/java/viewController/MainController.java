package viewController;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.JwtProperties;

@WebServlet("/main")
public class MainController extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader(JwtProperties.HEADER_STRING, (String) request.getAttribute("token"));
		request.getRequestDispatcher("/WEB-INF/views/main/main.jsp").forward(request, response);
	}
}
