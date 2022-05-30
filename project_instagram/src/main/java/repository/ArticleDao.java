package repository;

import java.util.List;

import entity.Article;
import entity.ArticleComment;
import entity.ArticleDetail;
import entity.ArticleMedia;

public interface ArticleDao {
	
	public int insertArticle(Article article);
	
	public int insertArticleMedias(List<ArticleMedia> media_list);

	public int updateArticle(Article article);
	
	public int deleteArticle(Article article);
	
	public List<ArticleDetail> selectArticleList(int user_id, int page_indicator);
	
	public int insertLikeArtice(int article_id, int user_id);

	public int deleteLikeArtice(int article_id, int user_id);
	
	public int insertComment(int article_id, String contents, int user_id);
	
	public int insertRelatedComment(int article_id, String contents, int user_id, int related_comment_id);
	
	public int deleteComment(ArticleComment comment);
	
	public List<ArticleComment> selectRelatedComments(int related_comment_id, int user_id);
	
	public List<ArticleDetail> selectArticleDetail(int article_id, int user_id);
	
	public int insertCommentLike(int comment_id, int user_id);
	
	public int deleteCommentLike(int comment_id, int user_id);
}
