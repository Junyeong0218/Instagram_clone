package service;

import java.util.List;

import response_dto.ArticleResDto;

public interface ArticleService {

	public List<ArticleResDto> selectArticles(int user_id);
	
	public int insertLikeArticle(int article_id, int user_id);

	public int deleteLikeArticle(int article_id, int user_id);
	
	public int insertComment(int article_id, String contents, int user_id);
}
