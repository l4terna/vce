package com.vce.vce._shared.security;

import com.vce.vce.user.User;
import com.vce.vce.usersession.UserSession;

public class UserContextHolder {
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();
    private static final ThreadLocal<UserSession> currentSession = new ThreadLocal<>();

    public static void setContext(User user, UserSession session) {
        currentUser.set(user);
        currentSession.set(session);
    }

    public static User getCurrentUser() {
        return currentUser.get();
    }

    public static UserSession getCurrentSession() {
        return currentSession.get();
    }

    public static void clear() {
        currentUser.remove();
        currentSession.remove();
    }
}
