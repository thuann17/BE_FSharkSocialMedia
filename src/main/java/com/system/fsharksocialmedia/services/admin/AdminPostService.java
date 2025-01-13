package com.system.fsharksocialmedia.services.admin;

import com.system.fsharksocialmedia.dtos.CommentDto;
import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.entities.Post;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.models.PostModel;
import com.system.fsharksocialmedia.repositories.PostRepository;
import com.system.fsharksocialmedia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminPostService {
    @Autowired
    private PostRepository postRepository;


    public Page<PostDto> getPost(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page number must be non-negative and size must be greater than zero");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(this::convertToDto);
    }


    public PostDto getPostById(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        return convertToDto(post);
    }

    public PostDto updatePost(Integer postId,PostModel postModel) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        post.setStatus(postModel.isStatus());
        return convertToDto(postRepository.save(post));
    }

    // Convert entity to DTO
    private PostDto convertToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setCreatedate(post.getCreatedate());
        postDto.setContent(post.getContent());
        postDto.setStatus(post.getStatus());
        if (post.getComments() != null && !post.getComments().isEmpty()) {
            Set<CommentDto> commentDtos = post.getComments().stream().map(comment -> {
                CommentDto commentDto = new CommentDto();
                commentDto.setId(comment.getId());
                commentDto.setContent(comment.getContent());
                commentDto.setImage(comment.getImage());
                commentDto.setCreatedate(comment.getCreatedate());

                if (comment.getUsername() != null) {
                    UserDto userDto = new UserDto();
                    userDto.setUsername(comment.getUsername().getUsername());
                    userDto.setFirstname(comment.getUsername().getFirstname());
                    userDto.setLastname(comment.getUsername().getLastname());
                    commentDto.setUsername(userDto);
                }
                return commentDto;
            }).collect(Collectors.toSet());
            postDto.setComments(commentDtos);
        }
        if (post.getUsername() != null) {
            UserDto userDto = new UserDto();
            userDto.setUsername(post.getUsername().getUsername());
            userDto.setFirstname(post.getUsername().getFirstname());
            userDto.setLastname(post.getUsername().getLastname());
            userDto.setEmail(post.getUsername().getEmail());
            postDto.setUsername(userDto);
        }
        long commentCount = postRepository.countCmtByPost(post.getId());
        long likeCount = postRepository.countLikeByPost(post.getId());
        postDto.setCountComment(commentCount);
        postDto.setCountLike(likeCount);
        return postDto;
    }
}
