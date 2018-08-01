package com.atdtl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Administrator
 * @since 2018/8/1 13:24
 */
@RequestMapping("/chat")
@Controller
public class ChatRoomController {

    @GetMapping
    public String fileupload(){
        return "chat-room";
    }
}
