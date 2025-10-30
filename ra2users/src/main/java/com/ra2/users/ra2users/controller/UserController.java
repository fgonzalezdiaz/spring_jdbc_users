package com.ra2.users.ra2users.controller;

import java.sql.Timestamp;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ra2.users.ra2users.model.User;
import com.ra2.users.ra2users.repositori.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;
    
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        user.setDataCreated(new Timestamp(System.currentTimeMillis()));
        user.setDataUpdated(new Timestamp(System.currentTimeMillis()));
        int result = userRepository.save(user.getNom(), user.getDesc(), user.getEmail(), user.getPasswd(), user.getUltimAcces(),user.getDataCreated(), user.getDataUpdated());
        if(result > 0){
            return ResponseEntity.status(201).body("Usuari crear correctament");
        } else {
            return ResponseEntity.internalServerError().body("Error de creacio");
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()) return ResponseEntity.ok().body(null);
        return ResponseEntity.ok().body(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getMethodName(@PathVariable Long id) {
        List<User> users = userRepository.findOne(id);
        if(users.isEmpty()) return ResponseEntity.ok().body(null);
        return ResponseEntity.ok().body(users.get(0));
    }
    
    
}
