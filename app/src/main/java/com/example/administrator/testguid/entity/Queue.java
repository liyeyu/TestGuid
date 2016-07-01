package com.example.administrator.testguid.entity;

import java.util.Stack;

/**
 * Created by Liyeyu on 2016/6/21.
 */
public class Queue {
    private Stack<Integer> mStack1,mStack2;
    public Queue() {
        mStack1 = new Stack<>();
        mStack2 = new Stack<>();
    }
    public void push(int value){
        mStack1.push(value);
    }
    public int pop(){
        if(mStack2.isEmpty()){
            while(!mStack1.isEmpty()){
                mStack2.push(mStack1.pop());
            }
        }
        return  mStack2.pop();
    }
}
