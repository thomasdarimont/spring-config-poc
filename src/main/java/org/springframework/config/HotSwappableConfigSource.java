package org.springframework.config;

import java.lang.reflect.Field;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HotSwappableConfigSource<T> implements ConfigSource<T> {

	private ConfigSource<T> target;

	private ReadWriteLock rwLock = new ReentrantReadWriteLock();

	public ConfigSource<T> getTarget() {

		rwLock.readLock().lock();
		try {
			return target;
		} finally {
			rwLock.readLock().unlock();
		}
	}

	public void setTarget(ConfigSource<T> target) {

		rwLock.writeLock().lock();
		try {
			this.target = target;
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	@Override
	public T getConfigValue(String key) {
		return getTarget().getConfigValue(key);
	}

	@Override
	public void configure(Field field, ConfigSetting<?> configSetting) {
		getTarget().configure(field, configSetting);
	}
}
