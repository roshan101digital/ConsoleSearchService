# Accouting API

## Introduction

This API currently support following following actions

* Add Customer - For handling customer registration requests
* Get Customer detail - For handling customer detail query requests
* Delete Customer - For handling un-registration requests


The endpoints can be configured based on the customer’s use-case.


## Setting up Accounting API

1. Configure Accounting API to map client entity deployment with the accounting provider implementation adapter
   Location: Adapter.Accounting/io.101digital.adapter.accounting.registry.resources/account-providers

2. Build the project (Adapter.Accounting)
 
3. Upload the CAPP to ESB
Location: Adapter.Accounting/io.101digital.adapter.accounting.capp/target
io.101digital.adapter.accounting.capp_1.0.0.car


## Accounting API Functionality

1. AccountService-Get Customer API
     

**Request **
```
GET http://localhost:8282/account-service/customers/{customer-id}
Content-Type: application/json
entity-Id: YRC

```
*Required Fields*

* entity-Id : Unique id for client implementation. Service will switch to different providers depending on the entitity id value

**Response **
```
{  
   "data":{  
      "account":{
         "id":82280,  
         "reference":"B1D00151",
         "name":"Customer_Test_YRC_04",
         "shortName":"YRCtest4",
         "balance":"0",
         "onHold":false,
         "accountStatusType":"AccountStatusActive",
         "currencyId":"2103",
         "exchangeRateType":"ExchangeRateSingle",
         "contactInfo":{  
            "telephoneCountryCode":"44",
            "telephoneAreaCode":"01742",
            "telephoneSubscriberNumber":"876234",
            "faxCountryCode":"44",
            "faxCreaCode":"01742",
            "faxSubscriberNumber":"876236",
            "website":"www.sage.co.uk",
            "address":{               
               "addressLine1":"No 30",
               "addressLine2":"Bakers Street",
               "addressLine3":"Gatwick",
               "addressLine4":"western",
               "city":"London",
               "country":"UK",
               "postCode":"11010"               
            }
         }
      },
      "additionalInfo":{  
         "creditLimit":"0",
         "countryCodeId":"13",
         "defaultTaxCodeId":"1729",
         "vatNumber":"123456789"
      },
      "contacts":[  
         {  
            "firstName":"Henry",
            "middleName":"James",
            "lastName":"Doe",
            "isDefault":true,
            "defaultTelephone":"42424232",
            "defaultEmail":"dayal@101digital.io",
            "emails":[  
               {  
                  "email":"dayal@101digital.io",
                  "isDefault":true
               }
            ]
         }
      ]
   }
}


```

-----

2. AccountService-Create Customer API

**Message Flow**

**Request from client**
```
POST http://localhost:8282/account-service/customers
Content-Type: application/json
entity-Id: YRC

{  
   "data":{  
      "account":{         
         "reference":"B1D00151",
         "name":"Customer_Test_YRC_04",
         "shortName":"YRCtest4",
         "balance":"0",
         "onHold":false,
         "accountStatusType":"AccountStatusActive",
         "currencyId":"2103",
         "exchangeRateType":"ExchangeRateSingle",
         "contactInfo":{  
            "telephoneCountryCode":"44",
            "telephoneAreaCode":"01742",
            "telephoneSubscriberNumber":"876234",
            "faxCountryCode":"44",
            "faxAreaCode":"01742",
            "faxSubscriberNumber":"876236",
            "website":"www.sage.co.uk",
            "address":{               
               "addressLine1":"No 30",
               "addressLine2":"Bakers Street",
               "addressLine3":"Gatwick",
               "addressLine4":"western",
               "city":"London",
               "country":"UK",
               "postCode":"11010"               
            }
         }
      },
      "additionalInfo":{  
         "creditLimit":"0",
         "countryCode_id":"13",
         "defaultTaxCodeId":"1729",
         "vatNumber":"123456789"
      },
      "contacts":[  
         {  
            "firstName":"Henry",
            "middleName":"James",
            "lastName":"Doe",
            "isDefault":true,              
            "emails":[  
               {  
                  "email":"dayal@101digital.io",
                  "isDefault":true
               }
            ]
         }
      ]
   }
}

```
*Required Fields*

* entity-Id : Unique id for client implementation. Service will switch to different providers depending on the entitity id value
* <TO-DO>


**Response **
```
{  
   "data":{  
      "account":{
         "id":82280,  
         "reference":"B1D00151",
         "name":"Customer_Test_YRC_04",
         "shortName":"YRCtest4",
         "balance":"0",
         "onHold":false,
         "accountStatusType":"AccountStatusActive",
         "currencyId":"2103",
         "exchangeRateType":"ExchangeRateSingle",
         "contactInfo":{  
            "telephoneCountryCode":"44",
            "telephoneAreaCode":"01742",
            "telephoneSubscriberNumber":"876234",
            "faxCountryCode":"44",
            "faxCreaCode":"01742",
            "faxSubscriberNumber":"876236",
            "website":"www.sage.co.uk",
            "address":{               
               "addressLine1":"No 30",
               "addressLine2":"Bakers Street",
               "addressLine3":"Gatwick",
               "addressLine4":"western",
               "city":"London",
               "country":"UK",
               "postCode":"11010"               
            }
         }
      },
      "additionalInfo":{  
         "creditLimit":"0",
         "countryCodeId":"13",
         "defaultTaxCodeId":"1729",
         "vatNumber":"123456789"
      },
      "contacts":[  
         {  
            "firstName":"Henry",
            "middleName":"James",
            "lastName":"Doe",
            "isDefault":true,
            "defaultTelephone":"42424232",
            "defaultEmail":"dayal@101digital.io",
            "emails":[  
               {  
                  "email":"dayal@101digital.io",
                  "isDefault":true
               }
            ]
         }
      ]
   }
}
```
-----
 

3. AccountService-Delete Customer API


**Request from client**
```
DELETE http://localhost:8282/account-service/customers/{customer-id}
Content-Type: application/json
entity-Id: YRC

```
*Required Fields*

* entity-Id : Unique id for client implementation. Service will switch to different providers depending on the entitity id value
* customer-id : unique customer id returend when registering the customer [add customer]

**Response **
```
HTTP 204 -No Content 
```

4. Exception Respone

**Response **
```
{
    "errors": [
        {
            "status": "400",
            "code": "SVC0001",
            "detail": "Invalid currency GB. Organisation is enabled for the following currencies: (GBP)"
        }
    ]
}
```
