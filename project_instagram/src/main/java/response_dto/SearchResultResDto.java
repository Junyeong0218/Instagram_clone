package response_dto;

import java.util.List;

import entity.ArticleDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultResDto {

	private List<ArticleDetail> article_list;
	private int related_article_count;
}
