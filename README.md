# WeatherWebApp

Weather Web App is an weather application which gives users live weather information based on the city user entered. Currently this application is buit in Java SPring Boot with Thymeleaf template. Database is develop in RDS-Postgress server and application is currently deploy on EC2 instance using jenkins.


Steps to Use Application:

This application is used by only registered users so, users must need to create their account to see the weather and add to their future list.


1> In registration page User must need to provide unque name and pasword must need to be 8 characters long. Password is currently encrypted and store in DB.
![image](https://user-images.githubusercontent.com/85809730/122705852-82cc1300-d224-11eb-9dd1-e86d23c83325.png)

2> After registration user will auto login and can use application. If User Log out and want to use app then he/she need to login to the system with their credentials.
![Screenshot (27)](https://user-images.githubusercontent.com/85809730/122706285-95931780-d225-11eb-860f-5a49dfcf99d1.png)

3> Once user authenticated he/she will redirect to application home page.
![Screenshot (28)](https://user-images.githubusercontent.com/85809730/122706324-b3607c80-d225-11eb-9127-859ba4c8a134.png)

4> In home page user can search their city name and see weather:
![Screenshot (29)](https://user-images.githubusercontent.com/85809730/122706399-e0ad2a80-d225-11eb-809b-6146ee643407.png)

5> If user want to save their location for future they can click on save button and it will store and will display to their saved location list:
![Screenshot (30)](https://user-images.githubusercontent.com/85809730/122706471-020e1680-d226-11eb-9101-0a962a712472.png)

