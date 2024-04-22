package com.dev.template_crud.service;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.template_crud.model.Role;
import com.dev.template_crud.model.Token;
import com.dev.template_crud.model.User;
import com.dev.template_crud.repository.RoleRepository;
import com.dev.template_crud.repository.TokenRepository;
import com.dev.template_crud.repository.UserRepository;


@Service
public class UserService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private   TokenRepository tokenRepository;
    @Autowired
    private  RoleRepository roleRepository;
    
    
    
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
    public  User getUserByName(String name) {
        // Récupérer tous les utilisateurs par leur nom
        // User allUsers = userRepository.findByName(name);
        User user = userRepository.findByName(name);
        // User user=new User();
        // Parcourir la liste des utilisateurs
        // for (User user : allUsers) {
        //     System.out.println(user.getName() + "  " + name);
            if (user.getName().equals(name)) {
                return user;
            }
        // }
        return null;
    }

    public  boolean userEmailExist(String email){
        User user = userRepository.findByEmail(email);
        return user != null;
    }
    
    public  User getUserByEmail(String email) {
        User users = userRepository.findByEmail(email);
        
        // if (!null) {
        //     return users; 
        // } else {
        //     return null; 
        // }
        return users;
    }
    
    public  User getUser(String identifiant) throws Exception{
        User user1=getUserByEmail(identifiant);
        if(user1!=null){
            return user1;
        }
        return null;
    }
    private  Role checkRoleExist(){
        Role role = new Role();
        role.setRole("user");
        return roleRepository.save(role);
    }

        
    public  void saveUser(User user) throws Exception {
        // int userid=user.getId();
        try {
            Role roles =roleRepository.findByRole("user");
        if(roles == null){
            roles = checkRoleExist();
        }
        User userCrypt = user;
        userCrypt.setPassword(encryptUserPassword(user.getPassword()));
        userRepository.save(userCrypt);
        // userRepository.save(user);
        user.setListRole(Arrays.asList(roles));
        User founduser =userRepository.findByEmail(user.getEmail());
        String token = genereToken(founduser.getId());
        Token tokenEntity = new Token();
        tokenEntity.setUser(founduser);
        tokenEntity.setToken(token);
        tokenRepository.save(tokenEntity);
        
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    public String genereToken(int id){
        String crypteUserid=encryptUserId(id);
        return crypteUserid+"a1";

    }
    public static String encryptUserId(int id) {
        try {
            // Création d'un objet MessageDigest avec l'algorithme SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Conversion de l'ID de l'utilisateur en une séquence d'octets
            byte[] idBytes = String.valueOf(id).getBytes();

            // Application du hachage sur l'ID de l'utilisateur
            byte[] hashBytes = digest.digest(idBytes);

            // Conversion du hachage en une chaîne hexadécimale
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                // Convertir chaque octet en sa représentation hexadécimale
                String hex = Integer.toHexString(0xff & hashByte);
                // S'assurer que chaque octet est représenté par deux caractères hexadécimaux
                if (hex.length() == 1) {
                    hexString.append('0'); // Si la représentation hexadécimale est un seul caractère, ajouter un '0' pour la compléter
                }
                hexString.append(hex);
            }

            // Retourner la chaîne hexadécimale résultante
            return hexString.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encryptUserPassword(String password) {
        try {
            // Création d'un objet MessageDigest avec l'algorithme SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Conversion de l'ID de l'utilisateur en une séquence d'octets
            byte[] idBytes = String.valueOf(password).getBytes();

            // Application du hachage sur l'ID de l'utilisateur
            byte[] hashBytes = digest.digest(idBytes);

            // Conversion du hachage en une chaîne hexadécimale
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                // Convertir chaque octet en sa représentation hexadécimale
                String hex = Integer.toHexString(0xff & hashByte);
                // S'assurer que chaque octet est représenté par deux caractères hexadécimaux
                if (hex.length() == 1) {
                    hexString.append('0'); // Si la représentation hexadécimale est un seul caractère, ajouter un '0' pour la compléter
                }
                hexString.append(hex);
            }

            // Retourner la chaîne hexadécimale résultante
            return hexString.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public int login(String email,String password) throws Exception{
        // List<User> users=userRepository.findAll();
        User user = this.userRepository.findByEmail(email);
        if(user.getEmail().equals(email)){
            if (user.getPassword().equals(encryptUserPassword(password))) {
                return 1;
            }
            else {
                return 0;
            }
        }
        return -1;
        // for(User user:users){
        //     if (user.getEmail().equals(email)) {

        //     }
        //     return 0;
        // }
        // return -1;
    }

}
