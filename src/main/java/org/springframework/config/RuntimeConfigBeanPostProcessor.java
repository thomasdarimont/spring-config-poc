package org.springframework.config;

import java.lang.reflect.Field;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

public class RuntimeConfigBeanPostProcessor implements BeanPostProcessor {

	@Autowired private ConfigSource<?> configSource;

	private static class ConfigSettingFieldCallback implements FieldCallback {

		private Object bean;
		private ConfigSource<?> configSource;

		public ConfigSettingFieldCallback(ConfigSource<?> configSource, Object bean) {

			this.bean = bean;
			this.configSource = configSource;
		}

		@Override
		public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {

			if (ConfigSetting.class.isAssignableFrom(field.getType())) {

				field.setAccessible(true);
				ConfigSetting<?> configSetting = ConfigSetting.class.cast(field.get(bean));

				((HotSwappableConfigSource) configSetting.getConfigSource()).setTarget(configSource);
				configSource.configure(field, configSetting);
			}
		}
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

		ReflectionUtils.doWithFields(bean.getClass(), new ConfigSettingFieldCallback(configSource, bean));
		return bean;
	}
}
