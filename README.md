# social-network

This program is meant to simulate a social network made up of users who
can follow each other.

Users are objects which store basic data such as their username and follower
counts. The users they follow and are followed by are kept as ArrayLists, which
keeps pointers to other users they follow and are followed by. Visually, this
could be represented with a digraph.

The program contains basic functions that are commonly found on social
network platforms.

A directory of all users in the social network is stored as a hash table that
uses separate chaining.
