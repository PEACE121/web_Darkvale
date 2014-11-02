package com.ntnu.game.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


public enum UserDao
{
	INSTANCE;
	
	
	public List<UserEntity> listUsers()
	{
		EntityManager em = EMFService.get().createEntityManager();
		// read the existing entries
		Query q = em.createQuery("select m from UserEntity m");
		List<UserEntity> todos = q.getResultList();
		return todos;
	}
	
	
	public void register()
	{
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null && getUser(user.getUserId()).size() == 0)
		{
			try
			{
				add(user.getUserId(), user.getNickname(), "");
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void updateProfileInformation(String userId, String username, String profileImage)
	{
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select t from UserEntity t where t.googleUserId = :userId");
		q.setParameter("userId", userId);
		List<UserEntity> users = q.getResultList();
		UserEntity userEnt = null;
		if (users.size() == 1)
		{
			userEnt = users.get(0);
			userEnt.setName(username);
			userEnt.setUserImage(profileImage);
		}
		em.persist(userEnt);
		em.close();
	}
	
	
	public void add(String userId, String name, String password) throws Exception
	{
		synchronized (this)
		{
			
			EntityManager em = Persistence.createEntityManagerFactory("transactions-optional").createEntityManager();
			UserEntity user = new UserEntity(userId, name, password);
			em.persist(user);
			em.close();
			
			
			// String APPLICATION_NAME = "Darkvale";
			//
			// // Set up the HTTP transport and JSON factory
			// HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			// JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
			//
			// // Load client secrets
			// GoogleClientSecrets.Details installedDetails = new GoogleClientSecrets.Details();
			// String CLIENT_ID = "162224671623-lba39vdfcdt54g0cbieu4klkjnqccqh3.apps.googleusercontent.com";
			// installedDetails.setClientId("162224671623-lba39vdfcdt54g0cbieu4klkjnqccqh3.apps.googleusercontent.com");
			// String CLIENT_SECRET = "Yt2ZfLKObVADHnyjlHTPdJ8d";
			// installedDetails.setClientSecret("Yt2ZfLKObVADHnyjlHTPdJ8d");
			//
			// GoogleClientSecrets clientSecrets = new GoogleClientSecrets();
			// clientSecrets.setInstalled(installedDetails);
			//
			// // Set up authorization code flow
			// GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory,
			// clientSecrets, Collections.singleton(PlusScopes.PLUS_ME)).setDataStoreFactory(DATA_STORE_FACTORY)
			// .setAccessType("offline").build();
			//
			// // Build credential from stored token data.
			// // JacksonFactory JSON_FACTORY = new JacksonFactory();
			// // NetHttpTransport netHttpTransport = new NetHttpTransport();
			// // GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(netHttpTransport,
			// JSON_FACTORY,
			// // "162224671623-lba39vdfcdt54g0cbieu4klkjnqccqh3.apps.googleusercontent.com", "Yt2ZfLKObVADHnyjlHTPdJ8d",
			// // code, "postmessage").execute();
			// // GoogleCredential googleCredential = new GoogleCredential.Builder()
			// // .setJsonFactory(JSON_FACTORY)
			// // .setTransport(netHttpTransport)
			// // .setClientSecrets("162224671623-lba39vdfcdt54g0cbieu4klkjnqccqh3.apps.googleusercontent.com",
			// // "Yt2ZfLKObVADHnyjlHTPdJ8d").build()
			// // .setFromTokenResponse(JSON_FACTORY.fromString(tokenResponse.toString(), GoogleTokenResponse.class));
			//
			// // Authorize
			// Credential credential = flow.loadCredential(userId);// new AuthorizationCodeInstalledApp(flow, new
			// // LocalServerReceiver()).authorize("user");
			//
			// // Set up the main Google+ class
			// // Builder builder = new Plus.Builder(httpTransport, jsonFactory, credential)
			// // .setApplicationName(APPLICATION_NAME);
			// // com.google.api.services.plus.Plus.builder().
			// // builder.setPlusRequestInitializer(arg0)
			// // builder.setGoogleClientRequestInitializer()
			// // AbstractGoogleJsonClientRequest request new AbstractGoogleJsonClientRequest<T>();
			// // PlusRequestInitializer requestInitializer = new PlusRequestInitializer().initializeJsonRequest()
			// // {
			// // })
			// // {
			// // });
			// // requestInitializer.initialize()
			// // builder.setPlusRequestInitializer(new PlusRequestInitializer());
			// // HttpTransport netTransport = new NetHttpTransport();
			// // JsonFactory jsonFactory2 = new JacksonFactory();
			// // GoogleAuthorizationCodeRequestUrl authUrl = new GoogleAuthorizationCodeRequestUrl(CLIENT_ID, "/",
			// scopes);
			// // authUrl.setAccessType("offline");
			// // authUrl.setApprovalPrompt("force");
			// // String token = new GoogleAuthorizationCodeTokenRequest(netTransport, jsonFactory2, CLIENT_ID,
			// CLIENT_SECRET,
			// // authorizationCode, "/").execute();
			// // System.out.println("Valid access token " + token.getAccessToken());
			// // GoogleCredential cd = new GoogleCredential().setAccessToken(token.getAccessToken());
			// GoogleAccessProtectedResource requestInitializer = new GoogleAccessProtectedResource(Auth.getAccessToken(),
			// Util.TRANSPORT, Util.JSON_FACTORY, Auth.CLIENT_ID, Auth.CLIENT_SECRET, Auth.getRefreshToken());
			// plus = Plus.builder(Util.TRANSPORT,
			// Util.JSON_FACTORY).setHttpRequestInitializer(requestInitializer).build();
			// Plus plus = new Plus.Builder(httpTransport, jsonFactory, credential).setApplicationName(APPLICATION_NAME)
			// .build();
			// // Oauth2 userInfoService = Oauth2.builder(new NetHttpTransport(), new JacksonFactory())
			// // .setHttpRequestInitializer(credential).build();
			// //
			//
			// // .setJsonHttpRequestInitializer(new JsonHttpRequestInitializer()
			// // {
			// // @Override
			// // public void initialize(JsonHttpRequest jsonHttpRequest) throws IOException
			// // {
			// // PlusRequest plusRequest = (PlusRequest) jsonHttpRequest;
			// // plusRequest.setKey("AIzaSyDQE5ecBafJQWRl62gPCLSPXEvYNu9sndM");
			// // }
			// // }).build();
			//
			//
			// // Make a request to access your profile and display it to console
			// Person profile = plus.people().get("me").execute();
			// System.out.println("ID: " + profile.getId());
			// System.out.println("Name: " + profile.getDisplayName());
			// System.out.println("Image URL: " + profile.getImage().getUrl());
			// System.out.println("Profile URL: " + profile.getUrl());
		}
	}
	
	
	// /**
	// * Setup the transport for our API calls.
	// * @throws java.io.IOException when the transport cannot be created
	// */
	// private static void setupTransport(Plus unauthenticatedPlus) throws IOException
	// {
	// // Here's an example of an unauthenticated Plus object. In cases where you
	// // do not need to use the /me/ path segment to discover the current user's
	// // ID, you can skip the OAuth flow with this code.
	//
	// // When we do not specify access tokens, we must specify our API key instead
	// // We do this using a JsonHttpRequestInitializer
	// unauthenticatedPlus.setJsonHttpRequestInitializer(new JsonHttpRequestInitializer()
	// {
	// @Override
	// public void initialize(JsonHttpRequest jsonHttpRequest) throws IOException
	// {
	// PlusRequest plusRequest = (PlusRequest) jsonHttpRequest;
	// plusRequest.setKey(Auth.GOOGLE_API_KEY);
	// }
	// }).build();
	//
	// // If, however, you need to use OAuth to identify the current user you must
	// // create the Plus object differently. Most programs will need only one
	// // of these since you can use an authenticated Plus object for any call.
	// Auth.authorize();
	// GoogleAccessProtectedResource requestInitializer = new GoogleAccessProtectedResource(Auth.getAccessToken(),
	// Util.TRANSPORT, Util.JSON_FACTORY, Auth.CLIENT_ID, Auth.CLIENT_SECRET, Auth.getRefreshToken());
	// plus = Plus.builder(Util.TRANSPORT, Util.JSON_FACTORY).setHttpRequestInitializer(requestInitializer).build();
	// }
	
	
	public List<UserEntity> getUser(String userId)
	{
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select t from UserEntity t where t.googleUserId = :userId");
		q.setParameter("userId", userId);
		List<UserEntity> todos = q.getResultList();
		return todos;
	}
	
	
	public UserEntity getUserById(String userId)
	{
		List<UserEntity> userEntities = getUser(userId);
		if (userEntities.size() == 1)
		{
			return userEntities.get(0);
		}
		return null;
	}
	
	
	public void remove(long id)
	{
		EntityManager em = EMFService.get().createEntityManager();
		try
		{
			UserEntity todo = em.find(UserEntity.class, id);
			em.remove(todo);
		} finally
		{
			em.close();
		}
	}
}