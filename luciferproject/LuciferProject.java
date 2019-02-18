/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package luciferproject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import static javafx.application.Platform.exit;

/**
 *
 * @author Administrator
 */

class UserInterface{
    
    Scanner cin;
    
    UserInterface(){
        cin = new Scanner(System.in);
    }

    public int MainMenu(){
        System.out.println("=== Main Menu ===");
        int choice = -1;
        while(choice <= 0 || choice > 4){
            if(choice != -1)
                System.out.println("\nInvalid Choice! Try Again");
            System.out.println("\n Select an Operation: \n 1. ShowAll\n 2. Search Record By ID\n 3. Insert New Record\n 4. Exit");
            choice = cin.nextInt();
            cin.nextLine();
        }
        return choice;
    }
    
    public StudentData GetInsertRecord(){
        String name, enroll;
        System.out.println("Reocrd Insertion");
        System.out.println("Enter Name: ");
        name = cin.nextLine();
        
        System.out.println("Enter Enrollment: ");
        enroll = cin.nextLine();
        
        return new StudentData(-1, name, enroll);
    }
    
    public void ShowAll(ArrayList<StudentData> students){
        System.out.println("Record Show (All)");
        
        for(StudentData s: students){
            System.out.println(Integer.toString(s.id) + ". " + s.name + " <" + s.enroll + ">");
        }
    }
    
    public void ShowRecord(StudentData s){
        System.out.println("Show Record");
        System.out.println(Integer.toString(s.id) + ". " + s.name + " <" + s.enroll + ">");
    }
    
    
    public int GetSearchId(){
        int id;
        System.out.println("Enter ID to Search:");
        id = cin.nextInt();
        cin.nextLine();
        return id;
    }

}

public class LuciferProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        DB db = new DB();
        UserInterface ui = new UserInterface();
        
        if(!db.connect()){
            exit();
        }
        
        int choice = ui.MainMenu();
        
        while(choice != 4){
            switch(choice){
                case 1:
                    ArrayList<StudentData> students = db.fetchAll();
                    ui.ShowAll(students);
                    break;
                case 2:
                    int id = ui.GetSearchId();
                    try{
                        StudentData s = db.fetch(id);
                        ui.ShowRecord(s);
                    }catch(DB.RecordNotFoundException e){
                        System.out.println(e.toString());
                    }
                    break;
                case 3:
                    StudentData s = ui.GetInsertRecord();
                    if(db.insert(s.name, s.enroll) == 1){
                        System.out.println("Inserted Successfully!");
                    }else{
                        System.out.println("An error occured!");
                    }
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid Choice!");
            }
            choice = ui.MainMenu();
        }
           
    }
    
}
