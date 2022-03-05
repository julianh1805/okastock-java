package com.julianhusson.okastock.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class Auditable {
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false)
    private Timestamp createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.DATE)
    @Column(insertable = false)
    private Timestamp updatedAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = new Timestamp(new Date().getTime());
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = new Timestamp(new Date().getTime());
    }
}
