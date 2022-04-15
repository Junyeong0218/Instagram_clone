package repository;

import java.util.List;

import entity.Article;

public interface ArticleDao {

	public List<Article> selectArticleList(int user_id);
}
