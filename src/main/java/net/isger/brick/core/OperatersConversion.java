package net.isger.brick.core;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import net.isger.brick.BrickException;
import net.isger.brick.util.SimpleLoader;
import net.isger.brick.util.reflect.Conversion;

public class OperatersConversion implements Conversion {

    private OperatersConversion() {
    }

    public boolean isSupport(Class<?> clazz) {
        return Operaters.class.isAssignableFrom(clazz);
    }

    @SuppressWarnings("unchecked")
    public Object convert(Object value) {
        if (value instanceof Map) {
            // 存在缺陷
            Operaters operaters = new Operaters(null);
            Object config;
            for (Entry<String, Object> entry : ((Map<String, Object>) value)
                    .entrySet()) {
                config = SimpleLoader.load(entry.getValue(), Operater.class);
                if (config instanceof Collection<?>) {
                    throw new BrickException(
                            "Unexpected convert the operater by " + config);
                }
                operaters.add(entry.getKey(), (Operater) config);
            }
            return operaters;
        }
        throw new BrickException("Unexpected operaters conversion for " + value);
    }

}
