package repository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;

import db.DBConnectionMgr;
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
			db.freeConnection(conn);
		}
		
		return password;
	}
	
	@Override
	public int signup(User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
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
			sb.append("last_username_update_date, create_date, update_date) values(?, ?, ?, ?, now(), now(), now());");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getEmail());
			
			result = pstmt.executeUpdate();
		} catch (SQLDataException e) {
			System.out.println("no row");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn);
		}
		
		return result;
	}
	
	@Override
	public User getUserByUsername(String username) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select "
						+ "um.*, "
						+ "`up`.file_name "
					+ "from "
						+ "user_mst um "
						+ "left outer join user_profile_image `up` on(um.id = `up`.user_id) "
					+ "where "
						+ "um.username = ? and um.disable_flag = 0;";
		User user = null;
		
		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);

			// set params
			pstmt.setString(1, username);
			
			rs = pstmt.executeQuery();
			// rs 가 null 인 상태에서 next() 호출 시 SQLDataException
			
			while(rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getCharacterStream("email") == null ? "" : rs.getString("email"));
				user.setPhone(rs.getCharacterStream("phone") == null ? "" : rs.getString("phone"));
				user.setWebsite(rs.getCharacterStream("website") == null ? "" : rs.getString("website"));
				user.setDescription(rs.getCharacterStream("description") == null ? "" : rs.getString("description"));
				user.setGender(rs.getInt("gender"));
				user.setHas_profile_image(rs.getInt("has_profile_image") == 1 ? true : false);
				user.setLast_username_update_date(rs.getTimestamp("last_username_update_date").toLocalDateTime());
				user.setCreate_date(rs.getTimestamp("create_date").toLocalDateTime());
				user.setUpdate_date(rs.getTimestamp("update_date").toLocalDateTime());
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
	public User getUserById(int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select "
						+ "um.*, "
						+ "`up`.file_name "
					+ "from "
						+ "user_mst um "
						+ "left outer join user_profile_image `up` on(um.id = `up`.user_id) "
					+ "where "
						+ "um.id = ? and um.disable_flag = 0;";
		User user = null;
		
		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);

			// set params
			pstmt.setInt(1, user_id);
			
			rs = pstmt.executeQuery();
			// rs 가 null 인 상태에서 next() 호출 시 SQLDataException
			
			while(rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getCharacterStream("email") == null ? "" : rs.getString("email"));
				user.setPhone(rs.getCharacterStream("phone") == null ? "" : rs.getString("phone"));
				user.setWebsite(rs.getCharacterStream("website") == null ? "" : rs.getString("website"));
				user.setDescription(rs.getCharacterStream("description") == null ? "" : rs.getString("description"));
				user.setGender(rs.getInt("gender"));
				user.setHas_profile_image(rs.getInt("has_profile_image") == 1 ? true : false);
				user.setLast_username_update_date(rs.getTimestamp("last_username_update_date").toLocalDateTime());
				user.setCreate_date(rs.getTimestamp("create_date").toLocalDateTime());
				user.setUpdate_date(rs.getTimestamp("update_date").toLocalDateTime());
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
		
		sql.append("update user_mst set ");
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
	
}
