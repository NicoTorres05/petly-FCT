package petly.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class InicioController {
    @GetMapping("/")
    public String init() {
        return "index";
    }
}
