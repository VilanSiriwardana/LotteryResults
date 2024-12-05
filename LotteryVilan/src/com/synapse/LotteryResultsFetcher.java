package com.synapse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class LotteryResultsFetcher {

    public static void main(String[] args) {
        // URL of the page displaying lottery results
        String url = "https://www.lankayp.com/dlb/result/jayoda";  // The lottery website URL

        // MySQL database connection parameters
        String dbUrl = "jdbc:mysql://13.234.242.165:3306/a1?useSSL=false";  // DB IP and name
        String dbUser = "iDevice";  // MySQL username
        String dbPassword = "iDevice_123456";  // MySQL password

        // SQL query to insert data into lotteryResultsVSS table
        String insertQuery = "INSERT INTO lotteryResultsVSS (drawDate, lottoGame, winningNumbers, nextJackpot, event) VALUES (?, ?, ?, ?, ?)";

        try {
            // Fetching the HTML content of the page
            Document doc = Jsoup.connect(url).get();

            // Selecting all rows in the table (adjust selector if needed)
            Elements lotteryRows = doc.select("table tbody tr");
            
            int i = 1;

            // Looping through each row and extracting the lottery data
            for (Element row : lotteryRows) {
                String drawDate = row.select("td:nth-child(1)").text(); 
                String lottoGame = row.select("td:nth-child(2)").text();  
                String winningNumbers = row.select("td:nth-child(3)").text();  
                String nextJackpot = row.select("td:nth-child(4)").text();  
                String event = row.select("td:nth-child(5)").text(); 
                
                

                // Checking if the row contains data
                if (!drawDate.isEmpty()) {
                    // Printing the results to the console (optional)
                	System.out.println("Result Number: " + i);
                    System.out.println("Draw Date: " + drawDate);
                    System.out.println("Lotto Game: " + lottoGame);
                    System.out.println("Winning Numbers: " + winningNumbers);
                    System.out.println("Next Jackpot: " + nextJackpot);
                    System.out.println("Event: " + event);
                    System.out.println("------------------------------");

                    // Connecting to the MySQL database and inserting the data
                    try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                         PreparedStatement statement = connection.prepareStatement(insertQuery)) {

                        // Setting parameters for the SQL query
                        statement.setString(1, drawDate);  
                        statement.setString(2, lottoGame); 
                        statement.setString(3, winningNumbers);  
                        statement.setString(4, nextJackpot);  
                        statement.setInt(5, Integer.parseInt(event));  

                        // Executing the insert statement
                        statement.executeUpdate();
//                        System.out.println("Data inserted into database.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
