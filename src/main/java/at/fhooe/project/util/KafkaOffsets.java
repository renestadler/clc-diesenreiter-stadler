package at.fhooe.project.util;

public class KafkaOffsets {

    private KafkaOffsets() {
        throw new UnsupportedOperationException("static utility class");
    }

    public static class Article {
        private Article() {
            throw new UnsupportedOperationException("static utility class");
        }

        // Key components
        public static final int ARTICLE_ID_OFFSET = 0;

        // Value components
        public static final int PRICE_OFFSET = 0;
        public static final int NAME_LENGTH_OFFSET = PRICE_OFFSET + Long.BYTES;
        public static final int NAME_OFFSET = NAME_LENGTH_OFFSET + Integer.BYTES;
    }

    public static class Customer {
        private Customer() {
            throw new UnsupportedOperationException("static utility class");
        }

        // Key components
        public static final int CUSTOMER_ID_OFFSET = 0;

        // Value components
        public static final int NAME_LENGTH_OFFSET = 0;
        public static final int NAME_OFFSET = NAME_LENGTH_OFFSET + Integer.BYTES;
        public static final int ADDRESS_LENGTH_OFFSET = 0;
        public static final int ADDRESS_OFFSET = ADDRESS_LENGTH_OFFSET + Integer.BYTES;
    }

    public static class Invoice {
        private Invoice() {
            throw new UnsupportedOperationException("static utility class");
        }

        // Key components
        public static final int CUSTOMER_ID_OFFSET = 0;
        public static final int INVOICE_ID_OFFSET = CUSTOMER_ID_OFFSET + Long.BYTES;

        // Value components
        public static final int ADDRESS_LENGTH_OFFSET = 0;
        public static final int ADDRESS_OFFSET = ADDRESS_LENGTH_OFFSET + Integer.BYTES;
        public static final int ENTRIES_LENGTH_OFFSET = 0;
        public static final int ARTICLE_ID_OFFSET = 0;
        public static final int AMOUNT_OFFSET = ARTICLE_ID_OFFSET + Long.BYTES;
    }
}
