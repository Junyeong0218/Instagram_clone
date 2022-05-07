package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DBConnectionMgr;

public class MessageDaoImpl implements MessageDao {

	private DBConnectionMgr db;
	
	public MessageDaoImpl() {
		db = DBConnectionMgr.getInstance();
	}
	
	@Override
	public Map<String, List<?>> selectRecentMessages(int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Map<String, List<?>> messageMap = new HashMap<String, List<?>>();
		
		try {
			conn = db.getConnection();
			sql = "SELECT "
						+ "dm.id, "
						
						+ "dm.user_id, "
						+ "um.username, "
						+ "um.`name`, "
						+ "um.has_profile_image, "
						+ "up.file_name, "
	
						+ "dm.target_user_id, "
						+ "um2.username, "
						+ "um2.`name`, "
						+ "um2.has_profile_image, "
						+ "up2.file_name, "
	
						+ "dm.`contents`, "
						+ "dm.is_image, "
						+ "dm.image_id, "
						+ "dmi.file_name AS image_message_file_name, "
						+ "dm.create_date, "
						+ "dmr.id AS reaction_flag "
					+ "FROM "
						+ "direct_message_mst dm "
						+ "LEFT OUTER JOIN user_mst um ON(um.id = dm.user_id AND um.id != ?) "
						+ "LEFT OUTER JOIN user_profile_image up ON(up.user_id = um.id) "
						+ "LEFT OUTER JOIN user_mst um2 ON(um2.id = dm.target_user_id AND um2.id != ?) "
						+ "LEFT OUTER JOIN user_profile_image up2 ON(up2.user_id = um2.id) "
						+ "LEFT OUTER JOIN direct_message_image dmi ON(dmi.id = dm.image_id) "
						+ "LEFT OUTER JOIN direct_message_reaction dmr ON(dmr.direct_message_id = dm.id) "
					+ "WHERE "
						+ "dm.target_user_id = ? OR dm.user_id = ?"
					+ "GROUP BY "
						+ "um.id "
					+ "ORDER BY"
						+ "dm.create_date desc;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, user_id);
			pstmt.setInt(3, user_id);
			pstmt.setInt(4, user_id);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
			}
		} catch (SQLDataException e) {
			System.out.println("no row!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}

		return messageMap;
	}
	
	@Override
	public int insertDirectTextMessage(int user_id, int target_user_id, String contents) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "insert into direct_message_mst values(0, ?, ?, ?, 0, null, now(), now(), 0, null);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, target_user_id);
			pstmt.setString(3, contents);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLDataException e) {
			System.out.println("no row!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return result;
	}
	
	@Override
	public boolean selectSpecificRoom(int user_id, List<Integer> target_user_ids) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			conn = db.getConnection();
			sql = "";
		} catch (SQLDataException e) {
			System.out.println("no row!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return false;
	}
	
	@Override
	public boolean insertNewRoom(int user_id, List<Integer> target_user_ids) {
		// TODO Auto-generated method stub
		return false;
	}
}
