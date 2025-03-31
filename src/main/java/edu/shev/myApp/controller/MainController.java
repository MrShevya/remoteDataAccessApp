package edu.shev.myApp.controller;

import edu.shev.myApp.domain.FileSystem;
import edu.shev.myApp.domain.User;
import edu.shev.myApp.repos.FilesRepo;
import edu.shev.myApp.repos.UserRepo;
import edu.shev.myApp.service.FilesService;
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
public class MainController {
    @Autowired
    private FilesRepo filesRepo;

    @Autowired
    private UserRepo userRepo;
    @Value("${upload.path}")
    private String uploadPath;


    FilesService filesService = new FilesService(filesRepo);

    @GetMapping("/")
    public String login() {
        // TODO сделать логику редиректа на main если человек залогинен
        return "login";
    }

    @GetMapping("/main")
    public String main(Model model) {
        //Iterable<File> files = filesRepo.findAll();
        model.addAttribute("files", filesRepo.findAll());
        return "uploadForm";
    }


    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file,
            Model model) throws IOException {

        filesService.addFile(file, user, "files", filesRepo);

        Iterable<FileSystem> files = filesRepo.findAll();

        model.addAttribute("files", files);

        return "uploadForm";
    }

    /*
    @PostMapping("/main/{$currentDir}")
    public String addInDir(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file,
            @PathVariable("currentDir") String currentDir,
            Model model) throws IOException {

        filesService.addFile(file, user, usercurrentDir);

        Iterable<FileSystem> files = filesRepo.findAll();

        model.addAttribute("files", files);

        return "uploadForm";
    }
*/






    @PostMapping("removefile")
    String removeFile(@AuthenticationPrincipal User user,
                      @RequestParam(name = "fileId") Long fileId){
        if (filesRepo.existsById(fileId)) {
            filesRepo.deleteById(fileId);
        }
        return "redirect:/main";
    }


    @RequestMapping(value = "/main/download/{file_link}", method = RequestMethod.GET)
    @PreAuthorize(value = "@filesRepo.findByLink(#file_link).owner.id eq authentication.principal.id  or" +
            " @filesRepo.findByLink(#file_link).recievers.contains(authentication.principal.id) == true")
    public ResponseEntity<Resource> downloadFile(
            @Param("file_link")
            @PathVariable("file_link")
            String fileLink) {
        String filename = filesRepo.findByLink(fileLink).getFilename();
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        final File file = new File(uploadPath + "/" + filename);
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
    public String filter(@RequestParam String filter, Model model) {
        List<FileSystem> files = filesRepo.findByFilenameStartingWith(filter);
        model.addAttribute("files", files);
        return "uploadForm";
    }

    @PostMapping("addreciever")
    public String addReciever(@AuthenticationPrincipal User user,
                                @RequestParam(name = "reciever") Long rec,
                                @RequestParam(name = "fileId") Long fileId){
        filesRepo.findById(fileId).get().addReciever(userRepo.findById(rec).get());
        filesRepo.save(filesRepo.findById(fileId).get());
        return "redirect:/main";
    }

    @PostMapping("removereciever")
    public String removeReciever(@AuthenticationPrincipal User user,
                              @RequestParam(name = "reciever") Long rec,
                              @RequestParam(name = "fileId") Long fileId){
        filesRepo.findById(fileId).get().removeReciever(userRepo.findById(rec).get());
        filesRepo.save(filesRepo.findById(fileId).get());
        return "redirect:/main";
    }

}