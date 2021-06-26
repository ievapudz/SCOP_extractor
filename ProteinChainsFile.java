import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class ProteinChainsFile{
    
    private String[] trainingSetFiles;
    private String[] validationSetFiles;
    private String[] testingSetFiles;
    
    public void setFilePaths(String setChoice){
        
    }
    
    public void parseFile(String path){
        try{
            fileContent = new ArrayList<>(Files.readAllLines(Paths.get(path)));
        }catch(Exception e){
            System.out.println("Exception in parseFile()");
        }
    }
    
    public static void main(String[] args){
        
    }
}
