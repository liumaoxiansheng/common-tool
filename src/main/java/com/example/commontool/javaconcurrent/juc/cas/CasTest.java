package com.example.commontool.javaconcurrent.juc.cas;

import lombok.Data;
import sun.misc.Unsafe;

/**
 * @ClassName CasTest
 * @Description cas测试
 * @Author tianhuan
 * @Date 2022/6/6
 **/
public class CasTest {

    public static void main(String[] args) {
        Entity entity = new Entity();
        Unsafe unsafe = UnSafeFactory.getUnsafeReflection();
        long offset = UnSafeFactory.getFieldOffset(unsafe, Entity.class, "age");
        System.out.println("age"+offset);
        boolean ok;
        ok= unsafe.compareAndSwapInt(entity, offset, 0, 3);
        System.out.println(ok + "\t" + entity.age);

        ok= unsafe.compareAndSwapInt(entity, offset, 3, 5);
        System.out.println(ok + "\t" + entity.age);

        ok= unsafe.compareAndSwapInt(entity, offset, 3, 8);
        System.out.println(ok + "\t" + entity.age);

        System.out.println("entity:"+entity);

        entity.setName("jack");
        long name = UnSafeFactory.getFieldOffset(unsafe, Entity.class, "name");
        System.out.println("name"+name);

        ok = unsafe.compareAndSwapObject(entity, name, "jack", "mark");
        System.out.println(ok + "\t" + entity.name);

        ok = unsafe.compareAndSwapObject(entity, name, "mark", "james");
        System.out.println(ok + "\t" + entity.name);

        ok = unsafe.compareAndSwapObject(entity, name, "mark", "kobe");
        System.out.println(ok + "\t" + entity.name);

    }



    @Data
    public static class Entity{
        private int age;
        private String name;
    }
}
