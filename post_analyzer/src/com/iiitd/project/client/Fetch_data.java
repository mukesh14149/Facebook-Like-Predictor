/*
 * @author Mukesh Gupta
 */
package com.iiitd.project.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Photo;
import com.restfb.types.Post;
import com.restfb.types.User;

/**
 * Servlet implementation class Fetch_data
 */
@WebServlet("/Fetch_data")
public class Fetch_data extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
//Change App ID and App Secret with your App Configuration.
	public static String APP_ID = "910867505672654";
	public static String APP_SECRET = "8bbeef0ed5bd3bd7ac978102c08a5132";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Fetch_data() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		//code is generated after authorization of app which can be used for extracting user access token.
		String code=request.getParameter("code");	
		
		//Url i.e used for redirecting the page after authorization, your app should be have the same website URL as written below.
		String URLEncodedRedirectURI = URLEncoder.encode("http://localhost:8080/post_analyzer/Fetch_data");   //redirect URL
		
		//send request to get user access token by using above code.
		String authURL = "https://graph.facebook.com/oauth/access_token?" +
                "client_id=" + Fetch_data.APP_ID + "&" +
                "redirect_uri=" + URLEncodedRedirectURI + "&" +
                "client_secret=" + Fetch_data.APP_SECRET + "&" +
                "code=" + code;
		
		
		//convert above string into URL
		URL url = new URL(authURL);
		
		//User access_token would come in this variable.
		String access_token=null;
		
		//send URL to Extract_access method that get access token in URL form
		String result = Extract_access_token(url);
		
		//process to extract access_token from result 
		String[] pairs = result.split("&");
		for (String pair : pairs) {
			String[] kv = pair.split("=");
			if (kv[0].equals("access_token")) {
				access_token = kv[1];
			}
		}
		
		
		//Send access_token to restfb's class facebookClient to fetch data  
		final FacebookClient facebookClient = new DefaultFacebookClient(access_token);
		
		
		//fetch user  i.e a client  data who logged in with given fields. 
		User loginUser = facebookClient.fetchObject("me", User.class,Parameter.with("fields","first_name,last_name,name,email,website"));
		
		
		//Connect to access another edge i.e posts of User.
		Connection<Post> feed=facebookClient.fetchConnection("me/posts", Post.class, Parameter.with("fields","name,story,id,full_picture,picture,likes.limit(1000){name,pic_large},type,created_time,link"),Parameter.with("limit", 300));
		
		//Make a list of feed data.
		List<Post> posts = feed.getData();
		
		
		//Hashmap of Friends with their likes on different post type.
		//profile pics and videos
		HashMap<String,Integer> timeline_friends = new HashMap<String,Integer>(); 
		
		//Shared posts
		HashMap<String,Integer> shared_friends = new HashMap<String,Integer>();
		
		//status of User
		HashMap<String,Integer> status_friends = new HashMap<String,Integer>();
		
		//Hashmap of Friends with their facebook id.
		HashMap<String,String> name_id = new HashMap<String,String>();
		 
		
		
		//Connect to access another edge i.e Photos of User that is uploaded by User itself.
		Connection<Photo>photos =facebookClient.fetchConnection("me/photos/uploaded", Photo.class, Parameter.with("fields","name,picture,link,likes.limit(1000){name,pic_large},created_time"),Parameter.with("limit", 300),Parameter.with("summary", true));
		
		//Take average of like on particular post
		int shared_avg=0,sa=1;
		int timeline_avg=0,ta=1;
		int status_avg=0,sta=1;
		
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String date = format.format(new Date());
		
		String[] time0=date.toString().split("/");
		String[] time1=null;
		
		//post_limit is Generalised post upto certain Date.
		int ti=0,post_limit=0;
		
		//this will give no. of post upto certain Data, in this case we took post upto last Year.
		while(true){
				String date2=format.format(posts.get(ti).getCreatedTime());
				time1=date2.split("/");
				int test=Integer.parseInt(time0[0].toString())-Integer.parseInt(time1[0].toString());
				if(test<=1){
					if(test==1){
						if(Integer.parseInt(time0[1].toString())-Integer.parseInt(time1[1].toString())>1)
						break;
					}
					else{
						post_limit++;
					}
				}
				ti++;
	   }
		
		int flag=0;       //flag for photos
		int flag1=0;      //flag for status
		int flag2=0; 	  //flag for shared
		int counter=0;    //counter for divided total status of size 100
		int counter1=0;   //counter for divided photos of size dated
		int counter2=0;   //counter for divided shared photos of size dated
		
		

		
		//Predict likes on 20% of post_limit post
		int lim=(post_limit*20)/100;
		
	
		
		for(int q=0;q<lim;q++){	
				
				//Working for post type status
				if(posts.get(q).getType().equals("status")){
				for(int w=0;w<posts.size();w++){
					
					//Take post upto last year from given data and generalised likes on them
					String date2=format.format(photos.getData().get(w).getCreatedTime());
					time1=date2.split("/");
					int test=Integer.parseInt(time0[0].toString())-Integer.parseInt(time1[0].toString());
					if(test<=1){
						if(test==1){
							if(Integer.parseInt(time0[1].toString())-Integer.parseInt(time1[1].toString())>1)
								break;
						}
					
					if(flag1==1)
						break;
					
					//Check Generalised post should not be same as post on which we predict like.
					if((posts.get(w).getId().equals(posts.get(q).getId())==false)&&posts.get(w).getType().equals("status")){
						counter++;
						List<NamedFacebookType> status_likes=posts.get(w).getLikes().getData();
						
						//counter to divide total likes to no. of post
						sta++;
						
						//add likes of every posts.
						status_avg+=status_likes.size();
						
						//Make hashmap of Unique friend to their total likes on certain kind of post.
						for(int w1=0;w1<status_likes.size();w1++){
							int c1=1;
							if(status_friends.get(status_likes.get(w1).getName())!=null){
								c1=Integer.parseInt(timeline_friends.get(status_likes.get(w1).getName()).toString());
								status_friends.put(status_likes.get(w1).getName(), ++c1);
							}
							else{
								status_friends.put(status_likes.get(w1).getName(), c1);
								name_id.put(status_likes.get(w1).getName(), status_likes.get(w1).getId());
							
							}
						}
					}
					}	
				}
			flag1=1;
			}
				
			//Working for post type profile pics and self uploaded photos and videos	
			if(posts.get(q).getType().equals("photo")||posts.get(q).getType().equals("link")||posts.get(q).getType().equals("video")){
				
				try{
				if(((posts.get(q).getName().equals("Mobile Uploads"))||posts.get(q).getName().equals("Mukesh's cover photo"))||(posts.get(q).getName().equals("Timeline Photos"))||(posts.get(q).getName().equals("Profile Pictures"))||(posts.get(q).getName().equals(loginUser.getName().toString()))){	
					
					
					for(int q1=0;q1<photos.getData().size();q1++){
						
						//Take post upto last year from given data and generalised likes on them.
						String date2=format.format(photos.getData().get(q1).getCreatedTime());
						time1=date2.split("/");
						int test=Integer.parseInt(time0[0].toString())-Integer.parseInt(time1[0].toString());
						if(test<=1){
							counter1++;
							if(test==1){
								if(Integer.parseInt(time0[1].toString())-Integer.parseInt(time1[1].toString())>1)
									break;
							}
							
						
							if(flag==1)
								break;
							
							//Check Generalised post should not be same as post on which we predict like.
							if(posts.get(q).getLink().equals(photos.getData().get(q1).getLink())==false){
									List<NamedFacebookType> timiline_linker=photos.getData().get(q1).getLikes();
						
									//counter to divide total likes to no. of post
									ta++;
									
									//add likes of every posts.
									timeline_avg+=timiline_linker.size(); 
									
									//Make hashmap of Unique friend to their total likes on certain kind of post.
									for(int j1=0;j1<timiline_linker.size();j1++){
										int c1=1;
										if(timeline_friends.get(timiline_linker.get(j1).getName())!=null){
											c1=Integer.parseInt(timeline_friends.get(timiline_linker.get(j1).getName()).toString());
											timeline_friends.put(timiline_linker.get(j1).getName(), ++c1);
										}
										else
											timeline_friends.put(timiline_linker.get(j1).getName(), c1);
											name_id.put(timiline_linker.get(j1).getName(), timiline_linker.get(j1).getId());
										}
								}	
						
						
						}
						else
							break;
					
				}
					flag=1;
				
				}
				
				else {
					//Working of post which was not included above type like shared post.	
					for(int q3=0;q3<posts.size();q3++){
					
						//Take post upto last year from given data and generalised likes on them.
						String date2=format.format(posts.get(q3).getCreatedTime());
						time1=date2.split("/");
						
						int test=Integer.parseInt(time0[0].toString())-Integer.parseInt(time1[0].toString());
						if(test<=1){
							counter2++;
							if(test==1){
								if(Integer.parseInt(time0[1].toString())-Integer.parseInt(time1[1].toString())>1)
									break;
							}
							
						
							if(flag2==1)
								break;
							
							//Check Generalised post should not be same as post on which we predict like.
							if(posts.get(q).getLink().equals(posts.get(q3).getLink())==false){
								//System.out.println(posts.get(q3));	
								try{
									List<NamedFacebookType> shared_linker=posts.get(q3).getLikes().getData();
									
									//counter to divide total likes to no. of post
									sa++;

									//add likes of every posts.
									shared_avg+=shared_linker.size(); 
									
									//Make hashmap of Unique friend to their total likes on certain kind of post.
									for(int j2=0;j2< shared_linker.size();j2++){
										int c1=1;
										if(shared_friends.get( shared_linker.get(j2).getName())!=null){
											c1=Integer.parseInt(shared_friends.get( shared_linker.get(j2).getName()).toString());
											shared_friends.put( shared_linker.get(j2).getName(), ++c1);
											
										}
										else
											shared_friends.put( shared_linker.get(j2).getName(), c1);
											name_id.put(shared_linker.get(j2).getName(), shared_linker.get(j2).getId());
										}
									}catch(Exception e){
									
								}
							}
						}
						else
							break;
				}
				flag2=1;
				}}catch( Exception e){}
				
			}
		}
		
		//Logic to find Special friend who definitely like post by checking their regular likes on previous post.
		HashMap<String,Integer> special_friends = new HashMap<String,Integer>();
		for(int i=0;i<5;i++){
			try{
				List<NamedFacebookType> special=posts.get(i).getLikes().getData();
				for(int j2=0;j2< special.size();j2++){
					int c1=1;
					if(special_friends.get( special.get(j2).getName())!=null){
						c1=Integer.parseInt(special_friends.get( special.get(j2).getName()).toString());
						special_friends.put( special.get(j2).getName(), ++c1);
					}
					else{
						special_friends.put( special.get(j2).getName(), c1);
					}
					
					
				}
			}catch(Exception e){}
		}
		
		//Find Average.
		shared_avg=shared_avg/sa;
		timeline_avg=timeline_avg/ta;
		status_avg=status_avg/sta;

	
		//Create Session for certain field to Use and print on Browser .
		HttpSession session=request.getSession();
		session.setAttribute("lim", lim);
		session.setAttribute("status_avg", status_avg);
		session.setAttribute("timeline_avg", timeline_avg);
		session.setAttribute("shared_avg", shared_avg);
		session.setAttribute("loginUser",loginUser );
		session.setAttribute("counter",counter );
		session.setAttribute("counter1",counter1 );
		session.setAttribute("counter2",counter2 );
		session.setAttribute("special_friends", special_friends);
		session.setAttribute("timeline_friends",timeline_friends );
		session.setAttribute("shared_friends",shared_friends );
		session.setAttribute("status_friends",status_friends );
		session.setAttribute("name_id",name_id );
		session.setAttribute("posts",posts );
		
		
		
		//Sort Hashmap based on their total likes.
		timeline_friends = sortByValues(timeline_friends); 
		shared_friends = sortByValues(shared_friends); 
		status_friends = sortByValues(status_friends); 
		
		//Send request to next page to print.
		response.sendRedirect(request.getContextPath() + "/Show_case.jsp");
				
		

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	//Extract_access_token URL from Graph of facebook
	private String Extract_access_token(URL url) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = url.openStream();
		int r;
		while ((r = is.read()) != -1) {
			baos.write(r);
		}
		return new String(baos.toByteArray());
	}
	
	
	//Hashmap Sort logic
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static HashMap sortByValues(HashMap map) { 
	       List list = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue())
	                  .compareTo(((Map.Entry) (o1)).getValue());
	            }
	       });

	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }
}
