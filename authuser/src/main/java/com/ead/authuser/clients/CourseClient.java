package com.ead.authuser.clients;

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

import com.ead.authuser.dtos.CourseRecordDto;
import com.ead.authuser.dtos.ResponsePageDto;
import com.ead.authuser.exceptions.ExternalRestClientException;

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

    public Page<CourseRecordDto> getAllCourseByUser(UUID userId, Pageable pageable) {
        var url = baseUrlCourse + "/courses?userId=" + userId +
                "&page=" + pageable.getPageNumber() +
                "&size=" + pageable.getPageSize() +
                "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");

        try {
            var responsePageDto = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<ResponsePageDto<CourseRecordDto>>() {
                    });

            log.debug("Successful microservice Course response: {}", responsePageDto);
            return responsePageDto;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error Request RestClient with couse: ", e.getMessage());
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
}
