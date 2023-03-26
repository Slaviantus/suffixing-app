package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.Mode.COPY;
import static org.example.Mode.MOVE;


public class SuffixEditor {

    private String suffix = "";
    private Enum mode = null;
    private List <String> files = new ArrayList<>();

    private static Logger logger = Logger.getLogger("SuffixLog");

    private boolean readProperties(String propertiesPath) throws IOException {
        Properties properties = new Properties();
        FileInputStream input = new FileInputStream(propertiesPath);
        properties.load(input);

        if (initFileNames(properties.getProperty("files")) &&
                initSuffix(properties.getProperty("suffix")) &&
                    initMode(properties.getProperty("mode")))
        {
            input.close();
            return true;
        }
        else
        {
            input.close();
            return false;
        }
    }

    private boolean initFileNames(String filesProperty)
    {
        if ((filesProperty == null) || filesProperty.equals(""))
        {
            logger.log(Level.WARNING, "No files are configured to be copied/moved");
            return false;
        }

        else
        {
            String[] filesList = filesProperty.split(":");

            for (String fileName : filesList)
                files.add(fileName);

            return true;
        }
    }

    private boolean initMode(String modeProperty)
    {
        if (modeProperty.toLowerCase().equals("copy"))
        {
            mode = COPY;
            return true;
        }
        else if (modeProperty.toLowerCase().equals("move"))
        {
            mode = MOVE;
            return true;
        }
        else
        {
            logger.log(Level.SEVERE, "Mode is not recognized: " + modeProperty);
            return false;
        }
    }


    private boolean initSuffix(String suffixProperty)
    {
        if ((suffixProperty == null) || suffixProperty.equals(""))
        {
            logger.log(Level.SEVERE, "No suffix is configured");
            return false;
        }
        else
        {
            suffix = suffixProperty;
            return true;
        }
    }



    public void addSuffix(String propertyPath) throws IOException {

        if (readProperties(propertyPath))
        {
            List<String> newFilesNames = new ArrayList<>();

            for (String file : files) {

                String fileName = file.split("\\.")[0];
                String extention = file.split("\\.")[1];

                StringBuilder builder = new StringBuilder();
                builder.append(fileName);
                builder.append(suffix);
                builder.append(".");
                builder.append(extention);
                newFilesNames.add(builder.toString());
            }

            saveFilesWithNewName(newFilesNames);
        }
    }

    private void saveFilesWithNewName(List<String> newFilesNames)
    {
        for (int i = 0; i < files.size(); i++)
        {
            File originalFile = new File(files.get(i));
            File copiedFile = new File(newFilesNames.get(i));

            if (originalFile.exists())
            {
                executeSavingFiles(originalFile, copiedFile);
            }
            else
            {
                logger.log(Level.SEVERE, "No such file: " + files.get(i));
            }
        }
    }


    private void executeSavingFiles(File originalFile, File copiedFile)
    {
        String arrowSymbol = " => ";

        try
        {
            Files.copy(originalFile.toPath(), copiedFile.toPath());

            if (mode == MOVE)
            {
                originalFile.delete();
            }
            else
            {
                arrowSymbol = " -> ";
            }

            logger.log(Level.INFO, originalFile.getPath().replace('\\', '/')
                                        + arrowSymbol
                                            + copiedFile.getPath().replace('\\', '/'));

        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


}