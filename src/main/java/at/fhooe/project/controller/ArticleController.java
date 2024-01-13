package at.fhooe.project.controller;

import at.fhooe.project.config.KafkaProperties;
import at.fhooe.project.model.Article;
import at.fhooe.project.util.ModelConverter;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);

    private final ReadOnlyKeyValueStore<Long, byte[]> articleStore;
    private final KafkaTemplate<byte[], byte[]> template;
    private final KafkaProperties properties;

    public ArticleController(KafkaTemplate<byte[], byte[]> template, KafkaStreams streams, KafkaProperties properties) {
        this.template = template;
        this.properties = properties;
        this.articleStore = streams.store(StoreQueryParameters.fromNameAndType(properties.getArticleStore(), QueryableStoreTypes.keyValueStore()));
    }

    @PostMapping
    public void addOrUpdateArticle(@RequestBody Article article) {

        KeyValue<Long, byte[]> pair = ModelConverter.fromArticle(article);
        template.send(properties.getArticleTopic(),
                ModelConverter.toByteArray(pair.key),
                pair.value
        );
        LOGGER.info("Added or updated article: {}", article);
    }

    @GetMapping("/{id}")
    public Article getArticle(@PathVariable long id) {
        byte[] bytes = articleStore.get(id);
        if (bytes != null) {
            Article article = ModelConverter.toArticle(KeyValue.pair(id, bytes));
            LOGGER.info("Retrieved article: {}", article);
            return article;
        }
        return null;
    }

    @GetMapping("/all")
    public List<Article> getAll() {
        List<Article> articles = new ArrayList<>();
        try (var a = articleStore.all()) {
            a.forEachRemaining(keyValue ->
                    articles.add(ModelConverter.toArticle(KeyValue.pair(keyValue.key, keyValue.value))));
        }
        LOGGER.info("Retrieve all articles");
        return articles;
    }
}
