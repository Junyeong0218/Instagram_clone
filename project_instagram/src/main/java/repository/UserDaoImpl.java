package repository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.util.Map;

import db.DBConnectionMgr;
import entity.SecurityContext;
import entity.User;

public class UserDaoImpl implements UserDao {

	private final DBConnectionMgr db;
	
	public UserDaoImpl() {
		db = DBConnectionMgr.getInstance();
	}
	
	@Override
	public int checkEmail(String email) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "select count(email) from user_mst where email = ? and disable_flag = 0;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLDataException e) {
			System.out.println("no row");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return result;
	}
	
	@Override
	public int checkPhone(String phone) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "select count(phone) from user_mst where phone = ? and disable_flag = 0;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, phone);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLDataException e) {
			System.out.println("no row");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return result;
	}
	
	@Override
	public int checkUsername(String username) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "select count(username) from user_mst where username = ? and disable_flag = 0;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLDataException e) {
			System.out.println("no row");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return result;
	}
	
	@Override
	public String selectOriginPassword(int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String password = null;
		
		try {
			conn = db.getConnection();
			sql = "select password from user_mst where id = ? and disable_flag = 0;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				password = rs.getString("password");
			}
		} catch (SQLDataException e) {
			System.out.println("no row");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn);
		}
		
		return password;
	}
	
	@Override
	public String selectPassword(String username) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String password = null;
		
		try {
			conn = db.getConnection();
			sql = "select password from user_mst where username = ? and disable_flag = 0;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				password = rs.getString("password");
			}
		} catch (SQLDataException e) {
			System.out.println("no row");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return password;
	}
	
	@Override
	public int selectIdByOauthEmail(String oauth_email) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int user_id = 0;
		
		try {
			conn = db.getConnection();
			sql = "select id from user_mst where email = ? and disable_flag = 0;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, oauth_email);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				user_id = rs.getInt(1);
			}
		} catch (SQLDataException e) {
			System.out.println("no row");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return user_id;
	}
	
	@Override
	public int selectOauthUserId(String oauth_username) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int user_id = 0;
		
		try {
			conn = db.getConnection();
			sql = "select id from user_mst where oauth_username = ? and disable_flag = 0;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, oauth_username);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				user_id = rs.getInt(1);
			}
		} catch (SQLDataException e) {
			System.out.println("no row");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return user_id;
	}
	
	@Override
	public int signup(User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		boolean isEmail = user.getEmail().contains("@");
		int result = 0;
		
		try {
			conn = db.getConnection();
			sb.append("insert into user_mst(username, password, name, ");
			if(isEmail) {
				sb.append("email, ");
			} else {
				sb.append("phone, ");
			}
			sb.append("create_date, update_date, role) values(?, ?, ?, ?, now(), now(), \"ROLE_USER\");");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getEmail());
			
			result = pstmt.executeUpdate();
			
			int secret_key_count = 0;
			if(result == 1) {
				pstmt.close();
				sb.delete(0, sb.length());
				sb.append("select id from user_mst where username = ?;");
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setString(1, user.getUsername());
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					user.setId(rs.getInt("id"));
				}
				
				String uuid = null;
				boolean isExist = true;
				while(isExist) {
					pstmt.close();
					rs.close();
					sb.delete(0, sb.length());
					uuid = SecurityContext.getInstance().generateUUID();
					sb.append("select count(secret_key) from user_auth_token where secret_key = ?;");
					pstmt = conn.prepareStatement(sb.toString());
					pstmt.setString(1, uuid);
					rs = pstmt.executeQuery();
					
					if(rs.next()) {
						secret_key_count = rs.getInt(1);
					}
					if(secret_key_count == 0) isExist = false;
				}
				sb.delete(0, sb.length());
				sb.append("update user_auth_token set secret_key = ? where user_id = ?;");
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setString(1, uuid);
				pstmt.setInt(2, user.getId());
				
				result += pstmt.executeUpdate();
			}
		} catch (SQLDataException e) {
			System.out.println("no row");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return result;
	}
	
	@Override
	public int oauthSignup(String provider, Map<String, String> userData) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "select username from user_mst where username like \"tempinsta%\" order by username desc limit 1;";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			String tempUsername = null;
			if(rs.next()) {
				tempUsername = rs.getString("username");
				String number = tempUsername.replace("tempinsta", "").replaceAll("0", "");
				tempUsername = "tempinsta";
				for(int i = 0; i < 7 - number.length(); i++) {
					tempUsername += "0";
				}
				int newNumber = Integer.parseInt(number);
				tempUsername += ++newNumber;
			} else {
				tempUsername = "tempinsta0000001";
			}
			
			pstmt.close();
			rs.close();
			
			sql = "insert into "
						+ "user_mst "
					+ "values("
						+ "0, "
						+ "?, "
						+ "?, "
						+ "?, "
						+ "?, "
						+ "?, "
						+ "?, "
						+ "?, "
						+ "now(), "
						+ "now(), "
						+ "0, "
						+ "null, "
						+ "\"ROLE_USER\""
					+ ");";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tempUsername);
			pstmt.setString(2, tempUsername);
			pstmt.setString(3, userData.get("name"));
			pstmt.setString(4, userData.get("email"));
			pstmt.setString(5, userData.get("phone").replaceAll("-", ""));
			pstmt.setString(6, provider + "_" + userData.get("id"));
			pstmt.setString(7, provider);
			
			result = pstmt.executeUpdate();
			
			if(result == 1) {
				pstmt.close();
				rs.close();
				sql = "select id from user_mst where oauth_username = ?;";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				int user_id = 0;
				if(rs.next()) {
					user_id = rs.getInt("id");
				}
				
				String uuid = null;
				boolean isExist = true;
				while(isExist) {
					pstmt.close();
					rs.close();
					int secret_key_count = 0;
					uuid = SecurityContext.getInstance().generateUUID();
					sql = "select count(secret_key) from user_auth_token where secret_key = ?;";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, uuid);
					rs = pstmt.executeQuery();
					
					if(rs.next()) {
						secret_key_count = rs.getInt(1);
					}
					if(secret_key_count == 0) isExist = false;
				}
				sql = "update user_auth_token set secret_key = ? where user_id = ?;";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, uuid);
				pstmt.setInt(2, user_id);
				
				result += pstmt.executeUpdate();
			}
		} catch (SQLDataException e) {
			System.out.println("no row");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return result;
	}
	
	@Override
	public int updateUserConnectOauth(int user_id, String oauth_username, String provider) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "update user_mst set oauth_username = ?, provider = ?, update_date = now() where id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, oauth_username);
			pstmt.setString(2, provider);
			pstmt.setInt(3, user_id);
			
			result = pstmt.executeUpdate();
		} catch (SQLDataException e) {
			System.out.println("no row");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		return result;
	}
	
	@Override
	public User getUserByOauthUsername(String oauth_username) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select "
								+ "um.id, "
								+ "um.username, "
								+ "um.`name`, "
								+ "um.role, "
								+ "uat.secret_key "
							+ "from "
								+ "user_mst um "
								+ "left outer join user_auth_token uat on(uat.user_id = um.id) "
							+ "where "
								+ "um.oauth_username = ?;";
		User user = null;
		
		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, oauth_username);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				user.setSecret_key(rs.getString("secret_key"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return user;
	}
	
	@Override
	public User getUserByUsername(String username) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select "
								+ "um.id, "
								+ "um.username, "
								+ "um.`name`, "
								+ "um.role, "
								+ "uat.secret_key "
							+ "from "
								+ "user_mst um "
								+ "left outer join user_auth_token uat on(uat.user_id = um.id) "
							+ "where "
								+ "um.username = ?;";
		User user = null;
		
		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				user.setSecret_key(rs.getString("secret_key"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return user;
	}
	
	@Override
	public User getUserById(int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select "
						+ "um.*, "
						+ "ud.has_profile_image, "
						+ "`up`.file_name "
					+ "from "
						+ "user_mst um "
						+ "left outer join user_detail ud on(ud.user_id = um.id) "
						+ "left outer join user_profile_image `up` on(um.id = `up`.user_id) "
					+ "where "
						+ "um.id = ? and um.disable_flag = 0;";
		User user = null;
		
		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			
			rs = pstmt.executeQuery();
			// rs 가 null 인 상태에서 next() 호출 시 SQLDataException
			
			while(rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getCharacterStream("email") == null ? "" : rs.getString("email"));
				user.setPhone(rs.getCharacterStream("phone") == null ? "" : rs.getString("phone"));
				user.setHas_profile_image(rs.getInt("has_profile_image") == 1 ? true : false);
				user.setFile_name(rs.getCharacterStream("file_name") == null ? "" : rs.getString("file_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return user;
	}

	@Override
	public int updateUserinfo(User sessionUser, User user) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		int result = 0;
		
		sql.append("update user_detail set ");
		Field[] fields = user.getClass().getDeclaredFields();
		Method[] methods = user.getClass().getDeclaredMethods();
		for(Field field : fields) {
			String varName = field.getName();
			if(varName.equals("password") || varName.contains("date") || varName.equals("file_name")) continue;
			Object userValue = null;
			Object sessionUserValue = null;
			for(Method method : methods) {
				String methodName = method.getName().toLowerCase();
				if(methodName.contains(varName) && methodName.contains("get") || methodName.contains(varName) && methodName.contains("is")) {
					System.out.println(methodName + " is invoked!!!");
					userValue = method.invoke(user);
					sessionUserValue = method.invoke(sessionUser);
					System.out.println(userValue);
					System.out.println(sessionUserValue);
					break;
				}
			}
			if(userValue == null && sessionUserValue == null) continue; // 변동사항 없음 ( 빈 값 )
			else if(userValue == null && sessionUserValue != null) sql.append(varName + " = null, "); // 원래 있던 값 삭제 ( null 로 update )
			else if(userValue != null && sessionUserValue == null) { // 값 update 필요
				if(varName.equals("gender")) {
					sql.append(varName + " = " + userValue + ", ");
				} else {
					sql.append(varName + " = \"" + userValue + "\", ");  
				}
			}
			else if(userValue != null && sessionUserValue != null) { // 두 값이 서로 다를때만 update
				if(varName.equals("has_profile_image")) {
					sql.append("has_profile_image = " + user.isHas_profile_image() + ", ");
				} else if(! userValue.equals(sessionUserValue)) {
					if(varName.equals("gender")) {
						sql.append(varName + " = " + userValue + ", ");
					} else {
						sql.append(varName + " = \"" + userValue + "\", ");  
					}
				}
			}
		}
		sql.replace(sql.lastIndexOf(","), sql.length(), "");
		sql.append(", update_date = now() where id = ?;");
		System.out.println(sql.toString());
		
		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, user.getId());
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return result;
	}
	
	@Override
	public int updateUserProfile(User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "";
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "update user_profile_image set file_name = ?, update_date = now() where user_id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getFile_name());
			pstmt.setInt(2, user.getId());
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return result;
	}
	
	@Override
	public int updatePassword(User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "";
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "update user_mst set password = ?, update_date = now() where id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getPassword());
			pstmt.setInt(2, user.getId());
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return result;
	}
	
	@Override
	public int registerJwtToken(int user_id, String jwt) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "";
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "update user_auth_token set jwt_token = ? where user_id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, jwt);
			pstmt.setInt(2, user_id);
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return result;
	}
	
	@Override
	public int updateJwtToken(int user_id, String jwt) {
		return registerJwtToken(user_id, jwt);
	}
	
	@Override
	public User selectTokenInfo(String jwt) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		User user = null;
		
		try {
			conn = db.getConnection();
			sql = "select "
						+ "um.id, "
						+ "um.username, "
						+ "um.name,"
						+ "um.role,"
						+ "uat.secret_key "
					+ "from "
						+ "user_auth_token uat "
						+ "left outer join user_mst um on(um.id = uat.user_id) "
					+ "where "
						+ "uat.jwt_token = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, jwt);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				user.setSecret_key(rs.getString("secret_key"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return user;
	}
}
