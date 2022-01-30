package ru.netology.web;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class UserInfo {
    private final String city;
    private final String name;
    private final String phone;
}
