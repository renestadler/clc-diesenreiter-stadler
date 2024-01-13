package at.fhooe.project.model;

import java.util.StringJoiner;

import static java.util.Objects.requireNonNull;

public record Article(long id, String name, double price) {

    public Article {
        requireNonNull(name);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "{", "}")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("price=" + price)
                .toString();
    }
}
