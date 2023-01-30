
package com.mycompany.gui;

import com.codename1.components.ScaleImageLabel;
import com.codename1.capture.Capture;

import com.codename1.ui.CheckBox;
import com.codename1.ui.ComboBox;

import com.codename1.ui.Component;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.mycompany.entities.Session;
import com.mycompany.services.UserService;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.ScaleImageLabel;
import com.codename1.io.FileSystemStorage;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.mycompany.services.UserService;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.mycompany.gui.BaseForm;
import java.io.IOException;


    
/**
 * The user profile form
 *
 * @author Shai Almog
 */
public class ProfileForm extends BaseForm {

    public ProfileForm(Resources res) {
        super(BoxLayout.y());
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        tb.setTitle("My Profile");
        getContentPane().setScrollVisible(true);
        super.addSideMenu(res);
           
        Image img = res.getImage("back-logo.png");
//        if(img.getHeight() > Display.getInstance().getDisplayHeight() / 3) {
//            img = img.scaledHeight(Display.getInstance().getDisplayHeight() / 3);
//        }
        ScaleImageLabel sl = new ScaleImageLabel(img);
        sl.setUIID("BottomPad");
        sl.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);

        Button modiff= new Button ("Edit");
        Button picture= new Button ("Image");
        
        add(LayeredLayout.encloseIn(
                sl,
                BorderLayout.south(
                    GridLayout.encloseIn(3,
                            FlowLayout.encloseCenter()
                )
                )
                       
        ));
        Session usSession= new Session();
        String us = usSession.getFirstName();
        System.out.println(usSession.getFirstName());
        
        TextField userName= new TextField(usSession.getFirstName() , "email");
        userName.setUIID("TextFieldBack");
        addStringValue("UserName", userName);
        
        TextField password= new TextField(usSession.getPassowrd(), "password", 20, TextField.PASSWORD);
        password.setUIID("TextFieldBack");
        addStringValue("pASSWORD", password);

        TextField email= new TextField(usSession.getEmail(), "email", 20, TextField.EMAILADDR);
        email.setUIID("TextFieldBack");
        addStringValue("email", email);
        
        
        picture.setUIID("Update ");
        modiff.setUIID("Edit");
        addStringValue("", picture);
         addStringValue("", modiff);

        TextField path= new TextField("");
       
//        picture.addActionListener(e-> { 
//       String i =  Capture.capturePhoto(Display.getInstance().getDisplayWidth(),-1) ;
//        
//        if (i != null) {      
//            Image im = null;
//           try {
//               im = Image.createImage(i);
//           } catch (IOException ex) {
//           }
//          im = im.scaled(res.getImage("photo-profile.jpg").getWidth(),
//                res.getImage("photo-profile.jpg").getHeight());
//        System.out.println(i);
//        path.setText(i);
//        }
//        });
        modiff.addActionListener((edit)-> {
            InfiniteProgress ip = new InfiniteProgress();
        final Dialog ipDig = ip.showInfiniteBlocking();
        
        UserService.EditUser(userName.getText(),password.getText(),email.getText());
        usSession.setFirstName(userName.getText());
        usSession.setPassowrd(password.getText());
        usSession.setEmail(email.getText());
        usSession.setImage(userName.getText()+".jpg");
        Dialog.show("Success", "Edit successfully", "OK",null);
        ipDig.dispose();
       refreshTheme(); 
    });
    }
    
    private void addStringValue(String s, Component v) {
        add(BorderLayout.west(new Label(s, "PaddedLabel")).
                add(BorderLayout.CENTER, v));
        add(createLineSeparator(0xeeeeee));
    }
}
