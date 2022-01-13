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
        //create directory
        System.out.println("Creating a new directory of users...");
        Directory newDict = new Directory();
        //create users
        System.out.println("Creating users...");
        User test = new User("Joe");
        User test2 = new User("Malak");
        User test3 = new User("Quin");
        User test4 = new User("Craig");
        User test5 = new User("Sarah");
        User test6 = new User("Kellie");
        User test7 = new User("Garett");
        User test8 = new User("Joe");
        
        //store unique users in array
        User[] array = {test,test2,test3,test4,test5,test6,test7,test8};
        System.out.println("Usernames created:");
        for (User user : array){
            System.out.println(user.username);
        }
        
        //add users to the directory
        System.out.println("Adding users to the directory...");
        try {
            for (int i = 0; i < array.length; i++){
                newDict.insert(array[i]);
            }
        } catch (UserExistsException uee){
            System.out.println(uee.getMessage()+"\n");
        }
        
        //simulate following
        System.out.println("Randomly following each other...");
        for (int i = 0; i < array.length; i++){
            for (int j = 0; j < array.length; j++){
                array[i].userFollow(newDict.getRandomUser());
            }
        }
        System.out.println("Randomly unfollowing each other...");
        for (int i = 0; i < array.length; i++){
            for (int j = 0; j < array.length; j++){
                array[i].userUnfollow(newDict.getRandomUser());
            }
        }
        
        //final report of the directory
        System.out.println(newDict);
        
        System.out.println("Removing a couple users...");
        newDict.remove(test2);
        newDict.remove(test3);
        
        System.out.println(newDict);
        System.out.println("Program finished running.");
    }//simulateSmallNetwork
    
}//SocialNetworkMain

/******
 * CLASS: User
 ******/

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

/******
 * CLASS: Directory
 *
 * This class uses a dictionary to store a directory of all the users currently registered
 * in the social network. The user's username is used as the key that is hashed to a certain
 * index in the dictionary.
 ******/

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
    protected Node[] dictionary;
    protected int numUsers;
    
    //constructor
    public Directory(){
        dictionary = new Node[SIZE];
        numUsers = 0;
    }
    
    //instance methods
/**
 * This method receives a new user as a parameter and inserts them into the directory.
 */  
    public void insert(User newUser) throws UserExistsException{
        int index = hash(newUser.username);
        
        if (!(this.search(newUser.username))){
            if (dictionary[index] == null){
                dictionary[index] = new Node(newUser, null);
                numUsers++;
            } else {
                dictionary[index] = new Node(newUser, dictionary[index]);
                numUsers++;
            }
        } else {
            throw new UserExistsException("Cannot create user with the name "+newUser.username+
                        " as a user with this name already exists.");
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
 * This method receives a username and searches the directory for that user. Returns true
 * if that user exists, false otherwise. Primarily used as a helper method to prevent duplicate
 * usernames.
 */
    public boolean search(String username){
        boolean exists = false;
        int index = hash(username);
        Node curr = dictionary[index];
        
        if (curr != null){
            while (curr != null && !(username.equals(curr.user.username))){
                curr = curr.next;
            }
            if (username.equals(curr.user.username)){
                exists = true;
            }
        }
        
        return exists;
    }//search

/**
 * This method returns a randomly selected user from the directory. Used as a helper method 
 * for testing.
 */
    public User getRandomUser(){
        User toReturn = null;
        
        while (toReturn == null){
            for (int i = 0; i < SIZE; i++){
                if (dictionary[i] != null){
                    double randomNum = Math.random();
                    Node curr = dictionary[i];
                    while (curr != null){
                        if (randomNum > 0.5){
                            return curr.user;
                        } else {
                            curr = curr.next;
                        }//if-else
                    }//while
                }//if
            }//for
        }//while
        
        return toReturn;
    }//getRandomUser
    
/**
 * Returns the dictionary's size.
 */
    public int getDictSize(){
        return SIZE;
    }//getDictSize    
    
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

/******
 * CLASS: UserExistsException
 ******/
 
class UserExistsException extends Exception{
    
    public UserExistsException(String message){
        super(message);
    }
    
}//UserExistsException
