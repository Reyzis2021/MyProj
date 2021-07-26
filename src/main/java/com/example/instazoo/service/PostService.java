package com.example.instazoo.service;

import com.example.instazoo.dto.PostDTO;
import com.example.instazoo.entity.ImageModel;
import com.example.instazoo.entity.Post;
import com.example.instazoo.entity.User;
import com.example.instazoo.exceptions.PostNotFoundException;
import com.example.instazoo.repository.ImageRepository;
import com.example.instazoo.repository.PostRepository;
import com.example.instazoo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {


    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public Post createPost(PostDTO postDTO, Principal principal){
        User user = getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLikes(0);
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());

        LOG.info("Get Post for User : {} ", user.getEmail());
        return postRepository.save(post);
    }

    public Post getPostById(Long postId, Principal principal){
        User user = getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId,user).orElseThrow(
                () ->  new PostNotFoundException("Post cannot be found for username : " +
                        user.getEmail())
        );

    }

    public List<Post> getAllPostsForUser(Principal principal){
        User user = getUserByPrincipal(principal);

        return postRepository.findAllByUserOrderByCreatedDateDesc(user);

    }

    public Post likePost(Long postId, String username){

        Post post = postRepository.findById(postId).orElseThrow(
                () ->  new PostNotFoundException("Post cannot be found"));

        Optional<String> userLiked = post.getLikedUsers()
                .stream()
                .filter(u -> u.equals(username)).findAny();

        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(username);
        }
        else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(username);
        }
        return postRepository.save(post);
    }

    public List<Post>  getAllPosts(){
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    public void deletePost(Long postId, Principal principal){

        Post post = getPostById(postId, principal);
        Optional<ImageModel> imageModel = imageRepository.findByPostId(postId);
        postRepository.delete(post);
        imageModel.ifPresent(imageRepository::delete);

    }
    private User getUserByPrincipal(Principal principal){
        String username = principal.getName();

        return userRepository.findUserByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Username not found with username " +
                        username));
    }
}
