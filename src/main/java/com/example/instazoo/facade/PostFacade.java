package com.example.instazoo.facade;


import com.example.instazoo.dto.PostDTO;
import com.example.instazoo.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {

    public PostDTO postToPostDTO(Post post){

        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setUsername(post.getUser().getUsername());
        postDTO.setCaption(post.getCaption());
        postDTO.setLikes(post.getLikes());
        postDTO.setLocation(post.getLocation());
        postDTO.setTitle(post.getTitle());
        postDTO.setUserLiked(post.getLikedUsers());

        return postDTO;
    }
}
