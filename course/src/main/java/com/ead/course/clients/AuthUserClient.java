package com.ead.course.clients;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.ead.course.dots.ResponsePageDto;
import com.ead.course.dots.UserRecordDto;
import com.ead.course.exceptions.ExternalRestClientException;

import lombok.var;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class AuthUserClient {

    @Value("${ead.api.url.authuser}")
    String baseUrlAuthUser;

    final RestClient restClient;

    public AuthUserClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public Page<UserRecordDto> getAllUsersByCourse(UUID courseId, Pageable pageable) {
        var url = baseUrlAuthUser + "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() + "&size="
                + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
        log.debug("Request URL {}", url);

        try {
            var responsePageDto = restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<ResponsePageDto<UserRecordDto>>() {});

            log.debug("Successful microservice AuthUser response: {}", responsePageDto);
            return responsePageDto;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Erro ao comunicar com o serviço de authuser: ", e.getMessage());
            throw new ExternalRestClientException(e.getStatusCode(),
                    "Erro ao comunicar com o serviço de authuser: " + e.getResponseBodyAsString(),
                    "AuthUser", e);
        } catch (ResourceAccessException e) {
            log.error("Erro ao comunicar com o serviço de authuser: ", e.getMessage());
            throw new ExternalRestClientException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Erro ao comunicar com o serviço de authuser", "AuthUser", e);
        } catch (RestClientException e) {
            log.error("Erro ao comunicar com o serviço de authuser: ", e.getMessage());
            throw new ExternalRestClientException(HttpStatus.BAD_GATEWAY, "Erro ao comunicar com o serviço de cursos",
                    "AuthUser", e);
        }
    }
}
