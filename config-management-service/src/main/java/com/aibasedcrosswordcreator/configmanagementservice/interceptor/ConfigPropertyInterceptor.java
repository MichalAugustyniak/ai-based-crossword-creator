package com.aibasedcrosswordcreator.configmanagementservice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Mono;

@Component
public class ConfigPropertyInterceptor implements HandlerInterceptor {

    private final WebClient.Builder builder;

    public ConfigPropertyInterceptor(WebClient.Builder builder) {
        this.builder = builder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            return;
        }
        String applicationName = request.getParameter("application-name");
        if (applicationName == null || applicationName.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Mono<ResponseEntity<Void>> mono = builder.build()
                .post()
                .uri(String.format("https://%s/actuator/busrefresh", applicationName))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, httpResponse -> {
                    System.out.println("Error 4xx occurred while making request to config server");
                    return Mono.empty();
                })
                .onStatus(HttpStatusCode::is5xxServerError, httpResponse -> {
                    System.out.println("Error 5xx occurred while making request to config server");
                    return Mono.empty();
                })
                .toBodilessEntity()
                .doOnTerminate(() -> System.out.println("Request finished"))
                .doOnError(throwable -> System.out.println("Error occurred: " + throwable.getMessage()));
        mono.subscribe();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
