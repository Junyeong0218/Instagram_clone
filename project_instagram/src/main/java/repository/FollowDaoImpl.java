package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

import db.DBConnectionMgr;
import entity.User;
import response_dto.FollowSummaryResDto;
import response_dto.UserRecommendResDto;

public class FollowDaoImpl implements FollowDao {
	
	private DBConnectionMgr db;
	
	public FollowDaoImpl() {
		db = DBConnectionMgr.getInstance();
	}
	
	@Override
	public List<UserRecommendResDto> selectRecommendUsers(int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		List<UserRecommendResDto> userList = new ArrayList<UserRecommendResDto>();
		
		try {
			conn = db.getConnection();
			sql = "select "
					+ "um.id ,"
					+ "um.username, "
					+ "um.name, "
					+ "um.has_profile_image, "
					+ "`up`.file_name "
				+ "from "
					+ "user_mst um "
					+ "left outer join user_profile_image `up` on(um.id = `up`.user_id) "
				+ "where "
					+ "um.id != ? and "
					+ "um.disable_flag = 0 and "
					+ "um.id not in(select fm.partner_user_id from follow_mst fm where fm.user_id = ?) "
				+ "limit 5;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, user_id);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				UserRecommendResDto resDto = new UserRecommendResDto();
				resDto.setId(rs.getInt("id"));
				resDto.setUsername(rs.getString("username"));
				resDto.setName(rs.getString("name"));
				resDto.setHas_profile_image(rs.getInt("has_profile_image") == 1 ? true : false);
				resDto.setFile_name(rs.getString("file_name"));
				
				userList.add(resDto);
			}
		} catch(SQLDataException e1) {
			System.out.println("no row!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return userList;
	}

	@Override
	public int insertFollowUser(int partner_user_id, int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "insert into follow_mst values(0, ?, ?, 'COMMON', now(), now());";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, partner_user_id);
			
			result = pstmt.executeUpdate();
		} catch(SQLDataException e1) {
			System.out.println("no row!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return result;
	}
	
	@Override
	public int deleteFollowUser(int partner_user_id, int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "delete from follow_mst where user_id = ? and partner_user_id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, partner_user_id);
			
			result = pstmt.executeUpdate();
		} catch(SQLDataException e1) {
			System.out.println("no row!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return result;
	}
	
	@Override
	public FollowSummaryResDto selectFollowSummary(int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		FollowSummaryResDto resDto = null; 
		
		try {
			conn = db.getConnection();
			sql = "select "
						+ "count(fm.partner_user_id) as following, "
						+ "count(fm2.user_id) as follower "
					+ "from "
						+ "follow_mst fm "
						+ "left outer join follow_mst fm2 on(fm2.partner_user_id = ?) "
					+ "where "
						+ "fm.user_id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, user_id);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				resDto = new FollowSummaryResDto();
				
				resDto.setFollower(rs.getInt("follower"));
				resDto.setFollowing(rs.getInt("following"));
			}
		} catch(SQLDataException e1) {
			System.out.println("no row!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return resDto;
	}
	
	@Override
	public List<User> selectFollowingUsers(int user_id, int count_indicator) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<User> followingUserList = new ArrayList<User>();
		
		try {
			conn = db.getConnection();
			sql = "select "
						+ "fm.id, "
						+ "fm.partner_user_id, "
						+ "um.username, "
						+ "um.name, "
						+ "um.has_profile_image, "
						+ "up.file_name "
					+ "from "
						+ "follow_mst fm "
						+ "left outer join user_mst um on(um.id = fm.partner_user_id) "
						+ "left outer join user_profile_image up on(up.user_id = fm.partner_user_id) "
					+ "where "
						+ "fm.user_id = ? "
					+ "limit ?, 11;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, count_indicator * 10);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				User user = new User();
				user.setId(rs.getInt("partner_user_id"));
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				user.setHas_profile_image(rs.getInt("has_profile_image") == 1 ? true : false);
				user.setFile_name(rs.getString("file_name"));
				
				followingUserList.add(user);
			}
		} catch(SQLDataException e1) {
			System.out.println("no row!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return followingUserList;
	}
	
	@Override
	public List<User> selectFollwers(int user_id, int count_indicator) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<User> followers = new ArrayList<User>();
		
		try {
			conn = db.getConnection();
			sql = "select "
						+ "fm.user_id, "
						+ "um.username, "
						+ "um.`name`, "
						+ "um.has_profile_image, "
						+ "up.file_name "
					+ "from "
						+ "follow_mst fm "
						+ "left outer join user_mst um on(um.id = fm.user_id) "
						+ "left outer join user_profile_image up on(up.user_id = fm.user_id) "
					+ "where "
						+ "fm.partner_user_id = ? "
					+ "limit ?, 11";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, count_indicator);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				User user = new User();
				user.setId(rs.getInt("user_id"));
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				user.setHas_profile_image(rs.getInt("has_profile_image") == 1 ? true : false);
				user.setFile_name(rs.getString("file_name"));
				
				followers.add(user);
			}
		} catch(SQLDataException e1) {
			System.out.println("no row!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return followers;
	}
}
