package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.dtos.*;
import com.system.fsharksocialmedia.entities.*;
import com.system.fsharksocialmedia.models.InteractModel;
import com.system.fsharksocialmedia.repositories.*;
import com.system.fsharksocialmedia.services.other.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class InteractService {
    @Autowired
    private LikepostRepository likepostRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ShareRepository shareRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    // Like a post
    public LikepostDto likePost(InteractModel model) {
        User u = userRepository.findById(model.getUsername()).orElseThrow(() ->
                new RuntimeException("Khôgn có username: " + model.getUsername()));
        Post p = postRepository.findById(model.getPostID()).orElseThrow(() ->
                new RuntimeException("Không có postID: " + model.getPostID()));
        Likepost likepost = new Likepost();
        likepost.setPost(p);
        likepost.setUsername(u);
        Likepost savedLikepost = likepostRepository.save(likepost);
        return convertToLikepostDto(savedLikepost);
    }

    // Unlike a post
    public boolean deleteLikePost(Integer idLike) {
        Optional<Likepost> likepost = likepostRepository.findById(idLike);
        if (likepost.isPresent()) {
            likepostRepository.delete(likepost.get());
            System.out.println("Xoá like thành công");
            return true;
        } else {
            System.out.println("Không tìm thấy like để xoá");
            return false;
        }
    }

    // Add a comment
    public CommentDto commentPost(InteractModel model) {
        User u = userRepository.findById(model.getUsername()).orElseThrow(() ->
                new RuntimeException("Khôgn có username: " + model.getUsername()));
        Post p = postRepository.findById(model.getPostID()).orElseThrow(() ->
                new RuntimeException("Không có postID: " + model.getPostID()));
        Comment comment = new Comment();
        comment.setPost(p);
        comment.setUsername(u);
        comment.setContent(model.getContent());
        comment.setCreatedate(Instant.now());
        comment.setImage(model.getImage());
        commentRepository.save(comment);
        return convertToCommentDto(comment);
    }

    public boolean deleteCommentPost(Integer commentID) {
        Optional<Comment> commentPost = commentRepository.findById(commentID);
        if (commentPost.isPresent()) {
            commentRepository.delete(commentPost.get());
            System.out.println("Xoá like thành công");
            return true;
        } else {
            System.out.println("Không tìm thấy like để xoá");
            return false;
        }
    }

    public CommentDto updateComment(Integer commentID, InteractModel model) {
        User u = userRepository.findById(model.getUsername()).orElseThrow(() ->
                new RuntimeException("Khôgn có username: " + model.getUsername()));
        Post p = postRepository.findById(model.getPostID()).orElseThrow(() ->
                new RuntimeException("Không có postID: " + model.getPostID()));
        Comment comment = commentRepository.findById(commentID).orElseThrow(() ->
                new RuntimeException("Không có cmt id: " + commentID));
        comment.setPost(p);
        comment.setUsername(u);
        comment.setContent(model.getContent());
        comment.setImage(model.getImage());
        commentRepository.save(comment);
        return convertToCommentDto(comment);
    }

    //  share bài
    public ShareDto sharePost(InteractModel model) {
        User u = userRepository.findById(model.getUsername()).orElseThrow(() ->
                new RuntimeException("Khôgn có username: " + model.getUsername()));
        Post p = postRepository.findById(model.getPostID()).orElseThrow(() ->
                new RuntimeException("Không có postID: " + model.getPostID()));
        Share share = new Share();
        share.setPost(p);
        share.setUsername(u);
        share.setContent(model.getContent());
        share.setCreatedate(Instant.now());
        shareRepository.save(share);
        return convertToShareDto(share);
    }

    public ShareDto updateSharePost(Integer shareID, InteractModel model) {
        Post p = postRepository.findById(model.getPostID()).orElseThrow(() ->
                new RuntimeException("Không có postID: " + model.getPostID()));
        Share share = shareRepository.findById(shareID).orElseThrow(()
                -> new RuntimeException("Không có id: " + shareID));
        share.setPost(p);
        share.setContent(model.getContent());
        shareRepository.save(share);
        return convertToShareDto(share);
    }

    public boolean deleteSharePost(Integer shareID) {
        Optional<Share> share = shareRepository.findById(shareID);
        if (share.isPresent()) {
            shareRepository.delete(share.get());
            System.out.println("Xoá like thành công");
            return true;
        } else {
            System.out.println("Không tìm thấy like để xoá");
            return false;
        }
    }

    // Convert Likepost to LikepostDto
    private LikepostDto convertToLikepostDto(Likepost likepost) {
        LikepostDto likepostDto = new LikepostDto();
        likepostDto.setId(likepost.getId());
        UserInfoService userInfoService = new UserInfoService();
        if (likepost.getUsername() != null) {
            likepostDto.setUsername(userInfoService.convertToUserDto(likepost.getUsername()));
        }
        UserPostService userPostService = new UserPostService();
        if (likepost.getPost() != null) {
            likepostDto.setPost(userPostService.convertToDto(likepost.getPost()));
        }
        return likepostDto;
    }

    // Convert Comment to CommentDto
    private CommentDto convertToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedate(comment.getCreatedate());
        UserInfoService userInfoService = new UserInfoService();
        if (comment.getUsername() != null) {
            commentDto.setUsername(userInfoService.convertToUserDto(comment.getUsername()));
        }
        UserPostService userPostService = new UserPostService();
        if (comment.getPost() != null) {
            commentDto.setPost(userPostService.convertToDto(comment.getPost()));
        }
        if (comment.getImage() != null) {
            commentDto.setImage(comment.getImage());
        }
        return commentDto;
    }

    // Convert Share to ShareDto
    private ShareDto convertToShareDto(Share share) {
        ShareDto shareDto = new ShareDto();
        shareDto.setId(share.getId());
        shareDto.setContent(share.getContent());
        shareDto.setCreatedate(share.getCreatedate());
        UserInfoService userInfoService = new UserInfoService();
        if (share.getUsername() != null) {
            shareDto.setUsername(userInfoService.convertToUserDto(share.getUsername()));
        }
        UserPostService userPostService = new UserPostService();
        if (share.getPost() != null) {
            shareDto.setPost(userPostService.convertToDto(share.getPost()));
        }
        return shareDto;
    }
}
