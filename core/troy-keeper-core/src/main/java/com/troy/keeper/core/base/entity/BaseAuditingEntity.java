package com.troy.keeper.core.base.entity;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditingEntity extends AuditEntity {


//    @Email
//    @Size(min = 5, max = 100)
//    @Column(length = 100, unique = true)
//    private String email;
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
}
