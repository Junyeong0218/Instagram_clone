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
						+ "um.has_profile_image, "
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
						+ "left outer join user_profile_image up on(up.user_id = lsr.searched_user_id) "
						+ "left outer join follow_mst fm on(fm.partner_user_id = lsr.searched_user_id) "
						+ "left outer join hash_tag_mst htm on(htm.id = lsr.hash_tag_id) "
						+ "left outer join follow_mst fm2 on(fm2.followed_hash_tag_id = lsr.hash_tag_id) "
					+ "where "
						+ "lsr.user_id = ? "
					+ "order by "
						+ "lsr.create_date desc; ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				LatestSearchDetail detail = new LatestSearchDetail();
				detail.setId(rs.getInt("id"));
				detail.setUser_id(rs.getInt("user_id"));
				detail.setSearched_user_id(rs.getInt("searched_user_id"));
				detail.setUsername(rs.getString("username"));
				detail.setName(rs.getString("name"));
				detail.setHas_profile_image(rs.getBoolean("has_profile_image"));
				detail.setFile_name(rs.getString("file_name"));
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
						+ "um.has_profile_image, "
						+ "up.file_name, "
						+ "fm.id AS user_follow_flag, "

						+ "htm.id AS hash_tag_id, "
						+ "htm.tag_name, "
						+ "fm2.id AS hash_tag_follow_flag "
					+ "FROM "
						+ "user_mst um "
						+ "LEFT OUTER JOIN user_profile_image up ON(up.user_id = um.id) "
						+ "LEFT OUTER JOIN follow_mst fm ON(fm.partner_user_id = um.id AND fm.user_id = ?) "
						+ "LEFT OUTER JOIN hash_tag_mst htm ON(htm.tag_name LIKE \"%" + keyword + "%\") "
						+ "LEFT OUTER JOIN follow_mst fm2 ON(fm2.followed_hash_tag_id = htm.id AND fm2.user_id = ?) "
					+ "WHERE "
						+ "um.username LIKE \"%" + keyword + "%\" AND um.id != ? "
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
				result.setFile_name(rs.getString("file_name"));
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
						
						+ "COUNT(distinct htl.id) AS related_article_count "
					+ "FROM  "
						+ "hash_tag_logs htl "
						+ "LEFT OUTER JOIN article_mst am ON(am.id = htl.article_id) "
						+ "LEFT OUTER JOIN article_media media ON(media.article_id = am.id AND media.media_name LIKE \"%01%\") "
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
}
