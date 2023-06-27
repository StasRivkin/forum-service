package telran.java47.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import telran.java47.post.dao.PostRepository;
import telran.java47.post.model.Post;

@Service
@RequiredArgsConstructor
public class CustomWebSecurity {
    final PostRepository postRepository;

    public boolean checkPostAuthor(String postId, String name) {
        Post post = postRepository.findById(postId).orElse(null);
        return post != null && name.equalsIgnoreCase(post.getAuthor());
    }
}
