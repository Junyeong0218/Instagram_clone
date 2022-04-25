package repository;

import java.util.List;

import entity.ArticleComment;
import entity.ArticleDetail;

public interface ArticleDao {

	public List<ArticleDetail> selectArticleList(int user_id);
	
	public int insertLikeArtice(int article_id, int user_id);

	public int deleteLikeArtice(int article_id, int user_id);
	
	public int insertComment(int article_id, String contents, int user_id);
	
	public int insertRelatedComment(int article_id, String contents, int user_id, int related_comment_id);
	
	public List<ArticleComment> selectRelatedComments(int related_comment_id, int user_id);
	
	public List<ArticleDetail> selectArticleDetail(int article_id, int user_id);
	
	public int insertCommentLike(int comment_id, int user_id);
	
	public int deleteCommentLike(int comment_id, int user_id);
}
