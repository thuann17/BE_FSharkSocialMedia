package com.system.fsharksocialmedia.services;

import com.system.fsharksocialmedia.dtos.ImageDto;
import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.dtos.UserroleDto;
import com.system.fsharksocialmedia.entities.Image;
import com.system.fsharksocialmedia.entities.Post;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.entities.Userrole;
import com.system.fsharksocialmedia.exceptions.UserNotFoundException;
import com.system.fsharksocialmedia.repositories.PostRepository;
import com.system.fsharksocialmedia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminProfileByUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    public UserDto getUserByUsername(String username) {
        User u = userRepository.findById(username).orElseThrow(() -> new UserNotFoundException("Không có user: " + username));
        return convertToDto(u);
    }

    public List<PostDto> getPostByUsername(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new UserNotFoundException("Không có user: " + username));
        List<Post> listPosts = postRepository.findByUsername(user);
        return listPosts.stream().map(this::convertPostToDto).toList();
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


        return dto;
    }

    public User convertToEntity(UserDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setActive(dto.getActive());
        user.setBio(dto.getBio());
        user.setEmail(dto.getEmail());
        user.setGender(dto.getGender());
        user.setLastname(dto.getLastname());
        user.setFirstname(dto.getFirstname());
        user.setBirthday(dto.getBirthday());
        user.setHometown(dto.getHometown());
        user.setCurrency(dto.getCurrency());

        if (dto.getRoles() != null) {
            user.setRoles(convertToEntity(dto.getRoles()));
        }


        return user;
    }

    private Userrole convertToEntity(UserroleDto dto) {
        Userrole userRole = new Userrole();
        userRole.setRole(dto.getRole());
        return userRole;
    }

    private Image convertToEntity(ImageDto dto) {
        Image image = new Image();
        image.setImage(dto.getImage());
        return image;
    }

    private PostDto convertPostToDto(Post post) {
        long commentCount = postRepository.countCmtByPost(post.getId());
        long likeCount = postRepository.countLikeByPost(post.getId());
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setContent(post.getContent());
        postDto.setCountComment(commentCount);
        postDto.setCountLike(likeCount);
        UserDto userDto = null;
        if (post.getUsername() != null) {
            userDto = new UserDto();
            userDto.setUsername(post.getUsername().getUsername());
        }
        postDto.setUsername(userDto);
        return postDto;
    }
}