package service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entity.ArticleComment;
import entity.ArticleDetail;
import repository.ArticleDao;
import response_dto.ArticleResDto;

public class ArticleServiceImpl implements ArticleService {
	
	private ArticleDao articleDao;
	
	public ArticleServiceImpl(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public List<ArticleResDto> selectArticles(int user_id) {
		List<ArticleDetail> articleList = articleDao.selectArticleList(user_id);
		Set<Integer> idForEvaluation = new HashSet<Integer>();
		
		List<ArticleResDto> compressedList = new ArrayList<ArticleResDto>();
		
		ArticleResDto dto = null;
		for(int i=0; i < articleList.size(); i++) {
			ArticleDetail detail = articleList.get(i);
			if(idForEvaluation.add(detail.getArticle_id())) {
				dto = null;
				dto = new ArticleResDto();
				dto.setId(detail.getArticle_id());
				dto.setUser_id(detail.getArticle_user_id());
				dto.setUsername(detail.getArticle_username());
				dto.setFeature(detail.getFeature());
				dto.setMedia_type(detail.getMedia_type());
				dto.setContents(detail.getContents());
				dto.setStored(detail.is_stored());
				dto.setArticle_create_date(detail.getCreate_date());
				dto.setLike_flag(detail.isLike_flag());
				
				dto.setMedia_name_list(new ArrayList<String>());
				dto.getMedia_name_list().add(detail.getMedia_name());
				
				dto.setArticle_comment_list(new ArrayList<ArticleComment>());
				ArticleComment comment = new ArticleComment();
				comment.setId(detail.getComment_id());
				comment.setArticle_id(detail.getArticle_id());
				comment.setCommented_user_id(detail.getCommented_user_id());
				comment.setCommented_username(detail.getCommented_username());
				comment.setContents(detail.getContents());
				comment.setComment_like_user_count(detail.getComment_like_user_count());
				dto.getArticle_comment_list().add(comment);
				
				dto.setTotal_like_count(detail.getLike_user_count());
				dto.setTotal_commented_user_count(detail.getTotal_commented_user_count());
				
				compressedList.add(dto);
			} else {
				List<String> mediaNameList = dto.getMedia_name_list();
				if(!mediaNameList.contains(detail.getMedia_name())) {
					mediaNameList.add(detail.getMedia_name());
				}
				
				List<ArticleComment> articleCommentList = dto.getArticle_comment_list();
				boolean isExist = false;
				for(ArticleComment each : articleCommentList) {
					if(each.equals(detail.getComment_id()))  {
						isExist = true;
					}
				}
				if(isExist == false) {
					ArticleComment comment = new ArticleComment();
					comment.setId(detail.getComment_id());
					comment.setArticle_id(detail.getArticle_id());
					comment.setCommented_user_id(detail.getCommented_user_id());
					comment.setCommented_username(detail.getCommented_username());
					comment.setContents(detail.getContents());
					comment.setComment_like_user_count(detail.getComment_like_user_count());
					
					articleCommentList.add(comment);
				}
			}
		}
		
		return compressedList;
	}
	
	@Override
	public int insertLikeArticle(int article_id, int user_id) {
		return articleDao.insertLikeArtice(article_id, user_id);
	}
	
	@Override
	public int deleteLikeArticle(int article_id, int user_id) {
		return articleDao.deleteLikeArtice(article_id, user_id);
	}
	
	@Override
	public int insertComment(int article_id, String contents, int user_id) {
		return articleDao.insertComment(article_id, contents, user_id);
	}
}
