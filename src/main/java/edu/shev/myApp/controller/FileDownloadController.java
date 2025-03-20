package edu.shev.myApp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
@RequestMapping(value = "main/download", method = RequestMethod.GET)
public class FileDownloadController {

    @Value("${upload.path}")
    private String downloadPath;




}
