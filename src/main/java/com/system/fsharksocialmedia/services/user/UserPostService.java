package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.dtos.*;
import com.system.fsharksocialmedia.entities.*;
import com.system.fsharksocialmedia.models.PostModel;
import com.system.fsharksocialmedia.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserPostService {
    @Autowired
    PostRepository postRepository;

    // Get all posts
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Add a post
    public PostDto addPost(PostModel postModel) {
        Post post = new Post();
        post.setContent(postModel.getContent());
        post.setStatus(false);
        post.setCreatedate(Instant.now());
        return convertToDto(postRepository.save(post));
    }

    // Update a post
    public PostDto updatePost(Integer postID, PostModel postModel) {
        Post post = postRepository.findById(postID).orElseThrow(() ->
                new RuntimeException("Không có id: " + postID));
        post.setContent(postModel.getContent());
        post.setStatus(postModel.isStatus());
        return convertToDto(postRepository.save(post));
    }

    // Delete a post
    public void deletePost(Integer postId) {
        if (postId == null) {
            throw new RuntimeException("Không có ID: " + postId);
        }
        postRepository.deleteById(postId);
    }

    // Convert Post to PostDto
    public PostDto convertToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setCreatedate(post.getCreatedate());
        postDto.setContent(post.getContent());
        postDto.setStatus(post.getStatus());

        if (post.getUsername() != null) {
            UserDto userDto = new UserDto();
            userDto.setUsername(post.getUsername().getUsername());
            userDto.setBio(post.getUsername().getBio());
            userDto.setEmail(post.getUsername().getEmail());
            postDto.setUsername(userDto);
        }

        if (post.getComments() != null && !post.getComments().isEmpty()) {
            Set<CommentDto> commentDtos = post.getComments().stream().map(this::convertToCommentDto)
                    .collect(Collectors.toSet());
            postDto.setComments(commentDtos);
        }

        if (post.getLikeposts() != null && !post.getLikeposts().isEmpty()) {
            Set<LikepostDto> likepostDtos = post.getLikeposts().stream().map(this::convertToLikepostDto)
                    .collect(Collectors.toSet());
            postDto.setLikeposts(likepostDtos);
        }

        if (post.getNotifications() != null && !post.getNotifications().isEmpty()) {
            Set<NotificationDto> notificationDtos = post.getNotifications().stream().map(this::convertToNotificationDto)
                    .collect(Collectors.toSet());
            postDto.setNotifications(notificationDtos);
        }

        if (post.getPostimages() != null && !post.getPostimages().isEmpty()) {
            Set<PostimageDto> postimageDtos = post.getPostimages().stream().map(this::convertToImage)
                    .collect(Collectors.toSet());
            postDto.setPostimages(postimageDtos);
        }

        if (post.getShares() != null && !post.getShares().isEmpty()) {
            Set<ShareDto> shareDtos = post.getShares().stream().map(this::convertToShareDto)
                    .collect(Collectors.toSet());
            postDto.setShares(shareDtos);
        }

        return postDto;
    }

    private CommentDto convertToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedate(comment.getCreatedate());

        if (comment.getUsername() != null) {
            UserDto userDto = new UserDto();
            userDto.setUsername(comment.getUsername().getUsername());
            userDto.setBio(comment.getUsername().getBio());
            userDto.setEmail(comment.getUsername().getEmail());
            commentDto.setUsername(userDto);
        }

        if (comment.getPost() != null) {
            PostDto postDto = new PostDto();
            postDto.setId(comment.getPost().getId());
            commentDto.setPost(postDto);
        }

        return commentDto;
    }

    private LikepostDto convertToLikepostDto(Likepost likepost) {
        LikepostDto likepostDto = new LikepostDto();
        likepostDto.setId(likepost.getId());

        if (likepost.getUsername() != null) {
            UserDto userDto = new UserDto();
            userDto.setUsername(likepost.getUsername().getUsername());
            likepostDto.setUsername(userDto);
        }

        if (likepost.getPost() != null) {
            PostDto postDto = new PostDto();
            postDto.setId(likepost.getPost().getId());
            likepostDto.setPost(postDto);
        }

        return likepostDto;
    }

    private PostimageDto convertToImage(Postimage postimage) {
        PostimageDto postimageDto = new PostimageDto();
        postimageDto.setId(postimage.getId());
        postimageDto.setImage(postimage.getImage());
        return postimageDto;
    }

    private NotificationDto convertToNotificationDto(Notification notification) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(notification.getId());
        notificationDto.setContent(notification.getContent());
        notificationDto.setCreatedate(notification.getCreatedate());

        if (notification.getUsername() != null) {
            UserDto userDto = new UserDto();
            userDto.setUsername(notification.getUsername().getUsername());
            notificationDto.setUsername(userDto);
        }

        return notificationDto;
    }

    private ShareDto convertToShareDto(Share share) {
        ShareDto shareDto = new ShareDto();
        shareDto.setId(share.getId());

        if (share.getPost() != null) {
            PostDto postDto = new PostDto();
            postDto.setId(share.getPost().getId());
            shareDto.setPost(postDto);
        }

        if (share.getUsername() != null) {
            UserDto userDto = new UserDto();
            userDto.setUsername(share.getUsername().getUsername());
            shareDto.setUsername(userDto);
        }

        return shareDto;
    }
}
