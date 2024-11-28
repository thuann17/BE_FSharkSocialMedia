package com.system.fsharksocialmedia.services;

import com.system.fsharksocialmedia.dtos.*;
import com.system.fsharksocialmedia.entities.*;
import com.system.fsharksocialmedia.models.PostModel;
import com.system.fsharksocialmedia.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserPostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LikepostRepository likepostRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    LikecmtRepository likecmtRepository;


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
            userDto.setFirstname(comment.getUsername().getFirstname());
            userDto.setLastname(comment.getUsername().getLastname());
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


    public Long countLikesForPost(Integer postId) {
        return likepostRepository.countByPostId(postId);
    }

    @Transactional
    public LikepostDto likePost(String username, Integer postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Bài đăng không tồn tại!"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

        Optional<Likepost> existingLike = likepostRepository.findByUsernameAndPost(user, post);
        if (existingLike.isPresent()) {
            throw new RuntimeException("Người dùng đã thích bài viết này!");
        }

        Likepost likePost = new Likepost();
        likePost.setUsername(user);
        likePost.setPost(post);

        Likepost savedLikePost = likepostRepository.save(likePost);

        LikepostDto likepostDto = new LikepostDto();
        likepostDto.setId(savedLikePost.getId());

        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setBio(user.getBio());
        userDto.setEmail(user.getEmail());

        likepostDto.setUsername(userDto);

        PostDto postDto = convertToDto(post);
        likepostDto.setPost(postDto);

        return likepostDto;
    }


    //unlike bài viết
    @Transactional
    public String removeLike(Integer postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

        Optional<Likepost> existingLike = Optional.ofNullable(likepostRepository.findByUsernameAndPostId(user, postId));

        if (existingLike.isPresent()) {
            // If the like exists, delete it
            likepostRepository.delete(existingLike.get());
            return "Successfully unliked the post.";
        } else {
            return "You haven't liked this post yet.";
        }
    }

    //kiểm tra bài viết đã thích chưa?
    public boolean hasUserLikedPost(String username, Integer postId) {
        // Fetch the user entity based on the username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Query the repository to check if the like exists
        return likepostRepository.existsByUsernameAndPostId(user, postId);
    }

    //lấy cmt theo postID
    public List<CommentDto> getCommentsByPostId(Integer postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(this::convertToCommentDto)
                .collect(Collectors.toList());
    }

    public int getLikesByCommentId(Integer commentId) {
        return likecmtRepository.getLikeCountByCommentId(commentId);
    }

    public boolean isLikedByUser(Integer commentId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return likecmtRepository.existsByCommentIdAndUsername(commentId, user);
    }
}
