package edu.shev.myApp.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.InputStream;

@Controller
@RequestMapping(value = "main/download", method = RequestMethod.GET)
public class FileDownloadController {

    @Value("${upload.path}")
    private String downloadPath;




}
