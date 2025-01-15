package com.system.fsharksocialmedia.services.admin;

import com.system.fsharksocialmedia.dtos.*;
import com.system.fsharksocialmedia.entities.Image;
import com.system.fsharksocialmedia.entities.Post;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.entities.Userrole;
import com.system.fsharksocialmedia.exceptions.UserNotFoundException;
import com.system.fsharksocialmedia.models.PasswordModel;
import com.system.fsharksocialmedia.models.UserModel;
import com.system.fsharksocialmedia.repositories.ImageRepository;
import com.system.fsharksocialmedia.repositories.PostRepository;
import com.system.fsharksocialmedia.repositories.UserRepository;
import com.system.fsharksocialmedia.services.user.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminProfileByUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ImageRepository imageRepository;

    public UserDto getUserByUsername(String username) {
        User u = userRepository.findById(username).orElseThrow(() -> new UserNotFoundException("Không có user: " + username));
        return convertToDto(u);
    }

    public List<PostDto> getPostByUsername(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new UserNotFoundException("Không có user: " + username));
        List<Post> listPosts = postRepository.findByUsername(user);
        return listPosts.stream().map(this::convertPostToDto).toList();
    }

    public UserDto updateProfile(String username, UserModel model) {
        try {
            User u = userRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
            u.setFirstname(model.getFirstname());
            u.setLastname(model.getLastname());
            u.setHometown(model.getHometown());
            u.setEmail(model.getEmail());
            return convertToDto(userRepository.save(u));
        } catch (RuntimeException e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }

    public UserDto updatePassword(String username, PasswordModel model) {
        try {
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                throw new RuntimeException("User not found.");
            }
            if (!passwordEncoder.matches(model.getOldPassword(), user.getPassword())) {
                throw new RuntimeException("Old password is incorrect.");
            }
            user.setPassword(passwordEncoder.encode(model.getNewPassword()));
            userRepository.save(user);
            return convertToDto(user);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while updating the password", e);
        }
    }

    public ImageDto updateImage(String username, String avatarUrl) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Image image = new Image();
        image.setAvatarrurl(avatarUrl);
        image.setStatus(true);
        image.setUsername(user);
        image.setCreatedate(Instant.now());
        imageRepository.save(image);
        return convertToImageDto(image);
    }


    public UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setActive(user.getActive());
        dto.setBio(user.getBio());
        dto.setEmail(user.getEmail());
        dto.setGender(user.getGender());
        dto.setLastname(user.getLastname());
        dto.setFirstname(user.getFirstname());
        dto.setBirthday(user.getBirthday());
        dto.setHometown(user.getHometown());
        dto.setCurrency(user.getCurrency());
        if (user.getRoles() != null) {
            UserroleDto userRoleDto = new UserroleDto();
            userRoleDto.setRole(user.getRoles().getRole());
            dto.setRoles(userRoleDto);
        }
        if (user.getImages() != null) {
            List<ImageDto> imageDtos = user.getImages().stream()
                    .map(this::convertToImageDto)
                    .collect(Collectors.toList());
            dto.setImages(imageDtos);
        }
        return dto;
    }

    public ImageDto convertToImageDto(Image image) {
        ImageDto imageDto = new ImageDto();
        imageDto.setId(image.getId());
        imageDto.setImage(image.getImage());
        imageDto.setCreatedate(image.getCreatedate());
        imageDto.setAvatarrurl(image.getAvatarrurl());
        imageDto.setCoverurl(image.getCoverurl());
        imageDto.setStatus(image.getStatus());
        return imageDto;
    }

    private PostDto convertPostToDto(Post post) {
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