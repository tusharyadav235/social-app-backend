package com.socialmedia.social_app.config;


import com.socialmedia.social_app.entity.User;
import com.socialmedia.social_app.repository.UserRepository;
import com.socialmedia.social_app.security.CustomUserDetails;
import com.socialmedia.social_app.security.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel
    ) {

        StompHeaderAccessor accessor =
                StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader =
                    accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                String token = authHeader.substring(7);

                if (jwtService.isTokenValid(token)) {

                    String email =
                            jwtService.extractUsername(token);

                    User user = userRepository.findByEmail(email)
                            .orElseThrow(() ->
                                    new RuntimeException("User not found"));

                    CustomUserDetails userDetails =
                            new CustomUserDetails(user);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // 🔥 THIS STORES userId IN WEBSOCKET SESSION
                    accessor.setUser(authentication);
                }
            }
        }
        return message;
    }
}
