package com.system.fsharksocialmedia.services;

import com.system.fsharksocialmedia.dtos.PostDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.entities.Post;
import com.system.fsharksocialmedia.models.PostModel;
import com.system.fsharksocialmedia.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//code Mẫu
@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    //    public PostDto savePosition(PostModel model) {
//        System.out.println("Model Service: "+model);
//        if (model == null) return null;
//        Post post = new Post();
//        post.setUsername(null);
//        post.setContent(model.getContent());
//        post.setCreatedate(new Date().toInstant());
//        Post savePost = postRepository.save(post);
//        return convertToDto(savePost);
//    }
//   Get all post
    public Page<PostDto> getPost(int page, int size, String search) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Post> posts = (search == null || search.isEmpty())
                    ? postRepository.findAll(pageable)
                    : postRepository.findByContentContainingIgnoreCase(search, pageable);
            return posts.map(this::convertToDto);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error retrieving post list: " + e.getMessage());
        }
    }

    public PostDto getPostById(Integer postId) {
        try {
            Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not id: " + postId));
            return convertToDto(post);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error retrieving post: " + e.getMessage());
        }
    }

    public PostDto updatePost(PostModel postModel, Integer postId) {
        try {
            Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not id: " + postId));
            if (post != null) {
                post.setContent(postModel.getContent());
            }
            return convertToDto(postRepository.save(post));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating post: " + e.getMessage());
        }
    }

    //    // Delete a post by its ID
//    public void deletePost(int postId) {
//        if (!postRepository.existsById(postId)) {
//            throw new RuntimeException("Post not found with ID: " + postId);
//        }
//        postRepository.deleteById(postId);
//    }
    //    convert entity post to dto
    private PostDto convertToDto(Post post) {
        long commentCount = postRepository.countCmtByPost(post.getId());
        long likeCount = postRepository.countLikeByPost(post.getId());
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setContent(post.getContent());
        postDto.setCommentCount(commentCount);
        postDto.setLikeCount(likeCount);
        UserDto userDto = null;
        if (post.getUsername() != null) {
            userDto = new UserDto();
            userDto.setUsername(post.getUsername().getUsername());
        }
        postDto.setUsername(userDto);
        return postDto;
    }


    //    convert dto post to entity
    public Post convertToEntity(PostDto dto) {
        if (dto == null) return null;
        Post post = new Post();
        post.setId(dto.getId());
        post.setContent(dto.getContent());
        post.setCreatedate(dto.getCreatedate());
        return post;
    }

    public List<PostDto> convertToDTOList(List<Post> positions) {
        return positions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


}
