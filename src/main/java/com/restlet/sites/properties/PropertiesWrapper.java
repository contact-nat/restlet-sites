/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.properties;

import java.util.Objects;
import java.util.Properties;
import java.util.Set;

public class PropertiesWrapper {

    public static final Converter<String> AS_STRING = new Converter<String>() {
        @Override
        public String convert(Object o) {
            if (o instanceof String) {
                return (String) o;
            } else if (o != null) {
                return o.toString();
            }
            return null;
        }

        @Override
        public String getTargetType() {
            return String.class.getSimpleName();
        }
    };
    public static final Converter<Integer> AS_INTEGER = new Converter<Integer>() {
        @Override
        public Integer convert(Object o) {
            if (o instanceof Integer) {
                return (Integer) o;
            } else if (o instanceof String) {
                return Integer.parseInt((String) o);
            }
            return null;
        }

        @Override
        public String getTargetType() {
            return Integer.class.getSimpleName();
        }
    };
    public static final Converter<Boolean> AS_BOOLEAN = new Converter<Boolean>() {
        @Override
        public Boolean convert(Object o) {
            if (o instanceof Boolean) {
                return (Boolean) o;
            } else if (o instanceof String) {
                return Boolean.parseBoolean((String) o);
            }
            return null;
        }

        @Override
        public String getTargetType() {
            return Boolean.class.getSimpleName();
        }
    };
    private final Properties properties;


    public PropertiesWrapper(Properties properties) {
        Objects.requireNonNull(properties);
        this.properties = properties;
    }

    public static boolean equals(PropertiesWrapper obj1, PropertiesWrapper obj2) {
        // check size
        if (obj1.properties.size() != obj2.getProperties().size()) {
            return false;
        }

        // check keys
        Set<Object> keys = obj1.getProperties().keySet();
        if (!keys.containsAll(obj2.getProperties().keySet())) {
            return false;
        }

        // check values
        for (Object key : keys) {
            if (!Objects.equals(obj1.getProperties().get(key), obj2.getProperties().get(key))) {
                return false;
            }
        }

        return true;
    }

    public <T> T get(String name, Converter<T> converter) {
        Objects.requireNonNull(converter);
        Object o = properties.get(name);
        try {
            return converter.convert(o);
        } catch (Exception e) {
            String message = "The value of property '%s' ('%s') is not a valid %s";
            throw new IllegalArgumentException(String.format(message, name, (o == null) ? "" : o.toString(), converter.getTargetType()));
        }
    }

    public <T> T get(String name, Converter<T> converter, T defaultValue) {
        T value = get(name, converter);

        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public <T> T getRequired(String name, Converter<T> converter) {
        T value = get(name, converter);

        Objects.requireNonNull(value, String.format("'%s' property is required.", name));

        return value;
    }

    private Properties getProperties() {
        return properties;
    }

    public Set<String> getPropertiesNames() {
        return properties.stringPropertyNames();
    }

    public static abstract class Converter<T> {
        public abstract T convert(Object o);

        public abstract String getTargetType();
    }

}
