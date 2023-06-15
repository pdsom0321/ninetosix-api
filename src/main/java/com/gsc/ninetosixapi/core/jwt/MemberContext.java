package com.gsc.ninetosixapi.core.jwt;

public class MemberContext {
    private static final ThreadLocal<Long> memberIdThreadLocal = new ThreadLocal<>();

    public static Long getMemberId() {
        return memberIdThreadLocal.get();
    }

    public static void setMemberId(Long memberId) {
        memberIdThreadLocal.set(memberId);
    }

    public static void clear() {
        memberIdThreadLocal.remove();
    }
}
