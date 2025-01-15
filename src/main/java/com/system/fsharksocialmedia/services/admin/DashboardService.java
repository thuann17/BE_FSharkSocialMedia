package com.system.fsharksocialmedia.services.admin;

import com.system.fsharksocialmedia.dtos.CommentDto;
import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.entities.Post;
import com.system.fsharksocialmedia.repositories.PostRepository;
import com.system.fsharksocialmedia.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class DashboardService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TripRepository tripRepository;

    public PostDto getPostAndTripCountByYearAndMonth(Integer year, Integer month) {
        Integer postCount = postRepository.countPostsByYearAndMonth(year, month);
        Integer tripCount = tripRepository.countTripsByYearAndMonth(year, month);
        PostDto postDto = new PostDto();
        postDto.setCountPost(postCount);
        postDto.setTripCount(tripCount);
        return postDto;
    }

    public List<Map<String, Object>> getUserPostCount(Integer year, Integer month) {
        return postRepository.getUserPostCount(year, month);
    }

    public List<Map<String, Object>> getUserPostCountTop5(Integer year, Integer month) {
        return postRepository.GetUserPostCountTop5(year, month);
    }

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
