package com.system.fsharksocialmedia.services;

import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.dtos.PostimageDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.entities.Post;
import com.system.fsharksocialmedia.entities.Postimage;
import com.system.fsharksocialmedia.models.PostModel;
import com.system.fsharksocialmedia.repositories.PostRepository;
import com.system.fsharksocialmedia.repositories.PostimageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostimageRepository postImageRepository;

    // Get all posts with optional search
    public Page<PostDto> getPost(int page, int size, String search) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page number must be non-negative and size must be greater than zero");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = (search == null || search.isEmpty())
                ? postRepository.findAll(pageable)
                : postRepository.findByContentContainingIgnoreCase(search, pageable);

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

        post.setContent(postModel.getContent());
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
        postDto.setCountLike(commentCount);
        postDto.setCountLike(likeCount);

        if (post.getUsername() != null) {
            UserDto userDto = new UserDto();
            userDto.setUsername(post.getUsername().getUsername());
            postDto.setUsername(userDto);
        }
        return postDto;
    }

    // Convert DTO to entity
    public Post convertToEntity(PostDto dto) {
        if (dto == null) return null;
        Post post = new Post();
        post.setId(dto.getId());
        post.setContent(dto.getContent());
        post.setCreatedate(dto.getCreatedate());
        return post;
    }

    public List<PostDto> convertToDTOList(List<Post> posts) {
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PostDto> getPostsWithUserDetails(String username) {
        // Fetch the results from the repository using the stored procedure
        List<Object[]> results = postRepository.getPostsWithUserDetails(username);

        // Map the results to PostDto
        return results.stream().map(result -> {
            PostDto postDto = new PostDto();
            UserDto userDto = new UserDto();
            Set<PostimageDto> postImageDtos = new HashSet<>();


            // Set up PostDto
            postDto.setId((Integer) result[0]);
            postDto.setContent((String) result[5]);

            // Convert Timestamp to Instant
            postDto.setCreatedate(((Timestamp) result[6]).toInstant());

            postDto.setStatus((Boolean) result[7]);
            postDto.setCountComment(((Number) result[8]).longValue()); // Convert Integer/Long to Long
            postDto.setCountLike(((Number) result[9]).longValue()); // Convert Integer/Long to Long
            // Set up UserDto
            userDto.setUsername((String) result[1]);
            userDto.setEmail((String) result[2]);
            userDto.setFirstname((String) result[3]);
            userDto.setLastname((String) result[4]);
            // Associate UserDto with PostDto
            postDto.setUsername(userDto);
            // Fetch associated post images using the full Post object
            Post post = new Post();
            post.setId(postDto.getId()); // Create a Post object to fetch the images
            List<Postimage> postImages = postImageRepository.findByPostid(post); // Use Post object
            // Convert Postimage entities to PostimageDto
            for (Postimage postImage : postImages) {
                PostimageDto postImageDto = new PostimageDto();
                postImageDto.setId(postImage.getId());
                postImageDto.setPostid(convertToDto(postImage.getPostid()));  // Convert Post to PostDto
                postImageDto.setImage(postImage.getImage());

                postImageDtos.add(postImageDto); // Add the PostimageDto to the list
            }
            // Add images to the PostDto
            postDto.setPostimages( postImageDtos);

            return postDto;
        }).collect(Collectors.toList());
    }
}
