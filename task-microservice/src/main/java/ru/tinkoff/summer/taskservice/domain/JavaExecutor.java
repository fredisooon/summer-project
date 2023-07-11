package ru.tinkoff.summer.taskservice.domain;

import java.io.*;

public class JavaExecutor implements LanguageExecutor {
    private final DriverProcessor driverProcessor;
    private final String PATH_TO_DRIVER = "Driver.java";

    public JavaExecutor() {
        this.driverProcessor = new JavaDriverProcessor();
    }

    @Override
    public Language getLanguage() {
        return Language.JAVA;
    }

    @Override
    public ExecutionResult execute(Attempt attempt) {
        var code = driverProcessor.getPreparedCode(attempt);
        var file = writeTempCode(code);
        var launcher = new ProgramLauncher();
        launcher.compileProgram("javac", file.getParent() + "/" + PATH_TO_DRIVER);
        var result =
                launcher.testProgram(
                        attempt.getTask().getTaskTestCases(),
                        "java",
                        "-cp",
                        file.getParent(),
                        "Driver");

       deleteFolder( file.getParentFile());

        return result;
    }

    public static void deleteFolder(File folder) {
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
            folder.delete();
        }
    }
    private File writeTempCode(String preparedDriver) {
        String path = PATH_TO_TMP + "/" + System.currentTimeMillis() + "/" + PATH_TO_DRIVER;

        File file = new File(path);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(preparedDriver);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
