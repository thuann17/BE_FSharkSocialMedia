package com.system.fsharksocialmedia.services.admin;

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
import java.util.stream.Collectors;

@Service
public class AdminPostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<PostDto> getPost(int page, int size, String search, Boolean status) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page number must be non-negative and size must be greater than zero");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts;

        if (status == null) {
            posts = (search == null || search.isEmpty())
                    ? postRepository.findAll(pageable)
                    : postRepository.findByContentContainingIgnoreCase(search, pageable);
        } else {
            posts = (search == null || search.isEmpty())
                    ? postRepository.findByStatus(status, pageable)
                    : postRepository.findByContentContainingIgnoreCaseAndStatus(search, status, pageable);
        }

        return posts.map(this::convertToDto);
    }


    public PostDto getPostById(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        return convertToDto(post);
    }

    public PostDto updatePost(PostModel postModel, Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        post.setStatus(postModel.isStatus());
        return convertToDto(postRepository.save(post));
    }

    // Convert entity to DTO
    private PostDto convertToDto(Post post) {
        long commentCount = postRepository.countCmtByPost(post.getId());
        long likeCount = postRepository.countLikeByPost(post.getId());
        PostDto postDto = new PostDto();

        postDto.setId(post.getId());
        postDto.setCreatedate(post.getCreatedate());
        postDto.setContent(post.getContent());
        postDto.setCountComment(commentCount);
        postDto.setCountLike(likeCount);
        postDto.setStatus(post.getStatus());
        if (post.getUsername() != null) {
            UserDto userDto = new UserDto();
            userDto.setUsername(post.getUsername().getUsername());
            userDto.setFirstname(post.getUsername().getFirstname());
            userDto.setLastname(post.getUsername().getLastname());
            userDto.setEmail(post.getUsername().getEmail());
            postDto.setUsername(userDto);
        }
        return postDto;
    }
}
