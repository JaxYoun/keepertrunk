package com.troy.keeper.core.base.entity;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by yjm on 2017/5/25.
 */
public class DateLongNew implements CompositeUserType, Serializable {
    @Override
    public String[] getPropertyNames() {
        return new String[]{"dateforlong"};
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{DateType.INSTANCE};
    }

    @Override
    public Object getPropertyValue(Object o, int i) throws HibernateException {
        return (Date)o;
    }

    @Override
    public void setPropertyValue(Object o, int i, Object o1) throws HibernateException {
            Date date = (Date)o;
            date= (Date) o1;
    }

    @Override
    public Class returnedClass() {
        return Date.class;
    }

    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
        if(o==null && o1==null){
            return true;
        } else if(o!=null && o1!=null){
            Date a =(Date)o;
            Date b=(Date)o1;

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
        Long time= (Long) LongType.INSTANCE.get(resultSet, strings[0], sharedSessionContractImplementor);
        Date date = null;
        if (time != null){
            String str = time+"000";
            date = new Date(Long.valueOf(str));
        }
        return date;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if (o != null){
            Date date = (Date)o;
            Long time = date.getTime();
            String timeNow = time.toString().substring(0,10);
            LongType.INSTANCE.nullSafeSet(preparedStatement,Long.valueOf(timeNow) , i, sharedSessionContractImplementor);
        }else{
            LongType.INSTANCE.nullSafeSet(preparedStatement, o, i, sharedSessionContractImplementor);
        }
    }

    @Override
    public Object deepCopy(Object o) throws HibernateException {
        if(o!=null){
            Date source =(Date)o;
            Date target =source;
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
