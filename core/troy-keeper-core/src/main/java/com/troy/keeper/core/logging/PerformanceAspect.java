package com.troy.keeper.core.logging;

import com.troy.keeper.core.base.entity.PerformanceCheck;
import com.troy.keeper.core.base.repository.CommonRepository;
import com.troy.keeper.core.config.CloudProperties;
import com.troy.keeper.core.enums.ResponseCode;
import com.troy.keeper.core.error.PerformanceCheckException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Aspect for performance execution of service and repository Spring components.
 *
 * By default, it only runs with the "dev" or "test" profile.
 */
@Aspect
public class PerformanceAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(PerformanceAspect.class);

    private final CloudProperties cloudProperties;

    @Autowired
    private CommonRepository commonRepository;

    public PerformanceAspect(CloudProperties cloudProperties) {
        this.cloudProperties = cloudProperties;
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) ")
    public void annotationRequest(){
    }
    /**
     * Advice that performance time when a method is entered and exited.
     */
    @Around("annotationRequest()")
    public Object performanceAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        if (shouldCheck(joinPoint) ) {
            long startTime = System.currentTimeMillis();
            result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            short seconds = cloudProperties.getPerformance().getCheck().getSeconds();
            long speedTime = endTime - startTime;
            if (speedTime > (seconds*1000)) {
                outOfException(speedTime, joinPoint);
            }
        } else {
            result = joinPoint.proceed();
        }
        return result;
    }

    private void outOfException(long speedTime, ProceedingJoinPoint joinPoint) {
        PerformanceCheck performanceCheck = new PerformanceCheck();
        String arguments = Arrays.toString(joinPoint.getArgs());
        if (ArrayUtils.isNotEmpty(joinPoint.getArgs())) {
            performanceCheck.setArguments(arguments);
        }
        performanceCheck.setClazz(joinPoint.getSignature().getDeclaringTypeName());
        performanceCheck.setMethod(joinPoint.getSignature().getName());
        performanceCheck.setMillisecond(speedTime);
        String reason = String.format("execute %d ms beyond expectation %d ms ,argument: %s in %s.%s()", speedTime,cloudProperties.getPerformance().getCheck().getSeconds()*1000,
                arguments, performanceCheck.getClazz(), performanceCheck.getMethod());
        performanceCheck.setReason(reason);
        commonRepository.add(performanceCheck);
        throw new PerformanceCheckException(Integer.valueOf(ResponseCode.CODE_444.getCode()), reason);
    }

    private boolean shouldCheck(ProceedingJoinPoint joinPoint) {
        if (!cloudProperties.getPerformance().getCheck().isEnabled()) {
            return false;
        }
        List<String> excludes = cloudProperties.getPerformance().getCheck().getExcludes();
        if (CollectionUtils.isNotEmpty(excludes)) {
            if (excludes.stream().anyMatch(s->s.equals(joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName()))) {
                return false;
            }
        }
        return true;
    }


}
