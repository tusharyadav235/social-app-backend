package com.socialmedia.social_app.config;


import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SwaggerGroupConfig {

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("auth-apis")
                .pathsToMatch("/api/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi postApi() {
        return GroupedOpenApi.builder()
                .group("post-apis")
                .pathsToMatch("/api/posts/**")
                .build();
    }


    @Bean
    public GroupedOpenApi commentApi() {
        return GroupedOpenApi.builder()
                .group("comment-apis")
                .pathsToMatch("/api/comments/**")
                .build();
    }

    @Bean
    public GroupedOpenApi likeApi() {
        return GroupedOpenApi.builder()
                .group("like-apis")
                .pathsToMatch("/api/likes/**")
                .build();
    }

    @Bean
    public GroupedOpenApi profileApi() {
        return GroupedOpenApi.builder()
                .group("profile-api")
                .pathsToMatch("/api/profile/**")
                .build();
    }



    @Bean
    public GroupedOpenApi followApi() {
        return GroupedOpenApi.builder()
                .group("follow-apis")
                .pathsToMatch("/api/follow/**")
                .build();
    }

    @Bean
    public GroupedOpenApi notificationApi(){
        return GroupedOpenApi.builder()
                .group("notification-apis")
                .pathsToMatch("/api/notifications/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin-apis")
                .pathsToMatch("/api/admin/**")
                .build();
    }



}
