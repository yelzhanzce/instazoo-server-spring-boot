package com.yelzhan.demo.service;

import com.yelzhan.demo.dto.PostDTO;
import com.yelzhan.demo.entity.ImageModel;
import com.yelzhan.demo.entity.Post;
import com.yelzhan.demo.entity.User;
import com.yelzhan.demo.exceptions.PostNotFoundException;
import com.yelzhan.demo.repository.ImageRepository;
import com.yelzhan.demo.repository.PostRepository;
import com.yelzhan.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);


    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public PostService(UserRepository userRepository, PostRepository postRepository, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
    }
    
    public Post createPost(PostDTO postDTO, Principal principal){
        User user = getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setLikes(0);

        LOG.info("Saving Post for User {}", user.getEmail());

        return postRepository.save(post);
    }

    public List<Post> getAllPosts(){
        return postRepository.findAllByOrderByCreateDateDesc();
    }

    public Post getPostById(Long postId, Principal principal){
        User user = getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post cannot found for User: " + user.getEmail()));
    }

    public List<Post> getAllPostsByUser(Principal principal){
        User user = getUserByPrincipal(principal);
        return postRepository.findAllByUserOrderByCreateDateDesc(user);
    }

    public Post likePost(Long postId, String username){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot found"));
        Optional<String> userLiked = post.getLiked()
                .stream()
                .filter(u -> u.equals(username)).findAny();
        if(userLiked.isPresent()){
            post.setLikes(post.getLikes() - 1);
            post.getLiked().remove(username);
        }else{
            post.setLikes(post.getLikes() + 1);
            post.getLiked().add(username);
        }

        return postRepository.save(post);
    }

    public void deletePost(Long postId, Principal principal){
        Post post = getPostById(postId, principal);
        Optional<ImageModel> imageModel = imageRepository.findByPostId(post.getId());
        postRepository.delete(post);
//        if(imageModel.isPresent()){
        imageModel.ifPresent(imageRepository::delete);
//            imageRepository.delete();
//        }
    }

    private User getUserByPrincipal(Principal principal){
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }
}
