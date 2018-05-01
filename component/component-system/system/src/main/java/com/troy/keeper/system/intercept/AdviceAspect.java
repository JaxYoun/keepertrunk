package com.troy.keeper.system.intercept;

import com.google.common.collect.Maps;
import com.troy.keeper.core.error.KeeperException;
import com.troy.keeper.core.utils.validate.Validate;
import com.troy.keeper.system.dto.SmUserDTO;
import com.troy.keeper.system.intercept.account.AccountAdviceService;
import com.troy.keeper.system.intercept.account.AccountService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by Harry on 2017/9/5.
 */
@Aspect
@Configuration
public class AdviceAspect {


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Environment env;

    public AdviceAspect(Environment env) {
        this.env = env;
    }

    private Map<Class, AdviceIntercept> accountAnnotations = Maps.newHashMap();


    /**
     * Advice that logs when a method is entered and exited.
     */
    @Around("@annotation(AdviceIntercept)")
    public Object adviceAccount(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!checkAccount(joinPoint)){
            return joinPoint.proceed();
        }
        Class[] argsClass = argsClass(joinPoint);
        AdviceIntercept adviceIntercept = getAnnotation(joinPoint, argsClass);
        if (null== adviceIntercept){
            return joinPoint.proceed();
        }
        List<?> delegateServices = InterceptServiceInstance.delegateServices(joinPoint.getTarget().getClass(), adviceIntercept.value());
        if (CollectionUtils.isEmpty(delegateServices)){
            return joinPoint.proceed();
        }

        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }

        try {
            handleIntercept(joinPoint, argsClass, adviceIntercept, InterceptEnum.PRE, delegateServices);
            Object result = joinPoint.proceed();
            handleIntercept(joinPoint, argsClass, adviceIntercept, InterceptEnum.PROCESS, delegateServices);
            handleIntercept(joinPoint, argsClass, adviceIntercept, InterceptEnum.POST, delegateServices);
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
    }

    private Class[] argsClass (ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Class[] argsClass = null;
        if (ArrayUtils.isNotEmpty(args)) {
            List<Class<?>> collect = Stream.of(args).map(t -> (t.getClass())).collect(toList());
            argsClass = new Class[collect.size()];
            argsClass = collect.toArray(argsClass);
        }
        return argsClass;
    }
    private AdviceIntercept getAnnotation(ProceedingJoinPoint joinPoint, Class[] argsClass) throws NoSuchMethodException {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        AdviceIntercept adviceIntercept = accountAnnotations.get(targetClass);
        if (null!= adviceIntercept) {
            return adviceIntercept;
        }
        AdviceIntercept annotation = joinPoint.getSignature().getDeclaringType().getDeclaredMethod(joinPoint.getSignature().getName(), argsClass).getAnnotation(AdviceIntercept.class);
        accountAnnotations.put(targetClass, annotation);
        return annotation;
    }

    private boolean checkAccount(ProceedingJoinPoint joinPoint) {
        return AccountService.class.isAssignableFrom(joinPoint.getSignature().getDeclaringType());
    }

    private void handleIntercept(ProceedingJoinPoint joinPoint, Class[] argsClass, AdviceIntercept adviceIntercept, InterceptEnum interceptEnum, List<?> accountServices) {
        if (CollectionUtils.isNotEmpty(accountServices)) {
            Object[] args = joinPoint.getArgs();
            accountServices.forEach(a->{
                String methodName = null;
                try {
                    String prefix = prefix(interceptEnum, adviceIntercept);
                    methodName = prefix + StringUtils.capitalize(joinPoint.getSignature().getName());
                    Method declaredMethod = a.getClass().getMethod(methodName, argsClass);
                    declaredMethod.invoke(a,args);
                } catch (NoSuchMethodException e) {
                    log.error("No Such Method: [{}] in {} with argument: {}",methodName, a.getClass().getName(),  Arrays.toString(joinPoint.getArgs()));
                } catch (InvocationTargetException e) {
                    throw new KeeperException(e);
                } catch (IllegalAccessException e) {
                    throw new KeeperException(e);
                }
            });
        }
    }

    private String prefix(InterceptEnum interceptEnum, AdviceIntercept adviceIntercept) {
        if (InterceptEnum.PRE.equals(interceptEnum)) {
            return adviceIntercept.prefix();
        }
        if (InterceptEnum.POST.equals(interceptEnum)) {
            return adviceIntercept.postfix();
        }
        if (InterceptEnum.PROCESS.equals(interceptEnum)) {
            return "";
        }
        return "";
    }

    private void processAccount(ProceedingJoinPoint joinPoint, Collection<AccountAdviceService> accountServices) {

        if (CollectionUtils.isNotEmpty(accountServices)) {
            Object[] args = joinPoint.getArgs();
            Validate.notEmpty(args, joinPoint.toShortString()+" must have parameter!");
            Validate.isInstanceOf(SmUserDTO.class,args[0], "parameter must be SmUserDTO!" );
            SmUserDTO smUserDTO = (SmUserDTO) args[0];
            accountServices.forEach(a->a.createData(smUserDTO));
        }
    }

    private void postAccount(ProceedingJoinPoint joinPoint, Collection<AccountAdviceService> accountServices) {

        if (CollectionUtils.isNotEmpty(accountServices)) {
            Object[] args = joinPoint.getArgs();
            Validate.notEmpty(args, joinPoint.toShortString()+" must have parameter!");
            Validate.isInstanceOf(SmUserDTO.class,args[0], "parameter must be SmUserDTO!" );
            SmUserDTO smUserDTO = (SmUserDTO) args[0];
            accountServices.forEach(a->a.postCreatedData(smUserDTO));
        }
    }
}
