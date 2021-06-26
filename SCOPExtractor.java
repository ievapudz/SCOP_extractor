import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Random;

public class SCOPExtractor{
    
    ArrayList<String> fileContent;
    
    ArrayList<String> allAlpha;
    ArrayList<String> allBeta;
    ArrayList<String> alphaSlashBeta;
    ArrayList<String> alphaPlusBeta;
    ArrayList<String> smallProtein;
    
    String[] classes;
    /*
     Classes:
     1000000 - all alpha
     1000001 - all beta
     1000002 - alpha / beta
     1000003 - alpha + beta
     1000004 - small protein
     */
    ArrayList<Integer> usedAllAlphaIndexSet;
    ArrayList<Integer> usedAllBetaIndexSet;
    ArrayList<Integer> usedAlphaSlashBetaIndexSet;
    ArrayList<Integer> usedAlphaPlusBetaIndexSet;
    ArrayList<Integer> usedSmallProteinIndexSet;
    
    public SCOPExtractor(){
        classes = new String[]{"all_alpha", "all_beta", "alpha_slash_beta", "alpha_plus_beta", "small_protein"};
    }
    
    public SCOPExtractor(String scopLatestFileName){
        try{
            fileContent = new ArrayList<>(Files.readAllLines(Paths.get(scopLatestFileName)));
            allAlpha = new ArrayList<>();
            allBeta = new ArrayList<>();
            alphaSlashBeta = new ArrayList<>();
            alphaPlusBeta = new ArrayList<>();
            smallProtein = new ArrayList<>();
            
            classes = new String[]{"all_alpha", "all_beta", "alpha_slash_beta", "alpha_plus_beta", "small_protein"};
            
            for(String str : fileContent){
                if(str.contains("CL=1000000")){
                    allAlpha.add(str);
                }else if(str.contains("CL=1000001")){
                    allBeta.add(str);
                }else if(str.contains("CL=1000002")){
                    alphaSlashBeta.add(str);
                }else if(str.contains("CL=1000003")){
                    alphaPlusBeta.add(str);
                }else if(str.contains("CL=1000004")){
                    smallProtein.add(str);
                }
            }
            
            System.out.println("All alpha: " + allAlpha.size());
            System.out.println("All beta: " + allBeta.size());
            System.out.println("Alpha / beta: " + alphaSlashBeta.size());
            System.out.println("Alpha + beta: " + alphaPlusBeta.size());
            System.out.println("Small protein: " + smallProtein.size());
            
            usedAllAlphaIndexSet = new ArrayList<>();
            usedAllBetaIndexSet = new ArrayList<>();
            usedAlphaSlashBetaIndexSet = new ArrayList<>();
            usedAlphaPlusBetaIndexSet = new ArrayList<>();
            usedSmallProteinIndexSet = new ArrayList<>();
            
        }catch(Exception e){
            System.out.println("Exception in SCOPExtractor constructor");
        }
    }
    
    public void parseOtherFile(String filePath){
        try{
            fileContent = new ArrayList<>(Files.readAllLines(Paths.get(filePath)));
        }catch(Exception e){
            System.out.println("Exception in SCOPExtractor constructor");
        }
    }
    
    public static void writeContentToFile(String filePath, ArrayList<String> content, String separator) {
        try {
            FileWriter myWriter = new FileWriter(filePath);
            for(String str : content){
                myWriter.write(str);
                myWriter.write(separator);
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void writeContentToFile(String filePath, ArrayList<String> content, String separator, int howMany) {
        try {
            FileWriter myWriter = new FileWriter(filePath);
            for(int i = 0; i < howMany; i++){
                myWriter.write(content.get(i));
                myWriter.write(separator);
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    public static ArrayList<String> modifyProtein(ArrayList<String> set){
        for(int i = 0; i < set.size(); i++){
            String[] splitStr = set.get(i).split(" ");
            set.set(i, splitStr[1]);
        }
        return set;
    }
    public static ArrayList<String> modifyProtein(ArrayList<String> set, boolean fromChains){
        for(int i = 0; i < set.size(); i++){
            set.set(i, set.get(i).substring(0, set.get(i).length() - 2));
        }
        return set;
    }
    
    public static ArrayList<String> modifyProteinChains(ArrayList<String> set){
        for(int i = 0; i < set.size(); i++){
            String[] splitStr = set.get(i).split(" ");
            String buffer = splitStr[1] + "_" + splitStr[2].charAt(0);
            set.set(i, buffer);
        }
        return set;
    }
    
    public void modifyAllClassesProteinChains(){
        allAlpha = modifyProteinChains(allAlpha);
        allBeta = modifyProteinChains(allBeta);
        alphaSlashBeta = modifyProteinChains(alphaSlashBeta);
        alphaPlusBeta = modifyProteinChains(alphaPlusBeta);
        smallProtein = modifyProteinChains(smallProtein);
    }
    
    public static String pickRandomElement(ArrayList<String> set){
        Random rand = new Random();
        int upperbound = set.size();
        int random_index = rand.nextInt(upperbound);
        return set.get(random_index);
    }
    
    public static ArrayList<String> pickRandomElements(String fraction, ArrayList<String> set){
        Random rand = new Random();
        ArrayList<String> randomSet = new ArrayList<>();
        ArrayList<Integer> usedNumberSet = new ArrayList<>();
        
        int randomSetSize = (int)(Math.floor(set.size() * Double.parseDouble(fraction)));
        int upperbound = set.size();
        
        while(randomSet.size() < randomSetSize){
            int randomIndex = rand.nextInt(upperbound);
            while(usedNumberSet.contains(randomIndex)){
                randomIndex = rand.nextInt(upperbound);
            }
            randomSet.add(set.get(randomIndex));
            usedNumberSet.add(randomIndex);
        }
        
        return randomSet;
    }
    
    public ArrayList<String> pickRandomElements(double fraction, ArrayList<String> set, String setChoice){
        Random rand = new Random();
        ArrayList<String> randomSet = new ArrayList<>();
        
        int randomSetSize = (int)(Math.floor(set.size() * fraction));
        int upperbound = set.size();
        
        while(randomSet.size() < randomSetSize){
            int randomIndex = rand.nextInt(upperbound);
            switch(setChoice){
                case "all_alpha":{
                    while(usedAllAlphaIndexSet.contains(randomIndex)){
                        randomIndex = rand.nextInt(upperbound);
                    }
                    randomSet.add(set.get(randomIndex));
                    usedAllAlphaIndexSet.add(randomIndex);
                    break;
                }
                case "all_beta":{
                    while(usedAllBetaIndexSet.contains(randomIndex)){
                        randomIndex = rand.nextInt(upperbound);
                    }
                    randomSet.add(set.get(randomIndex));
                    usedAllBetaIndexSet.add(randomIndex);
                    break;
                }
                case "alpha_slash_beta":{
                    while(usedAlphaSlashBetaIndexSet.contains(randomIndex)){
                        randomIndex = rand.nextInt(upperbound);
                    }
                    randomSet.add(set.get(randomIndex));
                    usedAlphaSlashBetaIndexSet.add(randomIndex);
                    break;
                }
                case "alpha_plus_beta":{
                    while(usedAlphaPlusBetaIndexSet.contains(randomIndex)){
                        randomIndex = rand.nextInt(upperbound);
                    }
                    randomSet.add(set.get(randomIndex));
                    usedAlphaPlusBetaIndexSet.add(randomIndex);
                    break;
                }
                default:{
                    while(usedSmallProteinIndexSet.contains(randomIndex)){
                        randomIndex = rand.nextInt(upperbound);
                    }
                    randomSet.add(set.get(randomIndex));
                    usedSmallProteinIndexSet.add(randomIndex);
                    break;
                }
            }
        }
        
        return randomSet;
    }
    
    public void extractProteinNames(String howMany){
        allAlpha = modifyProtein(allAlpha);
        allBeta = modifyProtein(allBeta);
        alphaSlashBeta = modifyProtein(alphaSlashBeta);
        alphaPlusBeta = modifyProtein(alphaPlusBeta);
        smallProtein = modifyProtein(smallProtein);
        
        writeContentToFile("./all_alpha/all_alpha.txt", allAlpha, ", ", Integer.parseInt(howMany));
        writeContentToFile("./all_beta/all_beta.txt", allBeta, ", ", Integer.parseInt(howMany));
        writeContentToFile("./alpha_slash_beta/alpha_slash_beta.txt", alphaSlashBeta, ", ", Integer.parseInt(howMany));
        writeContentToFile("./alpha_plus_beta/alpha_plus_beta.txt", alphaPlusBeta, ", ", Integer.parseInt(howMany));
        writeContentToFile("./small_protein/small_protein.txt", smallProtein, ", ", Integer.parseInt(howMany));
    }
    
    public void generateRandomProteinChainsFile(String POI){
        allAlpha = modifyProteinChains(allAlpha);
        allBeta = modifyProteinChains(allBeta);
        alphaSlashBeta = modifyProteinChains(alphaSlashBeta);
        alphaPlusBeta = modifyProteinChains(alphaPlusBeta);
        smallProtein = modifyProteinChains(smallProtein);
        
        ArrayList<String> proteinChainsFileContent = new ArrayList<>();
        proteinChainsFileContent.add(pickRandomElement(allAlpha));
        proteinChainsFileContent.add(pickRandomElement(allBeta));
        proteinChainsFileContent.add(pickRandomElement(alphaSlashBeta));
        proteinChainsFileContent.add(pickRandomElement(alphaPlusBeta));
        proteinChainsFileContent.add(pickRandomElement(smallProtein));
        proteinChainsFileContent.add(POI);
        
        writeContentToFile("./protein_chains.txt", proteinChainsFileContent, "\n");
        ArrayList<String> proteinChainsDownloadFileContent = modifyProtein(proteinChainsFileContent, true);
        writeContentToFile("./protein_chains_download.txt", proteinChainsDownloadFileContent, ", ");
    }
    
    public void generateProteinChainsFiles(){
        allAlpha = modifyProteinChains(allAlpha);
        allBeta = modifyProteinChains(allBeta);
        alphaSlashBeta = modifyProteinChains(alphaSlashBeta);
        alphaPlusBeta = modifyProteinChains(alphaPlusBeta);
        smallProtein = modifyProteinChains(smallProtein);
        
        writeContentToFile("./all_alpha_chains.txt", allAlpha, "\n");
        writeContentToFile("./all_beta_chains.txt", allBeta, "\n");
        writeContentToFile("./alpha_slash_beta_chains.txt", alphaSlashBeta, "\n");
        writeContentToFile("./alpha_plus_beta_chains.txt", alphaPlusBeta, "\n");
        writeContentToFile("./small_protein_chains.txt", smallProtein, "\n");
    }
    
    public void generateModelSet(String modelSetChoice, double fraction){
        ArrayList<String> modelSetAllAlpha = pickRandomElements(fraction, allAlpha, "all_alpha");
        ArrayList<String> modelSetAllBeta = pickRandomElements(fraction, allBeta, "all_beta");
        ArrayList<String> modelSetAlphaSlashBeta = pickRandomElements(fraction, alphaSlashBeta, "alpha_slash_beta");
        ArrayList<String> modelSetAlphaPlusBeta = pickRandomElements(fraction, alphaPlusBeta, "alpha_plus_beta");
        ArrayList<String> modelSetSmallProtein = pickRandomElements(fraction, smallProtein, "small_protein");
        
        ArrayList<String> proteinChainsDownloadFileContent = new ArrayList<>();
        proteinChainsDownloadFileContent.addAll(modelSetAllAlpha);
        proteinChainsDownloadFileContent.addAll(modelSetAllBeta);
        proteinChainsDownloadFileContent.addAll(modelSetAlphaSlashBeta);
        proteinChainsDownloadFileContent.addAll(modelSetAlphaPlusBeta);
        proteinChainsDownloadFileContent.addAll(modelSetSmallProtein);
        
        String[] setNames = new String[5];
        for(int i = 0; i < setNames.length; i++){
            setNames[i] = "./" + modelSetChoice + "_set/" + modelSetChoice + "_" + classes[i] + ".txt";
        }
        
        writeContentToFile(setNames[0], modelSetAllAlpha, "\n");
        writeContentToFile(setNames[1], modelSetAllBeta, "\n");
        writeContentToFile(setNames[2], modelSetAlphaSlashBeta, "\n");
        writeContentToFile(setNames[3], modelSetAlphaPlusBeta, "\n");
        writeContentToFile(setNames[4], modelSetSmallProtein, "\n");
        
        ArrayList<String> downloadFileContent = modifyProtein(proteinChainsDownloadFileContent, true);
        String downloadFileName = "./protein_chains_" + modelSetChoice + "_download.txt";
        writeContentToFile(downloadFileName, downloadFileContent, ", ");
        
    }
    
    public void generateLabelledSet(String setChoice){
        
        String[] setFileNames = new String[classes.length];
        
        for(int i = 0; i < classes.length; i++){
            String buffer = "./" + setChoice + "_set/" + setChoice + "_" + classes[i] + ".txt";
            setFileNames[i] = buffer;
            System.out.println(setFileNames[i]);
        }
        
        ArrayList<String> allChains = new ArrayList<>();
        for(int i = 0; i < classes.length; i++){
            this.parseOtherFile(setFileNames[i]);
            for(int j = 0; j < fileContent.size(); j++){
                String str = fileContent.get(j).concat(" " + classes[i]);
                fileContent.set(j, str);
            }
            allChains.addAll(fileContent);
        }
    
        System.out.println(allChains.size());
        String labelledSetFileName = "./" + setChoice + "_set/" + setChoice + ".txt";
        
        writeContentToFile(labelledSetFileName, allChains, "\n");
    }
    
    public static void main(String[] args){
        // args[0] - provide scop-cla-latest.txt file.
        // args[1] - choosing to get protein file (by "protein"). By default it provides chain file.
        // args[2] - how many elements are required to be written in the file / for "random" case - the protein chain that is POI / for "training" the proportion of all elements to be included in training set.
        //In the result it provides .txt files that contain list of proteins / protein chains.
            
        //SCOPExtractor scopExtr = new SCOPExtractor(args[0]);
        try{
            switch(args[1]){
                case "protein":{
                    SCOPExtractor scopExtr = new SCOPExtractor(args[0]);
                    scopExtr.extractProteinNames(args[2]);
                    break;
                }
                case "random":{
                    SCOPExtractor scopExtr = new SCOPExtractor(args[0]);
                    scopExtr.generateRandomProteinChainsFile(args[2]);
                    break;
                }
                case "model":{
                    SCOPExtractor scopExtr = new SCOPExtractor(args[0]);
                    
                    scopExtr.modifyAllClassesProteinChains();
                    double trainingFraction = Double.parseDouble(args[2]);
                    
                    if(trainingFraction >= 1){
                        throw new Exception("Too big number to set trainingFraction");
                    }
                    double validationFraction = (1 - trainingFraction) / 2;
            
                    scopExtr.generateModelSet("training", trainingFraction);
                    scopExtr.generateModelSet("validation", validationFraction);
                    scopExtr.generateModelSet("testing", validationFraction);
                    break;
                }
                case "labelled":{
                    // passing file, generated in "model" case.
                    
                    SCOPExtractor scopExtr = new SCOPExtractor();
                    scopExtr.generateLabelledSet("training");
                    scopExtr.generateLabelledSet("validation");
                    scopExtr.generateLabelledSet("testing");
                    break;
                }
                default:{
                    SCOPExtractor scopExtr = new SCOPExtractor(args[0]);
                    scopExtr.generateProteinChainsFiles();
                    break;
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
