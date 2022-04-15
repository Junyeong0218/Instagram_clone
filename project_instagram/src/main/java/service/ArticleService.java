package service;

import java.util.List;

import response_dto.ArticleResDto;

public interface ArticleService {

	public List<ArticleResDto> selectArticles(int user_id);
}
