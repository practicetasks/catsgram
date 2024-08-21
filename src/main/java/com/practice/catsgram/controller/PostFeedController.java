package com.practice.catsgram.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/feed/friends")
    List<Post> getFriendsFeed(@RequestBody String params) {
        FriendsParams friendsParams = parseParams(params);

        if (friendsParams == null || friendsParams.getFriends().isEmpty()) {
            throw new IllegalArgumentException("Неверно заполнены параметры");
        }

        List<Post> result = new ArrayList<>();
        for (String friend : friendsParams.friends) {
            result.addAll(postService.findAllByUserEmail(friend, friendsParams.size, friendsParams.sort));
        }
        return result;
    }

    private FriendsParams parseParams(String params) {
        try {
            String paramsFromString = objectMapper.readValue(params, String.class);
            return objectMapper.readValue(paramsFromString, FriendsParams.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Невалидный формат json", e);
        }
    }

    @Setter
    @Getter
    private static class FriendsParams {
        private String sort;
        private Integer size;
        private List<String> friends;
    }
}
