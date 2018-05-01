package com.troy.keeper.core.base.entity;


import com.troy.keeper.base.entity.IdEntity;

import javax.persistence.MappedSuperclass;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
@MappedSuperclass
public abstract class BaseEntity extends IdEntity {

    String reason;



}
