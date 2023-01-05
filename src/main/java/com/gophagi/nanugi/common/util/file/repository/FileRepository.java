package com.gophagi.nanugi.common.util.file.repository;

import com.gophagi.nanugi.common.util.file.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<Photo, Long> {
    Photo save(Photo photo);
}
