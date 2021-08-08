package com.fundamentosplatzi.springboot.fundamentos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @RequestMapping     // Para aceptar todas las solicitudes dentro del método a nivel HTTP
    @ResponseBody       // Responder un cuerpo a nivel de servicio
    public ResponseEntity<String> function() {
        return new ResponseEntity<>("Hello from controller", HttpStatus.OK);
    }
}
