package com.practice.catsgram.service;

import com.practice.catsgram.exceptions.PostNotFoundException;
import com.practice.catsgram.exceptions.UserNotFoundException;
import com.practice.catsgram.model.Post;
import com.practice.catsgram.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserService userService;
    private final List<Post> posts = new ArrayList<>();

    private static Integer globalId = 0;

    public List<Post> findAll() {
        return posts;
    }

    public Post create(Post post) {
        User postAuthor = userService.findUserByEmail(post.getAuthor());
        if (postAuthor == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    post.getAuthor()));
        }

        post.setId(getNextId());
        posts.add(post);
        return post;
    }

    public Post findPostById(Integer postId) {
        return posts.stream()
                .filter(post -> post.getId().equals(postId))
                .findFirst()
                .orElseThrow(() -> new PostNotFoundException(String.format("Пост № %d не найден", postId)));
    }

    private static Integer getNextId() {
        return globalId++;
    }
}