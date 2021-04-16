package org.feng.servlet.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) throws IOException {
        System.out.println(
                Files.lines(
                        Paths.get(
                                "/Users/aegon/weaver/project/e-builder/e-builder-e10/weapp-ebuilder-designer/tsconfig.json")
                ).collect(Collectors.joining("\n")));
    }
}
