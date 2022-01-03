/**
 * @author: Joe Gonzales
 * 
 * This program simulates a social network where users in the network can follow each
 * other. Each user is an object and their followings/followers are stored as lists which
 * then point to a user, depending on if they are following that user and/or being
 * followed by them.
 */

import java.util.ArrayList;

public class SocialNetworkMain{
    
    public static void main (String[] args){
        simulateSmallNetwork();
    }//main
    
    public static void simulateSmallNetwork(){
        Directory newDict = new Directory();
        User test = new User("Joe");
        User test2 = new User("Malak");
        User test3 = new User("Quin");
        User test4 = new User("Craig");
        User test5 = new User("Sarah");
        User test6 = new User("Kellie");
        User test7 = new User("Garett");
        User[] array = {test,test2,test3,test4,test5,test6,test7};
        
        for (int i = 0; i < array.length; i++){
            newDict.insert(array[i]);
        }
        
        for (int i = 0; i < array.length; i++){
            int newVal = (int)Math.floor(Math.random()*array.length);
            for (int j = newVal; j < array.length; j += Math.floor(Math.random()*3)){
                array[i].userFollow(array[j]);
            }
        }
        
        for (int i = 0; i < array.length; i++){
            int newVal = (int)Math.floor(Math.random()*array.length);
            for (int j = newVal; j < array.length; j += Math.floor(Math.random()*3)){
                array[i].userUnfollow(array[j]);
            }
        }
        
        System.out.println(newDict);
        
        System.out.println("Removing a couple users...");
        newDict.remove(test2);
        newDict.remove(test3);
        
        System.out.println(newDict);
    }//simulateSmallNetwork
    
}//SocialNetworkMain

class User{
    //instance variables
    protected String username;
    protected String userActivity;
    protected int followers;
    protected int following;
    protected ArrayList<User> userFollowing;
    protected ArrayList<User> userFollowers;
    
    //constructor
    public User(String newUsername){
        username = newUsername;
        userActivity = "";
        followers = 0;
        following = 0;
        userFollowing = new ArrayList<User>();
        userFollowers = new ArrayList<User>();
    }
    
    //instance methods
/**
 * This method allows one user to follow another. A new user to be followed is
 * passed in as a parameter, and all relevant instance variables are updated. A
 * user cannot follow themselves.
 */
    public void userFollow(User newUser){
        if (newUser != this){
            if (!(searchFollowing(newUser))){
                userFollowing.add(newUser);
                newUser.userFollowers.add(this);
                following++;
                newUser.followers++;
                userActivity += "You followed "+newUser.username+".\n";
                newUser.userActivity += username+" followed you!\n";
            }
        }
    }//userFollow

/**
 * This method allows a user to unfollow someone they are following. The user to be
 * unfollowed is passed in as a parameter, and the method checks if the user is in the
 * list of users being followed before unfollowing the user passed in as a parameter. A
 * user cannot unfollow themselves or unfollow the same user again.
 */
    public void userUnfollow(User newUser){
        if (newUser != this){
            if (searchFollowing(newUser)){
                userFollowing.remove(newUser);
                newUser.userFollowers.remove(this);
                following--;
                newUser.followers--;
                userActivity += "You unfollowed "+newUser.username+".\n";
                newUser.userActivity += username+" unfollowed you.\n";
            }
        }
    }//userUnfollow
    
/**
 * This is primarily a helper method for the userFollow() method in order to prevent a user
 * from following a user that they already follow. Based on the user that is passed in as
 * a parameter, this method determines if the current instance of User this method is called
 * on already follows the new user that was passed in as a parameter.
 */
    public boolean searchFollowing(User newUser){
        boolean exists = false;
        String toSearch = newUser.username;
        
        for (int i = 0; i < userFollowing.size(); i++){
            if (toSearch.equals(userFollowing.get(i).username)){
                exists = true;
            }
        }
        
        return exists;
    }//searchFollowing
    
/**
 * This method returns a list of all of a user's followers.
 */
    public String getFollowers(){
        String toReturn = "";
        
        for (int i = 0; i < userFollowers.size(); i++){
            toReturn += (i+1)+") "+userFollowers.get(i).username+"\n";
        }
        
        return toReturn;
    }//getFollowers

/**
 * This method returns a list of who a user follows.
 */
    public String getFollowings(){
        String toReturn = "";
        
        for (int i = 0; i < userFollowing.size(); i++){
            toReturn += (i+1)+") "+userFollowing.get(i).username+"\n";
        }
        
        return toReturn;
    }//getFollowings

/**
 * toString method which provides all relevant information about a user.
 */
    public String toString(){
        String toReturn = "";
        
        toReturn += "--------------\n"+"User: "+username+"\n"+
                    "Followers: "+followers+"\n"+
                    getFollowers()+
                    "Following: "+following+"\n"+
                    getFollowings()+
                    "Recent activity: \n"+userActivity;
        
        return toReturn;
    }//toString
}//class User

/**
 * This class uses a dictionary to store a directory of all the users currently registered
 * in the social network. The user's username is used as the key that is hashed to a certain
 * index in the dictionary.
 */

class Directory{
    /**
     * Node to be used in a separate chaining implementation.
     */
    private class Node{
        //instance variable
        protected User user;
        protected Node next;
        
        //constructor
        public Node (User newUser, Node newNext){
            user = newUser;
            next = newNext;
        }
        
        //instance methods
        public String toString(){
            return user.toString();
        }
    }//Node
    
    //class constants
    private final int SIZE = 79; //size of the array of Nodes
    private final int HASH_PRIME = 23; //the prime number used for hashing
    
    //instance variables
    private Node[] dictionary;
    private int numUsers;
    
    //constructor
    public Directory(){
        dictionary = new Node[SIZE];
        numUsers = 0;
    }
    
    //instance methods
/**
 * This method receives a new user as a parameter and inserts them into the directory.
 */  
    public void insert(User newUser){
        int index = hash(newUser.username);
        
        if (dictionary[index] == null){
            dictionary[index] = new Node(newUser, null);
            numUsers++;
        } else {
            dictionary[index] = new Node(newUser, dictionary[index]);
            numUsers++;
        }
        
    }//insert

/**
 * This method receives a user as a parameter that is to be removed from the directory. A
 * user cannot be removed if they do not exist in the directory in the first place.
 */
    public void remove(User newUser){
        int index = hash(newUser.username);
        
        if (dictionary[index] != null){
            Node prev = null;
            Node curr = dictionary[index];
            while (curr != null && !(newUser.username.equals(curr.user.username))){
                prev = curr;
                curr = curr.next;
            }
            if (prev == null){
                dictionary[index] = null;
                numUsers--;
            } else {
                prev.next = curr.next;
                numUsers--;
            }
        }
    }//remove
 
/**
 * This toString() method prints out all the users currently stored in the directory.
 */
    public String toString(){
        String users = "************\n"+"Number of users in the network: "+
                        numUsers+"\n************\n";
        
        for (int i = 0; i < SIZE; i++){
            if (dictionary[i] != null){
                Node toPrint = dictionary[i];
                while (toPrint != null){
                    users += toPrint+"\n";
                    toPrint = toPrint.next;
                }
            }
        }
        
        return users;
    }//printVariables
/**
 * Hash function. Uses horner's method in order to generate a polynomial hash code associated
 * with a user.
 */
    private int hash(String key){
        int index = 0;
        
        if (key.length() == 1){
            index = (key.charAt(0))%SIZE;
        } else {
            for (int i = 0; i < key.length(); i++){
                if (i != key.length()-1){
                    index += key.charAt(i); //add the next "chunk"
                    index *= HASH_PRIME; //multiply it by the prime constant
                    index %= SIZE; // modulo N
                } else {
                    index += key.charAt(i);
                    index %= SIZE;
                }
            }
        }
        
        return index;       
    }//hash
    
}//Directory
