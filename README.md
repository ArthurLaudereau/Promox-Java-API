# Project Goal

Managing and creating Containers using Proxmox API

## Getting Started

First and before running anything, you'll have to fill your INSA credentials in the ```Constants.java``` to be able to connect to the INSA's Proxomox servers.
We recommend you to change the CT_BASE_ID while you are in the ```Constants.java``` file (ex : 1320), to prevent some crashes if an ID was already existing during the creation.
We had a range of IDs from 1300 to 1399 reserved for us, on the servers PX5 and PX6 for our team.

### Prerequisites
We used Eclipse Neon 3 JEE Edition.
And the library JRE 1.8 but we think the 1.7 should be sufficient, it should be included in the project

### Installing

Just clone this repository

## Running the tests

We didn't implement the test class, instead we chose to follow the execution of the code via the console, we found it more useful and meaningful during the coding sessions.
So you just have to run the project and look at the console.
You will have both the generator and the monitor running, so you will have both console logs.

## Things you should know before diving in the code

To simulate the overload of the servers we chose to divide the RAM of the servers by 100 instead of creating a LOT of containers, we could see the migration was working, but this method also stopped the oldest container at each iteration. So we tested 2 functions in one run. (These two lines of code are commented in the ```Analyser.java``` file)

For the generation part, we chose to keep the exponential law with a mean of 30 seconds because we found it was convenient.
The only thing we didn't implement was the ID management : if a container already used the one we wanted to create, it would crash. 
But assuming we run this application at the start of our servers to manage them from the start to the eternity, it would work.

## Authors

* **Arthur Laudereau** & **Jean Sannac**
