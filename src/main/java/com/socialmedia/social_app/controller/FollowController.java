package com.socialmedia.social_app.controller;

import com.socialmedia.social_app.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{userId}")
    public String follow(@PathVariable Long userId) {
        return followService.followUser(userId);
    }

    @DeleteMapping("/{userId}")
    public String unfollow(@PathVariable Long userId) {
        return followService.unfollowUser(userId);
    }

    @GetMapping("/followers/{userId}")
    public long followersCount(@PathVariable Long userId) {
        return followService.getFollowersCount(userId);
    }

    @GetMapping("/following/{userId}")
    public long followingCount(@PathVariable Long userId) {
        return followService.getFollowingCount(userId);
    }

}