package at.fhooe.project.model;

import java.util.StringJoiner;

import static java.util.Objects.requireNonNull;

public record Customer(long id, String name, String address) {

    public Customer {
        requireNonNull(name);
        requireNonNull(address);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "{", "}")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("address='" + address + "'")
                .toString();
    }
}
