package org.springframework.config;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource
public class RuntimeConfigManager implements ConfigSource<Object> {

	private Map<String, ConfigSettingValue> configSettings = new ConcurrentHashMap<String, ConfigSettingValue>();
	private ConversionService conversionService = new DefaultConversionService();

	@Override
	public Object getConfigValue(String key) {

		ConfigSettingValue configSetting = getConfigSetting(key);
		return configSetting != null ? configSetting.getValueOrDefault() : null;
	}

	private ConfigSettingValue getConfigSetting(String key) {

		ConfigSettingValue configSetting = configSettings.get(key);
		return configSetting;
	}

	@Override
	public void configure(Field field, ConfigSetting<?> configSetting) {

		String preliminaryConfigKey = field.getDeclaringClass().getName() + "." + field.getName();

		if (configSetting.getKey() != null) {
			preliminaryConfigKey = configSetting.getKey();
		}

		String actualConfigKey = deriveActualConfigKey(field, preliminaryConfigKey);

		configSetting.key = actualConfigKey; // hack to propagate the actual key

		configSettings.put(actualConfigKey, new ConfigSettingValue(actualConfigKey, configSetting.getInitialValue()));
	}

	protected String deriveActualConfigKey(Field field, String configKey) {

		ExpressionParser parser = new SpelExpressionParser();
		Expression expr = parser.parseExpression(configKey);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fieldName", field.getName());
		map.put("fqcn", field.getDeclaringClass().getName());
		String actualConfigKey = String.class.cast(expr.getValue(map));

		return actualConfigKey;
	}

	@ManagedAttribute
	public Map<String, String> getConfigSettings() {

		Map<String, String> map = new TreeMap<String, String>();
		for (Map.Entry<String, ConfigSettingValue> configSetting : configSettings.entrySet()) {
			map.put(configSetting.getKey(), String.valueOf(configSetting.getValue().getValue()));
		}

		return map;
	}

	@ManagedOperation
	public void setProperty(String key, String valueString) {

		ConfigSettingValue configSetting = getConfigSetting(key);
		Object value = conversionService.convert(valueString, configSetting.getType());
		configSetting.setValue(value);
	}
}
