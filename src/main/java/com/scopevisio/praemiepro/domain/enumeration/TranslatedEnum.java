package com.scopevisio.praemiepro.domain.enumeration;

import com.scopevisio.praemiepro.util.I18NProvider;

public interface TranslatedEnum<E extends Enum<E>> {

    default String translate() {
        final String messageKey = this.getClass().getSimpleName() + '.' + ((E) this).name();

        return I18NProvider.getMessage(messageKey);
    }
}
