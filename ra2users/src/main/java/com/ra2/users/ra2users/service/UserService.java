package com.ra2.users.ra2users.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

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

import com.ra2.users.ra2users.logging.CustomLogging;
import com.ra2.users.ra2users.model.User;
import com.ra2.users.ra2users.repositori.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public List<User> findAll(){
        CustomLogging.info("Consultant tots els users", "UserSerice", "findAll");
        return userRepository.findAll();
    }

    public List<User> findOne(Long id){
        CustomLogging.info("Consultant l'user amb id = " + id, "UserSerice", "findOne");
        List<User> lista = userRepository.findOne(id);
        if (lista.isEmpty()){
            CustomLogging.error("L'usuari amb id = " + id + " no existeix", "UserSerice", "findOne");
        }
        return lista;
    }

    public int save(String nom, String desc, String email, String passwd, Timestamp ultimAcces, Timestamp dataCreated, Timestamp dataUpdated){
        CustomLogging.info("Creant un user", "UserSerice", "save");        
        int creats = userRepository.save(nom, desc, email, passwd, ultimAcces, dataCreated, dataUpdated);
        if(creats >= 1){
            CustomLogging.info("User creat correctament", "UserSerice", "save");        
        } else {
            CustomLogging.error("L'user amb nom " + nom + " no s'ha creat. ", "UserSerice", "save");        
        }
        return creats;
    }

    public int updateUser(Long id, String nom, String desc, String email, String passwd){
        CustomLogging.info("Modificant l'estudiant amb id = " + id, "UserSerice", "updateUser");                
        int creats = userRepository.updateUser(id, nom, desc, email, passwd);
        if(creats >= 1){
            CustomLogging.info("User modificat correctament", "UserSerice", "updateUser");         
        } else {
            CustomLogging.error("L'user amb id: " + id + " no existeix" , "UserSerice", "save");        
        }
        return creats;
    }

    public int updateNameUser(Long id, String name){
        CustomLogging.info("Modificant l'user amb id = " + id, "UserSerice", "updateNameUser"); 
        return userRepository.updateNameUser(id, name);
    }

    public int deleteUser(Long id){
        CustomLogging.info("Borrant l'user amb id = " + id, "UserService", "deleteUser");
        int eliminats = userRepository.deleteUser(id);
        if(eliminats >= 1){
            CustomLogging.info("User amb id = " + id + " eliminat correctament", "UserService", "deleteUser");
        } else {
            CustomLogging.error("User no existeix", "UserService", "deleteUser");
        }
        return eliminats;
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
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()));
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

    public String uploadMassiveUsersJson(MultipartFile jsonFile){
        int usersCreats = 0;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode usuaris = mapper.readTree(jsonFile.getInputStream());
            for(JsonNode usuari : usuaris){
            User user = new User();
            user.setNom(usuari.get("nom").asText());
            user.setDesc(usuari.get("desc").asText());
            user.setEmail(usuari.get("email").asText());
            user.setPasswd(usuari.get("passwd").asText());
            userRepository.save(user.getNom(), user.getDesc(), user.getEmail(), user.getPasswd(), null, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
            usersCreats++;
            }
            Path pathFitxer = Paths.get("private/json_processed/" + jsonFile.getOriginalFilename());
            Path pathDirectory = Paths.get("private/json_processed");

            Files.createDirectories(pathDirectory);
            Files.copy(jsonFile.getInputStream(), pathFitxer);
            
        } catch (IOException e) {
            e.printStackTrace();
            return("Usuaris no creats");
        }

        return usersCreats + " usuaris creats.";
    }
}
