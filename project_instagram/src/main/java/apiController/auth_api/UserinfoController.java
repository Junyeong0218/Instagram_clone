package apiController.auth_api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.FileUploadPathConfig;
import entity.JwtProperties;
import entity.SecurityContext;
import entity.User;
import entity.User.UserBuilder;
import repository.UserDao;
import request_dto.CheckInputReqDto;
import service.AuthService;
import service.AuthServiceImpl;
import service.FileService;

@WebServlet("/auth/userinfo")
@MultipartConfig(
	fileSizeThreshold = 1024 * 1024,
	maxFileSize = 1024 * 1024 * 3,
	maxRequestSize = 1024 * 1024 * 10
)
public class UserinfoController extends HttpServlet{
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
		
		int result = authService.checkInput(checkInputReqDto);

		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(result);
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 중간에 한글 경로 포함되어 있어도 알아서 파일 넣어줌.
		// form-data 로 받았을 때의 fileName으로 저장되기 때문에 js로 파일명 변경을 시도했으나, 보안 문제로 input type=file 의 value를 임의로 변경할 수 없었음.
		// 파일명을 username.extension 으로 변경한 새로운 File 객체를 생성하고 DataTransfer 객체의 items에 add 한 후 input type=file에 덮어쓰기로 해결함.
		User sessionUser = SecurityContext.certificateUser(request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, ""));
		UserBuilder builder = User.builder();
		Method[] methods = UserBuilder.class.getMethods();
		
		String formDataString = request.getParameter("formData");
		formDataString = formDataString.replaceAll("[\\{\\}]", "");
		List<String> variables = Arrays.asList(formDataString.split(","));
		for(String string : variables) {
			StringTokenizer st = new StringTokenizer(string, ":");
			String var = st.nextToken().replaceAll("\"", "");
			String value = st.nextToken().replaceAll("\"", "");
			for(Method method : methods) {
				if(method.getName().equals(var)) {
					try {
						if(var.equals("gender")) {
							builder = (UserBuilder) method.invoke(builder, Integer.parseInt(value));
						} else {
							builder = (UserBuilder) method.invoke(builder, value);
						}
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(FileService.hasFile(request.getParts())) {
			String file_name = FileService.uploadProfileImage(request.getParts(), FileUploadPathConfig.getFileUploadPath() + "/user_profile_images");
			builder = builder.file_name(file_name);
		}
		
		User buildedUser = builder.build();
		System.out.println("buildedUser : " + buildedUser);
		
		int result = authService.updateUserinfo(sessionUser, buildedUser);
		System.out.println(result);
		
		response.setContentType("text/plain; charset=UTF-8");
		if(result > 0) {
			// user select by id
			User updatedUser = authService.getUserByUsername(buildedUser.getUsername());
			// user_id username name token 재발급
			// principal setUser
			SecurityContext.reIssueToken(updatedUser, (String) request.getSession().getAttribute("UUID"));
			// true
			response.getWriter().print(true);
		} else {
			response.getWriter().print(false);
		}
	}
}
