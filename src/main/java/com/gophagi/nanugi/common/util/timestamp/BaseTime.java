package com.gophagi.nanugi.common.util.timestamp;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 생성시간, 변경시간 자동으로 넣어줄수 있는 entity
 * 필요한 entity에 extends BaseTimeEntity 하면 자동으로 넣어짐.
 */
@Getter
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class BaseTime {

    @CreatedDate
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    private LocalDateTime modifiedDateTime;
}
