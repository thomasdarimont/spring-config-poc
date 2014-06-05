package org.springframework.config;

import org.springframework.util.Assert;

public class ConfigSetting<T> {

	private static final String DEFAULT_CONFIG_SETTING_KEY = "#this['fqcn'] + '.' + #this['fieldName']";

	private final T initialValue;
	private final ConfigSource<T> configSource = new HotSwappableConfigSource<>();

	String key;

	public ConfigSetting(T initialValue) {
		this(DEFAULT_CONFIG_SETTING_KEY, initialValue);
	}

	public ConfigSetting(String key, T initialValue) {

		Assert.notNull(key, "Key must not be null!");

		this.key = key;
		this.initialValue = initialValue;
	}

	public T get() {
		return configSource.getConfigValue(key);
	}

	public String getKey() {
		return key;
	}

	public T getInitialValue() {
		return initialValue;
	}

	ConfigSource<T> getConfigSource() {
		return configSource;
	}
}
