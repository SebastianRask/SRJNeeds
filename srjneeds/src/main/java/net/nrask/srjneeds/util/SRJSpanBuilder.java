package net.nrask.srjneeds.util;

/**
 * Created by Sebastian Rask on 23-04-2017.
 */

public class SRJSpanBuilder {
	public static SRJSpanBuilderFactory init() {
		return init("");
	}

	public static SRJSpanBuilderFactory init(String initialString) {
		return new SRJSpanBuilderFactory(initialString);
	}
}
