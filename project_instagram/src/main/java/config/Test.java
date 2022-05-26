package config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import entity.User;

public class Test {

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid);
//		User user = new User();
//		user.setId(1);
//		user.setUsername("hippo2003");
//		user.setName("박준영");
//		user.setEmail("hippo2003@naver.com");
//		user.setPhone("01035947111");
//		user.setDescription("asdjgakjdlkf");
//		user.setGender(1);
//		
//		User sessionUser = new User();
//		sessionUser.setId(1);
//		sessionUser.setUsername("hippo2003");
//		sessionUser.setName("박준영");
//		sessionUser.setEmail("hippo2003@naver.com");
//		sessionUser.setGender(3);
//		
//		StringBuilder sql = new StringBuilder();
//		int result = 0;
//		
//		sql.append("update user_mst set ");
//		Field[] fields = user.getClass().getDeclaredFields();
//		Method[] methods = user.getClass().getDeclaredMethods();
//		for(Field field : fields) {
//			String varName = field.getName();
//			Object userValue = null;
//			Object sessionUserValue = null;
//			for(Method method : methods) {
//				String methodName = method.getName().toLowerCase();
//				if(methodName.contains(varName) && methodName.contains("get")) {
//					System.out.println(methodName + " is invoked!!!");
//					userValue = method.invoke(user);
//					sessionUserValue = method.invoke(sessionUser);
//					break;
//				}
//			}
//			if(userValue == null && sessionUserValue == null) continue;
//			if(! userValue.equals(sessionUserValue) && userValue != null) {
//				sql.append(varName + " = " + userValue + ", ");
//			}
//		}
//		sql.replace(sql.lastIndexOf(","), sql.length(), "");
//		sql.append(" where id = ?;");
//		System.out.println(sql.toString());
	}
}
