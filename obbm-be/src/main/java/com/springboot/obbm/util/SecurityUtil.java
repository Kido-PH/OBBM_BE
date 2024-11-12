package com.springboot.obbm.util;

import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.model.User;
import com.springboot.obbm.respository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityUtil {
    UserRepository userRepository;

    public User getCurrentUser() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        return userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));
    }

    public boolean isCurrentUser(String userId) {
        User currentUser = getCurrentUser();
        return currentUser.getUserId().equals(userId);
    }
}
