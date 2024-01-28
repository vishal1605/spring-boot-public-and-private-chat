package com.learn.websocket.learnwebsocket.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.learn.websocket.learnwebsocket.model.CustomMessage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class SocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    List<CustomMessage> connectedUser = new ArrayList<CustomMessage>();

    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }

    @MessageMapping("/connected")
    @SendTo("/all/connected")
    public CustomMessage addUser(@Payload CustomMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        connectedUser.add(chatMessage);
        System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
        return chatMessage;
    }

    @MessageMapping("/public")
    @SendTo("/all/public/message")
    public CustomMessage sendMessagePublic(@Payload CustomMessage message, SimpMessageHeaderAccessor headerAccessor){
        message.setSendDate(LocalDate.now().toString());
        System.out.println("WROKING/////////////////////////////////////////PUBLIC");
        return message;
    }

    @MessageMapping("/private-message")
    public CustomMessage sendMessagePrivate(@Payload CustomMessage message, SimpMessageHeaderAccessor headerAccessor){
        message.setSendDate(LocalDate.now().toString());
        simpMessagingTemplate.convertAndSendToUser(message.getReciever(), "/private", message);
        return message;
    }

    @GetMapping("/get-connected-user")
    public List<CustomMessage> getConnectedUser() {
        return connectedUser;
    }
    
    
    
}
