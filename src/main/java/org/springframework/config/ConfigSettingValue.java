package org.springframework.config;

import java.io.Serializable;

public class ConfigSettingValue implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String key;
	private Object value;
	private final Object defaultValue;
	private final Class<?> type;

	public ConfigSettingValue(String key, Object value) {
		this(key, value, null, value != null ? value.getClass() : Object.class);
	}

	public ConfigSettingValue(String key, Object value, Object defaultValue, Class<?> type) {

		this.key = key;
		this.value = value;
		this.defaultValue = defaultValue;
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public Class<?> getType() {
		return type;
	}

	public Object getValueOrDefault() {

		if (getValue() != null) {
			return getValue();
		}

		return getDefaultValue();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConfigSettingValue [key=" + key + ", value=" + value + ", defaultValue=" + defaultValue + ", type=" + type
				+ "]";
	}
}
