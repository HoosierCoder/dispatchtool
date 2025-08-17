package com.hoosiercoder.dispatchtool.views.user;

import com.hoosiercoder.dispatchtool.user.dto.UserDTO;
import com.hoosiercoder.dispatchtool.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Author: HoosierCoder
 *
 */
@Controller
@RequestMapping("/users")
public class UserViewController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public String listUsers(Model model) {
        List<UserDTO> userList = userService.listUsers();
        model.addAttribute("users", userList);
        return "users/list"; // Refers to src/main/resources/templates/users/list.html
    }
}
