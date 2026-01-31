package com.socialmedia.social_app.service;

import com.socialmedia.social_app.dto.PostResponse;
import com.socialmedia.social_app.entity.Post;
import com.socialmedia.social_app.entity.User;
import com.socialmedia.social_app.exception.ResourceNotFoundException;
import com.socialmedia.social_app.repository.PostRepository;
import com.socialmedia.social_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final ImageUploadService imageUploadService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // CREATE POST
    public PostResponse createPost(String content, MultipartFile image) {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String email;

        if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            email = userDetails.getUsername(); // email
        } else {
            email = principal.toString();
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email)
                );

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = imageUploadService.uploadImage(image);
        }

        Post post = Post.builder()
                .content(content)
                .imageUrl(imageUrl)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        return mapToResponse(postRepository.save(post));
    }


    // GET ALL POSTS (WITHOUT PAGINATION)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // GET ALL POSTS (WITH PAGINATION)
    public Page<PostResponse> getAllPosts(
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return postRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    // COMMON RESPONSE MAPPER
    private PostResponse mapToResponse(Post post) {

        if (post.getUser() == null) {
            throw new ResourceNotFoundException("Post user not found");
        }

        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .imageUrl(post.getImageUrl()) // ✅ IMPORTANT
                .userId(post.getUser().getId())
                .username(post.getUser().getUsername())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
