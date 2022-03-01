package com.yelzhan.demo.repository;

import com.yelzhan.demo.entity.Comment;
import com.yelzhan.demo.entity.Post;
import com.yelzhan.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

    Comment findByIdAndUserId(Long commentId, Long userId);
}
