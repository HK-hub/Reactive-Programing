package com.hk.reactive.nio;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author : HK意境
 * @ClassName : TestFilesWalkFileTree
 * @date : 2022/12/19 13:54
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class TestFilesWalkFileTree {



    public static void main(String[] args) throws IOException {

        LongAdder fileCounter = new LongAdder();
        LongAdder pathCounter = new LongAdder();
        Files.walkFileTree(Paths.get("./"), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("-->" + dir);
                pathCounter.increment();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                fileCounter.increment();
                System.out.println(file.getFileName());
                return super.visitFile(file, attrs);
            }
        });

        System.out.println(pathCounter.longValue() + "->" + fileCounter.longValue());

    }


}
