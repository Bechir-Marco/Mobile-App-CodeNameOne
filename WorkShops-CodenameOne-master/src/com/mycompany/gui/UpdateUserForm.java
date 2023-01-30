/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gui;

import com.codename1.components.FloatingHint;
import static com.codename1.push.PushContent.setTitle;
import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.mycompany.entities.User;
import com.mycompany.services.UserService;


/**
 *
 * @author tchet
 */
public class UpdateUserForm extends BaseForm {
    Form current;
    public UpdateUserForm(Resources res , User r) {
         super("Newsfeed",BoxLayout.y());
    
        Toolbar tb = new Toolbar(true);
        current = this ;
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Ajout Reclamation");
        getContentPane().setScrollVisible(false);
        
        
        super.addSideMenu(res);
        
        TextField firstName = new TextField(r.getFirstName(), "firstName" , 20 , TextField.ANY);
        TextField email = new TextField(r.getEmail(), "Email" , 20 , TextField.EMAILADDR);
 
       
        
        firstName.setUIID("NewsTopLine");
        email.setUIID("NewsTopLine");
       
        
        firstName.setSingleLineTextArea(true);
        email.setSingleLineTextArea(true);
        
        Button btnModifier = new Button("Edit");
       btnModifier.setUIID("Button");
       
       //Event onclick btnModifer
       
       btnModifier.addPointerPressedListener(l ->   { 
           
           r.setEmail(email.getText());
           r.setFirstName(firstName.getText());
           
         
       //appel fonction modfier reclamation men service
       
       if(UserService.getInstance().editUser(r)) { // if true
           new UsersListForm(res).show();
       }
        });
       Button btnAnnuler = new Button("Cancel");
       btnAnnuler.addActionListener(e -> {
           new UsersListForm(res).show();
       });
       
       
       Label l2 = new Label("");
       
       Label l3 = new Label("");
       
       Label l4 = new Label("");
       
       Label l5 = new Label("");
       
        Label l1 = new Label();
        
        Container content = BoxLayout.encloseY(
                l1, l2, 
                new FloatingHint(firstName),
                createLineSeparator(),
                new FloatingHint(email),
                createLineSeparator(),
                createLineSeparator(),//ligne de séparation
                btnModifier,
                btnAnnuler
                
               
        );
        
        add(content);
        show();
        
        
    }
}
