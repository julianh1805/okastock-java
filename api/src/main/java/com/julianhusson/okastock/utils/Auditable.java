package com.julianhusson.okastock.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class Auditable {
    @CreatedDate
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.DATE)
    private Date updateAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = new Date();
    }

    @PreUpdate
    public void preUpdate(){
        this.updateAt = new Date();
    }
}
