package com.ead.authuser.clients;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.ead.authuser.dtos.CourseRecordDto;
import com.ead.authuser.dtos.ResponsePageDto;
import com.ead.authuser.exceptions.ExternalRestClientException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CourseClient {

    final RestClient restClient;

    @Value("${ead.api.url.course}")
    String baseUrlCourse;

    public CourseClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    // Anotação utilizada quando se deseja a abordagem do Retry com Fallback
    // @Retry(name = "retryInstance", fallbackMethod = "retryFallback")
    // Anotação utilizada quando se deseja a abordagem do CircuitBreaker com
    // Fallback
    // @CircuitBreaker(name = "circuitbreakerInstance", fallbackMethod =
    // "circuitbreakerFallback")
    @CircuitBreaker(name = "circuitbreakerInstance")
    public Page<CourseRecordDto> getAllCourseByUser(UUID userId, Pageable pageable, String token) {
        var url = baseUrlCourse + "/courses?userId=" + userId +
                "&page=" + pageable.getPageNumber() +
                "&size=" + pageable.getPageSize() +
                "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");

        try {
            var responsePageDto = restClient.get()
                    .uri(url)
                    .header("Authorization", token)
                    .retrieve()
                    .body(new ParameterizedTypeReference<ResponsePageDto<CourseRecordDto>>() {
                    });

            log.debug("Successful microservice Course response: {}", responsePageDto);
            return responsePageDto;
        } catch (HttpStatusCodeException e) {
            log.error("Error Request RestClient with status: {}", e.getStatusCode(), e);

            throw new ExternalRestClientException(e.getStatusCode(),
                    "Error Request RestClient with couse: " + e.getResponseBodyAsString(),
                    "Course", e);
        } catch (ResourceAccessException e) {
            log.error("Error Request RestClient with couse: ", e.getMessage());
            throw new ExternalRestClientException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Error Request RestClient with couse", "Course", e);
        } catch (RestClientException e) {
            log.error("Error Request RestClient with couse: ", e.getMessage());
            throw new ExternalRestClientException(HttpStatus.BAD_GATEWAY, "Error Request RestClient with couse",
                    "Course", e);
        }
    }

    // *************************************************************************************
    // Metodos retryFallback e circuitbreakerFallback são utilizado para tratar o
    // error após as tentativas mau sucedidas do retry

    public Page<CourseRecordDto> retryFallback(UUID userId, Pageable pageable,
            Throwable throwable) {
        log.error("Inside retry retryFallback, couse - {}", throwable.toString());

        List<CourseRecordDto> searchResult = new ArrayList<>();
        return new PageImpl<>(searchResult);
    }

    public Page<CourseRecordDto> circuitbreakerFallback(UUID userId, Pageable pageable, Throwable throwable) {
        log.error("Inside circuit break Fallback, couse - {}", throwable.toString());

        List<CourseRecordDto> searchResult = new ArrayList<>();
        return new PageImpl<>(searchResult);
    }

    // *****************************************************************************************
}
