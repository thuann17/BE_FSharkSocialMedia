package com.system.fsharksocialmedia.services;

import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.dtos.PostimageDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.entities.Post;
import com.system.fsharksocialmedia.entities.Postimage;
<<<<<<< Updated upstream
import com.system.fsharksocialmedia.repositories.PostRepository;
import com.system.fsharksocialmedia.repositories.PostimageRepository;
import com.system.fsharksocialmedia.repositories.UserRepository;
=======
import com.system.fsharksocialmedia.models.PostModel;
import com.system.fsharksocialmedia.repositories.PostRepository;
import com.system.fsharksocialmedia.repositories.PostimageRepository;
>>>>>>> Stashed changes
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostimageRepository postImageRepository;
<<<<<<< Updated upstream
=======

    // Get all posts with optional search
    public Page<PostDto> getPost(int page, int size, String search) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page number must be non-negative and size must be greater than zero");
        }
>>>>>>> Stashed changes

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public PostDto convertToPostDto(Post post) {
        PostDto postDto = new PostDto();

<<<<<<< Updated upstream
        // Assuming Post has fields like id, content, and others, map them to PostDto
        postDto.setId(post.getId());  // Assuming Post has getId() method
        // Add more fields here if necessary
        // For example: postDto.setContent(post.getContent());
=======
        postDto.setId(post.getId());
        postDto.setCreatedate(post.getCreatedate());
        postDto.setContent(post.getContent());
        postDto.setCountLike(commentCount);
        postDto.setCountLike(likeCount);
>>>>>>> Stashed changes

        return postDto;
    }


    @Transactional
    public List<PostDto> getPostsWithUserDetails(String username) {
        // Fetch the results from the repository using the stored procedure
        List<Object[]> results = postRepository.getPostsWithUserDetails(username);

        // Map the results to PostDto
        return results.stream().map(result -> {
            PostDto postDto = new PostDto();
            UserDto userDto = new UserDto();
            List<String> postImageDtos = new ArrayList<>();

            // Set up PostDto
            postDto.setId((Integer) result[0]);
            postDto.setContent((String) result[5]);
            postDto.setCreatedate(((Timestamp) result[6]).toInstant());
            postDto.setStatus((Boolean) result[7]);
            postDto.setCommentCount((Integer) result[8]);
            postDto.setLikeCount((Integer) result[9]);

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
                postImageDto.setPostid(convertToPostDto(postImage.getPostid()));  // Convert Post to PostDto
                postImageDto.setImage(postImage.getImage());

                postImageDtos.add(String.valueOf(postImageDto));
            }

            // Add images to the PostDto
            postDto.setImages(postImageDtos);

            return postDto;
        }).collect(Collectors.toList());
    }

<<<<<<< Updated upstream
=======
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
            List<PostimageDto> postImageDtos = new ArrayList<>(); // Use PostimageDto instead of String

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
            postDto.setPostimages(postImageDtos);

            return postDto;
        }).collect(Collectors.toList());
    }



>>>>>>> Stashed changes
}
