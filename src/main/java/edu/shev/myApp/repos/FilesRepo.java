package edu.shev.myApp.repos;

import edu.shev.myApp.domain.FileObj;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FilesRepo extends CrudRepository<FileObj, Long> {

    List<FileObj> findByFilenameStartingWith(String prefix);

}
