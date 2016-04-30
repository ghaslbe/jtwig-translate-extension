package org.jtwig.translate.message.source;

import com.google.common.base.Optional;
import org.jtwig.environment.Environment;
import org.jtwig.environment.initializer.EnvironmentInitializer;

import java.util.Locale;

public class DefaultMessageSource implements MessageSource, EnvironmentInitializer {
    private final MessageSourceFactory messageSourceFactory;
    private MessageSource messageSource;

    public DefaultMessageSource(MessageSourceFactory messageSourceFactory) {
        this.messageSourceFactory = messageSourceFactory;
    }

    @Override
    public void initialize(Environment environment) {
        messageSource = messageSourceFactory.create(environment);
    }

    @Override
    public Optional<String> message(Locale locale, String message) {
        if (messageSource == null) {
            throw new IllegalStateException("Message source not loaded. Is this part of the environment initializers list?");
        } else {
            return messageSource.message(locale, message);
        }
    }
}
