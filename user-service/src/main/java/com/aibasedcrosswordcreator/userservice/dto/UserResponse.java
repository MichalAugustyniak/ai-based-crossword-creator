package com.aibasedcrosswordcreator.userservice.dto;

import java.util.Set;

public record UserResponse(String id, String username, Set<String> roles) {
}
