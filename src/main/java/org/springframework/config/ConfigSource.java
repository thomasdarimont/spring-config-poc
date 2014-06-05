package org.springframework.config;

import java.lang.reflect.Field;

public interface ConfigSource<T> {

	T getConfigValue(String key);

	void configure(Field field, ConfigSetting<?> configSetting);
}
