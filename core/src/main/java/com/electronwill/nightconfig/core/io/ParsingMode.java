package com.electronwill.nightconfig.core.io;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.utils.StringUtils;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author TheElectronWill
 */
public enum ParsingMode {
	/**
	 * Replaces the existing config by the parsed one.
	 */
	REPLACE(Config::clear, Config::set, Map::put),

	/**
	 * Merges the parsed config with the existing one: the parsed values are prioritary.
	 * This mode performs a shallow merge, not a deep one.
	 */
	MERGE(c -> {}, Config::set, Map::put),

	/**
	 * Adds the parsed values to the config: the existing values are prioritary and will not be
	 * replaced.
	 */
	ADD(c -> {}, (cfg, path, value) -> {cfg.add(path, value); return null;}, Map::putIfAbsent);

	private final Consumer<? super Config> preparationAction;
	private final PutAction putAction;
	private final MapPutAction mapPutAction;

	ParsingMode(Consumer<? super Config> preparationAction, PutAction putAction,
				MapPutAction mapPutAction) {
		this.preparationAction = preparationAction;
		this.putAction = putAction;
		this.mapPutAction = mapPutAction;
	}

	public void prepareParsing(Config config) {
		preparationAction.accept(config);
	}

	public Object put(Config config, List<String> key, Object value) {
		return putAction.put(config, key, value);
	}

	public Object put(Config config, String key, Object value) {
		return putAction.put(config, key, value);
	}

	public Object put(Map<String, Object> map, String key, Object value) {
		return mapPutAction.put(map, key, value);
	}

	@FunctionalInterface
	private interface PutAction {
		Object put(Config config, List<String> key, Object value);

		default Object put(Config config, String key, Object value) {
			return put(config, StringUtils.split(key, '.'), value);
		}
	}

	@FunctionalInterface
	private interface MapPutAction {
		Object put(Map<String, Object> map, String key, Object value);
	}
}