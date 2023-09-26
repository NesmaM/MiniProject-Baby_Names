import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.File;

/**
 * Write a description of BabyBirths here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BabyBirths {
    public void printNames(){
        FileResource fr = new FileResource();
        for (CSVRecord record : fr.getCSVParser(false)){
            int numBorn = Integer.parseInt(record.get(2));
            if ( numBorn <= 100){
                System.out.println("Name " + record.get(0) + 
                               " Gender " + record.get(1) +
                                " Num Born " + record.get(2));
            }       
        }
    }
    
    public void totalBirths(FileResource fr){
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        for (CSVRecord record : fr.getCSVParser(false)){
            int numBorn = Integer.parseInt(record.get(2));
            totalBirths += numBorn;
            if(record.get(1).equals("M")){
                totalBoys += numBorn;
            } else {
                totalGirls += numBorn;
            }
        }
        System.out.println("Total births = " + totalBirths);
        System.out.println("Total number of Boys = " + totalBoys);
        System.out.println("Total number of Girls = " + totalGirls);       
    }

    public void testTotalBirths(){
        FileResource fr = new FileResource("data/yob1905.csv");
        totalBirths(fr);
    }
    
    public int getRank(int year, String name, String gender){
        String fileName = "yob" + year + ".csv";
        FileResource fr = new FileResource("data/"+fileName);
        int rank = 1;
        for(CSVRecord record: fr.getCSVParser(false)){
            //Increment rank if gender matches param
            if (record.get(1).equals(gender)){
                //return name if name matches param
                if (record.get(0).equals(name)){
                    return rank;
                }
                rank ++;
            }
        }
        return -1;
    }
    
    public void testGetRank(){
        int year = 1971;
        String name = "Frank";
        String gender = "M";
        int rank = getRank(year, name, gender);
        System.out.println("Rank of " + name + ", " + gender + ", in " + year + ": " + rank);
    }
    
    public String getName (int year, int rank, String gender){
        String fileName = "yob" + year + ".csv";
        FileResource fr = new FileResource("data/"+fileName);
        String name = "";
        int currentRank = 0;
        //For every name in file
        for (CSVRecord record : fr.getCSVParser(false)){
            // get it's rank if gender matches param
            if (record.get(1).equals(gender)){
                //return last name whose rank matches parm
                if (currentRank == rank){
                    return name;
                }
                name = record.get(0);
                currentRank ++;
            }
        }    
        return "No NAME";
    }
    
    public void testGetName(){
        int year = 1982;
        int rank = 450;
        String gender = "M";
        String name = getName(year, rank, gender);
        System.out.println("The name: " + name + " has the rank: " + rank + " at year: " + year);
    
    }
    
    public void whatIsNameInYear (String name, int year, int newYear, String gender){
        //determine rank of name in the year they were born
        int rank = getRank(1974, name, gender);
        // Determine name born in newYear that is at the same rank and gender
        String newName = getName(newYear, rank, gender);
        System.out.println(name + " born in " + year + " would be " + newName + " if born in " + newYear);
           
    }
    
    public void testWhatIsNameInYear(){
        whatIsNameInYear("Owen", 1974, 2014, "F");
    }
    
    public int yearOfHighestRank (String name, String gender){
        //Allow user to select range of files
        DirectoryResource dr = new DirectoryResource();
        int year = 0;
        int rank = 0;
        //for every file selected
        for (File f : dr.selectedFiles()){
            //Extract year from the file name
            int currentYear = Integer.parseInt(f.getName().substring(3,7));
            //Determine rank of name in current year 
            int currentRank = getRank(currentYear, name, gender);
            System.out.println("Rank in year " + currentYear + " is: " + currentRank);
            //if current rank isn't valid
            if(currentRank != -1){
                //If on first file or if current rank is higher than saved rank
                if (rank == 0 || currentRank < rank){
                    //update tracker variables
                    rank = currentRank;
                    year = currentYear;
                }
            }
        }
        if (year == 0){
            return -1;
        } 
        return year;
    }
    
    public void testYearOfHighestRank(){
        String name = "Mich";
        String gender = "M";
        int year = yearOfHighestRank(name, gender);
        System.out.println("The year of the highest rank for " + name + " of gender " + gender + " is: " +year);
    }
    
    public double getAverageRank (String name, String gender){
        double totalRank = 0.0;
        DirectoryResource dr = new DirectoryResource();
        int count = 0;
        for (File f : dr.selectedFiles()){
            //Extract the year from file name 
            int currentYear = Integer.parseInt(f.getName().substring(3,7));
            //Determine rank of name in current year
            int currentRank = getRank(currentYear, name, gender);
            //Add rank total and increment count
            totalRank += currentRank;
            count ++;
        }
        //Return calculated average rank
        if (totalRank == 0){
            return -1;
        }
        double average = totalRank/count;
        return average;
    }
    
    public void testGetAverageRank(){
        String name = "Robert";
        String gender = "M";
        double avg = getAverageRank(name, gender);
        System.out.println("Average rank of " + name + " , " + gender + " is : " + avg);
    }
    
    public int getTotalBirthsRankedHigher (int year, String name, String gender){
        //get number of birth for given name and gender
        int numOfBirths = 0;
        FileResource fr = new FileResource();
        for (CSVRecord record : fr.getCSVParser(false)){
            if (record.get(0).equals(name) && record.get(1).equals(gender)){
                numOfBirths = Integer.parseInt(record.get(2));
            }
        }
        
        //Add up number of births greater than that for given name and gender
        int totalBirths = 0;
        for (CSVRecord record : fr.getCSVParser(false)){
            String currentGender = record.get(1);          
            //If name is not given name AND current gender matches param
            //AND current num of births is higher than for given name
            if(!record.get(0).equals(name) && currentGender.equals(gender) 
            && Integer.parseInt(record.get(2)) >= numOfBirths){
                //Add number of births to total
                totalBirths +=Integer.parseInt(record.get(2));
            }
        }
        return totalBirths;
    }
    
    public void testGetTotalBirthsRankedHigher(){
        int year = 1990;
        String name = "Drew";
        String gender = "M";
        int totalBirths = getTotalBirthsRankedHigher(year, name , gender);
        System.out.println("Total number of births of those with the same gender who are ranked higher than " + name + " , " + gender + " in " + year + " : " + totalBirths);
        
    }
}