package com.ufranco.springredisexample;

import com.ufranco.springredisexample.entities.Article;
import com.ufranco.springredisexample.repositories.ArticleRepository;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleRepository repository;

  @GetMapping("/random")
  Article getRandomArticle() {
    val articles = repository.count();

    val idx = Double.valueOf(Math.random() * articles)
        .longValue();

    return StreamSupport.stream(repository.findAll().spliterator(),false)
        .skip(idx)
        .findFirst()
        .orElse(null);

  }

  @GetMapping("/{page}")
  List<Article> getArticles(@PathVariable Long page) {
    return StreamSupport.stream(repository.findAll().spliterator(),false)
        .skip(page * 15)
        .limit(page * 15)
        .toList();

  }

  @PostMapping
  Article createArticle(@RequestBody Article article) {
    System.out.println(article);
    return repository.save(article);
  }

}
