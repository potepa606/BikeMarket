package com.example.BikeMarket.controller;


import com.example.BikeMarket.database.DataBaseAction;
import com.example.BikeMarket.model.Rental;
import com.example.BikeMarket.model.Skuter;
import com.example.BikeMarket.model.User;
import com.example.BikeMarket.storage.AmazonStorage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.PrintWriter;

import static com.example.BikeMarket.database.DataBaseAction.*;
import static com.example.BikeMarket.model.Rental.listOfRentals;

@Controller
public class BikeMarketController {
    public static String uploadDirectory = System.getProperty("user.dir")+"\\upload";
    int selector = 0;

    @RequestMapping(value = "/")
    public String mainPage(){

        return "index";
    }






    // Login controller
    @GetMapping(value = "/login")
    public String getLogin(Model model){
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping(value = "/login")
    public String postLogin(@ModelAttribute User user){
        boolean czyZalogowano = DataBaseAction.login(user);
        if(czyZalogowano){
            return "redirect:/maintenance";
        }else
            return "redirect:/login";
    }





    // Register user
    @GetMapping(value = "/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping(value = "/register")
    public String registerUser(@ModelAttribute User user){
        user.setId();
        DataBaseAction.registerUser(user);
        return "redirect:/login";
    }






    //maintenence page
    @GetMapping(value = "/maintenance")
    public String mainPage(Model model){
        readFromDataBase();
        model.addAttribute("skuters", Skuter.getAllSkuters());
        return "maintenance";
    }

    @GetMapping(value = "/addbike")
    public String addScuter(Model model){
        model.addAttribute("skuter", new Skuter());
        return "addBike";
    }



    // addbike Page
    @PostMapping(value = "/addbike")
    public String saveScuter(@ModelAttribute Skuter skuter, @RequestParam("file")MultipartFile file){
        String nameFile;
        nameFile = file.getOriginalFilename();
        byte[] imgaeBytes=null;
        try {
            imgaeBytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String url = AmazonStorage.sendPhoto(nameFile,imgaeBytes);
        skuter.setImageUrl(url);
        skuter.setIdBike();
        DataBaseAction.sendBike(skuter);
        return "redirect:/addbike";
    }





    //editBike
    @GetMapping(value = "/edit")
    public String editBike(Model model){
        readFromDataBase();
        model.addAttribute("skuters", Skuter.getAllSkuters());
        model.addAttribute("skuter", new Skuter());
        return "edit";
    }

    @PostMapping(value = "/edit")
    public String upDateBike(@ModelAttribute Skuter skuter, @RequestParam("file")MultipartFile file){

        String nameFile;
        nameFile = file.getOriginalFilename();
        if(!nameFile.equals("")){

            byte[] imgaeBytes=null;
            try {
                imgaeBytes = file.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String url = AmazonStorage.sendPhoto(nameFile,imgaeBytes);
            System.out.println(url);
            skuter.setImageUrl(url);
        }
        System.out.println(skuter.getIdBike());
        DataBaseAction.upDateBike(skuter);
        return "redirect:/edit";
    }





    // rentals page
    @GetMapping(value = "/rentals")
    public String editUsers(Model model){
        refreshData();
        model.addAttribute("users", User.listOfUsers);
        model.addAttribute("numer",selector);
        model.addAttribute("rental",new Rental());
        return "rentals";
    }

    @RequestMapping(value = "/deleterent", method = RequestMethod.POST)
    public String selectID(@RequestBody Integer idBike) {
        DataBaseAction.deleteRental(idBike);
        return "redirect:/";
    }


    @GetMapping(value = "/rentals:{id}")
    public String setTable(@PathVariable("id") int id, Model model) {
        refreshRentalsUsers();

        int numer=0;
        for(int i =0;i<User.listOfUsers.size();i++){
            if(User.listOfUsers.get(i).getId() == id){
                numer =i;
                break;
            }
        }
        System.out.println(numer);
        model.addAttribute("numer",numer);
        model.addAttribute("users", User.listOfUsers);
        model.addAttribute("idOfcustormer",id);
        model.addAttribute("rental",new Rental());


        svaeToFile(id);



        return "rentals";
    }

    final static String uploadDirectory2 = System.getProperty("user.dir")+"\\rez.txt";



    public void svaeToFile(int idUser) {
        try{
            PrintWriter pw = new PrintWriter(uploadDirectory2);
            System.out.println(uploadDirectory2);

            for (Rental rent : listOfRentals) {
                if (idUser == rent.getIdUser()) {
                    pw.print(rent.getIdUser() + ";");
                    pw.print(rent.getDateFrom() + ";");
                    pw.print(rent.getDateTo() + ";");
                    pw.println(rent.getPrice() + ";-");
                }
            }

            pw.flush();
            pw.close();

        }catch (IOException ex){

        }
    }



    @PostMapping(value = "/rentals")
    public String upDateRental(@ModelAttribute Rental rental){
        System.out.println(rental);
        DataBaseAction.upDateRental(rental);
        refreshRentalsUsers();

        return "redirect:/rentals:"+rental.getIdUser();
    }






    public static void refreshData(){
        ReadFromDataBase_Users();
        ReadFromDataBase_Rentals();

        for (User user: User.listOfUsers) {
            for (Rental rent: listOfRentals) {
                if(user.getId() == rent.getIdUser()){
                    user.getListUsersBikes().add(rent);
                }
            }
        }
    }
    public static void refreshRentalsUsers(){
        ReadFromDataBase_Rentals();
        for (User user: User.listOfUsers) {
            user.getListUsersBikes().clear();
            for (Rental rent: listOfRentals) {
                if(user.getId() == rent.getIdUser()){
                    user.getListUsersBikes().add(rent);
                }
            }
        }

    }




}
