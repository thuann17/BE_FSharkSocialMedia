package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.dtos.ImageDto;
import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.dtos.PostimageDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.entities.Post;
import com.system.fsharksocialmedia.entities.Postimage;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.models.PostModel;
import com.system.fsharksocialmedia.repositories.FriendRepository;
import com.system.fsharksocialmedia.repositories.PostRepository;
import com.system.fsharksocialmedia.repositories.PostimageRepository;
import com.system.fsharksocialmedia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

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
            postDto.setId((Integer) result[0]);  // Post ID
            postDto.setContent((String) result[5]);  // Post content
            postDto.setCreatedate(((Timestamp) result[6]).toInstant());  // Convert Timestamp to Instant
            postDto.setStatus((Boolean) result[7]);  // Post status
            postDto.setCountComment(((Number) result[8]).longValue());  // Count of comments (convert to Long)
            postDto.setCountLike(((Number) result[9]).longValue());  // Count of likes (convert to Long)

            // Set up UserDto
            userDto.setUsername((String) result[1]);  // Username
            userDto.setEmail((String) result[2]);  // Email
            userDto.setFirstname((String) result[3]);  // Firstname
            userDto.setLastname((String) result[4]);  // Lastname

            // Handle avatar URLs (result[10])
            String avatarUrlsString = (String) result[11];
            if (avatarUrlsString != null && !avatarUrlsString.isEmpty()) {
                // Parse the avatar URLs if they are comma-separated or in JSON format
                List<ImageDto> avatarUrls = parseAvatarUrls(avatarUrlsString);
                userDto.setImages(avatarUrls);  // Set the parsed avatar URLs
            }

            // Associate UserDto with PostDto
            postDto.setUsername(userDto);

            // Fetch associated post images using the Post ID
            Post post = new Post();
            post.setId(postDto.getId());  // Set Post ID to fetch images related to this post
            List<Postimage> postImages = postImageRepository.findByPostid(post);  // Fetch post images

            // Convert Postimage entities to PostimageDto
            for (Postimage postImage : postImages) {
                PostimageDto postImageDto = new PostimageDto();
                postImageDto.setId(postImage.getId());  // Postimage ID
                postImageDto.setPostid(convertToDto(postImage.getPostid()));  // Convert Post to PostDto
                postImageDto.setImage(postImage.getImage());  // Post image URL

                postImageDtos.add(postImageDto);  // Add to the set of images
            }

            // Add images to the PostDto
            postDto.setPostimages(postImageDtos);

            return postDto;  // Return the fully populated PostDto
        }).collect(Collectors.toList());  // Collect into a list of PostDto
    }

    // Helper method to parse avatar URLs
    private List<ImageDto> parseAvatarUrls(String avatarUrlsString) {
        List<ImageDto> avatarUrls = new ArrayList<>();
        try {
            // If the string is comma-separated
            String[] urls = avatarUrlsString.split(",");
            for (String url : urls) {
                ImageDto imageDto = new ImageDto();
                imageDto.setAvatarrurl(url.trim());  // Set the URL after trimming whitespace
                avatarUrls.add(imageDto);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log parsing errors if any
        }
        return avatarUrls;
    }

}
