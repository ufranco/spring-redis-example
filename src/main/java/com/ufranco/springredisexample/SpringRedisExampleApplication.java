package com.ufranco.springredisexample;

import com.github.javafaker.Faker;
import com.ufranco.springredisexample.entities.Article;
import com.ufranco.springredisexample.entities.Article.Type;
import com.ufranco.springredisexample.entities.Author;
import com.ufranco.springredisexample.repositories.ArticleRepository;
import com.ufranco.springredisexample.repositories.AuthorRepository;
import io.redisearch.Schema;
import io.redisearch.client.Client;
import io.redisearch.client.IndexDefinition;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;
import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.exceptions.JedisDataException;

@SpringBootApplication
@EnableRedisRepositories
public class SpringRedisExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRedisExampleApplication.class, args);
	}

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		return new JedisConnectionFactory();
	}

	@Bean
	RedisTemplate<String, String> redisTemplate() {
		val template = new RedisTemplate<String,String>();

		template.setConnectionFactory(jedisConnectionFactory());
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());

		return template;
	}

/*
	UNCOMMENT TO CREATE FAKE DATA
	@Bean
	CommandLineRunner createData(
			ArticleRepository articleRepository,
			AuthorRepository authorRepository,
			JedisConnectionFactory jedisConnectionFactory
	) {
		return args -> {
			val faker = new Faker();

			if(articleRepository.count() == 0) populateWithAuthors(authorRepository, faker);

			System.out.println(authorRepository.count());

			populateWithArticles(articleRepository, faker);
		};
	}

	private static String getFakeArticleTitle(Faker faker) {
		val keys = titles.keySet();

		val random = Double.valueOf(keys.size() * Math.random()).longValue();

		val key = keys.stream()
				.skip(random)
				.findAny()
				.orElse(Type.COOK);

		var param1 = "";
		var param2 = "";

		switch (key) {
			case INFLUENCE -> {
				param1 = faker.ancient().god();
				param2 = faker.food().dish();
			}
			case LEARN -> {
				param1 = faker.artist().name();
				param2 = faker.backToTheFuture().character();
			}
			case TEACH -> {
				param1 = faker.animal().name();
				param2 = faker.aviation().aircraft();
			}
			case COOK -> {
				param1 = faker.food().dish();
				param2 = StringUtils.capitalize(faker.animal().name());
			}
		}

		return String.format(titles.get(key), param1, param2);

	}

	static Map<Type, String> titles = Map.of(
			Type.INFLUENCE, "The influence of %s on %s",
			Type.LEARN, "How I learned %s from %s",
			Type.COOK, "How to cook %s with %s",
			Type.TEACH, "Teach your %s how to pilot a %s"
	);

	private void populateWithArticles(ArticleRepository articleRepository, Faker faker) {

		val artTitles = new HashSet<String>();

		IntStream.range(0, 40).forEach(i -> {
			var articleTitle = getFakeArticleTitle(faker);
			while (artTitles.contains(articleTitle)) {
				articleTitle = getFakeArticleTitle(faker);
			}

			artTitles.add(articleTitle);

			val article = Article.builder()
					.title(articleTitle)
					.price(Double.parseDouble(faker.commerce().price()))
					.authors(getFakeAuthors())
					.build();

			articleRepository.save(article);
		});
	}

	private void populateWithAuthors(AuthorRepository authorRepository, Faker faker) {
		IntStream.range(0, 20).forEach(
				n -> authorRepository.save(
						Author.builder()
								.name(faker.book().author())
								.build()
				)
		);
	}

	private Set<Author> getFakeAuthors() {
		val random = new Random();

		val result = new HashSet<Author>();
		IntStream.range(0, (random.nextInt(2) + 1))
				.forEach(j -> {
					val authorId = redisTemplate().opsForSet()
							.randomMember(Author.class.getName());

					result.add(Author.builder()
							.id(authorId)
							.name(null)
							.build()
					);
				});

		return result;
	}

	@Bean
	CommandLineRunner createSearchIndexes(JedisConnectionFactory jedisConnectionFactory) {
		return args -> {
			val searchIndexName = "article-idx";

			try (val client = new Client(
					searchIndexName,
					jedisConnectionFactory.getHostName(),
					jedisConnectionFactory.getPort()
			)) {
				val schema = new Schema()
						.addSortableTextField("title", 1.0)
						.addSortableNumericField("price");

				val indexDefinition = new IndexDefinition().setPrefixes(
						String.format("%s:", Article.class.getName())
				);

				client.createIndex(
						schema,
						Client.IndexOptions
								.defaultOptions()
								.setDefinition(indexDefinition)
				);

			} catch (JedisDataException e) {
				e.printStackTrace();
			}
		};
	}

*/


}
