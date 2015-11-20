<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Predictor.com</title>
</head>
<body>
		<%@ page import="java.net.URLEncoder" %>
		<%@ page import="com.iiitd.project.client.Fetch_data" %>
	
<div
		style="margin-top: -20px; margin-left:0px; background-image: url(./img/fbloginbckgrnd.jpg); height: 960px; width: 2010px;">
	<h1 style="position:relative;left:500px;">Predict your FB Posts's Likes</h1>
	<a href="http://www.facebook.com/dialog/oauth?+client_id=<%=Fetch_data.APP_ID%>&scope=email,user_birthday,user_about_me,user_likes,user_photos,user_tagged_places
	,user_friends,user_posts,publish_actions,read_page_mailboxes,user_actions.music,user_relationships,user_tagged_places
	,user_work_history,user_actions.books,user_actions.news,user_education_history,user_games_activity,user_location,user_religion_politics,user_videos,user_actions.fitness,user_actions.video,user_events,user_hometown,user_managed_groups,user_relationship_details,user_status,user_website,manage_pages,read_custom_friendlists&redirect_uri=<%=URLEncoder.encode("http://localhost:8080/post_analyzer/Fetch_data")%>"><img
			style="position:relative; left:590px;top:100px; width:400px;" src="./img/facebookloginbutton.png" /></a>
	</div>			

</body>
</html>