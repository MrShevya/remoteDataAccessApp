package edu.shev.myApp.service;

import edu.shev.myApp.domain.FileSystem;
import edu.shev.myApp.domain.User;
import edu.shev.myApp.repos.FilesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FilesService {

    private FilesRepo filesRepo;

    //@Value("${upload.path}")
    private String absoluteUploadPath = "E:/myApp/files";
    public FilesService(FilesRepo filesRepo) {
        this.filesRepo = filesRepo;
    }

    public void addFile(MultipartFile file, User owner, String path, FilesRepo filesRepo) throws IOException {

        Path uploadPath;

        if(path.equals("files")){
            uploadPath = Paths.get(absoluteUploadPath);
        } else {
            uploadPath = Paths.get(absoluteUploadPath + path);
        }

        System.out.println("Путь:" + uploadPath.toString());


        FileSystem fileSystem = new FileSystem(file.getOriginalFilename(), uploadPath, owner); // создается экземпляр файла для хранения в репозитории

        fileSystem.generateLink();
        System.out.println("Файл создан");

         // проверка существования директории и создание если ее не существует
            if (!Files.exists(uploadPath)) {
                Files.createDirectory(uploadPath);
            }

            // TODO сделать проверку файла на существование и запрещать загрузку
            //   TODO сделать загрузку в папки по юзернеймам и айдипользователя(лучше сделать хеши из них и называть папки хешами)

        System.out.println("Попытка переренести файл домой");
            Path fileUploadPath = Paths.get(uploadPath.toString() + "/" + file.getOriginalFilename());
            try (InputStream is = file.getInputStream()) {
                Files.copy(is, fileUploadPath);   //сохранение файла на диск
            }
            //  возможна реализация через multipartFile file
            //  file.transferTo()

        filesRepo.save(fileSystem);
    }



}
