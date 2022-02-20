package com.ufranco.springredisexample.entities;

import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@ToString
@RedisHash("Article")
public class Article {

  @Id
  private String id;

  private String title;

  private Double price;

  @Reference
  private Set<Author> authors;

  public void addAuthor(Author author) {
    authors.add(author);
  }

  public enum Type {
    INFLUENCE,
    LEARN,
    COOK,
    TEACH
  }
}
