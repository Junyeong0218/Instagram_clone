package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.DBConnectionMgr;
import response_dto.RecentStoryResDto;

public class StoryDaoImpl implements StoryDao {

	private DBConnectionMgr db;
	
	public StoryDaoImpl() {
		db = DBConnectionMgr.getInstance();
	}
	
	@Override
	public List<RecentStoryResDto> selectRecentStories(int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		List<RecentStoryResDto> userList = new ArrayList<RecentStoryResDto>();
		
		try {
			conn = db.getConnection();
			sql = "select "
					+ "um.id ,"
					+ "um.username, "
					+ "um.name, "
					+ "ud.has_profile_image, "
					+ "up.file_name, "
					+ "us.id "
				+ "from "
					+ "user_mst um "
					+ "left outer join user_detail ud on(ud.user_id = um.id) "
					+ "left outer join user_profile_image up on(um.id = up.user_id) "
					+ "left outer join user_story_mst us on(um.id = us.user_id) "
				+ "where "
					+ "um.id != ? and um.disable_flag = 0 and us.id is not null";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				RecentStoryResDto resDto = new RecentStoryResDto();
				resDto.setId(rs.getInt("id"));
				resDto.setUsername(rs.getString("username"));
				resDto.setName(rs.getString("name"));
				resDto.setHas_profile_image(rs.getInt("has_profile_image") == 1 ? true : false);
				resDto.setFile_name(rs.getCharacterStream("file_name") == null ? "" : rs.getString("file_name"));
				
				userList.add(resDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return userList;
	}
}
