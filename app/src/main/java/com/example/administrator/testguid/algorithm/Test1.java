package com.example.administrator.testguid.algorithm;

import com.example.administrator.testguid.entity.ListNode;
import com.example.administrator.testguid.entity.Queue;
import com.example.administrator.testguid.entity.TreeNode;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Created by Liyeyu on 2016/6/17.
 */
public class Test1 {

    public static String TAG = "TAG";
    /**
     * 2 阶斐波那契数列 f(0)=0,f(1)=1,f(n)=f(n-1)+f(n-2)
     * @param fibo0
     * @param fibo1
     * @param n
     */
    public static int Fibonacci2(int fibo0,int fibo1,int n){
        if(n==0){
            return fibo0;
        }else if(n==1){
            return fibo1;
        }else {
            return Fibonacci2(fibo0,fibo1,n-1)+Fibonacci2(fibo0,fibo1,n-2);
        }
    }

    /**
     * 斐波那契数列 n阶
     * f(0)=0,f(1)=1
     * f(n)=f(1)+..+f(n-1)->f(n)=2*f(n-1)+f(0) ->2^(n-1)+f(0)  (n>0);
     * @param n
     * @return
     */
    public static double JumpFloor1(int n){
//        return 1<<--n;
        if(n<=0){
            return 0;
        }else if(n==1){
            return 1;
        }else {
            return 2*JumpFloor1(n-1);
        }
    }

    /**
     * 在二维数组中查找num，右上角开始查找
     * @param arrs
     * @param num
     * @return
     */
    public static String findNum(int[][] arrs,int num) {

        //一维数组长度
        int outLength = arrs.length;
        //二维数组长度
        int innerLength = arrs[0].length;

         for (int i = 0, j = innerLength-1; i < outLength && j >= 0;) {
            if (num == arrs[i][j]) {
                return "arr[" + i + "][" + j + "]";
            } else if (arrs[i][j]<num) {
                i++;
                continue;
            }else if(j==0){
                i++;
                continue;
            }else{
                j--;
            }
        }
        return "no find";
    }

    /**
     * 正则替换字符串中的空格
     * @param str
     * @return
     */
    public static String replaceSpance(String str){
        if(str==null)
            return "";
        return  str.replaceAll("\\s","%20");
    }

    /**
     * 打印链表-利用栈先进后出
     * @param node
     * @return
     */
    public static List<Integer> prinftNode(ListNode node){
        List<Integer> list = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        while (node!=null){
            stack.add(node.val);
            node = node.next;
        }
        while (!stack.isEmpty()){
            //栈取数据
            list.add(stack.pop());
        }
        return list;
    }
    /**
     * 打印链表-利用栈先进后出
     * @param node
     * @return
     */
    public static List<Integer> prinftNode1(ListNode node,List<Integer> list){
        List<Integer> dest = null;
        if(list==null)
         list = new ArrayList<>();
        if(node!=null){
            list.add(node.val);
            dest = prinftNode1(node.next,list);
        }else if(!list.isEmpty()){
            dest = new ArrayList<>();
            for (int i=list.size()-1;i>=0;i--){
                dest.add(list.get(i));
            }
        }
        return dest;
    }

    public static TreeNode reConstructBinaryTree(int[] pre,int[] in){

        if(pre==null || in==null){
            return null;
        }

        return  reConstructBinaryTree(pre,0,pre.length-1,in,0,in.length-1);
    }

    /**
     * 根据先序遍历和中序遍历构建二叉树
     * @param pre
     * @param startPre
     * @param endPre
     * @param in
     * @param startIn
     * @param endIn
     * @return
     */
    public static TreeNode reConstructBinaryTree(int[] pre, int startPre, int endPre, int[] in, int startIn, int endIn){

        if(startPre>endPre||startIn>endIn){
            return null;
        }
        TreeNode root = new TreeNode(pre[startPre]);
        //取出根节点，先序遍历先遍历根节点，中序遍历根节点在中间位置
           for(int i=startIn;i<=endIn;i++){
            if(in[i]==pre[startPre]){
                //startPre+1->先序，第2个节点开始到i-1为左子树。中序，第i-1个节点开始到初始位置为左子树
                root.left = reConstructBinaryTree(pre,startPre+1,startPre+i-startIn,in,startIn,i-1);
                root.right = reConstructBinaryTree(pre,i-startIn+startPre+1,endPre,in,i+1,endIn);

            }
        }
        return root;
    }

    /**
     * 通过2个栈构建队列
     * @param pushArr
     * @return
     */
    public static String popQueue(int[] pushArr){
        String str="";
        Queue queue = new Queue();
        for (int i=0;i<pushArr.length;i++){
            queue.push(pushArr[i]);
        }

        for (int i=0;i<pushArr.length;i++){
            str+=queue.pop()+",";
        }
        return str;
    }

    /**
     * 讲一个升序旋转数组反转并返回最小值
     * @param arr
     * @return
     */
    public static int rotateArray(int[] arr){
        if(arr==null || arr.length==0){
            return 0;
        }
        int index = 0;
        int min = arr[index];
        int[] dst = new int[arr.length];
        for (int i=0;i<arr.length;i++){
            if(min>arr[i]){
                min = arr[i];
                System.arraycopy(arr,0,dst,arr.length-i,i);
                System.arraycopy(arr,i,dst,0,arr.length-i);
                Logger.i(Arrays.toString(dst));
                break;
            }
        }

        return min;
    }

    /**
     * 递归方式计算斐波那契数列
     * @param n
     * @param num
     * @return
     */
    public static int printfFibonacci(int n,int num){

        if(n<=1){
            return n;
        }
        for(int i=2;i<=n;i++){
            Logger.i("f("+i+")=f("+(i-1)+")+"+"f("+(i-2)+")");
            num = printfFibonacci(i-1,num)+printfFibonacci(i-2,num);
        }
        return num;
    }

    /**
     * 简单方式计算斐波那契数列
     * @param n
     * @return
     */
    public static int printfFibonacci1(int n){
        if(n<=1){
            return n;
        }
        int num = 0;
        for(int i=2;i<=n;i++){
            Logger.i("f("+i+")=f("+(i-1)+")+"+"f("+(i-2)+")");
            num += (i-1)+(i-2);
        }
        return num;
    }
}
