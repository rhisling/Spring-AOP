package edu.sjsu.cmpe275.aop;

import java.util.*;

public class TweetStatsImpl implements TweetStats {

    private int lengthOfLongestTweetAttempted = 0;
    private Map<String, HashSet<String>> followMap = new TreeMap<String, HashSet<String>>();
    private Map<String, HashSet<String>> blockMap = new TreeMap<String, HashSet<String>>();
    private Map<String, Integer> tweetLengthMap = new TreeMap<String, Integer>();


    /**
     * clears all the measurements to the defaults
     */
    @Override
    public void resetStatsAndSystem() {
        lengthOfLongestTweetAttempted = 0;
        followMap.clear();
        blockMap.clear();
        tweetLengthMap.clear();

    }

    /**
     * Find the length of the longest tweet attempted even if failed
     * @return length of the longest tweet attempted
     */
    @Override
    public int getLengthOfLongestTweetAttempted() {
        return lengthOfLongestTweetAttempted;
    }


    /**
     * Finds the most followed user based on the number of followers ,
     * if the user has blocked the followers, it is counted towards the calculation
     *
     * @return the user with maximum followers
     */
    @Override
    public String getMostFollowedUser() {
        int maxFollowers = 0;
        String user = null;
        for (Map.Entry<String, HashSet<String>> entry : followMap.entrySet()) {
            HashSet<String> followers = entry.getValue();
            int followersize = entry.getValue().size();
            for (String follower : followers) {
                if (blockMap.containsKey(follower)) {
                    if (blockMap.get(follower).contains(entry.getKey())) {
                        followersize--;
                    }
                }
            }
            if (followersize > maxFollowers) {
                maxFollowers = followersize;
                user = entry.getKey();
            }
        }
        return user;
    }

    /**
     * find the most productive user based on the total length of messages they tweeted from the beginning
     * if there is a tie returns the user in alphabetical order
     * @return the user who has tweeted most
     */
    @Override
    public String getMostProductiveUser() {
        String user = null;
        int maxLength = 0;
        for (Map.Entry<String, Integer> entry : tweetLengthMap.entrySet()) {
            if (entry.getValue() > maxLength) {
                maxLength = entry.getValue();
                user = entry.getKey();
            }
        }
        return user;
    }

    /**
     * finds the most blocked follower, if there is a tie returns the one in users in alphabetical order
     * @return most blocked follower
     */
    @Override
    public String getMostBlockedFollower() {
        int maxBlocks = 0;
        String mostBlockedFollower = null;
        for (Map.Entry<String, HashSet<String>> entry : blockMap.entrySet()) {
            int blocks = entry.getValue().size();
            if (blocks > maxBlocks) {
                maxBlocks = blocks;
                mostBlockedFollower = entry.getKey();
            }
        }
        return mostBlockedFollower;
    }

    /**
     * updates the tweetlength attempted with the maximum
     *
     * @param message
     */

    public void saveTweetLength(String message) {
        if (message.length() > lengthOfLongestTweetAttempted) {
            lengthOfLongestTweetAttempted = message.length();
        }
    }

    /**
     * creates a map using the name and message length
     *
     * @param name
     * @param message
     */
    public void saveTweet(String name, String message) {
        if (tweetLengthMap.containsKey(name)) {
            int currentLength = tweetLengthMap.get(name);
            currentLength += message.length();
            tweetLengthMap.put(name, currentLength);
        } else {
            tweetLengthMap.put(name, message.length());
        }


    }

    /**
     * Creates a map for using the followee and the follower
     *
     * @param followee
     * @param follower
     */
    public void saveFollow(String followee, String follower) {
        if (followMap.containsKey(followee)) {
            Set set = followMap.get(followee);
            set.add(follower);
        } else {
            HashSet<String> set = new HashSet<String>();
            set.add(follower);
            followMap.put(followee, set);
        }

    }


    /**
     * creates a map for the blocked user
     *
     * @param followee
     * @param user
     */

    public void saveBlock(String followee, String user) {
        if (blockMap.containsKey(followee)) {
            Set set = blockMap.get(followee);
            set.add(user);
        } else {
            HashSet<String> set = new HashSet<String>();
            set.add(user);
            blockMap.put(followee, set);
        }
    }
}



