package top.banner.lib.curl;

import java.util.Objects;

public class Query {

    private final String name;
    private final String value;

    public Query(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Query header = (Query) o;

        if (!Objects.equals(name, header.name)) {
            return false;
        }
        return Objects.equals(value, header.value);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
