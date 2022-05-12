package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

import db.DBConnectionMgr;
import entity.NonRead;

public class NewActivityDaoImpl implements NewActivityDao {

	private DBConnectionMgr db;
	
	public NewActivityDaoImpl() {
		db = DBConnectionMgr.getInstance();
	}
	
	@Override
	public List<NonRead> selectNonReadActivities(int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<NonRead> nonReads = new ArrayList<NonRead>();
		
		try {
			conn = db.getConnection();
			sql = "SELECT "
						+ "`logs`.id AS log_id, "
						+ "`logs`.related_user_read_flag, "
	
						+ "0 AS message_id, "
						+ "0 AS message_read_flag "
					+ "FROM "
						+ "activity_logs `logs` "
					+ "WHERE "
						+ "`logs`.related_user_id = 4 AND `logs`.related_user_read_flag = 0 "

					+ "UNION ALL "

					+ "SELECT  "
						+ "0 AS log_id, "
						+ "0 AS related_user_read_flag, "
	
						+ "message.id AS message_id, "
						+ "flags.id AS message_read_flag "
					+ "FROM "
						+ "direct_message_room_entered_users room_users "
						+ "LEFT OUTER JOIN direct_message_mst message ON(message.room_id = room_users.room_id) "
						+ "LEFT OUTER JOIN direct_message_read_flags flags ON(flags.direct_message_id = message.id) "
					+ "WHERE "
						+ "room_users.user_id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				NonRead nonRead = new NonRead();
				nonRead.setLog_id(rs.getInt("log_id"));
				nonRead.setRelated_user_read_flag(rs.getBoolean("related_user_read_flag"));
				nonRead.setMessage_id(rs.getInt("message_id"));
				nonRead.setMessage_read_flag(rs.getInt("message_read_flag") > 0 ? true : false);
				
				nonReads.add(nonRead);
			}
		} catch (SQLDataException e) {
			System.out.println("no rows!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return nonReads;
	}
	
	@Override
	public int updateActivityReadFlag(int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "update activity_logs set related_user_read_flag = true where related_user_id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			
			result = pstmt.executeUpdate();
		} catch (SQLDataException e) {
			System.out.println("no rows!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return result;
	}
	
	@Override
	public List<Integer> insertMessageReadFlag(int user_id, int room_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<Integer> message_ids = new ArrayList<Integer>();
		
		try {
			conn = db.getConnection();
			sql = "select "
						+ "dm.id "
					+ "from "
						+ "direct_message_mst dm "
					+ "where "
						+ "room_id = ? and "
						+ "dm.id not in (select direct_message_id from direct_message_read_flags where user_id = ?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, room_id);
			pstmt.setInt(2, user_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				message_ids.add(rs.getInt("id"));
			}
			
			if(message_ids.size() > 0) {
				for(int i = 0; i < message_ids.size(); i++) {
					pstmt.close();
					sql = "insert into direct_message_read_flags values(0, ?, ?, now(), now());";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, user_id);
					pstmt.setInt(2, message_ids.get(i));
					
					pstmt.executeUpdate();
				}
			}
		} catch (SQLDataException e) {
			System.out.println("no rows!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return message_ids;
	}
}
