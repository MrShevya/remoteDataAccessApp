package edu.shev.myApp.repos;

import edu.shev.myApp.domain.FileSystem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FilesRepo extends CrudRepository<FileSystem, Long> {

    List<FileSystem> findByFilenameStartingWith(String prefix);

    FileSystem findByLink(String link);
}
