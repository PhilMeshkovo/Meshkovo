package main.service;

import javassist.NotFoundException;
import main.model.Post;
import main.repository.PostRepo;
import main.request.MessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {

    @Autowired
    PostRepo postRepo;

    @Autowired
    MailSender mailSender;

    public List<Map<String, String>> getAllPosts() {
        List<Post> postList = postRepo.findAll();
        List<Map<String, String>> messageList = new ArrayList<>();
        for (Post post : postList) {
            if (post.getStatus().toUpperCase().equals("ACCEPTED")) {
                Map<String, String> mapMessage = postToMap(post);
                messageList.add(mapMessage);
            }
        }
        return messageList;
    }

    public void add(MessageRequest request, String remoteAddress) throws Exception {
        if (!request.getText().equals("") && !request.getName().equals("")
                && !request.getPhone().equals("")) {
            Post post = new Post();
            post.setStatus("NEW");
            post.setText(request.getText());
            post.setName(request.getName());
            post.setPhone(request.getPhone());
            post.setIp(remoteAddress);
            post.setTime(LocalDateTime.now().plusHours(3L));
            postRepo.save(post);
      mailSender.send("gmeshkovo1977@gmail.com", "new message",
              "text - '" + request.getText() + "' from - '" + request.getName() +
                      "' , phone number - '" + request.getPhone() + "'");
    mailSender.send("deniscamaro76@gmail.com", "new message",
        "text - '" + request.getText() + "' from - '" + request.getName() +
            "' , phone number - '" + request.getPhone() + "'");
        } else {
            throw new Exception("form of message is not correct");
        }
    }

    public List<Map<String, String>> getAllPostsForModeration() {
        List<Post> postList = postRepo.findAll();
        List<Map<String, String>> messageList = new ArrayList<>();
        for (Post post : postList) {
            if (post.getStatus().toUpperCase().equals("NEW")) {
                Map<String, String> mapMessage = postToMap(post);
                mapMessage.put("status", post.getStatus());
                messageList.add(mapMessage);
            }
        }
        return messageList;
    }

    public Map<String, String> getMessageById(long id) throws NotFoundException {
        Optional<Post> optionalMessage = postRepo.findById(id);
        if (optionalMessage.isPresent()) {
            Post post = optionalMessage.get();
            Map<String, String> mapMessage = postToMap(post);
            return mapMessage;
        } else {
            throw new NotFoundException("no such message");
        }
    }

    private Map<String, String> postToMap(Post post) {
        Map<String, String> mapMessage = new HashMap<>();
        mapMessage.put("id", String.valueOf(post.getId()));
        mapMessage.put("text", post.getText());
        mapMessage.put("name", post.getName());
        mapMessage.put("phone", post.getPhone());
        return mapMessage;
    }

    @Transactional
    public void update(long id, Map<String, String> message) throws NotFoundException {
        Optional<Post> optionalPost = postRepo.findById(id);
        if (optionalPost.isPresent()) {
            Post post = postRepo.getOne(id);
            post.setStatus(message.get("status"));
        } else {
            throw new NotFoundException("no such post");
        }

    }
}
