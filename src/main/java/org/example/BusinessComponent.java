package org.example;

import org.springframework.config.ConfigSetting;

public class BusinessComponent {

	private ConfigSetting<String> stringProperty = new ConfigSetting<>("default");

	private ConfigSetting<Integer> intProperty = new ConfigSetting<>(42);

	private ConfigSetting<Boolean> booleanProperty = new ConfigSetting<>(false);

	public void businessMethod(/*..*/) {

		if (booleanProperty.get()) {
			businessTransaction1();
		} else {
			businessTransaction2();
		}
	}

	private void businessTransaction2() {
		process(stringProperty.get());
		// ...
	}

	private void businessTransaction1() {
		process(intProperty.get());
	}

	private void process(Object value) {
		System.out.printf("%s Processing: %s (%s)%n", System.currentTimeMillis(), value, (value != null) ? value.getClass()
				: null);
	}

}
