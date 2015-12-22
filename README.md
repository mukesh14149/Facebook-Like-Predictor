# Predict Likes On Facebook Post

Java Web app that has a capability to predict likes on a fb's post.
___________________________________________________________________


Working Environment I have used:
--------------------------------
1. Java-1.7.0 openjdk amd64
2. Eclipse ide JavaEE indigo
3. Jar files required restfb-1.16.0.jar or above and Servlet.jar
4. Apache Tomcat v7.0


Features of this:
-----------------
1. Predict likes on 20% of post from last year.
3. Show Post name and picture on which had predict likes.
2. Shows friend's name and Picture who would going to like your post.
3. Shows friend who must like your post with "C" Sign on front of thier name
4. Show App Accuracy by analyzing previous user who used this app.


Prediction Approach:
--------------------
1. To predict the likes on a post , first of all we categorize the post in four different type like a post contain a profile picture , status update, shared content or just a photo upload.

2. Suppose a post is status update or photo upload:- After analyzing the post we came to conclusion that many people don't want to read a status, so no. of likes as compared to likes on pictures is less in many cases so for predicting likes on a status we analyze every post of previous 3-4 month(get approx 50 posts) and then make an average on these post and get no. of likes. To find the name of friends who is likely going to like this post, we can calculate probability of liking a post by a unique friend and get top most friends(=no. Of likes) and we can find friends who mostly like his/her posts.


3. Suppose a post is profile picture or photo :- After analyzing the posts we came to conclusion that many people likes photo type content, profile pic is like by people including his current friends,best friend and a old friend(they are not like there many post). So we check no. of likes on previous profile pics only+ those friend who likes his most post(on last few weeks) and made an average to get likes on his recent picture. But for accuracy of likes we even divided posts on timing base like at what time a post is posted and compare his/her previous post timing and get approx detail for getting likes in particular time(like a post gets more likes on sunday and saturday, and get less likes on working).


3. Suppose a post is shared content:- After analyzing the posts we came to conclusion that many
people doesn't like shared posts(because they may see that post already at other page or somewhere
so they don't bother about who posted that) so In this we analyze the shared post of previous 3-4
month(get approx 50 shared posts)and then make an average on these post and get no. of likes. To
find the name of friends who is likely going to like this post, we can calculate probability of liking a post by a unique friend and get top most friends(=no. Of likes) and we can find friends who mostly like his/her posts.


4. To find friends who definetely like User's post:- In this We can Analyze previous few posts and get a no. of user who like a post on regular interval and get a no. of user who comment on thier previous post.



How to run:
----------- 
1. Import This project in your eclipse ide.
2. Go to class Fetch_data under package(com.iiitd.project.client) of src folder.
3. Change app Id and app Secret with your app Id and Secret and your app website site URL must be same as redirect URL of fetch_data class i.e. http://localhost:8080/post_analyzer/Fetch_data
4. Start Tomcat Server.
5. Go to Browser and type http://localhost:8080/post_analyzer/index.jsp in URL area.
6. Click on Login With facebook, Authorise yourself and See result on Browser.


How To get Facebook app id and app Secret:
------------------------------------------
1. Ask Google
2. search on http://developers.facebook.com/

Lets have a fun at https://developers.facebook.com/tools/explorer 



Help:
-----
1. Create an issue or direclty contact me by going www.mukeshgupta.me.


