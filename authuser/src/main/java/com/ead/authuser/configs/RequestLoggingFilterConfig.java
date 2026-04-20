package com.ead.authuser.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingFilterConfig {

    // Configuração do filtro de log para registrar as requisições HTTP, incluindo
    // query string, payload e headers, com um prefixo personalizado. O filtro
    // também exclui o header "authorization" para evitar o registro de informações
    // sensíveis.
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        var filter = new CommonsRequestLoggingFilter();

        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setAfterMessagePrefix("REQUEST DATA: ");
        filter.setHeaderPredicate(header -> !header.equalsIgnoreCase("authorization"));

        return filter;
    }
}
