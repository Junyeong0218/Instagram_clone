package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DBConnectionMgr;
import entity.RoomInfo;

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
	public List<RoomInfo> selectSpecificRoom(int user_id, List<Integer> target_user_ids) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<RoomInfo> rooms = new ArrayList<RoomInfo>();
		
		try {
			conn = db.getConnection();
			sql = "SELECT "
						+ "room_users.id, "
						+ "room_users.room_id, "
						+ "room_users.user_id, "
						+ "COUNT(room_users2.user_id) AS entered_users_count "
					+ "FROM "
						+ "direct_message_room_entered_users room_users "
						+ "LEFT OUTER JOIN direct_message_room_mst room ON(room.id = room_users.room_id) "
						+ "LEFT OUTER JOIN direct_message_room_entered_users room_users2 ON(room_users2.room_id = room.id) "
					+ "WHERE "
						+ "room_users.user_id IN(" + user_id + ", ";
			for(int id : target_user_ids) {
				sql += id + ", ";
			}
			sql.substring(0, sql.lastIndexOf(","));
			sql += ") "
					+ "GROUP BY "
						+ "room_users.id;";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				RoomInfo room = new RoomInfo();
				room.setId(rs.getInt("id"));
				room.setRoom_id(rs.getInt("room_id"));
				room.setEntered_user_id(rs.getInt("user_id"));
				room.setEntered_users_count(rs.getInt("entered_users_count"));
				
				rooms.add(room);
			}
		} catch (SQLDataException e) {
			System.out.println("no row!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return rooms;
	}
	
	@Override
	public boolean insertNewRoom(int user_id, List<Integer> target_user_ids) {
		// TODO Auto-generated method stub
		return false;
	}
}
