package com.example.commontool.javaconcurrent.juc.cas;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @ClassName UnSafeFactory
 * @Description Cas Unsafe
 * @Author tianhuan
 * @Date 2022/6/6
 **/
public class UnSafeFactory {

    public static Unsafe getUnsafe(){
       return Unsafe.getUnsafe();
    }

    public static Unsafe getUnsafeReflection(){
        Field field = null;
        try {
            field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return  (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getFieldOffset(Unsafe unsafe,Class clazz,String fieldName){
        try {
            long l = unsafe.objectFieldOffset(clazz.getDeclaredField(fieldName));
            return l;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
