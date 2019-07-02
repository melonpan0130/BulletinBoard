package com.example.Board;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/articles")
public class ArticleController {
	// 페이지당 게시물 개수
	private Integer DEFAULT_PAGE_COUNT = 5;
	@Autowired private ArticleRepository repository;
	
	// 게시글 작성 폼
	@GetMapping("/write")
	public String getWriteArticle() {
		return "article/write";
	}
	
	// 게시글 작성 핸들러 (게시글 커맨드 객체를 그대로 저장)
    // 비어있는 Path값의 의미 (그냥 prefix(= /articles) 주소를 사용)
	@PostMapping("")
	public String postArticle(Article article) {
		repository.save(article);
		
		return "redirect:/articles";
	}
	
	// 게시글 페이지
	@GetMapping("/{id}")
	public String getArticle(@PathVariable("id") Integer id, Model m) {
		Optional<Article> article = repository.findById(id);
		m.addAttribute("article", article.isPresent() ? article.get() : null);
		m.addAttribute("comments", article.isPresent() ? article.get().getComments() : null);
		
		return "article/article";
	}
	
	// 게시글의 댓글 생성 핸들러 (댓글 커맨드 객체를 그대로 저장)
	@PostMapping("/{id}/comments")
	public String getArticle(@PathVariable("id") Integer id, Comment comment) {
		Article article = repository.findById(id).get();
		
		// https://stackoverflow.com/questions/46218606/jpa-adding-child-entity-to-parent-entity
		// Then since your relation is bidirectional its logical to have each side of the relation mapped to the other, hence you will need to have reference of each side in the other side
        // 명시적으로 부모, 자식간의 쌍방향(bidirectional) 관계 지정 필요
		comment.setArticle(article); // 부모 관계 지정 (article 객체 저장)
		article.getComments().add(comment); // 자식 관계 지정 (리스트에 추가)
        
		repository.save(article);
		
		return "redirect:/articles/" + id;
	}

	// 게시글 리스팅
	@GetMapping(value="")
	public String getArticleList(Model m, 
			@RequestParam(value="page", defaultValue="1") Integer page,
			@RequestParam(value="order", defaultValue="desc") String order) {
        // PageRequest의 of 메소드를 사용하여 PageRequest 객체 생성
        // 전달 인자 : (0부터 시작하는) 페이지 번호, 페이지당 게시글 개수, 정렬 방법(DESC, ASC), 정렬 칼럼
		m.addAttribute("articles", repository.findAll(PageRequest.of(page-1, DEFAULT_PAGE_COUNT, 
				order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "id")));
		m.addAttribute("count", (int) Math.ceil(repository.count() / DEFAULT_PAGE_COUNT.doubleValue()));
		m.addAttribute("order", order);
		
		return "article/list";
	}
}