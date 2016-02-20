package com.java.src;

public class TwitterStreamServer {
    public static void main(String[] args){

        final TwitterStreamConsumer streamConsumer = new TwitterStreamConsumer(); // final because we will later pull the latest Tweet
        streamConsumer.start();
    }
}

