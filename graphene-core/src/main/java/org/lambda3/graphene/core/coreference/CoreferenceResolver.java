package org.lambda3.graphene.core.coreference;

import com.typesafe.config.Config;
import org.lambda3.graphene.core.coreference.model.CoreferenceContent;

public abstract class CoreferenceResolver {
	protected Config config;

	public CoreferenceResolver(Config config) {
		this.config = config;
	}

	public abstract CoreferenceContent doCoreferenceResolution(String text);
}
