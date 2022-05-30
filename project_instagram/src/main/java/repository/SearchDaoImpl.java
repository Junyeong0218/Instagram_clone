package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

import db.DBConnectionMgr;
import entity.ArticleDetail;
import entity.LatestSearchDetail;
import entity.SearchKeyword;
import entity.User;

public class SearchDaoImpl implements SearchDao {

	private final DBConnectionMgr db;
	
	public SearchDaoImpl() {
		db = DBConnectionMgr.getInstance();
	}
	
	@Override
	public List<LatestSearchDetail> selectLatestSearches(int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<LatestSearchDetail> latestSearches = new ArrayList<LatestSearchDetail>();
		
		try {
			conn = db.getConnection();
			sql = "select "
						+ "lsr.searched_user_id, "
						+ "um.username, "
						+ "um.`name`, "
						+ "ud.has_profile_image, "
						+ "up.file_name, "
						+ "fm.id as user_follow_flag, "
						
						+ "lsr.hash_tag_id, "
						+ "htm.tag_name, "
						+ "fm2.id as hash_tag_follow_flag, "
						
						+ "lsr.create_date, "
						+ "lsr.update_date "
					+ "from "
						+ "latest_search_records lsr "
						+ "left outer join user_mst um on(um.id = lsr.searched_user_id) "
						+ "left outer join user_detail ud on(ud.user_id = lsr.searched_user_id) "
						+ "left outer join user_profile_image up on(up.user_id = lsr.searched_user_id) "
						+ "left outer join follow_mst fm on(fm.partner_user_id = lsr.searched_user_id) "
						+ "left outer join hash_tag_mst htm on(htm.id = lsr.hash_tag_id) "
						+ "left outer join follow_mst fm2 on(fm2.followed_hash_tag_id = lsr.hash_tag_id) "
					+ "where "
						+ "lsr.user_id = ? "
					+ "order by "
						+ "lsr.update_date desc "
					+ "limit 5;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				LatestSearchDetail detail = new LatestSearchDetail();
				detail.setId(rs.getInt("id"));
				detail.setUser_id(user_id);
				detail.setSearched_user_id(rs.getInt("searched_user_id"));
				detail.setUsername(rs.getString("username"));
				detail.setName(rs.getString("name"));
				detail.setHas_profile_image(rs.getBoolean("has_profile_image"));
				detail.setFile_name(rs.getCharacterStream("file_name") == null ? "" : rs.getString("file_name"));
				detail.setHash_tag_id(rs.getInt("hash_tag_id"));
				detail.setTag_name(rs.getString("tag_name"));
				detail.setCreate_date(rs.getTimestamp("create_date") == null ? null : rs.getTimestamp("create_date").toLocalDateTime());
				detail.setUpdate_date(rs.getTimestamp("update_date") == null ? null : rs.getTimestamp("update_date").toLocalDateTime());
				detail.setUser_follow_flag(rs.getInt("user_follow_flag") > 0 ? true : false);
				detail.setHash_tag_follow_flag(rs.getInt("hash_tag_follow_flag") > 0 ? true : false);
				
				latestSearches.add(detail);
			}
		} catch (SQLDataException e) {
			System.out.println("no row!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return latestSearches;
	}
	
	@Override
	public List<SearchKeyword> selectHashTags(String keyword, int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<SearchKeyword> hashTagList = new ArrayList<SearchKeyword>();
		
		try {
			conn = db.getConnection();
			sql = "select "
						+ "html.id, "
						+ "htm.tag_name,"
						+ "fm.id as hash_tag_follow_flag "
					+ "from "
						+ "hash_tag_mst htm "
						+ "left outer join follow_mst fm on(fm.followed_hash_tag_id = html.id and user_id = ?)"
					+ "where "
						+ "htm.tag_name like \"%?%\" "
					+ "order by "
						+ "hash_tag_follow_flag desc;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			pstmt.setString(2, keyword);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				SearchKeyword result = new SearchKeyword();
				result.setHash_tag_id(rs.getInt("id"));
				result.setTag_name(rs.getString("tag_name"));
				result.setHash_tag_follow_flag(rs.getInt("hash_tag_follow_flag") > 0 ? true : false);
				
				hashTagList.add(result);
			}
		} catch (SQLDataException e) {
			System.out.println("no row!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return hashTagList;
	}
	
	@Override
	public List<SearchKeyword> selectKeyword(String keyword, int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<SearchKeyword> resultList = new ArrayList<SearchKeyword>();
		
		try {
			conn = db.getConnection();
			sql = "SELECT "
						+ "um.id, "
						+ "um.username, "
						+ "um.`name`, "
						+ "ud.has_profile_image, "
						+ "up.file_name, "
						+ "fm.id AS user_follow_flag, "
						
						+ "0 AS hash_tag_id, "
						+ "null as tag_name, "
						+ "0 AS hash_tag_follow_flag "
					+ "FROM "
						+ "user_mst um "
						+ "LEFT OUTER JOIN user_detail ud ON(ud.user_id = um.id) "
						+ "LEFT OUTER JOIN user_profile_image up ON(up.user_id = um.id) "
						+ "LEFT OUTER JOIN follow_mst fm ON(fm.partner_user_id = um.id AND fm.user_id = ?) "
					+ "WHERE  "
						+ "um.username LIKE \"%" + keyword + "%\" and um.id != ? "
					
					+ "UNION ALL "
					
					+ "SELECT  "
						+ "0 as id, "
						+ "null as username, "
						+ "null as `name`, "
						+ "0 as has_profile_image, "
						+ "null as file_name, "
						+ "0 AS user_follow_flag, "
					
						+ "htm.id AS hash_tag_id, "
						+ "htm.tag_name, "
						+ "fm2.id AS hash_tag_follow_flag "
					+ "FROM "
						+ "hash_tag_mst htm "
						+ "LEFT OUTER JOIN follow_mst fm2 ON(fm2.followed_hash_tag_id = htm.id AND fm2.user_id = ?) "
					+ "WHERE "
						+ "htm.tag_name LIKE \"%" + keyword + "%\" "
					+ "ORDER BY "
						+ "user_follow_flag DESC, "
						+ "hash_tag_follow_flag DESC;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, user_id);
			pstmt.setInt(3, user_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				SearchKeyword result = new SearchKeyword();
				result.setSearched_user_id(rs.getInt("id"));
				result.setUsername(rs.getString("username"));
				result.setName(rs.getString("name"));
				result.setHas_profile_image(rs.getBoolean("has_profile_image"));
				result.setFile_name(rs.getCharacterStream("file_name") == null ? "" : rs.getString("file_name"));
				result.setUser_follow_flag(rs.getInt("user_follow_flag") > 0 ? true : false);
				result.setHash_tag_id(rs.getInt("hash_tag_id"));
				result.setTag_name(rs.getString("tag_name"));
				result.setHash_tag_follow_flag(rs.getInt("hash_tag_follow_flag") > 0 ? true : false);
				
				resultList.add(result);
			}
		} catch (SQLDataException e) {
			System.out.println("no row!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return resultList;
	}
	
	@Override
	public List<ArticleDetail> selectArticlesAboutHashTag(String tag_name) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<ArticleDetail> articleList = new ArrayList<ArticleDetail>();
		
		try {
			conn = db.getConnection();
			sql = "SELECT  "
						+ "am.id AS article_id, "
						+ "media.media_type, "
						+ "media.media_name, "
						
						+ "COUNT(distinct htl.id) AS related_article_count, "
						+ "COUNT(distinct ar.id) AS reaction_count, "
						+ "COUNT(distinct ac.id) AS comment_count "
					+ "FROM  "
						+ "hash_tag_logs htl "
						+ "LEFT OUTER JOIN article_mst am ON(am.id = htl.article_id) "
						+ "LEFT OUTER JOIN article_media media ON(media.article_id = am.id AND media.media_name LIKE \"%01%\") "
						+ "LEFT OUTER JOIN article_reaction ar on(ar.article_id = am.id) "
						+ "LEFT OUTER JOIN article_comment ac on(ac.article_id = am.id) "
					+ "WHERE  "
						+ "htl.hash_tag_id = (SELECT id FROM hash_tag_mst WHERE tag_name = ?) "
					+ "GROUP BY  "
						+ "htl.id "
					+ "ORDER BY  "
						+ "htl.create_date DESC;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tag_name);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ArticleDetail detail = new ArticleDetail();
				detail.setArticle_id(rs.getInt("article_id"));
				detail.setMedia_type(rs.getString("media_type"));
				detail.setMedia_name(rs.getString("media_name"));
				detail.setRelated_article_count(rs.getInt("related_article_count"));
				detail.setLike_user_count(rs.getInt("reaction_count"));
				detail.setComment_count(rs.getInt("comment_count"));
				
				articleList.add(detail);
			}
		} catch (SQLDataException e) {
			System.out.println("no row!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return articleList;
	}
	
	@Override
	public int insertLatestSearch(boolean isUser, int id, int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs =  null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "select "
						+ "id "
					+ "from "
						+ "latest_search_records "
					+ "where ";
			if(isUser) sql += "searched_user_id = ? and user_id = ?;";
			else			   sql += "hash_tag_id = ? and user_id = ?;";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			pstmt.setInt(2, user_id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				System.out.println("최근 검색 목록에 있음");
				pstmt.close();
				rs.close();
				sql = "update latest_search_records set update_date = now() where ";
				if(isUser) sql += "searched_user_id = ? and user_id = ?;";
				else			   sql += "hash_tag_id = ? and user_id = ?;";
				System.out.println(sql);
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, id);
				pstmt.setInt(2, user_id);
				
				result = pstmt.executeUpdate();
			} else {
				System.out.println("최근 검색 목록에 없음");
				pstmt.close();
				rs.close();
				sql = "insert into latest_search_records values(0, ?, ";
				if(isUser) sql += "?, null, now(), now());";
				else			   sql += "null, ?, now(), now());";
				System.out.println(sql);
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, user_id);
				pstmt.setInt(2, id);
				
				result = pstmt.executeUpdate();
			}
		} catch (SQLDataException e) {
			System.out.println("no row!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return result;
	}
	
	@Override
	public List<User> selectUsers(String keyword, int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<User> users = new ArrayList<User>();
		
		try {
			conn = db.getConnection();
			sql = "select "
						+ "um.id, "
						+ "um.username, "
						+ "um.`name`, "
						+ "ud.has_profile_image, "
						+ "up.file_name "
					+ "from "
						+ "user_mst um "
						+ "left outer join user_profile_image up on(up.user_id = um.id) "
					+ "where "
						+ "um.username like \"%" + keyword + "%\" and um.id != ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				user.setHas_profile_image(rs.getBoolean("has_profile_image"));
				user.setFile_name(rs.getCharacterStream("file_name") == null ? "" : rs.getString("file_name"));
				
				users.add(user);
			}
		} catch (SQLDataException e) {
			System.out.println("no row!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return users;
	}
}
