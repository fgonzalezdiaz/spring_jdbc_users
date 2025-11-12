package com.ra2.users.ra2users.controller;

import java.sql.Timestamp;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ra2.users.ra2users.model.User;
import com.ra2.users.ra2users.repositori.UserRepository;
import com.ra2.users.ra2users.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;
    
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        user.setDataCreated(new Timestamp(System.currentTimeMillis()));
        user.setDataUpdated(new Timestamp(System.currentTimeMillis()));
        int result = userService.save(user.getNom(), user.getDesc(), user.getEmail(), user.getPasswd(), user.getUltimAcces(),user.getDataCreated(), user.getDataUpdated());
        if(result > 0){
            return ResponseEntity.status(201).body("Usuari crear correctament");
        } else {
            return ResponseEntity.internalServerError().body("Error de creacio");
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        if(users.isEmpty()) return ResponseEntity.ok().body(null);
        return ResponseEntity.ok().body(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getPorId(@PathVariable Long id) {
        List<User> users = userService.findOne(id);
        if(users.isEmpty()) return ResponseEntity.ok().body(null);
        return ResponseEntity.ok().body(users.get(0));
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> modificaUsuari(@PathVariable Long id, @RequestBody User user_2) {
        int result = userService.updateUser(id, user_2.getNom(), user_2.getDesc(), user_2.getEmail(), user_2.getPasswd());
        String resposta = (result >= 1) ? "Usuari modificat correctament" : "Error, cap usuari modificat";
        return ResponseEntity.ok().body(resposta);
    }
    
    @PatchMapping("/{id}/name")
    public ResponseEntity<String> modificaNomUsuari(@PathVariable Long id, @RequestParam String name){
        int result = userService.updateNameUser(id, name);
        String resposta = (result >= 1) ? "Nom usuari modificat correctament" : "Nom usuari NO modificat";
        return ResponseEntity.ok().body(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminaUsuari(@PathVariable Long id){
        int result = userService.deleteUser(id);
        String resposta = (result >= 1) ? "Usuari esborrat amb exit" : "ERROR, cap usuari esborrat";
        return ResponseEntity.ok().body(resposta); 
    }

    @PostMapping("{id}/image")
    public String postMethodName(@PathVariable Long id, @RequestParam MultipartFile imageFile) {
        return userService.uploadImage(id, imageFile);
    }

    @PostMapping("upload-csv")
    public ResponseEntity<String> uploadCsv(@RequestParam MultipartFile csvFile) throws Exception {
        return ResponseEntity.ok().body(userService.uploadMassiveUsers(csvFile));
    }
    
    
}
