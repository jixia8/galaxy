package com.example.galaxy.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserVisitRecord {
    private Long userId;
    private String userAccount;
    private String userName;
    private Integer visitCount;
    private Date recordDate;
}