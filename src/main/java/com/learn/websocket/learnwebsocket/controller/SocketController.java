package com.learn.websocket.learnwebsocket.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.websocket.learnwebsocket.model.CustomMessage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
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

    @GetMapping("/home")
    public String homeTwo() {
        
        return "Or Bhai its Working";
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
    
    @GetMapping("/movie-data")
    public List<Map<String, Object>> getMovieData() throws IOException {
        System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
        return getMovie();
    }
    @GetMapping("/city-data")
    public List<Map<String, Object>> getCityData() throws IOException {
        System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
        return getCity();
    }

    public List<Map<String, Object>> getMovie() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("movie.json");
        return mapper.readValue(resource.getInputStream(), new TypeReference<List<Map<String, Object>>>() {});
    }

    public List<Map<String, Object>> getCity() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("city.json");
        return mapper.readValue(resource.getInputStream(), new TypeReference<List<Map<String, Object>>>() {});
    }

//////////////////////////////////////////////////////////DUMMY URL/////////////////////////////////////////////////////////
    @GetMapping("/dummy-url-save")
    public Map<String, Object> dummyUrl() {
        final String uri = "http://localhost:3000/dummy-url-save";
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Alex");
        map.put("email", "ajordanson0@usa.gov");
        map.put("password", "1232141241");
        map.put("age", 12);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(uri, map, String.class);

        try {
            System.out.println(result);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return map;
    }

    @GetMapping("/dummy-url-read")
    public String dummyUrlRead() {
        // AWS hosted not hit on localhost or 127.0.0.1 so i am using ngrok
        final String uri = "https://bd22-2409-40c0-1028-e267-24ae-3184-e189-8ebe.ngrok-free.app/dummy-url-read";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        try {
            System.out.println(result);
            return result;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return e.getMessage();
        }

    }

    @GetMapping("/fake-json-data")
    public String fakeJsonData() {
        final String uri = "https://jsonplaceholder.typicode.com/users";
        Map<String, Object> map = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        try {
            System.out.println(result);
            return result;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return e.getMessage();
        }

    }
//////////////////////////////////////////////////////////DUMMY URL/////////////////////////////////////////////////////////
    
    
}
