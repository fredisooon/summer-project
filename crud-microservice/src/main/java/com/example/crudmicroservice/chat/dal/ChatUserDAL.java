package com.example.crudmicroservice.chat.dal;


import com.example.crudmicroservice.chat.model.ChatUser;

import java.util.List;

public interface ChatUserDAL {
    List<ChatUser> getAllRooms(String userId);
}
