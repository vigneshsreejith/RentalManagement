//package controllers;
//
//import models.Tenant;
//import models.Landlord;
//import models.User;
//
//import java.util.List;
//
//public class UserController {
//    private List<User> users;
//
//    public UserController() {
//        // Here you would populate the list with real users, for example from a database
//        users = List.of(
//                new Tenant("tenantUser", "tenantPassword", "John Doe", "123-456-7890"),
//                new Landlord("landlordUser", "landlordPassword", "ABC Properties")
//        );
//    }
//
//    public boolean login(String username, String password) {
//        for (User user : users) {
//            if (user.login(username, password)) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
