package com.ufranco.springredisexample.repositories;

import com.ufranco.springredisexample.entities.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends CrudRepository<Article, String> {

}
