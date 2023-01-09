package com.gophagi.nanugi.common.util.file.repository;

import com.gophagi.nanugi.common.util.file.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileRepository extends JpaRepository<Photo, Long> {
    Photo save(Photo photo);
    @Query(value = "SELECT CASE WHEN MAX(e.fileIndex) IS NULL THEN 0 ELSE (MAX(e.fileIndex)+1) END  FROM Photo e WHERE e.groupbuyingBoard.id = :id")
    public Long findLastIndexByGroupbuyingBoardId(@Param("id") Long groupbuyingBoardId);
}
