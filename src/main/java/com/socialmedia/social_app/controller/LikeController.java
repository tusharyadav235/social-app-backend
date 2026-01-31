package com.socialmedia.social_app.controller;



import com.socialmedia.social_app.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}")
    public String likePost(@PathVariable Long postId) {
        return likeService.likePost(postId);
    }

    @DeleteMapping("/{postId}")
    public String unlikePost(@PathVariable Long postId) {
        return likeService.unlikePost(postId);
    }

    @GetMapping("/count/{postId}")
    public long getLikes(@PathVariable Long postId) {
        return likeService.getLikeCount(postId);
    }
}
