package edu.sjsu.cmpe275.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Around;

import java.io.IOException;

@Aspect
@Order(1)
public class RetryAspect {

    /**
     * attempts to retry sending tweet for 3 times and then throws the exception to main
     * @param theProceedingJoinPoint
     * @throws Throwable
     */
    @Around("execution(public void edu.sjsu.cmpe275.aop.TweetService.tweet(..))")
    public void tweetAroundAdvice(ProceedingJoinPoint theProceedingJoinPoint) throws Throwable {
        System.out.println("Executing around for tweet");
        try {
            theProceedingJoinPoint.proceed();
        } catch (IOException e) {
            try {
                retry(theProceedingJoinPoint, "tweet");
            } catch (IOException e1) {
                System.out.println("Throw to the main");
                throw e1;
            }
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * attempts to retry following for 3 times if it failed during the 1st try
     * and if it fails even after 3 attempts, the exception is thrown to main
     * @param theProceedingJoinPoint
     * @throws Throwable
     */
    @Around("execution(public void edu.sjsu.cmpe275.aop.TweetService.follow(..))")
    public void followAroundAdvice(ProceedingJoinPoint theProceedingJoinPoint) throws Throwable {
        System.out.println("Executing around for follow");
        try {
            theProceedingJoinPoint.proceed();
        } catch (IOException e) {
            System.out.println("Follow Failed");
            try {
                retry(theProceedingJoinPoint, "follow");
            } catch (Exception e1) {
                throw e1;
            }
        }
    }

    /**
     * attempts to retry blocking for 3 times if it failed during the 1st try
     * and if it fails even after 3 attempts, the exception is thrown to main
     * @param theProceedingJoinPoint
     * @throws Throwable
     */
    @Around("execution(public void edu.sjsu.cmpe275.aop.TweetService.block(..))")
    public void blockAroundAdvice(ProceedingJoinPoint theProceedingJoinPoint) throws Throwable {
        System.out.println("Executing around for block");
        try {
            theProceedingJoinPoint.proceed();
        } catch (IOException e) {
            System.out.println("Block Failed");
            try {
                retry(theProceedingJoinPoint, "block");
            } catch (Exception e1) {
                throw e1;
            }
        }
    }


    /**
     * takes in the proceedingjointpoint and attempts to retry thrice for the particular functionality type
     * @param theProceedingJoinPoint
     * @param type
     * @throws Throwable
     */
    public void retry(ProceedingJoinPoint theProceedingJoinPoint, String type) throws Throwable {
        try {
            System.out.println("Retrying to " + type + "-Attempt1");
            theProceedingJoinPoint.proceed();
        } catch (IOException e) {
            try {
                System.out.println("Retrying to " + type + "-Attempt2");
                theProceedingJoinPoint.proceed();
            } catch (IOException e1) {
                try {
                    System.out.println("Retrying to " + type + "-Attempt3");
                    theProceedingJoinPoint.proceed();
                } catch (IOException e3) {
                    throw e3;
                }
            }
        }

    }


}
