package com.julianhusson.okastock.utils;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
    @CreatedDate
    @Column(updatable = false, nullable = false)
    protected Long createdAt;

    @LastModifiedDate
    protected Long updatedAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = new Date().getTime();
        this.updatedAt = null;
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = new Date().getTime();
    }

}
