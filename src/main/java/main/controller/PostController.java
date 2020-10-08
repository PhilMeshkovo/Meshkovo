package main.controller;

import javassist.NotFoundException;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class PostController {

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    PostService postService;


    @GetMapping("/message")
    public @ResponseBody
    List<Map<String, String>> list() {
        return postService.getAllPosts();
    }

    @GetMapping("/message/{id}")
    private Map<String, String> getMessage(@PathVariable long id) throws NotFoundException {
        return postService.getMessageById(id);
    }

    @PostMapping("/message")
    public String create(@RequestParam String text,
                         @RequestParam String name,
                         @RequestParam String phone) throws Exception {
        postService.add(text, name, phone, httpServletRequest.getRemoteAddr());
        return "board";
    }

    @PutMapping("/moderation/{id}")
    public @ResponseBody
    void update(@PathVariable long id,
                @RequestBody Map<String, String> message) throws NotFoundException {
        postService.update(id, message);

    }

    @GetMapping("/moderation")
    public @ResponseBody
    List<Map<String, String>> listModerationMessage() {
        return postService.getAllPostsForModeration();
    }

}
