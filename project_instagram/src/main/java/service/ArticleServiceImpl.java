package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entity.Article;
import entity.ArticleComment;
import entity.ArticleDetail;
import entity.ArticleMedia;
import repository.ArticleDao;
import request_dto.InsertArticleReqDto;
import response_dto.ArticleDetailResDto;
import response_dto.ArticleResDto;

public class ArticleServiceImpl implements ArticleService {
	
	private ArticleDao articleDao;
	
	public ArticleServiceImpl(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}
	
	@Override
	public boolean insertArticle(InsertArticleReqDto insertArticleReqDto) {
		Article article = insertArticleReqDto.toArticleEntity();
		int article_id = articleDao.insertArticle(article);
		
		if(article_id == 0) return false;
		insertArticleReqDto.setArticle_id(article_id);
		
		List<ArticleMedia> media_list = insertArticleReqDto.toArticleMediaList();
		
		int result = articleDao.insertArticleMedias(media_list);
		System.out.println(result);
		if(result > 0) return true;
		else					 return false;
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
				dto.setHas_profile_image(detail.isArticle_user_has_profile_image());
				dto.setFile_name(detail.getArticle_user_file_name());
				dto.setFeature(detail.getFeature());
				dto.setContents(detail.getContents());
				dto.setStored(detail.is_stored());
				dto.setArticle_create_date(detail.getCreate_date());
				dto.setLike_flag(detail.isLike_flag());
				
				dto.setMedia_list(new ArrayList<ArticleMedia>());
				ArticleMedia media = new ArticleMedia();
				media.setMedia_type(detail.getMedia_type());
				media.setMedia_name(detail.getMedia_name());
				dto.getMedia_list().add(media);
				
				dto.setTotal_like_count(detail.getLike_user_count());
				dto.setTotal_commented_user_count(detail.getTotal_commented_user_count());
				
				compressedList.add(dto);
			} else {
				List<ArticleMedia> mediaNameList = dto.getMedia_list();
				if(!mediaNameList.contains(detail.getMedia_name())) {
					ArticleMedia media = new ArticleMedia();
					media.setMedia_type(detail.getMedia_type());
					media.setMedia_name(detail.getMedia_name());
					dto.getMedia_list().add(media);
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
		System.out.println("service 진입");
		return articleDao.insertComment(article_id, contents, user_id);
	}
	
	@Override
	public int insertRelatedComment(int article_id, String contents, int user_id, int related_comment_id) {
		return articleDao.insertRelatedComment(article_id, contents, user_id, related_comment_id);
	}
	
	@Override
	public List<ArticleComment> selectRelatedComments(int related_comment_id, int user_id) {
		return articleDao.selectRelatedComments(related_comment_id, user_id);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public ArticleDetailResDto selectArticleDetail(int article_id, int user_id) {
		List<ArticleDetail> articleDetailList = articleDao.selectArticleDetail(article_id, user_id);
		ArticleDetailResDto articleDetailResDto = new ArticleDetailResDto();
		
		for(int i = 0; i < articleDetailList.size(); i++) {
			System.out.println(articleDetailList.get(i));
			ArticleDetail detail = articleDetailList.get(i);
			if(i == 0) {
				articleDetailResDto.setId(detail.getArticle_id());
				articleDetailResDto.setUser_id(detail.getArticle_user_id());
				articleDetailResDto.setUsername(detail.getArticle_username());
				articleDetailResDto.setHas_profile_image(detail.isArticle_user_has_profile_image());
				articleDetailResDto.setFile_name(detail.getArticle_user_file_name());
				articleDetailResDto.setFeature(detail.getFeature());
				articleDetailResDto.setContents(detail.getContents());
				articleDetailResDto.setArticle_create_date(detail.getCreate_date());
				articleDetailResDto.setLike_flag(detail.isLike_flag());
				articleDetailResDto.setLike_user_count(detail.getLike_user_count());
				
				articleDetailResDto.setMedia_list(new ArrayList<ArticleMedia>());
				articleDetailResDto.setArticle_comment_list(new ArrayList<ArticleComment>());
			}
			List<ArticleMedia> media_list = articleDetailResDto.getMedia_list();
			ArticleMedia media = new ArticleMedia();
			media.setArticle_id(detail.getArticle_id());
			media.setMedia_type(detail.getMedia_type());
			media.setMedia_name(detail.getMedia_name());
			media_list.add(media);
			
			List<ArticleComment> article_comment_list = articleDetailResDto.getArticle_comment_list();
			if(!article_comment_list.contains(detail.getComment_id())) {
				ArticleComment comment = new ArticleComment();
				comment.setId(detail.getComment_id());
				comment.setArticle_id(detail.getArticle_id());
				comment.setUser_id(detail.getCommented_user_id());
				comment.setUsername(detail.getCommented_username());
				comment.setHas_profile_image(detail.isCommented_user_has_profile_image());
				comment.setFile_name(detail.getCommented_user_file_name());
				comment.setContents(detail.getComment_contents());
				comment.setCreate_date(detail.getComment_create_date());
				comment.setRelated_comment_count(detail.getRelated_comment_count());
				comment.setComment_like_user_count(detail.getComment_like_user_count());
				comment.setLike_flag(detail.isComment_like_flag());
				
				article_comment_list.add(comment);
			}
			
		}
		
		List<ArticleMedia> media_list = articleDetailResDto.getMedia_list();
		media_list.sort(new Comparator<ArticleMedia>() {
			@Override
			public int compare(ArticleMedia o1, ArticleMedia o2) {
				int prevIndex = Integer.parseInt(o1.getMedia_name().substring(5, 7));
				int nextIndex = Integer.parseInt(o2.getMedia_name().substring(5,  7));
				return prevIndex - nextIndex;
			}
		});
		
		return articleDetailResDto;
	}
	
	@Override
	public int insertCommentLike(int comment_id, int user_id) {
		return articleDao.insertCommentLike(comment_id, user_id);
	}
	
	@Override
	public int deleteCommentLike(int comment_id, int user_id) {
		return articleDao.deleteCommentLike(comment_id, user_id);
	}
}
