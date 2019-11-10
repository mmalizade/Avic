package ir.moovic.entertainment.utils;

import java.util.Stack;

public class SizedStack<T> extends Stack<T> {
    private int maxSize;
    private StackListener<T> stackListener;
    public SizedStack(int size, StackListener<T> stackListener) {
        super();
        this.stackListener = stackListener;
        this.maxSize = size;
    }

    @Override
    public T push(T object) {
        //If the stack is too big, remove elements until it's the right size.
        while (this.size() >= maxSize) {
            if(stackListener != null){
                stackListener.onForceRemoveFromStack(this.get(0));
            }
            this.remove(0);
        }
        return super.push(object);
    }

    public void moveToTop(T element){
        if(element == null) return;
        int index = indexOf(element);
        int size = this.size();
        if(index < 0 || index == (size-1) ){
            return;
        }
        Stack<T> temp = new Stack<>();
        while(this.size() > index){
            temp.push(this.pop());
        }
        if(!temp.isEmpty()){
            temp.pop();
        }
        while(!temp.isEmpty()){
            this.push(temp.pop());
        }
        this.push(element);
        temp = null;
    }

    public static interface StackListener<T> {
        void onForceRemoveFromStack(T obj);
    }
}