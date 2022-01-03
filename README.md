# social-network

This program is meant to simulate a social network made up of users who
can follow each other. The idea behind writing this program was to give a
real-world application of something that can be represented with a digraph.
Users are vertices, outdegrees are who a user is following, and indegrees
are a user's followers.

Users are objects which store basic data such as their username and follower
counts. The users they follow and are followed by are kept as ArrayLists, which
keeps pointers to other users they follow and are followed by.

The program contains basic functions that are commonly found on social
network platforms.

A directory of all users in the social network is stored as a hash table that
uses separate chaining.
