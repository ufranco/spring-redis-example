package com.ufranco.springredisexample.repositories;

import com.ufranco.springredisexample.entities.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, String> {

}
