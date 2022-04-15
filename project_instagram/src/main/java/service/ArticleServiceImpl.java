package service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entity.Article;
import repository.ArticleDao;
import response_dto.ArticleResDto;

public class ArticleServiceImpl implements ArticleService {
	
	private ArticleDao articleDao;
	
	public ArticleServiceImpl(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}
	
	@Override
	public List<ArticleResDto> selectArticles(int user_id) {
		List<Article> articleList = articleDao.selectArticleList(user_id);
		Set<Integer> idForEvaluation = new HashSet<Integer>();
		
		List<ArticleResDto> compressedList = new ArrayList<ArticleResDto>();
		
		ArticleResDto dto = null;
		for(int i=0; i < articleList.size(); i++) {
			Article article = articleList.get(i);
			if(idForEvaluation.add(article.getId())) {
				dto = null;
				dto = new ArticleResDto();
				dto.setId(article.getId());
				dto.setUser_id(article.getUser_id());
				dto.setUsername(article.getUsername());
				dto.setFeature(article.getFeature());
				dto.setMedia_type(article.getMedia_type());
				dto.setStored(article.isStored());
				dto.setMedia_name_list(new ArrayList<String>());
				dto.getMedia_name_list().add(article.getMedia_name());
				
				compressedList.add(dto);
			} else {
				dto.getMedia_name_list().add(article.getMedia_name());
			}
		}
		
		return compressedList;
	}
}
