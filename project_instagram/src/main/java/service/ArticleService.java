package service;

import java.util.List;

import entity.ArticleComment;
import request_dto.InsertArticleReqDto;
import response_dto.ArticleDetailResDto;
import response_dto.ArticleResDto;

public interface ArticleService {

	public boolean insertArticle(InsertArticleReqDto insertArticleReqDto);
	
	public List<ArticleResDto> selectArticles(int user_id);
	
	public int insertLikeArticle(int article_id, int user_id);

	public int deleteLikeArticle(int article_id, int user_id);
	
	public int insertComment(int article_id, String contents, int user_id);
	
	public int insertRelatedComment(int article_id, String contents, int user_id, int related_comment_id);
	
	public List<ArticleComment> selectRelatedComments(int related_comment_id, int user_id);
	
	public ArticleDetailResDto selectArticleDetail(int article_id, int user_id);
	
	public int insertCommentLike(int comment_id, int user_id);

	public int deleteCommentLike(int comment_id, int user_id);
}
