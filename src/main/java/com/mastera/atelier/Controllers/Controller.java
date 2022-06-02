package com.mastera.atelier.Controllers;

import com.mastera.atelier.Models.User;
import com.mastera.atelier.Services.OrderService;
import com.mastera.atelier.Services.UserService;
import com.mastera.atelier.Token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public String home(HttpServletRequest req, Model model){
        User currentUser = userService.getUserByUsername(getCurrentUser(req));
        if(currentUser != null){
            model.addAttribute("user", currentUser);
            model.addAttribute("masters", userService.getMasterUsers());
        }
        model.addAttribute("masters", userService.getMasterUsers());
        return "index";
    }

    @GetMapping("/user/delete/{username}")
    public String deleteUser(RedirectAttributes redir, @PathVariable("username") String username){
        User deleted = userService.delete(username);
        if(deleted != null) {
            redir.addFlashAttribute("success", deleted.getUsername() + " успешно удален!");
        }
        else{
            redir.addFlashAttribute("error","Ошибка!");
        }
        return "redirect:/employees";
    }

    @GetMapping("/images/{username}")
    public void images(@PathVariable("username") String username, HttpServletResponse res) throws IOException {
        User user = userService.getUserByUsername(username);
        res.setContentType("image/*");
        res.getOutputStream().write(user.getImage());
        res.getOutputStream().close();
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest req){
        User currentUser = userService.getUserByUsername(getCurrentUser(req));
        if(currentUser != null){
            model.addAttribute("user", currentUser);
        }
        return "profile";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req, HttpServletResponse res){
        Cookie[] cookies = req.getCookies();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("SESSION")){
                cookie.setValue("");
                res.addCookie(cookie);
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/signin")
    public String login_get(){
        return "login";
    }

    @PostMapping("/signin")
    public String login_post(RedirectAttributes redir, HttpServletResponse res, @RequestParam("username") String username, @RequestParam("password") String password){
        User user = userService.login(username,password);
        if(user == null){
            Cookie cookieError = new Cookie("SESSION","");
            res.addCookie(cookieError);
            redir.addFlashAttribute("error", "Ошибка с авторизацией!");
            return "login";
        }
        Token token = new Token();
        Cookie cookie = new Cookie("SESSION", token.tokenByUsername(username));
        res.addCookie(cookie);
        return "redirect:/";
    }

    public String getCurrentUser(HttpServletRequest req){
        Cookie[] cookies = req.getCookies();
        Token currentUserToken = new Token();
        String currentUser = null;
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("SESSION")){
                if(!cookie.getValue().isEmpty())
                    currentUser = currentUserToken.usernameByToken(cookie.getValue());
            }
        }
        return currentUser;
    }

    @GetMapping("/add/employee")
    public String addEmployee_get(Model model,HttpServletRequest req){
        User user = userService.getUserByUsername(getCurrentUser(req));
        model.addAttribute("user", user);
        return "newemployee";
    }


    @GetMapping("/new/employee/add")
    public String register_get(){
        return "register";
    }

    @PostMapping("/add/employee")
    public String addEmployee_post(RedirectAttributes redir,
                                @RequestParam("firstname") String firstname,
                                @RequestParam("lastname") String lastname,
                                @RequestParam("phone") String phone,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password,
                                @RequestParam("image") MultipartFile image,
                                @RequestParam("role") String role) throws IOException{
        User employee = userService.register(firstname,lastname,phone,username,password,image,role);
        if(employee == null){
            redir.addFlashAttribute("error", "Ошибка с регистрацией!");
            return "redirect:/add/employee";
        }
        redir.addFlashAttribute("success", "Успешно!");
        return "redirect:/add/employee";
    }

    @GetMapping("/orders")
    public String orders(Model model, HttpServletRequest req){
        User user = userService.getUserByUsername(getCurrentUser(req));
        model.addAttribute("user", user);
        model.addAttribute("orders",orderService.getAll());
        model.addAttribute("masters",userService.getAll());
        model.addAttribute("service",userService);
        return "orders";
    }

    @GetMapping("/orders/my")
    public String myOrders(Model model, HttpServletRequest req){
        User user = userService.getUserByUsername(getCurrentUser(req));
        model.addAttribute("user", user);
        model.addAttribute("orders",orderService.getOrdersByUsername(user.getUsername()));
        return "myOrders";
    }

    @PostMapping("/orders/my/{id}/delete")
    public String deleteOrderFromMy(HttpServletRequest req, RedirectAttributes redir,@PathVariable("id") Long id){
        User user = userService.getUserByUsername(getCurrentUser(req));
        orderService.changeUsername(id, "None");
        redir.addFlashAttribute("success", "Успешно удалено из ваших заказов!");
        return "redirect:/orders/my";
    }

    @GetMapping("/orders/{username}")
    public String usernameOrders(HttpServletRequest req, Model model, @PathVariable("username") String username){
        User user = userService.getUserByUsername(getCurrentUser(req));
        model.addAttribute("user", user);
        model.addAttribute("master", userService.getUserByUsername(username));
        model.addAttribute("orders", orderService.getOrdersByUsername(username));
        return "usernameOrders";
    }

    @PostMapping("/orders/{username}/{id}/delete")
    public String deleteOrderForUsername(@PathVariable("username") String username, RedirectAttributes redir,@PathVariable("id") Long id){
        orderService.changeUsername(id, "None");
        redir.addFlashAttribute("success", "Успешно удалено из ваших заказов!");
        return "redirect:/orders/" + username;
    }

    @GetMapping("/orders/add")
    public String addOrderGet(){
        return "addOrder";
    }

    @PostMapping("/orders/add")
    public String addOrderPost(RedirectAttributes redir, @RequestParam("name") String name, @RequestParam("phone") String phone){
        if(orderService.add(name, phone)) {
            redir.addFlashAttribute("success", "Успешно! Через некоторое время мы с вами свяжемся!");
        }
        else{
            redir.addFlashAttribute("error", "Ошибка!");
        }
        return "redirect:/orders/add";
    }

    @PostMapping("/orders/delete/{id}")
    public String deleteOrder(RedirectAttributes redir, @PathVariable("id") Long id){
        orderService.delete(id);
        redir.addFlashAttribute("success", "Успешно удалено!");
        return "redirect:/orders";
    }

    @PostMapping("/orders/{id}/for")
    public String addOrderFor(RedirectAttributes redir,@PathVariable("id") Long id, @RequestParam("username") String username){
        orderService.changeUsername(id, username);
        redir.addFlashAttribute("success", "Успешно изменено!");
        return "redirect:/orders";
    }

    @PostMapping("/orders/{id}/forme")
    public String addOrderForMe(HttpServletRequest req, RedirectAttributes redir,@PathVariable("id") Long id){
        User user = userService.getUserByUsername(getCurrentUser(req));
        orderService.changeUsername(id, user.getUsername());
        redir.addFlashAttribute("success", "Успешно добавлено в ваши заказы!");
        return "redirect:/orders";
    }

    @PostMapping("/orders/{id}/forme/delete")
    public String deleteOrderForMe(RedirectAttributes redir,@PathVariable("id") Long id){
        orderService.changeUsername(id, "None");
        redir.addFlashAttribute("success", "Успешно удалено из ваших заказов!");
        return "redirect:/orders";
    }

    @GetMapping("/employees")
    public String employees(Model model, HttpServletRequest req){
        User user = userService.getUserByUsername(getCurrentUser(req));
        model.addAttribute("user", user);
        model.addAttribute("employees", userService.getAll());
        return "employees";
    }

    @GetMapping("/images/profile")
    public void image(HttpServletResponse res, HttpServletRequest req) throws IOException {
        User user = userService.getUserByUsername(getCurrentUser(req));
        res.setContentType("image/*");
        res.getOutputStream().write(user.getImage());
        res.getOutputStream().close();
    }

    @PostMapping("/new/employee/add")
    public String register_post(RedirectAttributes redir,
                                @RequestParam("firstname") String firstname,
                                @RequestParam("lastname") String lastname,
                                @RequestParam("phone") String phone,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password,
                                @RequestParam("image") MultipartFile image,
                                @RequestParam("role") String role) throws IOException{
        User user = userService.register(firstname,lastname,phone,username,password,image,role);
        if(user == null){
            redir.addFlashAttribute("error", "Ошибка с регистрацией!");
            return "redirect:/new/employee/add";
        }
        redir.addFlashAttribute("success", "Успешно!");
        return "redirect:/new/employee/add";
    }

    @PostMapping("/profile/update")
    public String update(RedirectAttributes redir, HttpServletRequest req, @RequestParam("oldpass") String oldpass, @RequestParam("newpass") String newpass){
        User user = userService.getUserByUsername(getCurrentUser(req));
        if(user.getPassword().equals(oldpass)){
            userService.update(user, newpass);
            redir.addFlashAttribute("success", "Ваш пароль обновлен!");
            return "redirect:/profile";
        }
        redir.addFlashAttribute("error", "Что-то пошло не так!");
        return "redirect:/profile";
    }

}
