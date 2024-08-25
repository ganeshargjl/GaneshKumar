# Points Calculator based on customer transaction
#The rest API to get customer rewards based on customer Id

#A retailer offers a rewards program to its customers, awarding points based on each recorded purchase.   A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent over $50 in each transaction (e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points).   Given a record of every transaction during a three month period, calculate the reward points earned for each customer per month and total. 

- The package name is structured as com.retailer.rewards
- Exception is thrown if customer does not exists.
- SQL server database to store the information.
- Please check doc file provided in the project
- Install Oracle Sql Developer locally and run it and change the db settings in application.properties file.
- Do run the script.sql SQL Server to prepare the test data.
- Please refer RewardsApplication.pdf - https://github.com/ganeshargjl/GaneshKumar/blob/main/Test%20Case%20Documents/RewardsApplication.pdf
- Request Body -> Sending the parameter and it should be the customerId of Long Datatype
- Response Body ->
- {
    "customerId": 1001,
    "monthlyRewards": [
        {
            "monthName": "July 2024",
            "rewardPoints": 125
        },
        {
            "monthName": "June 2024",
            "rewardPoints": 210
        },
        {
            "monthName": "May 2024",
            "rewardPoints": 270
        }
    ],
    "totalRewards": 605

}
Flow Diagram ![image](https://github.com/user-attachments/assets/9b3eff97-5289-4c7e-af9e-c4bd51b48e56)


```
 http://localhost:8080/customers/{customerId}/rewards
```

By `https://www.linkedin.com/in/ganesh-kumar-b45334138/'
