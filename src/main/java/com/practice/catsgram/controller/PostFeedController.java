package com.practice.catsgram.controller;

import com.practice.catsgram.model.Post;
import com.practice.catsgram.service.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostFeedController {

    private final PostService postService;

    @PostMapping("/feed/friends")
    List<Post> getFriendsFeed(@RequestBody FeedParams feedParams) {
        if (feedParams == null || feedParams.getFriendsEmails().isEmpty()) {
            throw new IllegalArgumentException("Неверно заполнены параметры");
        }

        List<Post> result = new ArrayList<>();
        for (String friend : feedParams.friendsEmails) {
            result.addAll(postService.findAllByUserEmail(friend, feedParams.size, feedParams.sort));
        }
        return result;
    }

    @Setter
    @Getter
    private static class FeedParams {
        private String sort;
        private Integer size;
        private List<String> friendsEmails;
    }
}
