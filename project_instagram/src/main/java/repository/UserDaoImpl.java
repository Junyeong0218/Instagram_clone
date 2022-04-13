package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

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
			sql = "select count(username) from user_mst where username = ?;";
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
	public int signup(User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "insert into user_mst values(0, ?, ?, ?, ?, now(), now());";
			pstmt = conn.prepareStatement(sql);
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
						+ "um.username = ?;";
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
		String sql = "select * from user_mst where username = ?;";
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
				user.setCreate_date(rs.getTimestamp("create_date").toLocalDateTime());
				user.setUpdate_date(rs.getTimestamp("update_date").toLocalDateTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return user;
	}

	@Override
	public List<User> getAllUsers() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from user_mst;";
		List<User> userList = new ArrayList<User>();
		
		try {
			conn = db.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setCreate_date(rs.getTimestamp("create_date").toLocalDateTime());
				user.setUpdate_date(rs.getTimestamp("update_date").toLocalDateTime());
				
				userList.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userList;
	}
	
	
}
