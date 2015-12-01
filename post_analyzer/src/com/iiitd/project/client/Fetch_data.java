package com.iiitd.project.client;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.jni.File;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Album;
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
	public static String APP_ID = "758937600918147";
	public static String APP_SECRET = "127770845824b36aad9f23c3f1139670";
	  
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
		String code=request.getParameter("code");		//code is generated after authorisation of app which can be used for extracting user access token. 
		String URLEncodedRedirectURI = URLEncoder.encode("http://localhost:8080/post_analyzer/Fetch_data");  //Url i.e used for redirecting the page after authorisation
		
		//send request to get user access token by using above code.
		String authURL = "https://graph.facebook.com/oauth/access_token?" +
                "client_id=" + Fetch_data.APP_ID + "&" +
                "redirect_uri=" + URLEncodedRedirectURI + "&" +
                "client_secret=" + Fetch_data.APP_SECRET + "&" +
                "code=" + code;
		
		//convert above string into URL
		URL url = new URL(authURL);
		String access_token=null;
		
		//send URL to Extract_access method that get access token in url form
		String result = Extract_access_token(url);
		
		//process to extract access_token from result 
		String[] pairs = result.split("&");
		for (String pair : pairs) {
			String[] kv = pair.split("=");
			if (kv[0].equals("access_token")) {
				access_token = kv[1];
			}
		}
		//System.out.println(access_token);
		
		
		final FacebookClient facebookClient = new DefaultFacebookClient("CAACEdEose0cBAIjSZB1jcsIJcgwh0gvmZBXIotZC0JRHpz95U3GtS74RDZBQCZC8Fk9ZAX3pyMF3XNmQww87dFBRmZCB3mKxlb8udyEsCqSsJO4Pr4yfPruVZArKxRCxXZBSToEvMbySVzYaipBV4SGli4I2Ju5SiHPF2AMM2Dk19bWBrZAOPGiwKQ40AZCgU3E0R8iX0x2Dvvy0gZDZD");
		
		//fetch user  i.e a client  data who logged in with given fields. 
		User loginUser = facebookClient.fetchObject("me", User.class,Parameter.with("fields","first_name,last_name,name,email,website"));
		
		//we need to connect to access another edge
		Connection<Post> feed=facebookClient.fetchConnection("me/posts", Post.class, Parameter.with("fields","name,story,id,full_picture,picture,likes.limit(1000){name,pic_large},type,created_time,link"),Parameter.with("limit", 300));
		
		//String aloo="http://graph.facebook.com/"+feed.getData().get(0).getLikes().getData().get(0).getId()+"/picture";
		//URL url1=new URL(aloo);
		//System.out.println("yo"+Extract_access_token(url1));
		//User loginUser1 = facebookClient.fetchObject(feed.getData().get(0).getLikes().getData().get(0).getId(), User.class,Parameter.with("fields","first_name,last_name,name,picture"));
		//System.out.println(loginUser1.getPicture()+loginUser1.getName());
		
	//System.out.println(	loginUser.getEmail()
		//+loginUser.getWebsite());
		List<Post> posts = feed.getData();
		//System.out.println(feed.Say you clock has frequency f. Create a counter which counts from 1 to f/2.getData().size());
		
		 HashMap<String,Integer> timeline_friends = new HashMap<String,Integer>();
		 HashMap<String,Integer> shared_friends = new HashMap<String,Integer>();
		 HashMap<String,Integer> status_friends = new HashMap<String,Integer>();
		 HashMap<String,String> name_id = new HashMap<String,String>();
		List<NamedFacebookType> Likes_Data=null;
		Connection<Photo>photos =facebookClient.fetchConnection("me/photos/uploaded", Photo.class, Parameter.with("fields","name,picture,link,likes.limit(1000){name,pic_large},created_time"),Parameter.with("limit", 300),Parameter.with("summary", true));
		int shared_avg=0,sa=1;
		int timeline_avg=0,ta=1;
		int status_avg=0,sta=1;
	//change according to order of profile pic
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String date = format.format(new Date());
		
		String[] time0=date.toString().split("/");
		String[] time1=null;
		//System.out.println(posts.get(0).getCreatedTime().toString());
		int ti=0,post_limit=0;
		while(true){
			//System.out.println(ti+posts.get(ti).getId());
			
		String date2=format.format(posts.get(ti).getCreatedTime());
		
		time1=date2.split("/");
		//System.out.println(date2.toString());
		//System.out.println(time1[0]+" " +time1[1]+" " +time1[2]);
		//System.out.println(time0[0]+" " +time0[1]+" "+time0[2]);
		int test=Integer.parseInt(time0[0].toString())-Integer.parseInt(time1[0].toString());
		if(test<=1){
		
			if(test==1){
				if(Integer.parseInt(time0[1].toString())-Integer.parseInt(time1[1].toString())>1)
				{	break;
				
				
				}
			}
			else{
				post_limit++;
			}
				
		}
		ti++;
		}
		//System.out.println(date.toString());
		int flag=0;    //flag for photos
		int flag1=0;    //flag for status
		int flag2=0; 	//flag for shared
		int counter=0;         //counter for divided total status of size 100
		int counter1=0;          //counter for divided photos of size dated
		int counter2=0;          //counter for divided shared photos of size dated
		
		

		
		//System.out.println(post_limit);
		int lim=(post_limit*20)/100;
		
	//change
		for(int q=0;q<lim;q++){	
				//System.out.println(posts.get(q).getName());
				//if(posts.get(q).getName().toString().equals("Mukesh's cover photo")||posts.get(q).getName().toString().equals("Timeline Photos")||posts.get(q).getName().toString().equals("Profile Pictures")||posts.get(q).getName().toString().equals(loginUser.getName())){
			if(posts.get(q).getType().equals("status")){
				for(int w=0;w<posts.size();w++){
					
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
					if((posts.get(w).getId().equals(posts.get(q).getId())==false)&&posts.get(w).getType().equals("status")){
						counter++;
						List<NamedFacebookType> status_likes=posts.get(w).getLikes().getData();
						//System.out.println(status_likes.size()+"status");
						sta++;
						status_avg+=status_likes.size();
						for(int w1=0;w1<status_likes.size();w1++){
							int c1=1;
							
						//	System.out.println(posts.get(w).getId()+"ye ri");
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
			if(posts.get(q).getType().equals("photo")||posts.get(q).getType().equals("link")||posts.get(q).getType().equals("video")){
				//System.out.println(q+"yo");
				try{
				if(((posts.get(q).getName().equals("Mobile Uploads"))||posts.get(q).getName().equals("Mukesh's cover photo"))||(posts.get(q).getName().equals("Timeline Photos"))||(posts.get(q).getName().equals("Profile Pictures"))||(posts.get(q).getName().equals(loginUser.getName().toString()))){	
					
					
					for(int q1=0;q1<photos.getData().size();q1++){
						//System.out.println(q1+"yo"+(photos.getData().size()));
						String date2=format.format(photos.getData().get(q1).getCreatedTime());
						time1=date2.split("/");
						//System.out.println(date2.toString());
						//System.out.println(time1[0]+" " +time1[1]+" " +time1[2]);
						//System.out.println(time0[0]+" " +time0[1]+" "+time0[2]);
						int test=Integer.parseInt(time0[0].toString())-Integer.parseInt(time1[0].toString());
						if(test<=1){
							counter1++;
							if(test==1){
								if(Integer.parseInt(time0[1].toString())-Integer.parseInt(time1[1].toString())>1)
									break;
							}
							
						
							if(flag==1)
								break;
							if(posts.get(q).getLink().equals(photos.getData().get(q1).getLink())==false){
									List<NamedFacebookType> timiline_linker=photos.getData().get(q1).getLikes();
									//System.out.println(q+"yo"+photos.getData().get(q).getName());
									System.out.println(photos.getData().get(q1).getId());
									System.out.println(photos.getData().get(q1).getLikes().size());
									System.out.println("yoooo");
									
									//System.out.println(timiline_linker.size()+"photo"+q);
									 ta++;
									 timeline_avg+=timiline_linker.size(); 
									//			System.out.println(photos.getData().get(q1).getName());
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
					//System.out.println("ander aa " );
					
					for(int q3=0;q3<posts.size();q3++){
					//	System.out.println(q3+"yo"+(photos.getData().size()));
						String date2=format.format(posts.get(q3).getCreatedTime());
						time1=date2.split("/");
						//System.out.println(date2.toString());
						//System.out.println(time1[0]+" " +time1[1]+" " +time1[2]);
						//System.out.println(time0[0]+" " +time0[1]+" "+time0[2]);
						int test=Integer.parseInt(time0[0].toString())-Integer.parseInt(time1[0].toString());
						if(test<=1){
							counter2++;
							if(test==1){
								if(Integer.parseInt(time0[1].toString())-Integer.parseInt(time1[1].toString())>1)
									break;
							}
							
						
							if(flag2==1)
								break;
							if(posts.get(q).getLink().equals(posts.get(q3).getLink())==false){
								//System.out.println(posts.get(q3));	
								try{
									List<NamedFacebookType> shared_linker=posts.get(q3).getLikes().getData();
									
									//System.out.println(shared_linker.size()+"shared");
									sa++;
									 shared_avg+=shared_linker.size(); 
									
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
					
					
				}}catch( Exception e){
									
					
				}
				
				
				
				
			}
			
			
			
				
				
				
				
		}
		
		for (String name: timeline_friends.keySet()){
            String key =name.toString();
            float value =timeline_friends.get(name);  
            float a=(value/counter2)*100;
      //     System.out.println(key + " " + value+" "+ a);  
	} 
		 HashMap<String,Integer> special_friends = new HashMap<String,Integer>();

		for(int i=0;i<5;i++){
			try{
				List<NamedFacebookType> special=posts.get(i).getLikes().getData();
				System.out.println(i+"s");
				System.out.println(posts.get(i).getName());
				System.out.println(special.size());
				
			for(int j2=0;j2< special.size();j2++){
				int c1=1;
				if(special_friends.get( special.get(j2).getName())!=null){
					c1=Integer.parseInt(special_friends.get( special.get(j2).getName()).toString());
					special_friends.put( special.get(j2).getName(), ++c1);
				}
				else{
					special_friends.put( special.get(j2).getName(), c1);
				}
				
				
			}}catch(Exception e){}
		}
		for (String name: special_friends.keySet()){
            String key =name.toString();
            float value =special_friends.get(name);  
          //  float a=(value/counter2)*100;
          System.out.println(key+" "+value );  
	} 
		
		
		
		System.out.println("avg of shared"+shared_avg+" "+sa+"hs"+shared_avg/sa);
		System.out.println("avg of timiline"+timeline_avg+" "+ta+"hs"+timeline_avg/ta);
		System.out.println("avg of status"+status_avg+" "+sta+"hs"+status_avg/sta);
		shared_avg=shared_avg/sa;
		timeline_avg=timeline_avg/ta;
		status_avg=status_avg/sta;
		
		JSONObject obj=new JSONObject();
		JSONArray mainArray =new JSONArray();
		JSONObject subobj=new JSONObject();
		JSONArray likesarray=new JSONArray();
		//System.out.println(posts.size()+"Dekno");
		
		for(int i=0;i<posts.size();i++){
			mainArray.add(i, obj);
			obj.put(i, subobj);
			
			
			try{
				subobj.put("Name", feed.getData().get(i).getName().toString());
				subobj.put("ID", feed.getData().get(i).getId().toString());
					subobj.put("Story", feed.getData().get(i).getStory().toString());
			}catch(Exception e){
				
			}
			
			subobj.put("Likes", likesarray);
			//subobj.put("Picture", feed.getData().get(0).getPicture().toString());
			
		
		
		
		request.setAttribute("loginUser", loginUser);
		request.setAttribute("posts", posts);
		request.setAttribute("feed", feed);
		
		//System.out.println(posts.size());
		//System.out.println(i);
		
		//try{
		//	Likes_Data=feed.getData().get(i).getLikes().getData();
			//System.out.println(feed.getData().get(i).getName());
			//System.out.println(feed.getData().get(i).getStory());
			//System.out.println(feed.getData().get(i).getId());
			//System.out.println(feed.getData().get(i).getFullPicture());
			//for(int j=0;j<Likes_Data.size();j++){
				//JSONObject liker=new JSONObject();
					//likesarray.add(i, liker);
					//System.out.println(Likes_Data.get(j).getName());
					//System.out.println(Likes_Data.get(j).getId());
					//System.out.println(Likes_Data.get(i).getpic());
					//int c=1;
				//	if(friends.get(Likes_Data.get(j).getName())!=null){
					///	c=Integer.parseInt(friends.get(Likes_Data.get(j).getName()).toString());
						//friends.put(Likes_Data.get(j).getName(), ++c);
					}
					//else
						//friends.put(Likes_Data.get(j).getName(), c);
					//liker.put("Name", Likes_Data.get(j).getName().toString());
					//liker.put("ID", Likes_Data.get(j).getId().toString());
					//liker.put("Picture", Likes_Data.get(j));
			//	}
		//}catch(Exception e){	
	//	}
		//}
		
		
		
		
		try{
		BufferedWriter file=new BufferedWriter(new FileWriter("/media/mukesh/New Volume/post_analyzer/files/Data.json"));
		//file.write(obj.toJSONString());
			file.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	
		HttpSession session=request.getSession();

		//User loginUser1 = facebookClient.fetchObject("603031786473547", User.class,Parameter.with("fields","first_name,last_name,name,email,website"));
		//System.out.println(loginUser1.getName());
		
		session.setAttribute("lim", lim);
		session.setAttribute("status_avg", status_avg);
		session.setAttribute("timeline_avg", timeline_avg);
		session.setAttribute("shared_avg", shared_avg);
		session.setAttribute("loginUser",loginUser );
		session.setAttribute("counter",counter );
		session.setAttribute("counter1",counter1 );
		session.setAttribute("counter2",counter2 );
		
		timeline_friends = sortByValues(timeline_friends); 
		shared_friends = sortByValues(shared_friends); 
		status_friends = sortByValues(status_friends); 
		
		session.setAttribute("special_friends", special_friends);
		session.setAttribute("timeline_friends",timeline_friends );
		session.setAttribute("shared_friends",shared_friends );
		session.setAttribute("status_friends",status_friends );
		session.setAttribute("name_id",name_id );
		session.setAttribute("posts",posts );
	//	getServletConfig().getServletContext().getRequestDispatcher("/Show_case.jsp").forward(request, response);
		response.sendRedirect(request.getContextPath() + "/Show_case.jsp");
		//System.out.println("done");
		
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	private String Extract_access_token(URL url) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = url.openStream();
		int r;
		while ((r = is.read()) != -1) {
			baos.write(r);
		}
		return new String(baos.toByteArray());
	}
	
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
