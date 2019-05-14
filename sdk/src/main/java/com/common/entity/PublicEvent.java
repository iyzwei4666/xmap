package com.common.entity;

public class PublicEvent {
    public static class BackPressed {
        public int state ;
        public BackPressed(int state) {
            this.state = state;
        }
    }
}
