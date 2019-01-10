
package com.gupao.edu.vip.lion.api.spi;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class SpiLoader {
    private static final Map<String, Object> CACHE = new ConcurrentHashMap<>();

    public static void clear() {
        CACHE.clear();
    }

    public static <T> T load(Class<T> clazz) {
        return load(clazz, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(Class<T> clazz, String name) {
        String key = clazz.getName();
        Object o = CACHE.get(key);
        if (o == null) {
            T t = load0(clazz, name);
            if (t != null) {
                CACHE.put(key, t);
                return t;
            }
        } else if (clazz.isInstance(o)) {
            return (T) o;
        }
        return load0(clazz, name);
    }

    public static <T> T load0(Class<T>  clazz, String name) {
        ServiceLoader<T> factories = ServiceLoader.load(clazz);
        T t = filterByName(factories, name);

        if (t == null) {
            factories = ServiceLoader.load(clazz, SpiLoader.class.getClassLoader());
            t = filterByName(factories, name);
        }

        if (t != null) {
            return t;
        } else {
            throw new IllegalStateException("Cannot find META-INF/services/" + clazz.getName() + " on classpath");
        }
    }

    private static <T> T filterByName(ServiceLoader<T> factories, String name) {
        Iterator<T> it = factories.iterator();
        if (name == null) {
            List<T> list = new ArrayList<T>(2);
            while (it.hasNext()) {
                list.add(it.next());
            }
            if (list.size() > 1) {
                list.sort((o1, o2) -> {
                    Spi spi1 = o1.getClass().getAnnotation(Spi.class);
                    Spi spi2 = o2.getClass().getAnnotation(Spi.class);
                    int order1 = spi1 == null ? 0 : spi1.order();
                    int order2 = spi2 == null ? 0 : spi2.order();
                    return order1 - order2;
                });
            }
            if (list.size() > 0) return list.get(0);
        } else {
            while (it.hasNext()) {
                T t = it.next();
                if (name.equals(t.getClass().getName()) ||
                        name.equals(t.getClass().getSimpleName())) {
                    return t;
                }
            }
        }
        return null;
    }
}
