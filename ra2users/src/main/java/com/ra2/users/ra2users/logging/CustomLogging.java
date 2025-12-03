package com.ra2.users.ra2users.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class CustomLogging {
    public static Path obtenirFitxerAvui(){
        String rutaCarpeta = "logs/";
        String rutaFitxerAvui = "logs/log-" + LocalDate.now() + ".log";
        Path rutaArxiu = Path.of(rutaFitxerAvui);
        if(Files.exists(rutaArxiu)){
            return rutaArxiu;
        } else {
            try{
                Files.createDirectories(Path.of(rutaCarpeta));
                Files.createFile(rutaArxiu);
            } catch(Exception e){
                System.out.println("NO ES POT CREAR EL FITXER");
            } 
            return rutaArxiu;
        }
    }
    public static void escriuLog(String nivell, String missatge, String classe, String function){
        Path path = obtenirFitxerAvui();

        try(BufferedWriter fileWriter = Files.newBufferedWriter(
            path,
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        )){
            String timestamp = (new Timestamp(System.currentTimeMillis())).toString();
            String infoLog = "[" + timestamp + "] " + nivell + " - " + classe + " - " + function + " - " + missatge;
            fileWriter.write(infoLog);
            fileWriter.newLine();
        }  catch (Exception e){
            System.out.println("ERROR - NO ES POT GUARDAR AL FITXER LOGS" + e);
        }
        
    } 

    public static void error(String missatge, String classe, String function){
        escriuLog("ERROR", missatge, classe, function);
    }
    public static void warning(String missatge, String classe, String function){
        escriuLog("WARNING", missatge, classe, function);
    }
    public static void info(String missatge, String classe, String function){
        escriuLog("INFO", missatge, classe, function);
    }
}
