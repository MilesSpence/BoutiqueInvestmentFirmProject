import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BoutiqueInvestmentFirmProject {
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        try {
            String filename = args[0];
            System.out.println("JSON file being read from: " + filename);
            Object obj = parser.parse(new FileReader(filename));
            JSONObject jsonObject = (JSONObject) obj;

            String user_id = (String) jsonObject.get("user id");
            JSONArray buyList = (JSONArray) jsonObject.get("buy");
            JSONArray sellList = (JSONArray) jsonObject.get("sell");
            JSONArray cancelList = (JSONArray) jsonObject.get("cancel");

            ArrayList<String> requests = new ArrayList<>();
            if (buyList != null) {
                for (Object o : buyList) {
                    JSONObject buyObject = (JSONObject) o;
                    requests.add(createRequest("buy", String.valueOf(buyObject.get("shares")), (String) buyObject.get("stock symbol"), String.valueOf(buyObject.get("at max")), user_id));
                }
            }
            if (sellList != null) {
                for (Object o : sellList) {
                    JSONObject sellObject = (JSONObject) o;
                    requests.add(createRequest("sell", String.valueOf(sellObject.get("shares")), (String) sellObject.get("stock symbol"), String.valueOf(sellObject.get("at min")), user_id));
                }
            }
            if (cancelList != null) {
                for (Object o : cancelList) {
                    JSONObject cancelObject = (JSONObject) o;
                    requests.add("DELETE FROM BuyRequests, SellRequests WHERE Symbol = '" + cancelObject.get("stock symbol") + "';");
                }
            }
            System.out.println("Requests: " + requests);

            try {
                FileWriter myWriter = new FileWriter("/Users/miles/IdeaProjects/part2/src/MySuperDuperFile.sql");
                for (String request : requests) {
                    myWriter.write(request + "\n");
                }
                myWriter.close();
                System.out.println("Successfully wrote to the file MySuperDuperFile.sql");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            StringBuilder sb = new StringBuilder();
            int i = 0;
            if (cancelList != null) {
                for (Object o : cancelList) {
                    if (i > 0) {
                        sb.insert(0, ", ");
                    }
                    JSONObject cancelObject = (JSONObject) o;
                    sb.insert(0, "(" + cancelObject.get("stock symbol") + " cancel all requests)");
                    i++;
                }
            }
            if (sellList != null) {
                for (Object o : sellList) {
                    if (i > 0) {
                        sb.insert(0, ", ");
                    }
                    JSONObject sellObject = (JSONObject) o;
                    sb.insert(0, "(" + sellObject.get("shares") + " " + sellObject.get("stock symbol") + " shares sell at min " + sellObject.get("at min") + ")");
                    i++;
                }
            }
            if (buyList != null) {
                for (Object o : buyList) {
                    if (i > 0) {
                        sb.insert(0, ", ");
                    }
                    JSONObject buyObject = (JSONObject) o;
                    sb.insert(0, "(" + buyObject.get("shares") + " " + buyObject.get("stock symbol") + " shares buy at max " + buyObject.get("at max") + ")");
                    i++;
                }
            }

            sb.insert(sb.length(), " for account " + user_id + ".");

            String dslStatement = String.valueOf(sb);
            try {
                FileWriter myWriter = new FileWriter("/Users/miles/IdeaProjects/part2/src/MySuperDuperFile.dsl");
                myWriter.write(dslStatement);
                myWriter.close();
                System.out.println("Successfully wrote to the file MySuperDuperFile.dsl");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Private helper method
    private static String createRequest(String buyOrSell, String numShares, String symbol, String price, String accountID) {
        String returnString = "";
        if (buyOrSell.equals("buy")) {
            returnString += "BuyRequests (NumShares, Symbol, MaxPrice, AccountID) VALUES ('" + numShares + "', '" + symbol + "', '" + price + "', '" + accountID + "');";
        } else {
            returnString += "SellRequests (NumShares, Symbol, MinPrice, AccountID) VALUES ('" + numShares + "', '" + symbol + "', '" + price + "', '" + accountID + "');";
        }
        return "INSERT INTO " + returnString;
    }
}
