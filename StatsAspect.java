package edu.sjsu.cmpe275.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.TweetStatsImpl;

@Aspect
@Order(0)
public class StatsAspect {

    @Autowired
    TweetStatsImpl stats;


    /**
     * save the length of the tweets attempted to be sent using before advice
     * @param joinPoint
     */
    @Before("execution(public void edu.sjsu.cmpe275.aop.TweetService.tweet(..))")
    public void tweetlengthBeforeAdvice(JoinPoint joinPoint)  {
        System.out.printf("Before the execution of the method %s\n", joinPoint.getSignature().toShortString());
        String tweet = joinPoint.getArgs()[1].toString();
        stats.saveTweetLength(tweet);
    }


    /**
     * saves the follow once the following has been successfully done using after returning advice
     * @param joinPoint
     */
    @AfterReturning("execution(public void edu.sjsu.cmpe275.aop.TweetService.follow(..))")
    public void followAfterAdvice(JoinPoint joinPoint) {
        System.out.printf("After returning the execution of the method %s\n", joinPoint.getSignature().toShortString());
        String follower = joinPoint.getArgs()[0].toString();
        String followee = joinPoint.getArgs()[1].toString();
        stats.saveFollow(followee, follower);
    }

    /**
     * saves the blocks after blocking has been done successfully using after returning advice
     * @param joinPoint
     */
    @AfterReturning("execution(public void edu.sjsu.cmpe275.aop.TweetService.block(..))")
    public void blockAfterAdvice(JoinPoint joinPoint) {
        System.out.printf("After Returning the execution of the method %s\n", joinPoint.getSignature().toShortString());
        String follower = joinPoint.getArgs()[0].toString();
        String followee = joinPoint.getArgs()[1].toString();
        stats.saveBlock(followee, follower);
    }


    /**
     * saves the tweet message length after it has been successfully sent
     * @param joinPoint
     */
    @AfterReturning("execution(public void edu.sjsu.cmpe275.aop.TweetService.tweet(..))")
    public void tweetAfterReturningAdvice(JoinPoint joinPoint) {
        System.out.printf("After  returningfinally the execution of the method %s\n", joinPoint.getSignature().toShortString());
        String name = joinPoint.getArgs()[0].toString();
        String tweet = joinPoint.getArgs()[1].toString();
        stats.saveTweet(name, tweet);
        //stats.resetStats();
    }


}
