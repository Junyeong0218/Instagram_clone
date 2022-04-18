package repository;

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
			db.freeConnection(conn);
		}
		
		return result;
	}
	
	@Override
	public int checkOriginPassword(User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "select count(password) from user_mst where id = ? and password = ? and disable_flag = 0;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user.getId());
			pstmt.setString(2, user.getPassword());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result = rs.getInt(1);
			}
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
	public int signin(User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select "
						+ "count(um.username) + count(um2.password) "
					+ "from "
						+ "user_mst um "
						+ "left outer join user_mst um2 on(um.id = um2.id and um2.password = ?) "
					+ "where "
						+ "um.username = ? and um.disable_flag = 0;";
		int result = 0;
		
		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getPassword());
			pstmt.setString(2, user.getUsername());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("No row");
		} finally {
			db.freeConnection(conn);
		}
		
		return result;
	}

	@Override
	public User getUser(String username) {
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
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				user.setWebsite(rs.getString("website"));
				user.setDescription(rs.getString("description"));
				user.setGender(rs.getInt("gender"));
				user.setHas_profile_image(rs.getInt("has_profile_image") == 1 ? true : false);
				user.setLast_username_update_date(rs.getTimestamp("last_username_update_date").toLocalDateTime());
				user.setCreate_date(rs.getTimestamp("create_date").toLocalDateTime());
				user.setUpdate_date(rs.getTimestamp("update_date").toLocalDateTime());
				user.setFile_name(rs.getString("file_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return user;
	}

	@Override
	public int updateUserinfo(User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "";
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "update user_mst "
				+ "set "
					+ "username = ?, "
					+ "name = ?, "
					+ "email = ?, "
					+ "phone = ?, "
					+ "website = ?, "
					+ "description = ?,"
					+ "gender = ?, "
					+ "has_profile_image = ?, "
					+ "update_date = now() "
				+ "where "
					+ "id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getName());
			pstmt.setString(3, user.getEmail());
			pstmt.setString(4, user.getPhone());
			pstmt.setString(5, user.getWebsite());
			pstmt.setString(6, user.getDescription());
			pstmt.setInt(7, user.getGender());
			pstmt.setInt(8, user.isHas_profile_image() ? 1 : 0);
			pstmt.setInt(9, user.getId());
			
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
