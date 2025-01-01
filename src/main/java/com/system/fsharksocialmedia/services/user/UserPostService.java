package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.dtos.*;
import com.system.fsharksocialmedia.entities.*;
import com.system.fsharksocialmedia.models.PostModel;
import com.system.fsharksocialmedia.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikepostRepository likepostRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikecmtRepository likecmtRepository;

    @Autowired
    private FriendRepository friendRepository;

    public List<PostDto> getPostsByFriends(String username) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

        List<User> friends = friendRepository.findAllByUserSrcOrUserTargetAndStatus(currentUser, currentUser, true)
                .stream()
                .map(friend -> friend.getUserSrc().equals(currentUser) ? friend.getUserTarget() : friend.getUserSrc())
                .collect(Collectors.toList());

        if (friends.isEmpty()) {
            throw new RuntimeException("Bạn không có bài viết nào từ bạn bè.");
        }

        List<Post> posts = postRepository.findAllByUsernameIn(friends);
        return posts.stream()
                .map(post -> convertToDto(post, currentUser))
                .collect(Collectors.toList());
    }

    // Add a new post
    public PostDto addPost(PostModel postModel) {
        Post post = new Post();
        post.setContent(postModel.getContent());
        post.setStatus(false);
        post.setCreatedate(Instant.now());
        return convertToDto(postRepository.save(post), null); // Pass null if no currentUser
    }

    // Update a post
    public PostDto updatePost(Integer postID, PostModel postModel) {
        Post post = postRepository.findById(postID).orElseThrow(() ->
                new RuntimeException("Không có id: " + postID));
        post.setContent(postModel.getContent());
        post.setStatus(postModel.isStatus());
        return convertToDto(postRepository.save(post), null); // Pass null if no currentUser
    }

    // Delete a post
    public void deletePost(Integer postId) {
        if (postId == null) {
            throw new RuntimeException("Không có ID: " + postId);
        }
        postRepository.deleteById(postId);
    }

    public Long countLikesForPost(Integer postId) {
        return postRepository.countLikeByPost(postId);
    }

    public long countCommentsForPost(Integer postId) {
        return postRepository.countCmtByPost(postId);
    }

    // Like a post
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
        return convertToLikepostDto(savedLikePost);
    }

    @Transactional
    public String removeLike(Integer postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

        Optional<Likepost> existingLike = Optional.ofNullable(likepostRepository.findByUsernameAndPostId(user, postId));

        if (existingLike.isPresent()) {
            likepostRepository.delete(existingLike.get());
            return "Successfully unliked the post.";
        } else {
            return "You haven't liked this post yet.";
        }
    }

    public boolean hasUserLikedPost(String username, Integer postId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));
        return likepostRepository.existsByUsernameAndPostId(user, postId);
    }

    // Get comments by postId
    public List<CommentDto> getCommentsByPostId(Integer postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(this::convertToCommentDto)
                .collect(Collectors.toList());
    }

    // Get like count by commentId
    public int getLikesByCommentId(Integer commentId) {
        return likecmtRepository.getLikeCountByCommentId(commentId);
    }

    // Check if a comment is liked by the user
    public boolean isLikedByUser(Integer commentId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));
        return likecmtRepository.existsByCommentIdAndUsername(commentId, user);
    }

    // Convert Post to PostDto
    public PostDto convertToDto(Post post, User currentUser) {
        long commentCount = postRepository.countCmtByPost(post.getId());
        long likeCount = postRepository.countLikeByPost(post.getId());
        PostDto postDto = new PostDto();
        postDto.setCountComment(commentCount);
        postDto.setCountLike(likeCount);
        postDto.setId(post.getId());
        postDto.setCreatedate(post.getCreatedate());
        postDto.setContent(post.getContent());
        postDto.setStatus(post.getStatus());

        if (post.getUsername() != null) {
            postDto.setUsername(convertToUserDto(post.getUsername()));
        }

        // Convert Comments
        postDto.setComments(post.getComments() != null ?
                post.getComments().stream().map(this::convertToCommentDto).collect(Collectors.toSet()) : Collections.emptySet());

        // Convert Likeposts
        postDto.setLikeposts(post.getLikeposts() != null ?
                post.getLikeposts().stream().map(this::convertToLikepostDto).collect(Collectors.toSet()) : Collections.emptySet());

        // Convert Notifications
        postDto.setNotifications(post.getNotifications() != null ?
                post.getNotifications().stream().map(this::convertToNotificationDto).collect(Collectors.toSet()) : Collections.emptySet());

        // Convert Postimages
        postDto.setPostimages(post.getPostimages() != null ?
                post.getPostimages().stream().map(this::convertToImage).collect(Collectors.toSet()) : Collections.emptySet());

        // Convert Shares
        postDto.setShares(post.getShares() != null ?
                post.getShares().stream().map(this::convertToShareDto).collect(Collectors.toSet()) : Collections.emptySet());

        if (currentUser != null) {
            boolean isLikedByUser = post.getLikeposts().stream()
                    .anyMatch(like -> like.getUsername().getUsername().equals(currentUser.getUsername()));
            postDto.setLikedByUser(isLikedByUser);
        }

        return postDto;
    }

    // Helper method to convert User to UserDto
    public UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setBio(user.getBio());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public CommentDto convertToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedate(comment.getCreatedate());

        if (comment.getUsername() != null) {
            commentDto.setUsername(convertToUserDto(comment.getUsername()));
        }

        if (comment.getPost() != null) {
            PostDto postDto = new PostDto();
            postDto.setId(comment.getPost().getId());
            commentDto.setPost(postDto);
        }

        return commentDto;
    }

    public LikepostDto convertToLikepostDto(Likepost likepost) {
        LikepostDto likepostDto = new LikepostDto();
        likepostDto.setId(likepost.getId());

        if (likepost.getUsername() != null) {
            likepostDto.setUsername(convertToUserDto(likepost.getUsername()));
        }

        if (likepost.getPost() != null) {
            PostDto postDto = new PostDto();
            postDto.setId(likepost.getPost().getId());
            likepostDto.setPost(postDto);
        }

        return likepostDto;
    }

    public PostimageDto convertToImage(Postimage postimage) {
        PostimageDto postimageDto = new PostimageDto();
        postimageDto.setId(postimage.getId());
        postimageDto.setImage(postimage.getImage());
        return postimageDto;
    }

    public NotificationDto convertToNotificationDto(Notification notification) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(notification.getId());
        notificationDto.setContent(notification.getContent());
        notificationDto.setCreatedate(notification.getCreatedate());

        if (notification.getUsername() != null) {
            notificationDto.setUsername(convertToUserDto(notification.getUsername()));
        }

        return notificationDto;
    }

    public ShareDto convertToShareDto(Share share) {
        ShareDto shareDto = new ShareDto();
        shareDto.setId(share.getId());

        if (share.getPost() != null) {
            PostDto postDto = new PostDto();
            postDto.setId(share.getPost().getId());
            shareDto.setPost(postDto);
        }

        if (share.getUsername() != null) {
            shareDto.setUsername(convertToUserDto(share.getUsername()));
        }

        return shareDto;
    }
}
