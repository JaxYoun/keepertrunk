package com.troy.keeper.core.base.entity;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.InstantType;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoField;

/**
 * Created by yjm on 2017/5/25.
 */
public class InstantLong implements CompositeUserType, Serializable {
    @Override
    public String[] getPropertyNames() {
        return new String[]{"instantlong"};
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{InstantType.INSTANCE};
    }

    @Override
    public Object getPropertyValue(Object o, int i) throws HibernateException {
        return (Instant)o;
    }

    @Override
    public void setPropertyValue(Object o, int i, Object o1) throws HibernateException {
        Instant instant = (Instant)o;
        instant= (Instant) o1;
    }

    @Override
    public Class returnedClass() {
        return Instant.class;
    }

    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
        if(o==null && o1==null){
            return true;
        } else if(o!=null && o1!=null){
            Instant a =(Instant)o;
            Instant b=(Instant)o1;

            if(a.compareTo(b) != 0) return false;
            else return true;
        } else{
            return false;
        }
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return 0;
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        Long str= (Long) LongType.INSTANCE.get(resultSet, strings[0], sharedSessionContractImplementor);
        Instant instant = null;
        if (str != null){
            instant = Instant.ofEpochSecond(str);
        }
        return instant;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if (o != null){
            Instant instant = (Instant)o;
            Long time = instant.getLong(ChronoField.INSTANT_SECONDS);
            LongType.INSTANCE.nullSafeSet(preparedStatement,time , i, sharedSessionContractImplementor);
        }else{
            LongType.INSTANCE.nullSafeSet(preparedStatement, o, i, sharedSessionContractImplementor);
        }
    }

    @Override
    public Object deepCopy(Object o) throws HibernateException {
        if(o!=null){
            Instant source =(Instant)o;
            Instant target =source;
            return target;
        }
        else{
            return o;
        }
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object o, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException {
        return (Serializable)o;
    }

    @Override
    public Object assemble(Serializable serializable, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return serializable;
    }

    @Override
    public Object replace(Object o, Object o1, SharedSessionContractImplementor sharedSessionContractImplementor, Object o2) throws HibernateException {
        return o;
    }
}
