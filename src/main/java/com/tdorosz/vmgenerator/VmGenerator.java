package com.tdorosz.vmgenerator;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Scanner;

public class VmGenerator {

    private final static String DEFAULT_BOX_VERSION = "0.0.1";
    private final static String BASE_VM_PATH = "D:\\vagrant\\";

    public static void main(String[] args) throws IOException {
        TemplateResolver.TemplateResolverBuilder templateResolverBuilder = TemplateResolver.builder();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Project name:");
        String projectName = scanner.nextLine();
        templateResolverBuilder.hostname(projectName);

        System.out.println("IP Address:");
        String ipAddress = scanner.nextLine();
        templateResolverBuilder.ipAddress(ipAddress);

        System.out.printf("Box Version (default %s)%n", DEFAULT_BOX_VERSION);
        String boxVersion = scanner.nextLine();
        templateResolverBuilder.boxVersion(boxVersion.isBlank() ? DEFAULT_BOX_VERSION : boxVersion);

        Path vmPath = Path.of(BASE_VM_PATH + projectName);
        TemplateResolver templateResolver = templateResolverBuilder
                .template(readFileFromResources("/Vagrantfile"))
                .build();

        System.out.println("Summary:");
        System.out.printf("Project name: %s%n", templateResolver.getHostname());
        System.out.printf("Target: %s%n", vmPath);
        System.out.printf("IP Address: %s%n", templateResolver.getIpAddress());
        System.out.printf("Box Version: %s%n", templateResolver.getBoxVersion());
        System.out.println("Do you want to create new VM? (y/n)");

        String confirmation = scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("y")) {
            System.out.println("Project hasn't been created");
            return;
        }

        System.out.println("Creating project....");


        createFile(vmPath.resolve("Vagrantfile"), templateResolver.generateVagrantfile());
        copyFileFromResources(vmPath, "provision/init.sh");
    }

    private static void copyFileFromResources(Path vmPath, String filePath) throws IOException {
        createFile(vmPath.resolve(filePath), readFileFromResources("/" + filePath));
    }

    private static void createFile(Path path, String content) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, content, StandardOpenOption.CREATE_NEW);
    }

    @SneakyThrows
    private static String readFileFromResources(String path) {
        InputStream resourceAsStream = VmGenerator.class.getResourceAsStream(path);
        if (Objects.isNull(resourceAsStream)) {
            throw new RuntimeException(String.format("File: [%s] not found in resources.", path));
        }
        return new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
