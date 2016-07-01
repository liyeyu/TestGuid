package com.example.administrator.testguid.entity;

/**
 * Created by Liyeyu on 2016/6/18.
 */
public class TreeNode {

    public int data;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data+"";
    }
}
