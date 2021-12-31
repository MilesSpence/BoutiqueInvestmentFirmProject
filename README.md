# BoutiqueInvestmentFirmProject

### The following are the instructions for the project:

### Overview

In a small boutique investment firm, the front-end and back-end teams are developing a highly customized software system. The front-end team maintains a GUI through which traders issue stock trade requests. The back-end team processes these requests. A domain-specific language (DSL) for trading stocks formalizes the interactions between the teams, while also leaving a record that non-technical users and managers can understand. 

The DSL has this grammar:

    <stock_trade_requests> →  ‘(' <trade> {‘,’ <trade>} ‘) for account' <acct_ident>’.’
 
    \<trade\> → <number> <stock_symbol> ‘shares’ (‘buy at max' | ‘sell at min') <number>
    
    \<number\> →  [1-9] {[0-9]}
  
    \<stock_symbol\> → 'AAPL'|'HP'|'IBM'|'AMZN'|'MSFT'|'GOOGL'|'INTC'|'CSCO'|'ORCL'|'QCOM'
    
    \<acct_ident\> →  ‘“‘alpha_char { alpha_char | digit | ’_’} ‘“‘

Note:  ‘“‘ is a “ surrounded by ‘
    
The DSL is processed by generating SQL statements. As the processor occasionally produces buggy output, you are to build a test data generator for testing the DSL processor. The generator is to accept an input file in JSON format and generate two files containing: (1) the expected DSL statement and (2) the expected SQL statements, one per line. These statements will then be used for testing the DSL’s processing engine (not your responsibility).

Consider the following JSON representation:

    {
        "user id" : "Hokie123",
        "buy" : [
            {"stock symbol" : "IBM", "shares" : 100, "at max" : 45},
            {"stock symbol" : "GOOGL", "shares" : 50, "at max" : 60},
            {"stock symbol" : "AMZN", "shares" : 120, "at max" : 70}
        ],
        "sell" : [
            {"stock symbol" : "ORCL", "shares" : 30, "at min" : 25},
            {"stock symbol" : "GOOGL", "shares" : 20, "at min" : 40} 
        ] 
    }


This JSON can be used to generate the DSL statement that puts into effect the following functionality: User “Hokie123” requests: (1) to buy 100 shares of IBM, when the stock is priced at most $45 per share, (2) to buy 50 shares of GOOGL, when the stock is priced at most $60 per share, (3) to buy 120 shares of AMZN, when the stock is priced at most $70 per share, (4) to sell 30 shares of ORCL, when the price is not lower than $25 per share, (5) to sell 20 shares of GOOGL, when the price is not lower than $40 per share. 

When running the generated DSL statement, it would output the following SQL statements:

    INSERT INTO BuyRequests (NumShares, Symbol, MaxPrice, AccountID) VALUES (‘100’, ‘IBM’, ‘45’,  ‘Hokie123’)
    …
    ...
    INSERT INTO SellRequests(NumShares, Symbol, MinPrice, AccountID) VALUES(‘30’, ‘ORCL’, ‘25’, ‘Hokie123’)
    …
Each SQL statement should be a single line. 

### Part B

Enhance the DSL with the functionality to cancel all trade requests for a given stock made by the account holder. First, change the DSL grammar and the input JSON format; then modify your test data generator to reflect the changes. Determine which SQL statement should be generated to realize the cancel trade requests and add this functionality to your implementation.

### Multiple JSON files are included for thorough testing. Sample output .dsl and .sql files are also included.
