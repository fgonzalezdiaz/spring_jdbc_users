package com.ra2.users.ra2users.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ra2.users.ra2users.model.User;
import com.ra2.users.ra2users.repositori.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public List<User> findAll(){
        return userRepository.findAll(); 
    }

    public List<User> findOne(Long id){
        return userRepository.findOne(id);
    }

    public int save(String nom, String desc, String email, String passwd, Timestamp ultimAcces, Timestamp dataCreated, Timestamp dataUpdated){
        return userRepository.save(nom, desc, email, passwd, ultimAcces, dataCreated, dataUpdated);
    }

    public int updateUser(Long id, String nom, String desc, String email, String passwd){
        return userRepository.updateUser(id, nom, desc, email, passwd);
    }

    public int updateNameUser(Long id, String name){
        return userRepository.updateNameUser(id, name);
    }

    public int deleteUser(Long id){
        return userRepository.deleteUser(id);
    }



    public String uploadImage(Long id, MultipartFile imageFile) {
        List<User> users = userRepository.findOne(id);
        if(!users.isEmpty()){
            try{
                Path pathFitxer = Paths.get("private/images/" + imageFile.getOriginalFilename());
                Path pathDirectory = Paths.get("private/images");

                Files.createDirectories(pathDirectory);
                Files.copy(imageFile.getInputStream(), pathFitxer);

                if(userRepository.saveImagePath(pathFitxer.toString(), users.get(0).getId()) >= 1){
                    return "Upload correctly";
                } else {
                    return "Upload gone wrong";
                }

            } catch (Exception e) {
                return "Error";
            }
        } else {
            return "No existeix l'usuari";
        }
    }

    public String uploadMassiveUsers(MultipartFile csvFile) throws Exception{
        int usersCreats = 0;
        try{BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()));
            String line;
            while((line = bufferedReader.readLine()) != null){
                String arguments[] = line.split(",");
                usersCreats += userRepository.save(arguments[0], arguments[1], arguments[2], arguments[3], null, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
            }

            Path pathFitxer = Paths.get("private/csv_processed/" + csvFile.getOriginalFilename());
            Path pathDirectory = Paths.get("private/csv_processed");

            Files.createDirectories(pathDirectory);
            Files.copy(csvFile.getInputStream(), pathFitxer);
            
        } catch (Exception e){
            return e.getMessage();
        }
        return "Has introduit " + Integer.toString(usersCreats) + " usuaris";
    }
}
