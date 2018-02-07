# Credit Card Fraud Detection Hackathon
adyen.riddles.io/

Analysis on training set data was performed using Python [Pandas](https://pandas.pydata.org/) library.
- ## [Check 1](bot/checkpoint/Check1.java): Issuer Country vs Shopper Country

  Of the 100 transactions in the training data set, 20% are classified as fraudulent. Of these 20%, 60% of them had shopper_country != issuer_country. For legitimate transactions, 6.25% of them had the above condition.

  Check 1 filtered the above condition and marked fraud on transactions where the amount > 2000. This amount was chosen so that >75% of different country fraud transaction can be blocked while keeping the false positive to <3% (50% * 6.25%).
  ```  
  legit_diff_country['amount'].describe()
  ```
  | | |
  |---| --- 
  |count|5.000000
  |mean|1413.200000
  |std|1143.962281
  |min|255.000000 
  |25%|376.000000
  |50%|1442.000000
  |75%|2012.000000
  |max|2981.000000
  |Name: amount|dtype: float64
  ```  
  fraud_diff_country['amount'].describe()
  ```
  |||
  |---|--- 
  |count|12.000000
  |mean|8273.583333
  |std|9606.352533
  |min|635.000000
  |25%|1377.000000
  |50%|3801.500000
  |75%|13937.250000
  |max|29290.000000
  |Name: amount|dtype: float64
  
- ## [Check 2](bot/checkpoint/Check2.java): Repeated Transactions
  Group by shopper_email showed a fraud behavior of performing multiples transactions by the same card within minutes of each other. This pattern was not observed in legitimate transactions. Check 2 will record the time of transaction by card_number_hash and reject subsequent transactions that are performed within minutes of each other.
  ```
  fraud_same_country.sort_values(['shopper_email', 'txid'])
  ```
  ||||||||||||
  |---|---|---|---|---|---|---|---|---|---|---|
  |txid|issuer_country|txvariantcode|bin|amount|currency|shopper_country|creation_date|shopper_email|card_number_hash|fraud
  |13|US|MCCREDIT|723703|3050|USD|US|2014-02-26 14:24:02|lizeth-reichert@comcast.net|card978104|True
  |34|US|MCCREDIT|723703|1310|USD|US|2014-02-26 14:23:37|lizeth-reichert@comcast.net|card978104|True
  |47|US|MCCREDIT|723703|12942|USD|US|2014-02-26 14:24:01|lizeth-reichert@comcast.net|card978104|True
  |59|US|MCCREDIT|723703|1940|USD|US|2014-02-26 14:22:00|lizeth-reichert@comcast.net|card978104|True
  |62|US|MCCREDIT|723703|999|USD|US|2014-02-26 14:24:25|lizeth-reichert@comcast.net|card978104|True

- ## [Check 3](bot/checkpoint/Check3.java): User Check
  Another pattern noticed was that in legitimate transactions the shopper_email are closely related. Where for fraudulent transactions the same card can be associated with widely different addresses. Check 3 will remove domain names, underscore, and periods from shopper_email and see if the address is the same as previous transactions.
  ```
  legit.loc[legit['card_number_hash'] == 'card840790']
  ```
  ||||||||||||
  |---|---|---|---|---|---|---|---|---|---|---|
  |txid|issuer_country|txvariantcode|bin|amount|currency|shopper_country|creation_date|shopper_email|card_number_hash|fraud
  |2|AE|MCDEBIT|458187|3887|AED|AE|2014-02-14 21:13:39|carola.giron@outlook.com|card840790|False
  |8|AE|MCDEBIT|458187|4126|AED|AE|2014-02-11 11:21:55|carola.giron@bellsouth.net|card840790|False
  |26|AE|MCDEBIT|458187|146|AED|AE|2014-03-11 06:47:37|carola.giron@outlook.com|card840790|False
  |31|AE|MCDEBIT|458187|2693|AED|AE|2014-02-28 23:11:06|carola.giron@bellsouth.net|card840790|False
  |64|AE|MCDEBIT|458187|4441|AED|AE|2014-02-18 21:41:40|carola.giron@outlook.com|card840790|False
  |87|AE|MCDEBIT|458187|4715|AED|AE|2014-03-09 01:40:27|carola.giron@bellsouth.net|card840790|False

- ## [Check 4](bot/checkpoint/Check4.java): Multiple Issuer Country
  In fradulant transactions, the same shopper_email was seen using cards by different issuer_country, a pattern not found in legitimate transactions. However this check didn't have any effect on improving the result for assessment set.
  
  ||||||||||||
  |---|---|---|---|---|---|---|---|---|---|---|
	|53|AE|VISACLASSIC|789249|906|AED|NO|2014-02-22 18:19:50|loraine-parrott@aol.com|card529253|True
  |75|AE|VISACLASSIC|789249|1167|AED|NO|2014-02-15 23:42:49|loraine-parrott@aol.com|card589253|True
  |56|FR|MCCREDIT|347045|1447|EUR|NO|2014-02-28 09:48:54|loraine-parrott@aol.com|card765633|True
  |44|ES|MCDEBIT|757660|635|EUR|NO|2014-02-17 21:55:47|loraine-parrott@aol.com|card801330|True
  
## Overall Result
Best assessed: 35 (53%)
Best tested: 35 (74%)

