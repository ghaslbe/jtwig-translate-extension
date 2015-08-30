package org.jtwig.translate.function;

import com.google.common.base.Supplier;
import org.jtwig.context.RenderContext;
import org.jtwig.context.RenderContextHolder;
import org.jtwig.environment.Environment;
import org.jtwig.functions.annotations.JtwigFunction;
import org.jtwig.functions.annotations.Parameter;
import org.jtwig.i18n.decorate.MessageDecorator;
import org.jtwig.i18n.decorate.ReplacementMessageDecorator;
import org.jtwig.translate.configuration.TranslateConfiguration;
import org.jtwig.translate.decorator.PluralSelector;
import org.jtwig.value.JtwigValueFactory;
import org.jtwig.value.configuration.ValueConfiguration;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Arrays.asList;

public class TranslateFunction {
    @JtwigFunction(value = "translate", aliases = {"trans", "message"})
    public String translate (@Parameter("message") final String message, @Parameter("replacements") Map replacements, @Parameter("locale") Locale locale) {
        return new Translator(getEnvironment())
                .translate(message, locale, Collections.<MessageDecorator>singletonList(new ReplacementMessageDecorator(toReplacementCollection(replacements, getValueConfiguration()))));
    }


    @JtwigFunction(value = "translate", aliases = {"trans", "message"})
    public String translate (@Parameter("message") final String message, @Parameter("replacements") Map replacements) {
        return translate(message, replacements, getLocaleSupplier().get());
    }

    @JtwigFunction(value = "translate", aliases = {"trans", "message"})
    public String translate (@Parameter("message") final String message) {
        return translate(message, Collections.emptyMap(), getLocaleSupplier().get());
    }

    @JtwigFunction(value = "translate", aliases = {"trans", "message"})
    public String translate (@Parameter("message") final String message, @Parameter("locale") Locale locale) {
        return translate(message, Collections.emptyMap(), locale);
    }

    @JtwigFunction(value = "translateChoice", aliases = {"transchoice"})
    public String translateChoice (@Parameter("message") final String message, @Parameter("count") BigDecimal count, @Parameter("replacements") Map replacements, @Parameter("locale") Locale locale) {
        return new Translator(getEnvironment())
                .translate(message, locale, asList(
                        new PluralSelector(count.intValue()),
                        new ReplacementMessageDecorator(toReplacementCollection(replacements, getValueConfiguration()))
                ));
    }

    @JtwigFunction(value = "translateChoice", aliases = {"transchoice"})
    public String translateChoice (@Parameter("message") final String message, @Parameter("count") BigDecimal count, @Parameter("replacements") Map replacements) {
        return translateChoice(message, count, replacements, getLocaleSupplier().get());
    }

    @JtwigFunction(value = "translateChoice", aliases = {"transchoice"})
    public String translateChoice (@Parameter("message") final String message, @Parameter("count") BigDecimal count) {
        return translateChoice(message, count, Collections.emptyMap(), getLocaleSupplier().get());
    }

    @JtwigFunction(value = "translateChoice", aliases = {"transchoice"})
    public String translateChoice (@Parameter("message") final String message, @Parameter("count") BigDecimal count, @Parameter("locale") Locale locale) {
        return translateChoice(message, count, Collections.emptyMap(), locale);
    }

    private Supplier<Locale> getLocaleSupplier() {
        return TranslateConfiguration.currentLocaleSupplier(getRenderContext().environment());
    }

    private Environment getEnvironment() {
        return getRenderContext().environment();
    }

    private ValueConfiguration getValueConfiguration() {
        return getRenderContext().environment().valueConfiguration();
    }

    private Collection<ReplacementMessageDecorator.Replacement> toReplacementCollection(Map<Object, Object> replacements, ValueConfiguration valueConfiguration) {
        Collection<ReplacementMessageDecorator.Replacement> result = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : replacements.entrySet()) {
            if (entry.getKey() != null) {
                String key = entry.getKey().toString();
                String stringValue = JtwigValueFactory.value(entry.getValue(), valueConfiguration).asString();
                result.add(new ReplacementMessageDecorator.Replacement(key, stringValue));
            }
        }
        return result;
    }

    protected RenderContext getRenderContext() {
        return RenderContextHolder.get();
    }
}