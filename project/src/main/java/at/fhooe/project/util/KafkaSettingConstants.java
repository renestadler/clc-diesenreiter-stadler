package at.fhooe.project.util;

public final class KafkaSettingConstants {
    private KafkaSettingConstants() {
        throw new UnsupportedOperationException("static utility class");
    }

    public static final String INVOICE_DETAIL_TOPIC = "invoice.detail.topic";
    public static final String INVOICE_TOPIC = "invoice.topic";
    public static final String CUSTOMER_TOPIC = "customer.topic";
    public static final String ARTICLE_TOPIC = "article.topic";
    public static final String CUSTOMER_STORE = "customer.store";
    public static final String ARTICLE_STORE = "article.store";
}
