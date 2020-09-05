package wendergalan.github.io.desafiosambatech.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import wendergalan.github.io.desafiosambatech.service.MessageByLocaleService;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageByLocaleServiceImpl implements MessageByLocaleService {

    private final MessageSource messageSource;

    @Override
    public String getMessage(String id) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(id, null, locale);
    }
}
