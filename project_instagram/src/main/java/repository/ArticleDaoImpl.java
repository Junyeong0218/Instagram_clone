package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

import db.DBConnectionMgr;
import entity.ArticleComment;
import entity.ArticleDetail;

public class ArticleDaoImpl implements ArticleDao {
	
	private final DBConnectionMgr db;
	
	public ArticleDaoImpl() {
		db = DBConnectionMgr.getInstance();
	}
	
	@Override
	public List<ArticleDetail> selectArticleList(int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		List<ArticleDetail> articleDetailList = new ArrayList<ArticleDetail>();
		
		try {
			conn = db.getConnection();
			sql = "select "
					+ "am.id, "
					+ "am.user_id, "
					+ "um.username as article_username, "
					+ "um.has_profile_image, "
					+ "up.file_name, "
					+ "am.feature, "
					+ "am.media_type, "
					+ "am.`contents`, "
					+ "am.is_stored, "
					+ "am.create_date, "
					
					+ "media.media_name, "
					
					+ "count(distinct ar.like_user_id) as `like_user_count`, "
					+ "ar2.like_user_id, "
					
					+ "count(distinct ac.id) as `total_commented_user_count` "
				+ "from "
					+ "article_mst am "
					+ "left outer join user_mst um on(um.id = am.user_id) "
					+ "left outer join user_profile_image up on(up.user_id = am.user_id) "
					+ "left outer join article_media media on(am.id = media.article_id) "
					+ "left outer join article_reaction ar on(am.id = ar.article_id) "
					+ "left outer join article_reaction ar2 on(am.id = ar2.article_id and ar2.like_user_id = ?) "
					+ "left outer join article_comment ac on(am.id = ac.article_id) "
				+ "where "
					+ "am.user_id != ? and "
					+ "am.user_id in(select fm.partner_user_id from follow_mst fm where fm.user_id = ?) "
				+ "order by "
					+ "am.create_date desc;";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, user_id);
			pstmt.setInt(3, user_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ArticleDetail detail = new ArticleDetail();
				detail.setArticle_id(rs.getInt("id"));
				detail.setArticle_user_id(rs.getInt("user_id"));
				detail.setArticle_username(rs.getString("article_username"));
				detail.setArticle_user_has_profile_image(rs.getInt("has_profile_image") == 1 ? true : false);
				detail.setArticle_user_file_name(rs.getString("file_name"));
				detail.setFeature(rs.getString("feature"));
				detail.setMedia_type(rs.getString("media_type"));
				detail.setContents(rs.getString("contents"));
				detail.set_stored(rs.getInt("is_stored") == 1 ? true : false);
				detail.setCreate_date(rs.getTimestamp("create_date") != null ? rs.getTimestamp("create_date").toLocalDateTime() : null);
				detail.setMedia_name(rs.getString("media_name"));
				detail.setLike_user_count(rs.getInt("like_user_count"));
				detail.setLike_flag(rs.getInt("like_user_id") == 0 ? false : true);
				detail.setTotal_commented_user_count(rs.getInt("total_commented_user_count"));
				
				articleDetailList.add(detail);
			}
			
		} catch(SQLDataException e1) {
			System.out.println("no rows");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return articleDetailList;
	}
	
	@Override
	public int insertLikeArtice(int article_id, int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "insert into article_reaction values(0, ?, ?, now(), now());";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, article_id);
			pstmt.setInt(2, user_id);
			
			result = pstmt.executeUpdate();
			
		} catch(SQLDataException e1) {
			System.out.println("no rows");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return result;
	}
	
	@Override
	public int deleteLikeArtice(int article_id, int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "delete from article_reaction where article_id = ? and like_user_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, article_id);
			pstmt.setInt(2, user_id);
			
			result = pstmt.executeUpdate();
			
		} catch(SQLDataException e1) {
			System.out.println("no rows");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return result;
	}
	
	@Override
	public int insertComment(int article_id, String contents, int user_id) {
		System.out.println("dao 진입");
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int result = 0;
		
		try {
			System.out.println("getConnection");
			conn = db.getConnection();
			sql = "insert into article_comment values(0, ?, ?, ?, now(), now(), 0, null, 0, null);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, article_id);
			pstmt.setInt(2, user_id);
			pstmt.setString(3,  contents);
			System.out.println("pstmt end");
			result = pstmt.executeUpdate();
			System.out.println("executed!");
		} catch(SQLDataException e1) {
			System.out.println("no rows");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return result;
	}
	
	@Override
	public int insertRelatedComment(int article_id, String contents, int user_id, int related_comment_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "insert into article_comment values(0, ?, ?, ?, now(), now(), 0, null, 1, ?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, article_id);
			pstmt.setInt(2, user_id);
			pstmt.setString(3,  contents);
			pstmt.setInt(4, related_comment_id);
			
			result = pstmt.executeUpdate();
			
		} catch(SQLDataException e1) {
			System.out.println("no rows");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return result;
	}
	
	@Override
	public List<ArticleComment> selectRelatedComments(int related_comment_id, int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<ArticleComment> comments = new ArrayList<ArticleComment>();
		
		try {
			conn = db.getConnection();
			sql = "SELECT "
						+ "ac.id, "
						+ "ac.article_id, "
						+ "ac.commented_user_id, "
						+ "um.username, "
						+ "um.has_profile_image, "
						+ "up.file_name, "
						+ "ac.`contents`, "
						+ "ac.create_date, "
						
						+ "COUNT(acr.id) AS comment_like_user_count, "
						+ "acr2.like_user_id "
					+ "FROM "
						+ "article_comment ac "
						+ "LEFT OUTER JOIN user_mst um ON(um.id = ac.commented_user_id) "
						+ "LEFT OUTER JOIN user_profile_image up ON(up.user_id = ac.commented_user_id) "
						+ "LEFT OUTER JOIN article_comment_reaction acr ON(acr.article_comment_id = ac.id) "
						+ "LEFT OUTER JOIN article_comment_reaction acr2 ON(acr2.article_comment_id = ac.id AND acr2.like_user_id = ?) "
					+ "WHERE "
						+ "ac.related_comment_id = ? "
					+ "GROUP BY "
						+ "ac.id;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, related_comment_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ArticleComment comment = new ArticleComment();
				comment.setId(rs.getInt("id"));
				comment.setArticle_id(rs.getInt("article_id"));
				comment.setUser_id(rs.getInt("commented_user_id"));
				comment.setUsername(rs.getString("username"));
				comment.setHas_profile_image(rs.getInt("has_profile_image") == 1 ? true : false);
				comment.setFile_name(rs.getString("file_name"));
				comment.setContents(rs.getString("contents"));
				comment.setCreate_date(rs.getTimestamp("create_date").toLocalDateTime());
				comment.setComment_like_user_count(rs.getInt("comment_like_user_count"));
				comment.setLike_flag(rs.getInt("like_user_id") == 0 ? false : true);
				
				comments.add(comment);
			}
		} catch(SQLDataException e1) {
			System.out.println("no rows");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return comments;
	}
	
	@Override
	public List<ArticleDetail> selectArticleDetail(int article_id, int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<ArticleDetail> articleDetailList = new ArrayList<ArticleDetail>();
		
		try {
			conn = db.getConnection();
			sql = "select "
						+ "am.id, "
						+ "am.user_id, "
						+ "um.username, "
						+ "um.has_profile_image, "
						+ "up.file_name, "
						+ "am.feature, "
						+ "am.media_type, "
						+ "am.`contents`, "
						+ "am.create_date, "

						+ "media.media_name, "
						
						+ "count(distinct ar.like_user_id) as like_user_count, "
						+ "ar2.like_user_id, "
						
						+ "ac.id as comment_id, "
						+ "ac.commented_user_id, "
						+ "um2.username as commented_username, "
						+ "um2.has_profile_image as commented_user_has_profile_image, "
						+ "up2.file_name as commented_user_file_name, "
						+ "ac.`contents` as comment_contents, "
						+ "ac.create_date as comment_create_date, "
						+ "count(distinct ac2.id) as related_comment_count, "
						+ "count(distinct acr.like_user_id) as comment_like_user_count,"
						+ "acr2.like_user_id as comment_like_flag "
					+ "from "
						+ "article_mst am "
						+ "left outer join user_mst um on(um.id = am.user_id) "
						+ "left outer join user_profile_image up on(up.user_id = um.id) "
						+ "left outer join article_media media on(am.id = media.article_id) "
						+ "left outer join article_reaction ar on(am.id = ar.article_id) "
						+ "left outer join article_reaction ar2 on(am.id = ar.article_id and ar.like_user_id = ?) "
						+ "left outer join article_comment ac on(am.id = ac.article_id and ac.related_flag = 0) "
						+ "left outer join user_mst um2 on(um2.id = ac.commented_user_id) "
						+ "left outer join user_profile_image up2 on(up2.user_id = ac.commented_user_id) "
						+ "left outer join article_comment ac2 on(ac2.related_comment_id = ac.id and ac2.related_flag = 1) "
						+ "left outer join article_comment_reaction acr on(acr.article_comment_id = ac.id) "
						+ "left outer join article_comment_reaction acr2 on(acr2.article_comment_id = ac.id and acr2.like_user_id = ?) "
					+ "where "
						+ "am.id = ? "
					+ "group by "
						+ "comment_id "
					+ "order by "
						+ "comment_like_user_count desc, "
						+ "comment_create_date asc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, user_id);
			pstmt.setInt(3,  article_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ArticleDetail detail = new ArticleDetail();
				detail.setArticle_id(rs.getInt("id"));
				detail.setArticle_user_id(rs.getInt("user_id"));
				detail.setArticle_username(rs.getString("username"));
				detail.setArticle_user_has_profile_image(rs.getInt("has_profile_image") == 1 ? true : false);
				detail.setArticle_user_file_name(rs.getString("file_name"));
				detail.setFeature(rs.getString("feature"));
				detail.setMedia_type(rs.getString("media_type"));
				detail.setContents(rs.getString("contents"));
				detail.setCreate_date(rs.getTimestamp("create_date").toLocalDateTime());
				detail.setLike_user_count(rs.getInt("like_user_count"));
				detail.setLike_flag(rs.getInt("like_user_id") == 0 ? false : true);
				detail.setMedia_name(rs.getString("media_name"));
				detail.setComment_id(rs.getInt("comment_id"));
				detail.setCommented_user_id(rs.getInt("commented_user_id"));
				detail.setCommented_username(rs.getString("commented_username"));
				detail.setCommented_user_has_profile_image(rs.getInt("commented_user_has_profile_image") == 1 ? true : false);
				detail.setCommented_user_file_name(rs.getString("commented_user_file_name"));
				detail.setComment_contents(rs.getString("comment_contents"));
				detail.setComment_create_date(rs.getTimestamp("comment_create_date") != null ? rs.getTimestamp("comment_create_date").toLocalDateTime() : null);
				detail.setRelated_comment_count(rs.getInt("related_comment_count"));
				detail.setComment_like_user_count(rs.getInt("comment_like_user_count"));
				detail.setComment_like_flag(rs.getInt("comment_like_flag") == 0 ? false : true);
				System.out.println(detail);
				articleDetailList.add(detail);
			}
			
		} catch(SQLDataException e1) {
			System.out.println("no rows");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt, rs);
		}
		
		return articleDetailList;
	}
	
	@Override
	public int insertCommentLike(int comment_id, int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "insert into article_comment_reaction values(0, ?, ?, now(), now());";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, comment_id);
			pstmt.setInt(2, user_id);
			
			result = pstmt.executeUpdate();
			
		} catch(SQLDataException e1) {
			System.out.println("no rows");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return result;
	}
	
	@Override
	public int deleteCommentLike(int comment_id, int user_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int result = 0;
		
		try {
			conn = db.getConnection();
			sql = "delete from article_comment_reaction where article_comment_id = ? and like_user_id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, comment_id);
			pstmt.setInt(2, user_id);
			
			result = pstmt.executeUpdate();
			
		} catch(SQLDataException e1) {
			System.out.println("no rows");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.freeConnection(conn, pstmt);
		}
		
		return result;
	}
}
