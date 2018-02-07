import pandas as pd

training_set = pd.read_csv("training_set.csv")

training_set[training_set['fraud']]
fraud_same_country = training_set[training_set['fraud']].query('issuer_country == shopper_country')
fraud_diff_country = training_set[training_set['fraud']].query('issuer_country != shopper_country')


(len(fraud_same_country) + len(fraud_diff_country)) / len(training_set)
len(fraud_diff_country) / (len(fraud_same_country) + len(fraud_diff_country))

legit_diff_country = training_set[~training_set['fraud']].query('issuer_country != shopper_country')

# show ratio of above vs all 'fraud' == 'false'. potential false positive percentage
len(legit_diff_country) / len(training_set[~training_set['fraud']])

legit_diff_country
legit_diff_country['amount'].describe()

fraud_same_country
fraud_same_country['amount'].describe()
fraud_diff_country
fraud_diff_country['amount'].describe()


# do some groupby shopping email
training_set[training_set['fraud']].groupby('shopper_email').count()

# frequency of transaction
training_set[~training_set['fraud']].groupby('shopper_email').count()
legit = training_set[~training_set['fraud']]
legit.loc[legit['shopper_email'] == 'rhea-mayers@rediffmail.com']

# no similarities observed other than fraudster used the same email with different cards issued by different country
legit.loc[legit['shopper_country'] == 'NO']
fraud_diff_country.loc[fraud_diff_country['shopper_country'] == 'NO']

fraud_same_country.sort_values(['shopper_email', 'txid'])
fraud_diff_country.sort_values(['shopper_email', 'card_number_hash'])

training_set[~training_set['fraud']].groupby('card_number_hash').count()
legit = training_set[~training_set['fraud']]
legit.loc[legit['card_number_hash'] == 'card840790']
legit.sort_values(['card_number_hash', 'shopper_email'])

training_set[~training_set['fraud']].groupby(['shopper_email','card_number_hash']).count()
legit.loc[legit['shopper_email'] == 'wyatt.cohen@comcast.net']
