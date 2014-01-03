package net.isger.brick.core.inject;

/**
 * 键
 * 
 * @author issing
 * @param <T>
 */
public class Key<T> {

    private static final String FORMAT_KEY = "{type : \"%s\", name : \"%s\"}";

    final Class<T> type;

    final String name;

    final int hashCode;

    private Key(Class<T> type, String name) {
        if (type == null || name == null) {
            throw new NullPointerException("Invalid parameter");
        }
        this.type = type;
        this.name = name;
        this.hashCode = type.hashCode() * 31 + name.hashCode();
    }

    public static <T> Key<T> newInstance(Class<T> type, String name) {
        return new Key<T>(type, name);
    }

    public Class<T> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int hashCode() {
        return hashCode;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Key)) {
            return false;
        } else if (o == this) {
            return true;
        }
        Key<?> key = (Key<?>) o;
        return name.equals(key.name) && type.equals(key.type);
    }

    public String toString() {
        return String.format(FORMAT_KEY, type.getName(), name);
    }

}
