package org.lambda3.graphene.core.coreference;

import com.typesafe.config.Config;
import org.lambda3.graphene.core.coreference.model.CoreferenceContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public abstract class CoreferenceResolver {
	private static final Logger logger = LoggerFactory.getLogger(CoreferenceResolver.class);

	protected Config config;

	public CoreferenceResolver(Config config) {
		this.config = config;
	}

	public abstract CoreferenceContent doCoreferenceResolution(String text);

	public static CoreferenceResolver loadResolverFromClassName(String className, Config settings) {
		CoreferenceResolver coreferenceResolver = null;
		logger.info("Loading CoreferenceResolver... '{}'", className);

		try {
			Class<?> clazz = Class.forName(className);
			Constructor[] constructors = clazz.getConstructors();

			if (CoreferenceResolver.class.isAssignableFrom(clazz)) {
				// It's our internal factory hence we inject the core dependency.
				coreferenceResolver = (CoreferenceResolver) constructors[0].newInstance(settings);
			}
		} catch (Exception e) {
			throw new RuntimeException("Fail to initialize CoreferenceResolver: " + className, e);
		}
		if (coreferenceResolver == null) {
			throw new RuntimeException("Fail to initialize CoreferenceResolver: " + className);
		}

		return coreferenceResolver;
	}

	public abstract boolean isActivated();
}
