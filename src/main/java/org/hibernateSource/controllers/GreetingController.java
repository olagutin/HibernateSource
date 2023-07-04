package org.hibernateSource.controllers;

import org.hibernateSource.models.Message;
import org.hibernateSource.repository.MessageRepo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
public class GreetingController {

    private final MessageRepo messageRepo;

    public GreetingController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping
    public ModelAndView greeting(Map<String, Object> model) {
        return new ModelAndView("grt");
    }

    @GetMapping("main")
    public ModelAndView main(Map<String, Object> model) {
        Iterable<Message> messages = messageRepo.findAll();
        model.put("messages", messages);
        return new ModelAndView("index");
    }

    @PostMapping("main")
    public ModelAndView add(@RequestParam String text, @RequestParam String tag, Map<String, Object> model) {
        Message message = new Message(text, tag);
        messageRepo.save(message);
        Iterable<Message> messages = messageRepo.findAll();

        model.put("messages", messages);
        return new ModelAndView("index");
    }

    @PostMapping("filter")
    public ModelAndView filter(@RequestParam String filter, Map<String, Object> model) {
        Iterable<Message> messages;
        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }
        model.put("messages", messages);
        return new ModelAndView("index");
    }
}
