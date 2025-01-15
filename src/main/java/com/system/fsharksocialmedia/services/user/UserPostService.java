package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.dtos.*;
import com.system.fsharksocialmedia.entities.*;
import com.system.fsharksocialmedia.models.CommentModel;
import com.system.fsharksocialmedia.models.PostModel;
import com.system.fsharksocialmedia.models.ShareModel;
import com.system.fsharksocialmedia.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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
    private ShareRepository shareRepository;

    @Autowired
    private LikecmtRepository likecmtRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    PostimageRepository postImageRepository;

    @Autowired
    private PostimageRepository postimageRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public List<PostDto> getPostsByFriends(String username) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

        // Lấy danh sách bạn bè
        List<User> friends = friendRepository.findAllByUserSrcOrUserTargetAndStatus(currentUser, currentUser, true)
                .stream()
                .map(friend -> friend.getUserSrc().equals(currentUser) ? friend.getUserTarget() : friend.getUserSrc())
                .collect(Collectors.toList());

        // Thêm chính người dùng vào danh sách
        friends.add(currentUser);

        // Lấy tất cả bài viết của người dùng và bạn bè, sắp xếp theo ngày tạo (mới nhất -> cũ nhất)
        List<Post> posts = postRepository.findAllByUsernameInAndStatusTrueOrderByCreatedateDesc(friends);

        return posts.stream()
                .map(post -> convertToDto(post, currentUser))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ShareDto> getSharesByUsername(String username) {
        List<Object[]> results = shareRepository.getSharesByUsername(username);

        return results.stream().map(record -> {
            ShareDto shareDto = new ShareDto();
            shareDto.setId((Integer) record[0]);

            // UserDto for the person who shared the post
            UserDto sharedBy = new UserDto();
            sharedBy.setUsername((String) record[1]);
            sharedBy.setFirstname((String) record[12]);  // First name of the user sharing the post
            sharedBy.setLastname((String) record[13]);   // Last name of the user sharing the post

            ImageDto sharedByAvatar = new ImageDto();
            sharedByAvatar.setAvatarrurl((String) record[2]);
            sharedBy.setImages(List.of(sharedByAvatar));
            shareDto.setUsername(sharedBy);

            shareDto.setContent((String) record[3]);
            shareDto.setCreatedate(((java.sql.Timestamp) record[4]).toInstant());

            // PostDto for the shared post
            PostDto postDto = new PostDto();
            postDto.setId((Integer) record[5]);

            // UserDto for the post author
            UserDto postAuthor = new UserDto();
            postAuthor.setUsername((String) record[6]);
            postAuthor.setFirstname((String) record[14]);  // First name of the post author
            postAuthor.setLastname((String) record[15]);   // Last name of the post author

            ImageDto postAuthorAvatar = new ImageDto();
            postAuthorAvatar.setAvatarrurl((String) record[7]);
            postAuthor.setImages(List.of(postAuthorAvatar));
            postDto.setUsername(postAuthor);

            postDto.setContent((String) record[8]);
            postDto.setCreatedate(((java.sql.Timestamp) record[9]).toInstant());
            postDto.setStatus((Boolean) record[10]);

            // Post image (if any)
            PostimageDto postImage = new PostimageDto();
            postImage.setImage((String) record[11]);
            postDto.setPostimages(Set.of(postImage));

            shareDto.setPost(postDto);

            return shareDto;
        }).collect(Collectors.toList());
    }

    public ShareDto addShare(String username, Integer postId, ShareModel model) {

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

        Post postid = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại!"));

        // Convert ShareDto to Share entity
        Share share = new Share();
        share.setUsername(currentUser);  // Set the user
        share.setPost(postid);  // Set the post
        share.setContent(model.getContent());  // Set the content

        // Set current timestamp as the creation date
        share.setCreatedate(Instant.now());

        // Save the entity to the database
        Share savedShare = shareRepository.save(share);

        // Create and set the ShareDto
        ShareDto savedShareDto = new ShareDto();
        savedShareDto.setId(savedShare.getId());

        // Map User entity to UserDto
        UserDto userDto = new UserDto();
        userDto.setUsername(currentUser.getUsername());
        userDto.setFirstname(currentUser.getFirstname());
        userDto.setLastname(currentUser.getLastname());
        // Add any other required fields here

        savedShareDto.setUsername(userDto);

        // Map Post entity to PostDto
        PostDto postDto = new PostDto();
        postDto.setId(postid.getId());
        postDto.setContent(postid.getContent());
        postDto.setCreatedate(postid.getCreatedate());
        // Add any other required fields here

        savedShareDto.setPost(postDto);

        savedShareDto.setContent(savedShare.getContent());
        savedShareDto.setCreatedate(savedShare.getCreatedate());

        return savedShareDto;
    }



    public PostDto addPost(String username, PostModel postModel) {
        // Fetch the user from the database
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

        // Create a new post object and set its properties
        Post post = new Post();
        post.setUsername(currentUser);
        post.setContent(postModel.getContent());
        post.setStatus(true); // Set post status (adjust based on your use case)
        post.setCreatedate(Instant.now());

        // Save the post and get the saved post
        Post savedPost = postRepository.save(post);

        // If there are images in the post model, save them
        if (postModel.getImagePosts() != null && !postModel.getImagePosts().isEmpty()) {
            // Iterate over each image in the list
            for (String image : postModel.getImagePosts()) {
                Postimage postimage = new Postimage();
                postimage.setPostid(savedPost); // Set the saved post for the Postimage
                postimage.setImage(image);     // Set the image URL (or path)
                postimageRepository.save(postimage); // Save each image
            }
        }

        // Convert the saved post to DTO and return it
        return convertToDto(savedPost, null);
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
    @Transactional
    public void deletePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài đăng với ID: " + postId));

        // Xóa Comment liên quan
        commentRepository.deleteByPost(post);

        // Xóa Like liên quan
        likepostRepository.deleteByPost(post);

        // Xóa hình ảnh của bài viết
        postImageRepository.deleteByPostid(post);

        // Xóa Share liên quan
        shareRepository.deleteByPost(post);

        // Xóa bài đăng
        postRepository.delete(post);
    }

    // Delete a comment
    public void deleteComment(Integer cmtId) {
        Comment cmt = commentRepository.findById(cmtId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cmt với ID: " + cmtId));
        likecmtRepository.deleteByComment(cmt);

        // Xóa Comment liên quan
        commentRepository.deleteById(cmtId);

    }

    // Delete a comment
    public void deleteShare(Integer shareId) {
        Share cmt = shareRepository.findById(shareId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cmt với ID: " + shareId));

        // Xóa Comment liên quan
        shareRepository.deleteById(shareId);

    }

    public Long countLikesForPost(Integer postId) {
        return postRepository.countLikeByPost(postId);
    }

    public long countCommentsForPost(Integer postId) {
        return postRepository.countCmtByPost(postId);
    }

    public CommentDto addComment(Integer postId, String username, CommentModel commentModel) {
        Post post = postRepository.findById(postId).orElse(null);
        User u = userRepository.findByUsername(username).orElse(null);
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setContent(commentModel.getContent());
        comment.setUsername(u);
        comment.setCreatedate(Instant.now());
        comment.setPost(post);
        comment.setImage(commentModel.getImage());
        return convertToCommentDto(commentRepository.save(comment));
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
        simpMessagingTemplate.convertAndSend("/topic/post/" + postId, "User " + username + " liked your post.");
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

        // Initialize PostDto
        PostDto postDto = new PostDto();
        postDto.setCountComment(commentCount);
        postDto.setCountLike(likeCount);
        postDto.setId(post.getId());
        postDto.setCreatedate(post.getCreatedate());
        postDto.setContent(post.getContent());
        postDto.setStatus(post.getStatus());

        // Convert UserDto
        UserDto userDto = new UserDto();
        if (post.getUsername() != null) {
            userDto.setUsername(post.getUsername().getUsername());
            userDto.setFirstname(post.getUsername().getFirstname());
            userDto.setLastname(post.getUsername().getLastname());
            if (post.getId() != null) {
                List<ImageDto> imageDtos = post.getUsername().getImages().stream()
                        .map(this::convertToImageDto) // Convert Image to ImageDto
                        .collect(Collectors.toList()); // Collect into a List<ImageDto>
                userDto.setImages(imageDtos);
            }
        }

        // Set UserDto to PostDto
        postDto.setUsername(userDto);

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

        // Check if currentUser has liked the post
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

        UserDto userDto = new UserDto();
        userDto.setUsername(comment.getUsername().getUsername());
        userDto.setFirstname(comment.getUsername().getFirstname());
        userDto.setLastname(comment.getUsername().getLastname());

        // Set UserDto to PostDto
        commentDto.setUsername(userDto);

//        if (comment.getPost() != null) {
//            PostDto postDto = new PostDto();
//            postDto.setId(comment.getPost().getId());
//            commentDto.setPost(postDto);
//        }

        return commentDto;
    }

    public LikepostDto convertToLikepostDto(Likepost likepost) {
        LikepostDto likepostDto = new LikepostDto();
        likepostDto.setId(likepost.getId());

//        if (likepost.getUsername() != null) {
//            likepostDto.setUsername(convertToUserDto(likepost.getUsername()));
//        }

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

//        if (notification.getUsername() != null) {
//            notificationDto.setUsername(convertToUserDto(notification.getUsername()));
//        }

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

//        if (share.getUsername() != null) {
//            shareDto.setUsername(convertToUserDto(share.getUsername()));
//        }

        return shareDto;
    }
    public ImageDto convertToImageDto(Image image) {
        ImageDto imageDto = new ImageDto();

        imageDto.setId(image.getId());
        imageDto.setImage(image.getImage());
        imageDto.setCreatedate(image.getCreatedate());
        imageDto.setAvatarrurl(image.getAvatarrurl());
        imageDto.setCoverurl(image.getCoverurl());
        imageDto.setStatus(image.getStatus());
        if (image.getUsername() != null) {
            UserDto userDto = new UserDto();
            userDto.setUsername(image.getUsername().getUsername());
            userDto.setFirstname(image.getUsername().getFirstname());
            userDto.setLastname(image.getUsername().getLastname());
            imageDto.setUsername(userDto);
        }

        return imageDto;
    }
}
