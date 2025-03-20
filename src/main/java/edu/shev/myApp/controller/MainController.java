package edu.shev.myApp.controller;

import edu.shev.myApp.domain.FileSystem;
import edu.shev.myApp.domain.User;
import edu.shev.myApp.repos.FilesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class MainController{
    @Autowired
    private FilesRepo filesRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String login(){
        //сделать логику редиректа на main если человек залогинен
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
            @RequestParam("file")MultipartFile file,
            Model model) throws IOException {

        FileSystem fileSystem = new FileSystem(file.getOriginalFilename() , user); // создается экземпляр файла для хранения в репозитории

        if(file != null) { // проверка существования директории и создание если ее не существует
            Path uploadDir = Paths.get(uploadPath);
            if(!Files.exists(uploadDir)){
                Files.createDirectory(uploadDir);
            } // TODO сделать проверку файла на существование и запрещать загрузку
              // TODO сделать загрузку в папки по юзернеймам и айдипользователя(лучше сделать хеши из них и называть папки хешами)

            Path fileUploadPath = Paths.get(uploadDir + "/" + file.getOriginalFilename());
            try (InputStream is = file.getInputStream()){
            Files.copy(is, fileUploadPath);   //сохранение файла на диск
            }
                            //  возможна реализация через multipartFile file
                            //  file.transferTo()
        }

        filesRepo.save(fileSystem);

        Iterable<FileSystem> files = filesRepo.findAll();

        model.addAttribute("files", files);

        return "uploadForm";
    }
    @RequestMapping(value = "/main/download/{file_name}", method = RequestMethod.GET)
    @PreAuthorize(value = "@filesRepo.findById(#file_name).get().owner.id eq authentication.principal.id") // || returnObject.recievers.contains(principal.username) == true
    public ResponseEntity<Resource> downloadFile(
            @Param("file_name")
            @PathVariable
                    ("file_name") Long fileStorageName){
        final HttpHeaders httpHeaders = new HttpHeaders();
        final File file = new File(uploadPath + "/" + filesRepo.findById(fileStorageName).get().getFilename());
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
        List<FileSystem> files = filesRepo.findByFilenameStartingWith(filter);
        model.addAttribute("files", files);
        return "uploadForm";
    }

}