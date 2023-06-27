package telran.java47.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import telran.java47.accounting.controller.UserAccountController;
import telran.java47.accounting.dao.UserAccountRepository;
import telran.java47.accounting.model.UserAccount;
import telran.java47.post.dao.PostRepository;
import telran.java47.post.model.Post;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomWebSecurity {
    final PostRepository postRepository;
    final UserAccountRepository userAccountRepository;
    final UserAccountController userAccountController;

    public boolean checkPostAuthor(String postId, String name) {
        Post post = postRepository.findById(postId).orElse(null);
        return post != null && name.equalsIgnoreCase(post.getAuthor());
    }

    public boolean checkPasswordExp(String name) {
        UserAccount userAccount = userAccountRepository.findById(name).orElse(null);
        return userAccount != null && userAccount.getPasswordExpirationDate() != null && userAccount.getPasswordExpirationDate().isAfter(LocalDateTime.now());
    }
}
