# DonutChat

### it's a Group-chat application that lets the user sign up, Login, create and join groups of different Topics, and communicate with users that join the same groups
### the app conssists of 4 activities, 4 viewModels and a repository
### In order for the app to work you need to have a firebase account, link it to the project and setup both firebase authentication and firebase RealTime Database


## Features :
### MVVM, LiveData, dataBinding, Firebase Authentication, Firebase RealTime Database, RecyclerView, CardView ...
#
### import the following json file into your Firebase project RealTime database :

```json

{
  "Groups": {
    "Hiking": {
      "joiners": [
        null,
        
        {
          "value": "hanane"
        },
        
        {
          "value": "Mike"
        }
      ],
      "messages": [
        null,
        "hanane : hey what up",
        "Mike : hey guys"

      ],
      "number of joiners": 2,
      "number of messages": 2
    },
    "Physics": {
      "joiners": [
        null,
        {
          "value": "hanane"
        }
      ],
      "messages": "",
      "number of joiners": 1,
      "number of messages": 0
    },
    "cooking": {
      "joiners": [
        null,
        {
          "value": "hanane"
        },
        {
          "value": "Mike"
        }
      ],
      "messages": "",
      "number of joiners": 2,
      "number of messages": 0
    }
  },
  "MyApp": {
    "groups": [
      null,
      "Hiking",
      "Physics",
      "cooking"
      
    ],
    "users": [
      null,
      "hanane",
      "Mike"
    ]
  },
  "Users": {
    "hanane": "hanane@gmail.com",
    "Mike": "mike@gmail.com"
  }
}

```




<image src="https://github.com/25THELL52/DonutChat/assets/79938851/36470af1-65af-4222-b921-f68aed497366" width="30%" height="30%">     <image src="https://github.com/25THELL52/DonutChat/assets/79938851/4f4c02f1-9704-4952-8547-6fdeeec05000" width="30%" height="30%"> 
<image src="https://github.com/25THELL52/DonutChat/assets/79938851/13d466b7-b964-44f8-b536-b697e7c35114" width="30%" height="30%">   <image src="https://github.com/25THELL52/DonutChat/assets/79938851/da333202-5702-4218-90d3-773ee5ce3182" width="30%" height="30%">



