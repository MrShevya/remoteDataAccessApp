package edu.shev.myApp.controller;

import edu.shev.myApp.domain.FileObj;
import edu.shev.myApp.domain.User;
import edu.shev.myApp.repos.FilesRepo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.UUID;


@Controller
public class MainController{
    @Autowired
    private FilesRepo filesRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String poopoo(){
        return "login";
    }


    @GetMapping("/main")
    public  String main(Model model){
        //Iterable<File> files = filesRepo.findAll();
        model.addAttribute("files", filesRepo.findAll());
        return "uploadForm";
    }

    // заранее хуйовый вариант, когда на один request path мы регаем методы, отличающиеся типом запроса - get/post
    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String filename,
            @RequestParam("file")MultipartFile file,
            Model model) throws IOException {

        FileObj fileObj = new FileObj(filename, user);

        // такой логики в контроллере, конечно, быть не должно
        if(file != null) {
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            fileObj.setFile_id(UUID.randomUUID());
            String res = fileObj.getFile_id().toString() + "." + file.getOriginalFilename();
            fileObj.setFileStorageName(res);

            file.transferTo(new File(uploadPath + "/" + res));
        }

        filesRepo.save(fileObj);

        Iterable<FileObj> files = filesRepo.findAll();

        model.addAttribute("files", files);

        return "uploadForm";
    }

    @RequestMapping(value = "/main/download/{file_name}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@PathVariable("file_name") String fileStorageName){
        final HttpHeaders httpHeaders = new HttpHeaders();
        final File file = new File(uploadPath + "/" + fileStorageName);
        final FileSystemResource resource = new FileSystemResource(file);
        httpHeaders.set(HttpHeaders.LAST_MODIFIED, String.valueOf(file.lastModified()));
        httpHeaders.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }





    @PostMapping("filter")
    public String filter(@RequestParam String filter, Model model){
        List<FileObj> files = filesRepo.findByFilenameStartingWith(filter);
        model.addAttribute("files", files);
        return "uploadForm";
    }


}