package com.bankSim.utils;

import java.util.ArrayList;
import java.util.List;

public class JWTBlacklist {
    private final List<String> blacklistedTokens;

    public JWTBlacklist(List<String> blacklistedTokens) {
        this.blacklistedTokens = new ArrayList<>();
    }

    public void addToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    public void removeToken(String token) {
        blacklistedTokens.remove(token);
    }

    
}
