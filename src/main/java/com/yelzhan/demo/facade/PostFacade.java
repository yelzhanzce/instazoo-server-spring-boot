package com.yelzhan.demo.facade;

import com.yelzhan.demo.dto.PostDTO;
import com.yelzhan.demo.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {

    public PostDTO posToPostDTO(Post post){
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setCaption(post.getCaption());
        postDTO.setLikes(post.getLikes());
        postDTO.setUsername(post.getUser().getUsername());
        postDTO.setLocation(post.getLocation());
        postDTO.setTitle(post.getTitle());
        postDTO.setUsersLiked(post.getLiked());

        return postDTO;
    }
}
