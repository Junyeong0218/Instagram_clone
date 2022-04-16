package repository;

import java.util.List;

import entity.ArticleDetail;

public interface ArticleDao {

	public List<ArticleDetail> selectArticleList(int user_id);
	
	public int insertLikeArtice(int article_id, int user_id);

	public int deleteLikeArtice(int article_id, int user_id);
	
	public int insertComment(int article_id, String contents, int user_id);
}
