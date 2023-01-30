/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.services;

import com.codename1.components.FloatingHint;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.SpanLabel;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.MultipartRequest;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.Resources;
import com.mycompany.entities.User;
import com.mycompany.services.UserService;
import com.mycompany.utils.Statics;
import com.sun.mail.smtp.SMTPTransport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
 import com.mycompany.entities.Session;
import com.mycompany.gui.ProfileForm;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 *
 * @author tchet
 */
public class UserService {
    //singleton est de restreindre l'instanciation d'une classe à un seul objet
     public static UserService instance = null;
     public static boolean result= true;  
      String json;
     //initialitation connection request
     private ConnectionRequest req;
     
     public static UserService getInstance(){
     if (instance == null)
         instance = new UserService ();
     return instance;
     }
     
     public UserService(){
     req= new ConnectionRequest(); 
     }
     
     //to signup
     
     public void  signUp(TextField username, TextField email,TextField password ,TextField confirmPassword, ComboBox<String> roles , Resources res)
     {
  
             String url = Statics.BASE_URL +"/user/signup?email="+email.getText().toString()+"&password="+password.getText().toString()
                     +"&roles="+roles.getSelectedItem().toString()+"&firstName="+username.getText().toString();
                              
         req.setUrl(url);

         //control saisie
         if (email.getText().equals(" ")&& password.getText().equals(" "))
         {
         Dialog.show("Error","fill the fields", "OK", null);
         
         }
         
         //when there is an url execution
         req.addResponseListener((e)-> {
     
     //bring the data from the form
     byte[] data = (byte[]) e.getMetaData(); // prepare it metadata to get id of each textfield
     String resData = new String(data); // then we take the content
             System.out.println("data ===>"+resData);
             
     }
         );
         // after the request execution( url) we wait for the server response   
          NetworkManager.getInstance().addToQueueAndWait(req);
             
}
     
     //to SignIn
   
    
    public void signin(TextField email,TextField password, Resources rs ) {
        
        
        String url = Statics.BASE_URL +"/user/signin?email="+email.getText().toString()+"&password="+password.getText().toString();
        req = new ConnectionRequest(url, false); //false ya3ni url mazlt matba3thtich lel server
        req.setUrl(url);
        
        req.addResponseListener((e) ->{
            
            JSONParser j = new JSONParser();
            
            String json = new String(req.getResponseData()) + "";
            
            
            try {
            
            if(json.equals("failed")) {
                Dialog.show("Authentification failed","email or password are incorrect","OK",null);
            }
            else {
                System.out.println("data =="+json);
                
                Map<String,Object> user = j.parseJSON(new CharArrayReader(json.toCharArray()));

                //Session 
                float id = Float.parseFloat(user.get("id").toString());
                Session usSession= new Session();
                usSession.setId((int)id);//Brought the loggedin user id and store it in my session 
                
                usSession.setPassowrd(user.get("password").toString());
                usSession.setFirstName(user.get("username").toString());

                usSession.setEmail(user.get("email").toString());
                
                //image 
                
//                if(user.get("image") != null)
//                    Session.setImage(user.get("image").toString());
                
                System.out.println("current_user is=="+usSession.getEmail()+" ,"+usSession.getFirstName()+" , "+usSession.getPassowrd());
                
                if(user.size() >0 ) // l9a user
                   // new ListReclamationForm(rs).show();//yemchi lel list reclamation
                    new ProfileForm(rs).show();
                    
                    }
            
            }catch(Exception ex) {
                ex.printStackTrace();
            }
            
            
            
        });
    
         //ba3d execution ta3 requete ely heya url nestanaw response ta3 server.
        NetworkManager.getInstance().addToQueueAndWait(req);
    }
    
    
    public static void EditUser(String userName, String password, String email){
    
       
    String url=Statics.BASE_URL +"/user/edit?firstName="+userName+"&password="+password+"&email="+email;
    MultipartRequest req = new MultipartRequest();
    
   Session usSession= new Session();
    req.setUrl(url);
    req.setPost(true);
    req.addArgument("id", String.valueOf(usSession.getId()));
    req.addArgument("username", userName);
    req.addArgument("password", password);
    req.addArgument("email", email);
    System.out.println(email);
    req.addResponseListener((response)-> {
        byte[] data = (byte[]) response.getMetaData();
        String s = new String(data);
        System.out.println(s);
        if(s.equals("success")) {
        } else {
            Dialog.show("Error", "can't edit user", "OK", null);
        }
        
    }) ;
    NetworkManager.getInstance().addToQueueAndWait(req);
    }
    
    
    
    
    public ArrayList<User>displayUsers() {
        ArrayList<User> result = new ArrayList<>();
        
        String url = Statics.BASE_URL+"/user/list";
        req.setUrl(url);
        
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                JSONParser jsonp ;
                jsonp = new JSONParser();
                
                try {
                    Map<String,Object>mapUser = jsonp.parseJSON(new CharArrayReader(new String(req.getResponseData()).toCharArray()));
                    
                    List<Map<String,Object>> listOfMaps =  (List<Map<String,Object>>) mapUser.get("root");
                    
                    for(Map<String, Object> obj : listOfMaps) {
                        User re = new User ();
                        
                        //dima id fi codename one float 5outhouha
                        float id = Float.parseFloat(obj.get("id").toString());
                        

                        String firstName = obj.get("firstName").toString();
                         String email = obj.get("email").toString();

                        
                        re.setId((int)id);
                        re.setEmail(email);
                        re.setFirstName(firstName);
                        
                        //insert data into ArrayList result
                        result.add(re);
                       
                    
                    }
                    
                }catch(Exception ex) {
                    
                    ex.printStackTrace();
                }
            
            }
        });
        
      NetworkManager.getInstance().addToQueueAndWait(req);//execution ta3 request sinon yet3ada chy dima nal9awha

        return result;
        
        
    }
    
    
    //Delete 
    public boolean deleteUser(int id ) {
        String url = Statics.BASE_URL +"/user/delete?id="+id;
        
        req.setUrl(url);
        
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                    
                    req.removeResponseCodeListener(this);
            }
        });
        
        NetworkManager.getInstance().addToQueueAndWait(req);
        return  result;
    }
    
    
    
    
     //Update 
    public boolean editUser(User user) {
        String url = Statics.BASE_URL +"/user/edit?id="+user.getId()+"&firstName="+user.getFirstName()+"&email="+user.getEmail()+"&password="+user.getPassword();
        req.setUrl(url);
        
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                result = req.getResponseCode() == 200 ;  // Code response Http 200 ok
                req.removeResponseListener(this);
            }
        });
        
    NetworkManager.getInstance().addToQueueAndWait(req);//execution ta3 request sinon yet3ada chy dima nal9awha
    return result;
        
    }
    
 
    public String getPasswordByEmail(String email, Resources rs ) {
        
        
        String url = Statics.BASE_URL+"/user/password?email="+email;
        req = new ConnectionRequest(url, false); //false ya3ni url mazlt matba3thtich lel server
        req.setUrl(url);
        
        req.addResponseListener((e) ->{
            
            JSONParser j = new JSONParser();
            
             json = new String(req.getResponseData()) + "";
            
            
            try {

                System.out.println("data =="+json);
                
                Map<String,Object> password = j.parseJSON(new CharArrayReader(json.toCharArray()));

            }catch(Exception ex) {
                ex.printStackTrace();
            }
 
        });
    
         //after url execution we w8 server response
        NetworkManager.getInstance().addToQueueAndWait(req);
    return json;
    }

}
  
