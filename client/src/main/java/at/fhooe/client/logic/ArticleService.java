package at.fhooe.client.logic;

import at.fhooe.client.model.Article;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Service
public class ArticleService {

    public List<Article> generateRandomArticles(int count) {
        Faker faker = new Faker(new Locale("de-AT"));

        List<Article> articles = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            Article article = new Article();
            article.setId(new Random().nextLong());
            article.setName(faker.commerce().productName());
            article.setPrice(Double.parseDouble(faker.commerce().price().replace(',', '.')));
            articles.add(article);
        }
        return articles;
    }
}
