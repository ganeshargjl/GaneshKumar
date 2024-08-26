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

Flow Diagram ![image](https://github.com/user-attachments/assets/9b3eff97-5289-4c7e-af9e-c4bd51b48e56)



### API documentation :

### 1. Get Rewards

**URI:** `"api/reward/customer/[customerId]"`  
**Description:**  
Calculates reward points for a customer for each transaction over a given period and prepares a report.

#### Response Body:

```json
{
    "customerId": 1001,
    "monthlyRewards": [
        {
            "monthName": "May 2024",
            "rewardPoints": 270
        },
        {
            "monthName": "June 2024",
            "rewardPoints": 210
        },
        {
            "monthName": "July 2024",
            "rewardPoints": 125
        }
    ],
    "totalRewards": 605
}
```

---

### 2. Get Rewards for All Customers

**URI:** `"api/reward/allcustomers/"`  
**Description:**  
Calculates reward points for all customers for each transaction.

#### Response Body:

```json
[
    {
        "customerId": 1001,
        "monthlyRewards": [
            {
                "monthName": "May 2024",
                "rewardPoints": 270
            },
            {
                "monthName": "June 2024",
                "rewardPoints": 210
            },
            {
                "monthName": "July 2024",
                "rewardPoints": 125
            }
        ],
        "totalRewards": 605
    },
    {
        "customerId": 1002,
        "monthlyRewards": [
            {
                "monthName": "August 2024",
                "rewardPoints": 106
            },
            {
                "monthName": "June 2024",
                "rewardPoints": 148
            },
            {
                "monthName": "July 2024",
                "rewardPoints": 324
            }
        ],
        "totalRewards": 578
    },
    {
        "customerId": 1003,
        "monthlyRewards": [
            {
                "monthName": "May 2024",
                "rewardPoints": 1924
            },
            {
                "monthName": "June 2024",
                "rewardPoints": 306
            },
            {
                "monthName": "July 2024",
                "rewardPoints": 88
            }
        ],
        "totalRewards": 2318
    }
]
```

---

### 3. List of Specific Customers

**URI:** `"api/rewards/bulkTransaction"`  
**Method:** `POST`  
**Description:**  
Processes multiple transactions for specified customers.

#### Request Body:

```json
[1001, 1002]
```

#### Response:

```json
[
    {
        "customerId": 1001,
        "monthlyRewards": [
            {
                "monthName": "May 2024",
                "rewardPoints": 270
            },
            {
                "monthName": "June 2024",
                "rewardPoints": 210
            },
            {
                "monthName": "July 2024",
                "rewardPoints": 125
            }
        ],
        "totalRewards": 605
    },
    {
        "customerId": 1002,
        "monthlyRewards": [
            {
                "monthName": "August 2024",
                "rewardPoints": 106
            },
            {
                "monthName": "June 2024",
                "rewardPoints": 148
            },
            {
                "monthName": "July 2024",
                "rewardPoints": 324
            }
        ],
        "totalRewards": 578
    }
]
```
```

By `https://www.linkedin.com/in/ganesh-kumar-b45334138/'
