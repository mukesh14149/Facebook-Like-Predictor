<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta name="author" content="Mukesh Gupta">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Predictor.com</title>
	<style>
		body{
			background-color:black;
		}
	</style>
</head>
	<body >
			
			<%@ page import="java.net.URLEncoder" %>    
			<%@ page import="com.iiitd.project.client.Fetch_data" %>
		
			<div
					style="margin-top: -20px; margin-left:200px;margin-top:100px; background-image: url(./img/fbloginbckgrnd.jpg); height: 460px; width: 1010px;">
				<h1 style="position:relative;left:240px;">Predict Likes on Facebook Posts</h1>
				
				 <!--Send Oauth request to facebook for authentication purpose-->
				
				<a href="http://www.facebook.com/dialog/oauth?+client_id=<%=Fetch_data.APP_ID%>&scope=email,user_birthday,user_about_me,user_likes,user_photos,user_tagged_places
				,user_friends,user_posts,publish_actions,read_page_mailboxes,user_actions.music,user_relationships,user_tagged_places
				,user_work_history,user_actions.books,user_actions.news,user_education_history,user_games_activity,user_location,user_religion_politics,user_videos,user_actions.fitness,user_actions.video,user_events,user_hometown,user_managed_groups,user_relationship_details,user_status,user_website,manage_pages,read_custom_friendlists&redirect_uri=<%=URLEncoder.encode("http://localhost:8080/post_analyzer/Fetch_data")%>"><img
						style="position:relative; left:300px;top:100px; width:400px;" src="./img/facebookloginbutton.png" /></a>
			
			</div>			
	
	</body>
</html>