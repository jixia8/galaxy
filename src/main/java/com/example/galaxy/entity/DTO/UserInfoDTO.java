package com.example.galaxy.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoDTO {
    private String userAccount;
    private List<String> roles;
    private List<String> permissions;
}