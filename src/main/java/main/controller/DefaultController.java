package main.controller;

import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DefaultController {

  @Autowired
  UserService userService;

  @RequestMapping("/")
  public String index() {
    return "index";
  }

  @GetMapping("{link}.html")
  public String link(@PathVariable String link) {
    return link;
  }

  @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET}, value = "/**/{path:[^\\.]*}")
  public String redirectToIndex() {
    return "forward:/";
  }

  @PostMapping("/login")
  public String login(
      @RequestParam String username,
      @RequestParam String password) {
    boolean answer = userService.login(username, password);
    if (answer) {
      return "moderation";
    } else {
      return "index";
    }
  }
}

